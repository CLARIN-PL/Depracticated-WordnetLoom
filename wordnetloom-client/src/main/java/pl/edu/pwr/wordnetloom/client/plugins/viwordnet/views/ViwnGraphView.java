package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.GraphChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode.Direction;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeCand;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeCandExtension;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeRoot;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeWord;
import pl.edu.pwr.wordnetloom.client.systems.common.Quadruple;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

/**
 * [View] VisualizesactiveGraphView a graph.
 */
public class ViwnGraphView extends AbstractView {

    public enum Mode {
        SYNSET,
        CANDS
    }

    private Mode mode;

    public ViwnGraphView(Workbench workbench, String title) {
        this(workbench, title, new ViwnGraphViewUI());
    }

    public ViwnGraphView(Workbench workbench, String title, ViwnGraphViewUI graphUI) {
        super(workbench, title, graphUI);
    }

    public void addSynsetSelectionChangeListener(SynsetSelectionChangeListener listener) {
        getUI().synsetSelectionChangeListeners.add(listener);
    }

    public void removeSynsetSelectionChangeListener(SynsetSelectionChangeListener listener) {
        getUI().synsetSelectionChangeListeners.remove(listener);
    }

    public Mode getMode() {
        return mode;
    }

    public void loadSynset(Synset synset) {
        mode = Mode.SYNSET;
        getUI().refreshView(synset);
    }

    private ArrayList<TreeSet<ViwnNodeSynset>> createSubgraphs(ArrayList<ViwnNodeSynset> exp) {
        HashMap<Long, ViwnNodeSynset> synsetToNode = new HashMap<>();
        TreeSet<Long> synsets = new TreeSet<>();

        exp.stream().map((node) -> {
            synsetToNode.put(node.getId(), node);
            return node;
        }).forEachOrdered((node) -> {
            synsets.add(node.getId());
        });

        ArrayList<TreeSet<ViwnNodeSynset>> groups = new ArrayList<>();

        while (synsets.size() > 0) {
            Long synsetId = synsets.iterator().next();
            synsets.remove(synsetId);

            TreeSet<Long> synsetGroup = new TreeSet<>();
            TreeSet<Long> synsetGroupQueue = new TreeSet<>();
            synsetGroupQueue.add(synsetId);

            while (synsetGroupQueue.size() > 0) {
                Long synsetInGroup = synsetGroupQueue.iterator().next();
                synsetGroupQueue.remove(synsetInGroup);
                synsetGroup.add(synsetInGroup);

                synsetToNode
                        .get(synsetInGroup).getRelation(Direction.BOTTOM).stream().filter((relDown) -> (synsets.contains(relDown.getChild()))).map((relDown) -> {
                    synsetGroupQueue.add(relDown.getChild());
                    return relDown;
                }).forEachOrdered((relDown) -> {
                    synsets.remove(relDown.getChild());
                });

                synsetToNode.get(synsetInGroup)
                        .getRelation(Direction.TOP)
                        .stream()
                        .filter((relUp) -> (synsets.contains(relUp.getChild()))).map((relUp) -> {
                    synsetGroupQueue.add(relUp.getChild());
                    return relUp;
                }).forEachOrdered((relUp) -> {
                    synsets.remove(relUp.getChild());
                });
            }

            TreeSet<ViwnNodeSynset> candGroup = new TreeSet<>();
            synsetGroup.forEach((id) -> {
                candGroup.add(synsetToNode.get(id));
            });
            groups.add(candGroup);
        }
        return groups;
    }

    private ArrayList<ViwnNodeCand> createNodeRoots(ArrayList<TreeSet<ViwnNodeSynset>> groups, String word) {
        ArrayList<ViwnNodeCand> roots = new ArrayList<>();

        for (TreeSet<ViwnNodeSynset> group : groups) {
            if (group.isEmpty()) {
                roots.add(null);
                continue;
            }

            // find node with maximum score and any node containg selected word
            boolean containsWord = false;
            ViwnNodeCand max = null;
            for (ViwnNodeSynset n : group) {
                if (n instanceof ViwnNodeCand) {
                    ViwnNodeCand node = (ViwnNodeCand) n;
                    if ((max == null || node.getExt().getScore1() > max.getExt().getScore1())
                            || (Objects.equals(node.getExt().getScore1(), max.getExt().getScore1())
                            && node.getExt().getScore2() > max.getExt().getScore2())) {
                        max = node;
                    } else {
                    }

                    if (node.isAdded()) {
                        containsWord = true;
                    }
                }
            }

            if (max != null) {
                max.setCenter();
                roots.add(max);

                if (max.isAdded() || containsWord) {
                    max.setEvaluated();
                }
            }
        }
        return roots;
    }

