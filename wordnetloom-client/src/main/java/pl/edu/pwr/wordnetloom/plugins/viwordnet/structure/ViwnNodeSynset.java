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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.dto.DataEntry;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.utils.Common;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

@SuppressWarnings("unchecked")
public class ViwnNodeSynset extends ViwnNodeRoot implements Comparable<ViwnNodeSynset> {
	public enum State {
		EXPANDED, SEMI_EXPANDED, NOT_EXPANDED
	}

	public static HashMap<PartOfSpeech, Color> PosBgColors = new HashMap<PartOfSpeech, Color>();

	public final static Color vertexBackgroundColorVerbStroke = new Color(239,224,52);
	public final static Color vertexBackgroundColorNounStroke = new Color(27,221,27);
	public final static Color vertexBackgroundColorAdjStroke = new Color(28,193,177);
	public final static Color vertexBackgroundColorAdvStroke = new Color(81,115,193);

	public final static Color vertexBackgroundColorSelected = Color.yellow;
	public final static Color vertexBackgroundColorRoot = new Color(255, 178,178);
	public final static Color vertexBackgroundColorMarked = new Color(255, 195, 195);

	protected static SynsetNodeShape geom = new SynsetNodeShape();

	public static Set<RelationTypes>[] relTypes = new Set[] {
			new HashSet<RelationTypes>(), new HashSet<RelationTypes>(),
			new HashSet<RelationTypes>(), new HashSet<RelationTypes>() };

	private Set<ViwnEdgeSynset> edges_to_this_ = new HashSet<ViwnEdgeSynset>();
	private Set<ViwnEdgeSynset> edges_from_this_ = new HashSet<ViwnEdgeSynset>();

	private Synset synset;
	private List<Sense> units;
	private ViwnNodeSet in_set_ = null;
	private boolean hasFrame = false;
	private boolean is_dirty_cache_;

	private String ret = null;
	private PartOfSpeech pos = null;

	private ArrayList<ViwnEdgeSynset>[] relations = new ArrayList[] {
			new ArrayList<ViwnEdgeSynset>(), new ArrayList<ViwnEdgeSynset>(),
			new ArrayList<ViwnEdgeSynset>(), new ArrayList<ViwnEdgeSynset>() };

	protected State[] states = new State[] { State.NOT_EXPANDED,
			State.NOT_EXPANDED, State.NOT_EXPANDED, State.NOT_EXPANDED };

	public int compareTo(ViwnNodeSynset o) {
		return synset.getId().compareTo(o.synset.getId());
	}

	public void setSet(ViwnNodeSet set) {
		in_set_ = set;
	}

	public ViwnNodeSet getSet() {
		return in_set_;
	}

	public static void reproduceCache(HashMap<Long, ViwnNodeSynset> cache) {
		for (Long l : cache.keySet()) {
			ViwnNodeSynset s = cache.get(l);
			for (Direction d : Direction.values()) {
				s.states[d.ordinal()] = State.NOT_EXPANDED;
				s.relations[d.ordinal()].clear();
			}
			s.is_dirty_cache_ = true;
		}
	}

	public boolean isDirty() {
		return is_dirty_cache_;
	}

	public Shape getShape() {
		return geom.shape;
	}

	public Shape getButtonArea(Direction dir) {
		return geom.buttons[dir.ordinal()];
	}

	public Long getId() {
		return synset.getId();
	}

	public boolean containsRelation(ViwnEdgeSynset rel) {
		if (edges_from_this_.contains(rel) || edges_to_this_.contains(rel))
			return true;

		return false;
	}

	private void add_if_new(SynsetRelation rel) {
		if(LexiconManager.getInstance().getLexicons().contains(rel.getRelation().getLexicon().getId())){
			if (rel.getSynsetTo().getId().equals(synset.getId())) {
				ViwnEdgeSynset new_edge = new ViwnEdgeSynset(rel);
				edges_to_this_.add(new_edge);
			} else if (rel.getSynsetFrom().getId().equals(synset.getId())) {
				ViwnEdgeSynset new_edge = new ViwnEdgeSynset(rel);
				edges_from_this_.add(new_edge);
			} else
				System.err.println("Database sanity error");
			}
	}

