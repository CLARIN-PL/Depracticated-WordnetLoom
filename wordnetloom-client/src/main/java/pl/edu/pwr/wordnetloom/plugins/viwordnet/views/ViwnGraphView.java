/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
*/

package pl.edu.pwr.wordnetloom.plugins.viwordnet.views;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.listeners.GraphChangeListener;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.*;
import pl.edu.pwr.wordnetloom.systems.common.Quadruple;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.ExtGraph;
import pl.edu.pwr.wordnetloom.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeCand;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeCandExtension;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeRoot;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeWord;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;

/**
 * [View]
 * VisualizesactiveGraphView a graph.
 * 
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 *
 */
public class ViwnGraphView extends AbstractView {

	public enum Mode {
		SYNSET,
		CANDS
	}

	private Mode mode;

	/**
	 * @param workbench
	 * @param title
	 */
	public ViwnGraphView(Workbench workbench, String title) {
		this(workbench, title, new ViwnGraphViewUI());
	}

	/**
	 * @param workbench
	 * @param title title of this view
	 * @param graphUI ui of this view
	 */
	public ViwnGraphView(Workbench workbench, String title, ViwnGraphViewUI graphUI) {
		super(workbench, title, graphUI);
	}

	/**
	 * @param listener
	 */
	public void addSynsetSelectionChangeListener(SynsetSelectionChangeListener listener){
		getUI().synsetSelectionChangeListeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeSynsetSelectionChangeListener(SynsetSelectionChangeListener listener){
		getUI().synsetSelectionChangeListeners.remove(listener);
	}

	public Mode getMode() {
		return mode;
	}

	/**
	 * @param synset
	 */
	public void loadSynset(Synset synset) {
		mode = Mode.SYNSET;
		getUI().refreshView(synset);
	}

	private ArrayList<TreeSet<ViwnNodeSynset>> createSubgraphs(ArrayList<ViwnNodeSynset> exp) {
		HashMap<Long, ViwnNodeSynset> synsetToNode = new HashMap<Long, ViwnNodeSynset>();
		TreeSet<Long> synsets = new TreeSet<Long>();

		for (ViwnNodeSynset node : exp) {
			synsetToNode.put(node.getId(), node);
			synsets.add(node.getId());
		}

		ArrayList<TreeSet<ViwnNodeSynset>> groups = new ArrayList<TreeSet<ViwnNodeSynset>>();

		while (synsets.size() > 0) {
			Long synsetId = synsets.iterator().next();
			synsets.remove(synsetId);

			TreeSet<Long> synsetGroup = new TreeSet<Long>();
			TreeSet<Long> synsetGroupQueue = new TreeSet<Long>();
			synsetGroupQueue.add(synsetId);
			
			while (synsetGroupQueue.size() > 0) {
				Long synsetInGroup = synsetGroupQueue.iterator().next();
				synsetGroupQueue.remove(synsetInGroup);
				synsetGroup.add(synsetInGroup);
				
				for (ViwnEdgeSynset relDown : synsetToNode
						.get(synsetInGroup).getRelation(ViwnNode.Direction.BOTTOM))
				{
					if (synsets.contains(relDown.getChild())) {
						synsetGroupQueue.add(relDown.getChild());
						synsets.remove(relDown.getChild());
					}
				}

				for (ViwnEdgeSynset relUp : synsetToNode.get(synsetInGroup)
						.getRelation(ViwnNode.Direction.TOP))
				{
					if (synsets.contains(relUp.getChild())) {
						synsetGroupQueue.add(relUp.getChild());
						synsets.remove(relUp.getChild());
					}
				}
			}
			
			TreeSet<ViwnNodeSynset> candGroup = new TreeSet<ViwnNodeSynset>();
			for (Long id : synsetGroup) {
				candGroup.add(synsetToNode.get(id));
			}
			groups.add(candGroup);
		}
		return groups;
	}
	
	private ArrayList<ViwnNodeCand> createNodeRoots(ArrayList<TreeSet<ViwnNodeSynset>> groups, String word) {
		ArrayList<ViwnNodeCand> roots = new ArrayList<ViwnNodeCand>();
		
		for (TreeSet<ViwnNodeSynset> group : groups) {
			if (group.size() == 0) {
				roots.add(null);
				continue;
			}

			// find node with maximum score and any node containg selected word
			boolean containsWord = false;
			ViwnNodeCand max = null;
			for (ViwnNodeSynset n : group) {
				if (n instanceof ViwnNodeCand) {
					ViwnNodeCand node = (ViwnNodeCand)n;
					if (max == null ||
						node.getExt().getScore1() > max.getExt().getScore1() ||
						(node.getExt().getScore1() == max.getExt().getScore1() && 
						 node.getExt().getScore2() > max.getExt().getScore2()) )
					{
						max = node;
					}
					
					if (node.isAdded())
						containsWord = true;
				}
			}

			if (max != null) {
				max.setCenter();
				roots.add(max);
				
				if (max.isAdded() || containsWord)
					max.setEvaluated();
			}
		}
		return roots;
	}
	
	private boolean setupSpawner(ViwnNodeSynset to_set, TreeSet<ViwnNodeSynset> nodes) {
		for(ViwnNodeSynset node : nodes) {
			for (ViwnEdgeSynset e : node.getRelation(ViwnNode.Direction.BOTTOM)) {
				if (e.getChild().equals(to_set.getId())) {
					to_set.setSpawner(node, ViwnNode.Direction.BOTTOM);
					return true;
				}
			}

			for (ViwnEdgeSynset e : node.getRelation(ViwnNode.Direction.TOP)) {
				if (e.getChild().equals(to_set.getId())) {
					to_set.setSpawner(node, ViwnNode.Direction.TOP);
					return true;
				}
			}
		}
		return false;
	}

	private void setupNodes(
			ArrayList<TreeSet<ViwnNodeSynset>> groups,
			ArrayList<ViwnNodeCand> roots,
			ViwnNodeRoot root,
			ArrayList<ViwnNodeSynset> freeSyns)
	{

		for (int i=0; i<groups.size(); ++i) {
			roots.get(i).setSpawner(root, ViwnNode.Direction.BOTTOM);
			TreeSet<ViwnNodeSynset> setuped = new TreeSet<ViwnNodeSynset>();
			setuped.add(roots.get(i));
			ArrayDeque<ViwnNodeSynset> to_setup = new ArrayDeque<ViwnNodeSynset>(groups.get(i));

			int count = 0;
			while (!to_setup.isEmpty() && count != to_setup.size()) {
				ViwnNodeSynset node = to_setup.pop();
				if (node != roots.get(i)) {
					if (setupSpawner(node, setuped)) {
						setuped.add(node);
						count = 0;
					} else {
						to_setup.add(node);
						count++;
					}
				}
			}
			if (count == to_setup.size() && count != 0) {
				System.out.println("nodes left unset");
			}
		}
	}

	/**
	 * Pobiera listę rozszerzeń dla danego słowa. Metoda wydzielona z LoadCandidate
	 * żeby nie zmieniać typu zwracanego przez tamtą metodę (W Quadruple moga byc tylko 4 wartosci)
	 * @author lburdka
	 * @param word 
	 * @param packageNo 
	 * @param pos 
	 * @return ArrayList<ViwnNodeCandExtension>
	 */
	// FIXME: za dużo ustawiania rzeczy jakie zostały już pobrane, usunąć, wywalić niepotrzebne merge etc.
	public ArrayList<ViwnNodeCandExtension> loadExtensions(String word, int packageNo, PartOfSpeech pos){
		//SELECT i JOIN potrzebnych struktur
		List<ExtGraphExtension> extensions=(ArrayList<ExtGraphExtension>) RemoteUtils.extGraphExtensionRemote.dbFullGet(word, packageNo);
		extensions = RemoteUtils.extGraphExtensionRemote.dbGetRelation(extensions);
		Long[] ids= new Long[extensions.size()];
		for(int i=0;i<ids.length;i++){
			ids[i]=extensions.get(i).getExtGraph().getId();
			extensions.get(i).setRelationType(extensions.get(i).getRelationType());
			RemoteUtils.extGraphExtensionRemote.mergeObject(extensions.get(i));
		}
		ArrayList<ExtGraph> graphs = null;
		if(ids.length>0){
			graphs=(ArrayList<ExtGraph>) RemoteUtils.extGraphRemote.dbFullGet(ids);
		}

		if(graphs!=null){
			
			HashMap<Long,ExtGraphExtension> graphExts=new HashMap<Long,ExtGraphExtension>();
			
			for(ExtGraphExtension ege:extensions){
				for(ExtGraph eg:graphs){
					if(ege.getExtGraph().getId().equals(eg.getId())){
						graphExts.put(eg.getId(), ege);
					}
				}
			}
			
			ids=new Long[graphs.size()];
			for(int i=0;i<ids.length;i++){
				ids[i]=graphs.get(i).getSynset().getId();
			}
			List<Synset> synsets = RemoteUtils.synsetRemote.dbFullGet(ids);
	
			//Stworzenie odpowiednich węzłów na podstawie pobranych informacji
			if(synsets!=null&&synsets.size()>0){
				
				HashMap<Long,ExtGraph> synGraphs=new HashMap<Long,ExtGraph>();
				
				for(ExtGraph eg:graphs){
					for(Synset ss:synsets){
						if(eg.getSynset().getId().equals(ss.getId())){
							synGraphs.put(ss.getId(), eg);
						}
					}
				}
				
				//Uzupełnienie synsetów
				//synsets = RemoteUtils.synsetRemote.dbGetUnits(synsets);
			//	synsets = RemoteUtils.synsetRemote.dbGetSynsetsRels(synsets);
				
				//Uzupełnienie jednostek w rozszerzeniach
				for(Synset syn : synsets){
					ExtGraphExtension exge = graphExts.get(synGraphs.get(syn.getId()).getId());
					// TODO: check me
					//graphExts.get(synGraphs.get(syn.getId()).getId()).setLexicalUnit(syn);
					RemoteUtils.extGraphExtensionRemote.mergeObject(exge);
				}

				ArrayList<ViwnNodeCandExtension> result=new ArrayList<ViwnNodeCandExtension>();

				for(Synset s:synsets){
					ViwnNodeCandExtension ext = new ViwnNodeCandExtension(s,graphExts.get(synGraphs.get(s.getId()).getId()), getUI());
					ext.setRelName(graphExts.get(synGraphs.get(s.getId()).
							getId()).
							getRelationType().
							getName().getText());
					ext.setColor(graphExts.get(synGraphs.get(s.getId()).getId()).getColor());

					result.add(ext);
				}
				return result;
			}else
				return null;
		}else
			return null;
	}

	public Quadruple<ViwnNodeWord, ArrayList<TreeSet<ViwnNodeSynset>>,ArrayList<ViwnNodeCand>,
                ArrayList<ViwnNodeSynset>> loadCandidate(String word, int packageNo, PartOfSpeech pos) {

		getUI().getCache().clear();

		Collection<ExtGraph> cands = RemoteUtils.extGraphRemote.dbFullGet(word, packageNo);
		mode = Mode.CANDS;
		List<Synset> synsets = new ArrayList<Synset>();
		for (ExtGraph ext : cands) {
			synsets.add(ext.getSynset());
		}

		TreeSet<Long> synsetsWithWord = new TreeSet<Long>();
		List<Synset> synsetsWithWordCol = new ArrayList<Synset>();
		synsetsWithWordCol = RemoteUtils.synsetRemote.dbFastGetSynsets(word,LexiconManager.getInstance().getLexicons());

		for (Synset synset : synsetsWithWordCol)
			synsetsWithWord.add(synset.getId());

		ExtGraph extMaxRwf = null;
		int nodesAddedCount = 0;

		ArrayList<ViwnNodeSynset> viwnCands = new ArrayList<ViwnNodeSynset>();
		ArrayList<ViwnNodeSynset> freeSyns = new ArrayList<ViwnNodeSynset>();

		if (cands.size() > 0) {
			for (ExtGraph ext : cands) {
				Synset synset = ext.getSynset();
				// remember item with the highest rwf
				if (extMaxRwf == null
						|| ext.getScore2() > extMaxRwf.getScore2())
					extMaxRwf = ext;

				ViwnNodeCand node = null;

				if (synsetsWithWord.contains(ext.getSynset().getId())) {
					node = new ViwnNodeCand(synset, ext, true, getUI());
					synsetsWithWord.remove(ext.getSynset().getId());
				} else {
					node = new ViwnNodeCand(synset, ext, false,getUI());
				}
				
				viwnCands.add(node);
				getUI().getCache().put(synset.getId(), node);
				nodesAddedCount++;
			}
			
			// add max RWF if any nodes were added
			if (nodesAddedCount == 0 && extMaxRwf != null) {
				Synset synset = extMaxRwf.getSynset();

				ViwnNodeCand node = null;

				if (synsetsWithWord.contains(extMaxRwf.getSynset().getId())) {
					node = new ViwnNodeCand(synset, extMaxRwf, true, getUI());
					synsetsWithWord.remove(extMaxRwf.getSynset().getId());
				} else {
					node = new ViwnNodeCand(synset, extMaxRwf, false, getUI());
				}
				
				viwnCands.add(node);
				getUI().getCache().put(synset.getId(), node);
			}
			
			// add other synsets which contain selected word
			for (Synset synset : synsetsWithWordCol) {
				ViwnNodeSynset node = new ViwnNodeSynset(synset,getUI());
				node.setFrame(true);
				getUI().getCache().put(synset.getId(), node);
				freeSyns.add(node);
			}
		}

		ViwnNodeWord nodeWord = new ViwnNodeWord(word, packageNo, pos);

		ArrayList<TreeSet<ViwnNodeSynset>> groups = createSubgraphs(viwnCands);
		ArrayList<ViwnNodeCand> roots = createNodeRoots(groups, word);
		setupNodes(groups, roots, nodeWord, freeSyns);

		return new Quadruple<ViwnNodeWord,
					ArrayList<TreeSet<ViwnNodeSynset>>,
					ArrayList<ViwnNodeCand>,
					ArrayList<ViwnNodeSynset>>
			(nodeWord, groups, roots, freeSyns);
	}

	/**
	 * @param gcl GraphChangeListener to add
	 */		
	public void addGraphChangeListener(GraphChangeListener gcl) {
		getUI().graphChangeListeners.add(gcl);
	}
	
	/**
	 * @param gcl GraphChangeListener to remove
	 */
	public void removeGraphChangeListener(GraphChangeListener gcl) {
		getUI().graphChangeListeners.remove(gcl);
	}

	/**
	 * make getUI public
	 * */
	public ViwnGraphViewUI getUI() {
		return (ViwnGraphViewUI) super.getUI();
	}
}