    private boolean setupSpawner(ViwnNodeSynset to_set, TreeSet<ViwnNodeSynset> nodes) {
        for (ViwnNodeSynset node : nodes) {
            for (ViwnEdgeSynset e : node.getRelation(Direction.BOTTOM)) {
                if (e.getChild().equals(to_set.getId())) {
                    to_set.setSpawner(node, Direction.BOTTOM);
                    return true;
                }
            }

            for (ViwnEdgeSynset e : node.getRelation(Direction.TOP)) {
                if (e.getChild().equals(to_set.getId())) {
                    to_set.setSpawner(node, Direction.TOP);
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
            ArrayList<ViwnNodeSynset> freeSyns) {

        for (int i = 0; i < groups.size(); ++i) {
            roots.get(i).setSpawner(root, Direction.BOTTOM);
            TreeSet<ViwnNodeSynset> setuped = new TreeSet<>();
            setuped.add(roots.get(i));
            ArrayDeque<ViwnNodeSynset> to_setup = new ArrayDeque<>(groups.get(i));

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
     * Pobiera listę rozszerzeń dla danego słowa. Metoda wydzielona z
     * LoadCandidate żeby nie zmieniać typu zwracanego przez tamtą metodę (W
     * Quadruple moga byc tylko 4 wartosci)
     *
     * @author lburdka
     * @param word
     * @param packageNo
     * @param pos
     * @return ArrayList<ViwnNodeCandExtension>
     */
    // FIXME: za dużo ustawiania rzeczy jakie zostały już pobrane, usunąć, wywalić niepotrzebne merge etc.
    public ArrayList<ViwnNodeCandExtension> loadExtensions(String word, int packageNo, PartOfSpeech pos) {
        //SELECT i JOIN potrzebnych struktur
        List<ExtGraphExtension> extensions = new ArrayList<>();//(ArrayList<ExtGraphExtension>) RemoteUtils.extGraphExtensionRemote.dbFullGet(word, packageNo);
        //extensions = RemoteUtils.extGraphExtensionRemote.dbGetRelation(extensions);
        Long[] ids = new Long[extensions.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = extensions.get(i).getExtGraph().getId();
            extensions.get(i).setRelationType(extensions.get(i).getRelationType());
            //RemoteUtils.extGraphExtensionRemote.mergeObject(extensions.get(i));
        }
        ArrayList<ExtGraph> graphs = null;
        if (ids.length > 0) {
            //graphs = (ArrayList<ExtGraph>) RemoteUtils.extGraphRemote.dbFullGet(ids);
        }

        if (graphs != null) {

            HashMap<Long, ExtGraphExtension> graphExts = new HashMap<>();

            for (ExtGraphExtension ege : extensions) {
                for (ExtGraph eg : graphs) {
                    if (ege.getExtGraph().getId().equals(eg.getId())) {
                        graphExts.put(eg.getId(), ege);
                    }
                }
            }

            ids = new Long[graphs.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = graphs.get(i).getSynset().getId();
            }
            // List<Synset> synsets = RemoteUtils.synsetRemote.dbFullGet(ids);

            //Stworzenie odpowiednich węzłów na podstawie pobranych informacji
//            if (synsets != null && synsets.size() > 0) {
//
//                HashMap<Long, ExtGraph> synGraphs = new HashMap<>();
//
//                for (ExtGraph eg : graphs) {
//                    synsets.stream().filter((ss) -> (eg.getSynset().getId().equals(ss.getId()))).forEachOrdered((ss) -> {
//                        synGraphs.put(ss.getId(), eg);
//                    });
//                }
//
//                //Uzupełnienie synsetów
//                //synsets = RemoteUtils.synsetRemote.dbGetUnits(synsets);
//                //	synsets = RemoteUtils.synsetRemote.dbGetSynsetsRels(synsets);
//                //Uzupełnienie jednostek w rozszerzeniach
//                synsets.stream().map((syn) -> graphExts.get(synGraphs.get(syn.getId()).getId())).forEachOrdered((exge) -> {
//                    // TODO: check me
//                    //graphExts.get(synGraphs.get(syn.getId()).getId()).setLexicalUnit(syn);
//                    RemoteUtils.extGraphExtensionRemote.mergeObject(exge);
//                });
//
//                ArrayList<ViwnNodeCandExtension> result = new ArrayList<>();
//
//                synsets.stream().map((s) -> {
//                    ViwnNodeCandExtension ext = new ViwnNodeCandExtension(s, graphExts.get(synGraphs.get(s.getId()).getId()), getUI());
//                    ext.setRelName(graphExts.get(synGraphs.get(s.getId()).
//                            getId()).
//                            getRelationType().
//                            getName().getText());
//                    ext.setColor(graphExts.get(synGraphs.get(s.getId()).getId()).getColor());
//                    return ext;
//                }).forEachOrdered((ext) -> {
//                    result.add(ext);
//                });
//                return result;
//            } else {
//                return null;
//            }
//        } else {
//            return null;
        }
        return null;
    }

    public Quadruple<ViwnNodeWord, ArrayList<TreeSet<ViwnNodeSynset>>, ArrayList<ViwnNodeCand>, ArrayList<ViwnNodeSynset>> loadCandidate(String word, int packageNo, PartOfSpeech pos) {

        getUI().getCache().clear();

        //Collection<ExtGraph> cands = RemoteUtils.extGraphRemote.dbFullGet(word, packageNo);
        mode = Mode.CANDS;
        List<Synset> synsets = new ArrayList<>();
//        cands.forEach((ext) -> {
//            synsets.add(ext.getSynset());
//        });

        TreeSet<Long> synsetsWithWord = new TreeSet<>();
        List<Synset> synsetsWithWordCol = new ArrayList<>();
        //  synsetsWithWordCol = RemoteUtils.synsetRemote.dbFastGetSynsets(word, LexiconManager.getInstance().getLexicons());

        for (Synset synset : synsetsWithWordCol) {
            synsetsWithWord.add(synset.getId());
        }

        ExtGraph extMaxRwf = null;
        int nodesAddedCount = 0;

        ArrayList<ViwnNodeSynset> viwnCands = new ArrayList<>();
        ArrayList<ViwnNodeSynset> freeSyns = new ArrayList<>();
//
//        if (cands.size() > 0) {
//            for (ExtGraph ext : cands) {
//                Synset synset = ext.getSynset();
//                // remember item with the highest rwf
//                if (extMaxRwf == null
//                        || ext.getScore2() > extMaxRwf.getScore2()) {
//                    extMaxRwf = ext;
//                }
//
//                ViwnNodeCand node = null;
//
//                if (synsetsWithWord.contains(ext.getSynset().getId())) {
//                    node = new ViwnNodeCand(synset, ext, true, getUI());
//                    synsetsWithWord.remove(ext.getSynset().getId());
//                } else {
//                    node = new ViwnNodeCand(synset, ext, false, getUI());
//                }
//
//                viwnCands.add(node);
//                getUI().getCache().put(synset.getId(), node);
//                nodesAddedCount++;
//            }
//
//            // add max RWF if any nodes were added
//            if (nodesAddedCount == 0 && extMaxRwf != null) {
//                Synset synset = extMaxRwf.getSynset();
//
//                ViwnNodeCand node = null;
//
//                if (synsetsWithWord.contains(extMaxRwf.getSynset().getId())) {
//                    node = new ViwnNodeCand(synset, extMaxRwf, true, getUI());
//                    synsetsWithWord.remove(extMaxRwf.getSynset().getId());
//                } else {
//                    node = new ViwnNodeCand(synset, extMaxRwf, false, getUI());
//                }
//
//                viwnCands.add(node);
//                getUI().getCache().put(synset.getId(), node);
//            }
//
//            // add other synsets which contain selected word
//            for (Synset synset : synsetsWithWordCol) {
//                ViwnNodeSynset node = new ViwnNodeSynset(synset, getUI());
//                node.setFrame(true);
//                getUI().getCache().put(synset.getId(), node);
//                freeSyns.add(node);
//            }
//        }
//
//        ViwnNodeWord nodeWord = new ViwnNodeWord(word, packageNo, pos);
//
//        ArrayList<TreeSet<ViwnNodeSynset>> groups = createSubgraphs(viwnCands);
//        ArrayList<ViwnNodeCand> roots = createNodeRoots(groups, word);
//        setupNodes(groups, roots, nodeWord, freeSyns);

        return null;///new Quadruple<>(nodeWord, groups, roots, freeSyns);
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
     *
     */
    @Override
    public ViwnGraphViewUI getUI() {
        return (ViwnGraphViewUI) super.getUI();
    }
}