	public void removeRelation(ViwnEdgeSynset e) {
		ArrayList<Iterator<ViwnEdgeSynset>> iters = new ArrayList<Iterator<ViwnEdgeSynset>>();

		for (Direction dir : Direction.values())
			iters.add(relations[dir.ordinal()].iterator());
		iters.add(edges_from_this_.iterator());
		iters.add(edges_to_this_.iterator());

		for (Iterator<ViwnEdgeSynset> it : iters)
			while (it.hasNext()) {
				ViwnEdgeSynset ee = it.next();
				if (e.equals(ee) || e.equalsReverse(ee)) {
					it.remove();
					break;
				}
			}
	}

	public void construct() {
		Set<ViwnEdgeSynset> rels = new HashSet<ViwnEdgeSynset>(edges_from_this_);
		Set<ViwnEdgeSynset> to = new HashSet<ViwnEdgeSynset>(edges_to_this_);

		for (Direction dir : Direction.values()) {
			relations[dir.ordinal()].clear();
		}

		Iterator<ViwnEdgeSynset> it = rels.iterator();
		while (it.hasNext()) {
			ViwnEdgeSynset e = it.next();

			for (Direction dir : Direction.values()) {

				if (relTypes[dir.ordinal()].contains(e.getRelationType())) {
					relations[dir.ordinal()].add(e);
					rels.remove(e);

					to.remove(e);

					ViwnEdgeSynset rev = e.createDummyReverse();
					if (rev != null) {
						to.remove(rev);
					}

					it = rels.iterator();
				}
			}
		}

		it = to.iterator();
		while (it.hasNext()) {
			ViwnEdgeSynset e = it.next();
			if (skip(e))
				continue;
			for (Direction dir : Direction.values()) {
				if (e.getRelationType() != null) {
					if (relTypes[dir.ordinal()].contains(RelationTypes.get(e
							.getRelationType().rev_id()))) {
						if (skip(e))
							continue;
						relations[dir.ordinal()].add(e);
						to.remove(e);
						it = to.iterator();
					} else if (relTypes[dir.getOposite().ordinal()].contains(e
							.getRelationType())) {
						if (skip(e))
							continue;
						relations[dir.ordinal()].add(e);
						to.remove(e);
						it = to.iterator();
					}
				}
			}
		}

		is_dirty_cache_ = false;
	}

	private boolean skip(ViwnEdgeSynset e) {
		String ee = e.toString();
		return ee.equals("fzn") || ee.equals("bzn") || ee.equals("Sim")
				|| ee.equals("Similar_to");
	}

	public void rereadDB() {
		setup();
	}

	private void setup() {
		edges_to_this_.clear();
		edges_from_this_.clear();

		// first - primary cache
		List<SynsetRelation> relsUP = ui.getUpperRelationsFor(synset.getId());
		List<SynsetRelation> relsDW = ui.getSubRelationsFor(synset	.getId());

		// no cache? fetch from database
		if (relsUP == null)
			relsUP = RemoteUtils.synsetRelationRemote.dbGetUpperRelations(
					synset, null, LexiconManager.getInstance().getLexicons());
		if (relsDW == null)
			relsDW = RemoteUtils.synsetRelationRemote.dbGetSubRelations(synset,
					null, LexiconManager.getInstance().getLexicons());

		// Get relations 'OTHER synset' -> 'THIS synset'
		for (SynsetRelation rel : relsUP)
			add_if_new(rel);

		// Get relations 'THIS synset' -> 'OTHER synset'
		for (SynsetRelation rel : relsDW)
			add_if_new(rel);

		// fetching poses from temporary cache
		getPos();

		// adding relations to appropiate groups
		construct();
	}
	private ViwnGraphViewUI ui;
	public ViwnNodeSynset(Synset synset, ViwnGraphViewUI ui) {
		this.synset = synset;
		this.ui = ui;
		this.ui.addSynsetToCash(synset.getId(), this); // check me
		units = RemoteUtils.lexicalUnitRemote.dbFastGetUnits(synset, LexiconManager.getInstance().getLexicons());
		setup();
	}

	private boolean hadCheckedPOS = false;

	/**
	 * Get synset part of speech.
	 * 
	 * @return Pos --- synset part of speech
	 */
	public PartOfSpeech getPos() {
		if (pos == null && !hadCheckedPOS) {
			DataEntry dataSet = ui.getEntrySetFor(getId());
			Long l = null;

			if (dataSet == null || dataSet.getPosID() == null)
				l = RemoteUtils.synsetRemote.fastGetPOSID(this.synset);
			else
				l = dataSet.getPosID();

			if (l == null) {
				pos = null;
				hadCheckedPOS = true;
				pos = PosManager.getInstance().getFromID(0);
			} else
				pos = PosManager.getInstance().getFromID(l.intValue());
		}
		return pos;
	}

	public boolean hasCache() {
		return pos != null || ret != null;
	}

	public ViwnNodeSynset setPos(PartOfSpeech pos) {
		this.pos = pos;
		if (pos == null)
			unitsStr = null;
		return this;
	}

	/**
	 * Get synset DTO object.
	 * 
	 * @return Synset --- synset object.
	 */
	public Synset getSynset() {
		return this.synset;
	}

	public void mouseClick(MouseEvent me, ViwnGraphViewUI ui) {
		Point p = absToVertexRel(me.getPoint(), this,
				ui.getVisualizationViewer());

		for (Direction rel : Direction.values()) {
			Collection<ViwnEdgeSynset> edges = getRelation(rel);
			if (edges.size() > 0) {
				Area ar = new Area(getButtonArea(rel));

				if (ar.contains(p)) {
					switch (getState(rel)) {
					case EXPANDED:
						ui.setSelectedNode(this);
						ui.hideRelation(this, rel);
						ui.recreateLayout();
						break;
					case SEMI_EXPANDED:
						ui.showRelation(this, new Direction[] { rel });
						ui.recreateLayout();
						break;
					case NOT_EXPANDED:
						ui.showRelation(this, new Direction[] { rel });
						ui.recreateLayout();
						break;
					}
				}
			}
		}
	}

	@Override
	public String getLabel() {
		if (ret == null) {
			DataEntry dataSet = ui.getEntrySetFor(getId());
			if (dataSet == null || dataSet.getLabel() == null) {
				String ret = "";
				if (Synset.isAbstract(Common.getSynsetAttribute(synset,
						Synset.ISABSTRACT)))
					ret = "S ";
				// check if synset isnt null or empty
				if (units != null && !units.isEmpty()) {
					ret += ((Sense) units.iterator().next()).toString();
					if (units.size() > 1)
						ret += " ...";
				} else {
					ret = "! S.y.n.s.e.t p.u.s.t.y !";
				}
				this.ret = ret;
			} else {
				this.ret = dataSet.getLabel();
			}
		}
		return ret;
	}

	public String getLexiconLabel() {
		if (units != null && !units.isEmpty()) {
			Sense unit = ((Sense) units.iterator().next());
			return unit.getLexicon().getLexiconIdentifier().getText();
		}
		return "";
	}

	public ViwnNodeSynset setLabel(String s) {
		ret = s;
		if (s == null)
			unitsStr = null;
		return this;
	}

	/**
	 * @param dir
	 * @return relations of specified class
	 */
	public Collection<ViwnEdgeSynset> getRelation(Direction dir) {
		return relations[dir.ordinal()];
	}

	public void setState(Direction dir, State state_new) {
		states[dir.ordinal()] = state_new;
	}

	public State getState(Direction dir) {
		return states[dir.ordinal()];
	}

	public void setFrame(boolean frame) {
		hasFrame = frame;
	}

	public boolean getFrame() {
		return hasFrame;
	}

	@Override
	public String toString() {
		return getLabel();
	}

	private String unitsStr;

	public String getUnitsStr() {
		if (getSynset() != null) {
			if (unitsStr == null) {
				unitsStr = RemoteUtils.synsetRemote
						.dbRebuildUnitsStr(getSynset(), LexiconManager
						.getInstance().getLexicons());
			}
			return unitsStr;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((synset == null) ? 0 : synset.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViwnNodeSynset other = (ViwnNodeSynset) obj;
		if (synset == null) {
			if (other.synset != null)
				return false;
		} else if (!synset.equals(other.synset))
			return false;
		return true;
	}

}
