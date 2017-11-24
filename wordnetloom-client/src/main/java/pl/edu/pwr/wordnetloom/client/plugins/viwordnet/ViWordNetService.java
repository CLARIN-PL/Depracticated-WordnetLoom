package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import com.alee.laf.menu.WebMenu;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.LexicalUnitsView;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.SynsetPropertiesView;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.SynsetStructureView;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.SynsetView;
import pl.edu.pwr.wordnetloom.client.plugins.relations.da.RelationsDA;
import pl.edu.pwr.wordnetloom.client.plugins.relations.views.ToolbarViewUI;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.LockerChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnLockerViewUI.LockerElementRenderer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexRenderer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.DeleteRelationWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.MakeNewLexicalRelationWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.MakeNewRelationWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.common.Quadruple;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.misc.SimpleListenerWrapper;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

/**
 * [Service] Installs views used by the Viwn perspective.
 *
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 * @author amusial
 */
public class ViWordNetService extends AbstractService implements
        SynsetSelectionChangeListener, SimpleListenerInterface,
        LockerChangeListener, ActionListener, Loggable {

    private String perspectiveName = null;
    private ViWordNetPerspective perspective = null;

    private final static int GRAPH_VIEWS_LIMIT = 6;
    private ViwnGraphView activeGraphView = null;
    private final List<ViwnGraphView> graphViews = new ArrayList<>(GRAPH_VIEWS_LIMIT);

    private LexicalUnitsView luView = null;
    private SynsetView synsetView = null;
    private ViwnLexicalUnitRelationsView unitsRelationsView = null;

    private SynsetStructureView synsetStructureView = null;

    private SynsetPropertiesView synsetPropertiesView = null;

    private ViwnSatelliteGraphView satelliteGraphView = null;

    private ViwnLockerView lockerView = null;

    private CandidatesView candView = null;

    private ViwnExamplesView examplesView = null;
    private ViwnExampleKPWrView kpwrExamples = null;

    private Object objectForReload;
    private Integer tagForReload;
    private final Map<String, PartOfSpeech> posMap = new HashMap<>();

    /**
     * @param workbench       <code>Workbench</code> of this
     * @param perspectiveName perspective name
     * @param perspective     <code>ViWordNetPerspective</code> of this
     */
    public ViWordNetService(Workbench workbench, String perspectiveName,
                            ViWordNetPerspective perspective) {
        super(workbench);
        this.perspectiveName = perspectiveName;
        this.perspective = perspective;

        PartOfSpeechManager.getInstance().getAll().stream().forEach((pos) -> {
            // posMap.put(pos.getName(RemoteConnectionProvider.getInstance().getLanguage()), pos);
        });
    }

    @Override
    public void installMenuItems() {

        // FIXME: too bad, shortcuts provi thisded by workbench doesn't work
        // with working shortcuts this code could be moved to them
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher((KeyEvent e) -> {
                    if (e.getID() == KeyEvent.KEY_PRESSED
                            && e.isControlDown()
                            && e.getKeyCode() == KeyEvent.VK_T) {
                        addGraphView();
                    }
                    return false;
                });

        WebMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        other.add(new MMenuItem(Labels.SAVE_CANDIDATES_GRAPH)
                .withMnemonic(KeyEvent.VK_Z)
                .withActionListener(this));

        other.addSeparator();
    }

    public void refreshViews() {
        luView.refreshData();
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void installViews() {
        ViwnGraphViewUI graphUI = new ViwnGraphViewUI();
        activeGraphView = new ViwnGraphView(workbench,
                Labels.VISUALIZATION, graphUI);
        activeGraphView.addSynsetSelectionChangeListener(this);
        workbench.installView(activeGraphView,
                ViWordNetPerspective.SPLIT_MAIN_VIEW, perspectiveName);
        graphViews.add(activeGraphView);

        luView = new LexicalUnitsView(workbench, Labels.UNITS);
        luView.addUnitChangeListener(this);
        workbench.installView(luView, ViWordNetPerspective.SPLIT_LEFT_VIEW, perspectiveName);

        synsetView = new SynsetView(workbench, Labels.SYNSETS);
        synsetView.addUnitChangeListener(this);
        workbench.installView(synsetView, ViWordNetPerspective.SPLIT_LEFT_VIEW, perspectiveName);

        candView = new CandidatesView(workbench, Labels.CANDIDATES);
        candView.addCandidateChangeListener(new SimpleListenerWrapper(this, "candidateSelection"));
        workbench.installView(candView, ViWordNetPerspective.SPLIT_LEFT_VIEW, perspectiveName);

        satelliteGraphView = new ViwnSatelliteGraphView(workbench, Labels.PREVIEW, graphUI);
        workbench.installView(satelliteGraphView, ViWordNetPerspective.SPLIT_RIGHT_TOP_VIEW, perspectiveName);
        activeGraphView.addGraphChangeListener(satelliteGraphView);

        examplesView = new ViwnExamplesView(workbench, Labels.EXAMPLES);
        kpwrExamples = new ViwnExampleKPWrView(workbench, Labels.KPWR, graphUI);
        workbench.installView(examplesView, ViWordNetPerspective.SPILT_LOCKER_VIEW, perspectiveName);
        workbench.installView(kpwrExamples, ViWordNetPerspective.SPILT_LOCKER_VIEW, perspectiveName);

        lockerView = new ViwnLockerView(workbench, Labels.CLIPBOARD);
        lockerView.addSynsetSelectionChangeListener(this);
        lockerView.addLockerChangeListener(this);
        workbench.installView(lockerView, ViWordNetPerspective.SPILT_LOCKER_VIEW, perspectiveName);

        synsetStructureView = new SynsetStructureView(workbench, Labels.SYNSET, false, false, false, 1, graphUI);
        synsetStructureView.addUnitChangeListener(new SimpleListenerWrapper(this, "synsetUnitSelection"));
        workbench.installView(synsetStructureView, ViWordNetPerspective.SPLIT_RIGHT_CENTRAL_VIEW, perspectiveName);

        synsetPropertiesView = new SynsetPropertiesView(workbench, Labels.PROPERTIES, 1, graphUI);
        synsetPropertiesView.addChangeListener(synsetStructureView);
        workbench.installView(synsetPropertiesView, ViWordNetPerspective.SPLIT_RIGHT_CENTRAL_VIEW, perspectiveName);

        unitsRelationsView = new ViwnLexicalUnitRelationsView(workbench, Labels.UNIT_RELATIONS);
        workbench.installView(unitsRelationsView, ViWordNetPerspective.SPLIT_RIGHT_BOTTOM_VIEW, perspectiveName);

        setActiveGraphView(graphUI.getContent());
    }

    private void loadPosBackgroundColors() {
        HashMap<PartOfSpeech, Color> pos_bg_colors = new HashMap<>();

        InputStreamReader istrem = null;
        try {
            istrem = new InputStreamReader(ViWordNetService.class.getClassLoader().getResourceAsStream("pos_bg_color.cfg"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
        }

        Properties conf = new Properties();
        try {
            if (istrem == null) {
                throw new IOException();
            }

            conf.load(istrem);
            Enumeration<Object> keys = conf.keys();

            while (keys.hasMoreElements()) {
                String pos = (String) keys.nextElement();
                String val = (String) conf.get(pos);
                Color col = null;
                try {
                    col = Color.decode(val.trim());
                } catch (Exception e) {
                    logger().warn(val + " is not a valid color");
                }

                PartOfSpeech posT = posMap.get(pos.replace("_", " "));
                if (posT != null) {
                    pos_bg_colors.put(posT, col);
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        ViwnNodeSynset.PosBgColors = pos_bg_colors;
    }

    private void loadPosFrameColors() {
        HashMap<PartOfSpeech, Color> pos_frame_colors = new HashMap<>();

        InputStreamReader istrem = null;
        try {
            istrem = new InputStreamReader(ViWordNetService.class.getClassLoader().getResourceAsStream("pos_frame_color.cfg"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
        }

        Properties conf = new Properties();
        try {
            if (istrem == null) {
                throw new IOException();
            }

            conf.load(istrem);
            Enumeration<Object> keys = conf.keys();

            while (keys.hasMoreElements()) {
                String pos = (String) keys.nextElement();
                String val = (String) conf.get(pos);
                Color col = null;
                try {
                    col = Color.decode(val.trim());
                } catch (Exception e) {
                    logger().warn(val + " is not a valid color");
                }

                PartOfSpeech posT = posMap.get(pos.replace("_", " "));
                if (posT != null) {
                    pos_frame_colors.put(posT, col);
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        ViwnVertexRenderer.PosFrameColors = pos_frame_colors;
    }

    private void loadRelsColors() {
        HashMap<Long, Color> rel_colors = new HashMap<>();

        InputStreamReader istrem = null;
        try {
            istrem = new InputStreamReader(ViWordNetService.class.getClassLoader().getResourceAsStream("relations_color.cfg"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
        }

        Properties conf = new Properties();
        try {
            if (istrem == null) {
                throw new IOException();
            }

            conf.load(istrem);
            Enumeration<Object> keys = conf.keys();

            while (keys.hasMoreElements()) {
                String rel = (String) keys.nextElement();
                String val = (String) conf.get(rel);
                Color col = null;
                try {
                    col = Color.decode(val.trim());
                } catch (Exception e) {
                    logger().warn(val + " is not a valid color");
                }

                //RelationTypes rt = RelationTypeManager.getByName(rel);
//                if (rt != null) {
//                    Collection<RelationTypes> children = rt.getChildren();
//                    if (children != null && !children.isEmpty()) {
//                        for (RelationTypeManager r : children) {
//                            rel_colors.put(r.Id(), col);
//                        }
//                    } else {
//                        rel_colors.put(rt.Id(), col);
//                    }
//                }
            }

        } catch (IOException e) {
        }
        ViwnEdgeSynset.relsColors = rel_colors;
    }

    private void loadRelationsSides() {

        InputStreamReader istrem = null;
        try {
            istrem = new InputStreamReader(ViWordNetService.class.getClassLoader().getResourceAsStream("disp_relations.cfg"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
        }

        ArrayList[] arrayLists = new ArrayList[5];
        Arrays.fill(arrayLists, new ArrayList<>());

//        ArrayList<RelationTypeManager>[] relTypes = arrayLists;
        ArrayList<RelationType>[] relTypes = arrayLists;

        Properties conf = new Properties();
        try {
            if (istrem == null) {
                throw new IOException();
            }

            conf.load(istrem);

            for (NodeDirection dir : NodeDirection.values()) {
                String val = conf.getProperty(dir.getAsString());
                if (val != null) {
                    String[] rels = val.split(",");
                    for (String s : rels) {
//                        RelationTypeManager rt = RelationTypeManager.getByName(s);
//                        if (rt != null) {
//                            Collection<RelationTypes> children = rt
//                                    .getChildren();
//                            if (children != null) {
//                                children.stream().forEach((r) -> {
//                                    relTypes[dir.ordinal()].add(r);
//                                });
//                            } else {
//                                relTypes[dir.ordinal()].add(rt);
//                            }
//                        } else {
//                            Logger.getLogger(ViWordNetPlugin.class).log(
//                                    Level.WARN, s + " is not a relation");
//                        }
                    }
                } else {
                    logger().warn("relations in direction " + dir.name() + " are not defined");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger().warn("DEFAULT RELATION TYPES");

            // adding default relations
//            relTypes[0].addAll(RelationTypeManager.getByName("holonimia").getChildren());
//            relTypes[1].addAll(RelationTypeManager.getByName("meronimia").getChildren());
//
//            relTypes[2].add(RelationTypeManager.getByName("hiperonimia"));
//            relTypes[2].add(RelationTypeManager.getByName("mieszkaniec"));
//            relTypes[2].add(RelationTypeManager.getByName("bliskoznaczność"));
//
//            relTypes[3].add(RelationTypeManager.getByName("hiponimia"));
        }

//        ArrayList<RelationTypeManager> order = new ArrayList<>();
        ArrayList<RelationType> order = new ArrayList<>();

        for (NodeDirection dir : NodeDirection.values()) {
            ViwnNodeSynset.relTypes[dir.ordinal()].addAll(relTypes[dir.ordinal()]);
            order.addAll(relTypes[dir.ordinal()]);
        }

        ViwnNodeAlphabeticComparator.order = order;
    }

    @Override
    public void onStart() {

        new Thread(() -> {
            loadRelationsSides();
            loadRelsColors();
            loadPosBackgroundColors();
            loadPosFrameColors();
            workbench.setBusy(false);
        }, "Starting").start();
        workbench.setBusy(true);

    }

    public void reload() {
        candidateSelection(objectForReload, tagForReload);
    }

    @Override
    public void synsetSelectionChangeListener(ViwnNode node) {
        if (node != null && node instanceof ViwnNodeSynset) {
            ViwnNodeSynset synset = (ViwnNodeSynset) node;

            synsetStructureView.doAction(synset.getSynset(), 1);
            synsetPropertiesView.doAction(synset.getSynset(), 1);
        }
    }

    @Override
    public void doAction(Object object, int tag) {
        if (object instanceof Sense) {
            getActiveGraphView().getUI().setCriteria(luView.getCriteria());
            LoadGraphTask task = new LoadGraphTask((Sense) object, tag);
            task.execute();
        }
    }

    class LoadGraphTask extends SwingWorker<Void, Void> {

        private final Sense unit;
        private final Integer my_tag;

        public LoadGraphTask(Sense unit, int tag) {
            this.unit = unit;
            my_tag = tag;
        }

        @Override
        public Void doInBackground() {
            workbench.setBusy(true);
            Synset rootSynset = RemoteService.synsetRemote.findSynsetBySense(unit, LexiconManager.getInstance().getLexiconsIds());
            getActiveGraphView().getUI().releaseDataSetCache();
            if (rootSynset != null) {
                Map<Long, DataEntry> entries = RemoteService.synsetRemote.prepareCacheForRootNode(rootSynset, LexiconManager.getInstance().getLexiconsIds());
                if (entries != null) {
                    getActiveGraphView().getUI().setEntrySets((HashMap<Long, DataEntry>) entries);
                }
                //pobieranie synsetu z wcześniej pobranej mapy, aby otrzymać obiekt, który ma relacje (nie są leniwymi kolekcjami)
                DataEntry dataEntry = entries.get(rootSynset.getId());
                if(dataEntry != null){
                    rootSynset = dataEntry.getSynset();
                } //TODO sprawdzić, czy nie można w inny sposób rozwiązać leniwych relacji, które spowodują błąd w LoadSynsetTask.
                LoadSynsetTask task = new LoadSynsetTask(rootSynset, unit, my_tag);
                task.execute();
            } else {
                getActiveGraphView().getUI().clear();
                Synset empty = new Synset();
                empty.setId(new Long(0));
                activeGraphView.loadSynset(empty);
                workbench.setBusy(false);
            }
            // final Synset rootSynset = RemoteUtils.synsetRemote.fetchSynsetForSense(unit, LexiconManager.getInstance().getLexicons());
//            if (rootSynset != null) {
//                getActiveGraphView().getUI().releaseDataSetCache();
//             //   HashMap<Long, DataEntry> entries = RemoteUtils.synsetRemote.prepareCacheForRootNode(rootSynset, LexiconManager.getInstance().getLexicons());
////                if (entries != null) {
////                    getActiveGraphView().getUI().setEntrySets(entries);
////                }
////                LoadSynsetTask synsetTask = new LoadSynsetTask(rootSynset, unit, my_tag);
//                synsetTask.execute();
//            } else {
//                getActiveGraphView().getUI().releaseDataSetCache();
//                getActiveGraphView().getUI().removeAllNodes();
//                Synset empty = new Synset();
//                empty.setId(new Long(0));
//                activeGraphView.loadSynset(empty);
//                workbench.setBusy(false);
//            }

            return null;
        }

        @Override
        public void done() {
        }
    }

    class LoadSynsetTask extends SwingWorker<Void, Void> {

        private final Sense unit;
        private final Integer my_tag;
        private final Synset rootSynset;

        public LoadSynsetTask(Synset rootSynset, Sense unit, Integer tag) {
            this.unit = unit;
            my_tag = tag;
            this.rootSynset = rootSynset;
        }

        @Override
        public Void doInBackground() {

            activeGraphView.loadSynset(rootSynset);
            examplesView.load_examples(unit.getWord().getWord());
            ((ViWordNetPerspective) workbench.getActivePerspective())
                    .setTabTitle("<html><font color=green>" + unit.getWord()
                            + "</font> #"
                            + (my_tag == 0 ? unit.getVariant() : my_tag)
                            + "</html>");
            kpwrExamples.load_examples(unit);
            return null;
        }

        @Override
        public void done() {
            workbench.setBusy(false);
        }
    }

    /**
     * @param object
     * @param tag
     */
    public void synsetUnitSelection(Object object, Integer tag) {
        if (object instanceof Sense) {
            Sense unit = (Sense) object;
            unitsRelationsView.loadLexicalUnit(unit);
        }
    }

    /**
     * this method is invoked when candidate word has been selected
     *
     * @param pair - Pair<String, Pos> of selected word and its part of speach
     * @param tag  - package number
     */
    public void candidateSelection(Object pair, Integer tag) {
        // PUNKT WEJŚCIA
        // A-słowo, B-część mowy, Tag-numer paczki
        objectForReload = pair;
        tagForReload = tag;
        workbench.setBusy(true);
        CandidateTask worker = new CandidateTask(pair, tag);
        worker.execute();
    }

    class CandidateTask extends SwingWorker<Void, Void> {

        Quadruple<ViwnNodeWord, ArrayList<TreeSet<ViwnNodeSynset>>, ArrayList<ViwnNodeCand>, ArrayList<ViwnNodeSynset>> result;
        ArrayList<ViwnNodeCandExtension> extensionResult = new ArrayList<>();
        final Pair<String, PartOfSpeech> p;
        final Integer tag;

        public CandidateTask(Object pair, Integer tag) {
            p = (Pair<String, PartOfSpeech>) pair;
            this.tag = tag;
        }

        @Override
        protected Void doInBackground() throws Exception {
            result = getActiveGraphView().loadCandidate(p.getA(), tag, p.getB());
            examplesView.load_examples(p.getA());
            kpwrExamples.load_examples(p.getA());
            return null;
        }

        @Override
        protected void done() {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    ((ViWordNetPerspective) workbench.getActivePerspective())
                            .setTabTitle("<html><font color=green>" + p.getA() + "</font> #" + tag + "</html>");
                    extensionResult = getActiveGraphView().loadExtensions(p.getA(), tag, p.getB());
                    getActiveGraphView().getUI().loadCandidate(result.getA(), result.getB(), result.getC(), result.getD(), extensionResult);
                    return null;
                }

                @Override
                protected void done() {
                    workbench.setBusy(false);
                }
            };
            worker.execute();
        }
    }

    /**
     * Add new item to locker, every item remember a node which it represents
     * and a <code>ViwnGraphViewUI</code> to which it belongs
     *
     * @param vn
     * @param ler
     */
    public void addToLocker(Object vn, LockerElementRenderer ler) {
        lockerView.addToLocker(vn, ler);
    }

    /**
     * <code>LockerChangeListener</code> interface implementation
     */
    // TODO: do not show message here, after return from here do something
    @Override
    public ViwnNode lockerSelectionChanged(ViwnNode vn) {
        for (ViwnGraphView vgv : graphViews) {
            if (vgv.getUI().getGraph()
                    .containsVertex(vn)) {
                setActiveGraphView(vgv.getUI().getContent());
                return vn;
            }
        }
        // in case of failure, recreate the graph
        if (graphViews.size() < GRAPH_VIEWS_LIMIT) {
            if (DialogBox.showYesNo("Nie znaleziono źródłowej wizualizacji synsetu, otworzyć nową?") == DialogBox.YES) {
                addGraphView();
                // reload active graph view
                activeGraphView.loadSynset(((ViwnNodeSynset) vn).getSynset());
                // change tab title and tool tip
                ((ViWordNetPerspective) workbench.getActivePerspective())
                        .setTabTitle(activeGraphView.getUI().getRootNode().getLabel());

                return activeGraphView.getUI().getRootNode();
            }
        }
        return vn;
    }

    /**
     * Add new graph view tab (<code>ViwnGraphView</code>), and set it to active
     * graph view tab refresh other views of this perspective
     */
    public void addGraphView() {
        if (graphViews.size() < GRAPH_VIEWS_LIMIT) {
            ViwnGraphViewUI graphUI = new ViwnGraphViewUI();
            graphUI.setOpenedFromTabIndex(perspective.leftView.getSelectedIndex());
            activeGraphView = new ViwnGraphView(workbench, Labels.VISUALIZATION, graphUI);
            activeGraphView.addSynsetSelectionChangeListener(this);
            if (isMakeRelationModeOn()) {
                graphUI.setCursor(MAKE_RELATION_CURSOR);
            }
            workbench.installView(activeGraphView, 1, perspectiveName);
            graphViews.add(activeGraphView);

            setActiveGraphView(graphUI.getContent());
        }
    }

    /**
     * Close <code>ViwnGraphView</code> producing JPanel given as an argument
     *
     * @param jp JPanel of <code>ViwnGraphView</code> which is closing
     */
    public void closeGraphView(JPanel jp) {
        for (ViwnGraphView vgv : graphViews) {
            if (vgv.getPanel().equals(jp)) {
                graphViews.remove(vgv);
                return;
            }
        }
    }

    /**
     * sets activeGraphView to graph view which <code>ViwnGraphViewUI</code>
     * produces panel jp
     *
     * @param jp (JPanel) panel from view
     * @return true when <code>JPanel jp</code> found, otherwise false
     */
    public boolean setActiveGraphView(JPanel jp) {
        for (ViwnGraphView vgv : graphViews) {
            if (vgv.getPanel().equals(jp)) {
                // set active in service
                activeGraphView = vgv;
                luView.setCriteria(activeGraphView.getUI().getCriteria());
                // refresh satellite view
                setSatteliteOwner(activeGraphView);

                // set active selected tab
                try {
                    perspective.graphView.setSelectedComponent(jp);
                    perspective.leftView.setSelectedIndex(activeGraphView.getUI().getOpenedFromTabIndex());
                } catch (IllegalArgumentException iae) {
                    /* split view makes some mess with locker */
                }

                ViwnNode root = vgv.getUI().getRootNode();
                if (root instanceof ViwnNodeWord) {
                    ViwnNodeWord word = (ViwnNodeWord) root;
                    examplesView.load_examples(word.getLabel());
                }

                return true;
            }
        }
        return false;
    }

    /**
     * set satellite view parent/owner and refresh satellite view
     *
     * @param vgv <code>ViwnGraphView</code> new satelliteView owner
     */
    public void setSatteliteOwner(ViwnGraphView vgv) {
        // refresh satellite view
        vgv.removeGraphChangeListener(satelliteGraphView);
        satelliteGraphView.setGraphViewUI(vgv.getUI());
        satelliteGraphView.refreshView();
        vgv.addGraphChangeListener(satelliteGraphView);
    }

    /**
     * Set this.perspective
     *
     * @param perspective <code>ViWoedNetPerspective</code> connected with this
     *                    service
     */
    public void setPerspective(ViWordNetPerspective perspective) {
        this.perspective = perspective;
    }

    /**
     * @return <code>ViwnGraphView</code> currently set as active
     */
    public ViwnGraphView getActiveGraphView() {
        return activeGraphView;
    }

    /**
     * gets graph view which <code>ViwnGraphViewUI</code> produces panel jp
     *
     * @param jp (JPanel) panel from view
     * @return <code>ViwnGraphView</code> which ViwnGraphViewUI produces panel
     * jp if found, null otherwise
     */
    public ViwnGraphView getGraphView(JPanel jp) {
        for (ViwnGraphView vgv : graphViews) {
            if (vgv.getPanel().equals(jp)) {
                return vgv;
            }
        }
        return null;
    }

    /**
     * @return number of graph views
     */
    public int graphViewsCount() {
        return graphViews.size();
    }

    /* MAKE AND DELETE RELATION SECTION */
    // //////////////////////////////////////////////////////////////
    /**
     * in make relation mode user can make new relation value should be changed
     * only by using of setMakeRelationMode(boolean) method
     */
    private boolean makeRelationMode = false;

    private boolean mergeSynsetsMode = false;

    /**
     * first selected object which will be connected with other
     */
    private Object first = null;
    /**
     *
     */
    private static final Cursor MAKE_RELATION_CURSOR = Cursor
            .getPredefinedCursor(Cursor.HAND_CURSOR);

    private static final Cursor MERGE_SYNSETS_CURSOR = Cursor
            .getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

    /**
     *
     */
    private static final Cursor DEFAULT_CURSOR = Cursor
            .getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    /**
     * @return true if we are making relation, false otherwise
     */
    public boolean isMakeRelationModeOn() {
        return makeRelationMode;
    }

    /**
     * change value of makeRelationOn to !makeRelationOn
     */
    public void switchMakeRelationMode() {
        setMakeRelationMode(!makeRelationMode);
    }

    /**
     * @return true if we are merging synsets, false otherwise
     */
    public boolean isMergeSynsetsModeOn() {
        return mergeSynsetsMode;
    }

    /**
     * change value of mergeSynsetsMode to !mergeSynsetsMode
     */
    public void switchMergeSynsetsMode() {
        setMergeSynsetsMode(!mergeSynsetsMode);
    }

    /**
     * @param first an object which could be connected with a relation it turns
     *              on make relation mode
     */
    public void setFirstMakeRelation(Object first) {
        this.first = first;

        setMakeRelationMode(true);
    }

    public void setFirstMergeSynsets(Object first) {
        this.first = first;

        setMergeSynsetsMode(true);
    }

    /**
     * @param set true - switch to make relation mode, false - normal mode
     */
    private void setMakeRelationMode(boolean set) {
        // set new value
        makeRelationMode = set;
        // turned on
        if (makeRelationMode) {
            for (ViwnGraphView vgv : graphViews) {
                vgv.getUI().setCursor(MAKE_RELATION_CURSOR);
            }
            lockerView.getViewUI().setCursor(MAKE_RELATION_CURSOR);
        } // turned off
        else {
            for (ViwnGraphView vgv : graphViews) {
                vgv.getUI().setCursor(DEFAULT_CURSOR);
            }
            lockerView.getViewUI().setCursor(DEFAULT_CURSOR);
        }
    }

    /**
     * @param set true - switch to synsets merge mode, false - normal mode
     */
    private void setMergeSynsetsMode(boolean set) {
        // set new value
        mergeSynsetsMode = set;
        // turned on
        if (mergeSynsetsMode) {
            for (ViwnGraphView vgv : graphViews) {
                vgv.getUI().setCursor(MERGE_SYNSETS_CURSOR);
            }
            lockerView.getViewUI().setCursor(MERGE_SYNSETS_CURSOR);
        } // turned off
        else {
            for (ViwnGraphView vgv : graphViews) {
                vgv.getUI().setCursor(DEFAULT_CURSOR);
            }
            lockerView.getViewUI().setCursor(DEFAULT_CURSOR);
        }
    }

    private ViwnNodeSynset findProposed(ViwnNodeSynset s, Long[] depth) {
        if (s instanceof ViwnNodeCand) {
            return (ViwnNodeCand) s;
        }
        ViwnNode spawner = s.getSpawner();
        if (spawner == null || !(spawner instanceof ViwnNodeSynset)) {
            return null;
        } else {
            depth[0]++;
            return findProposed((ViwnNodeSynset) spawner, depth);
        }
    }

    //    private boolean evaluateProposedConnection(Sense newUnit, IRelationType relationType, ViwnNodeSynset synset, ViwnNodeWord word) {
//
//        Synset assignedSynset = null;
//        Long distance[] = new Long[]{new Long(1)};
//        ViwnNodeSynset found = findProposed(synset, distance);
//        @SuppressWarnings("unused")
//        Synset proposedSynset = null;
//        if (found != null) {
//            proposedSynset = found.getSynset();
//        }
//
//        if (relationType.getArgumentType() == RelationArgument.LEXICAL_SPECIAL) {
//            assignedSynset = synset.getSynset();
//        } else {
//            assignedSynset = new Synset();
//        }
//
//        // Dodane zamiast oceny
//        // TODO: FIXME
//        RemoteUtils.synsetRemote.persistObject(assignedSynset);
//
//        newUnit.setSenseNumber(RemoteUtils.lexicalUnitRemote
//                .dbGetHighestVariant(word.getLabel(), LexiconManager
//                        .getInstance().getLexicons()) + 1);
//        RemoteUtils.lexicalUnitRemote.updateSense(newUnit);
//
//        if (relationType.getArgumentType() == RelationArgument.LEXICAL) {
//            if (synset instanceof ViwnNodeCandExtension) {
//                LexicalDA.addConnection(newUnit, assignedSynset);
//                ViwnNodeCandExtension ext = (ViwnNodeCandExtension) synset;
//                if (ext.getExtGraphExtension().getBase()) {
//                    if (RemoteUtils.lexicalRelationRemote.dbMakeRelation(
//                            newUnit, ext.getExtGraphExtension()
//                            .getLexicalUnit(), relationType)) {
//                        if (relationType.isAutoReverse()
//                                || (RelationsDA
//                                .getReverseRelation(relationType) != null && DialogBox
//                                .showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION,
//                                        LexicalDA.getRelationName(RelationsDA.getReverseRelation(relationType)))) == DialogBox.YES)) {
//                            RemoteUtils.lexicalRelationRemote
//                                    .dbMakeRelation(ext.getExtGraphExtension().getLexicalUnit(),
//                                            newUnit,
//                                            RelationsDA.getReverseRelation(relationType));
//                        }
//                        DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
//                        reload();
//                        return true;
//                    } else {
//                        DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
//                    }
//                } else if (RemoteUtils.lexicalRelationRemote.dbMakeRelation(ext
//                        .getExtGraphExtension().getLexicalUnit(), newUnit,
//                        relationType)) {
//                    if (relationType.isAutoReverse()
//                            || (RelationsDA
//                            .getReverseRelation(relationType) != null && DialogBox
//                            .showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION,
//                                    LexicalDA.getRelationName(RelationsDA
//                                            .getReverseRelation(relationType)))) == DialogBox.YES)) {
//                        RemoteUtils.lexicalRelationRemote.dbMakeRelation(
//                                newUnit, ext.getExtGraphExtension()
//                                .getLexicalUnit(), RelationsDA
//                                .getReverseRelation(relationType));
//                    }
//                    DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
//                    reload();
//                    return true;
//                } else {
//                    DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
//                }
//                RemoteUtils.lexicalRelationRemote.dbMakeRelation(newUnit, ext
//                        .getExtGraphExtension().getLexicalUnit(), relationType);
//            }
//        } else if (relationType.getArgumentType() == RelationArgument.LEXICAL_SPECIAL) {
//            // // New synonim relation
//            LexicalDA.addConnection(newUnit, synset.getSynset());
//
//        } else {
//
//            LexicalDA.addConnection(newUnit, assignedSynset);
//
//            if (RelationsDA.checkIfRelationExists(assignedSynset,
//                    synset.getSynset(), relationType)) {
//                DialogBox.showInformation(Messages.FAILURE_RELATION_EXISTS);
//            } else if (RemoteUtils.synsetRelationRemote.dbMakeRelation(
//                    assignedSynset, synset.getSynset(), relationType)) {
//                if (relationType.isAutoReverse()
//                        || DialogBox.showYesNo(String
//                                .format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION,
//                                        LexicalDA.getRelationName(RelationsDA
//                                                .getReverseRelation(relationType)))) == DialogBox.YES) {
//                    RemoteUtils.synsetRelationRemote.dbMakeRelation(
//                            synset.getSynset(), assignedSynset,
//                            RelationsDA.getReverseRelation(relationType));
//                }
//                DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
//                reload();
//                return true;
//            } else {
//                DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
//            }
//        }
//        reload();
//
//        return true;
//    }
    private void newCandRelation(ViwnNodeSynset synset, ViwnNodeWord word) {
//        ArrayList<Sense> parentUnits = new ArrayList<>();
//        Synset s = synset.getSynset();
//        Collection<Sense> units = RemoteUtils.lexicalUnitRemote.dbFullGetUnits(
//                s, 1, LexiconManager.getInstance().getLexicons());
//        if (units.isEmpty()) {
//            System.err.println("Nie mozna przetwarzac pustego synsetu");
//        }
//        Sense lu = (Sense) units.toArray()[0];
//        PartOfSpeech p = lu.getPartOfSpeech();
//
//        Sense toAdd = new Sense();
//        toAdd.setLemma(new Word(word.getLabel()));
//        toAdd.setPartOfSpeech(p);
//        toAdd.setDomain(DomainManager.getInstance().getByID(0));
//        parentUnits.add(toAdd);
//
//        ArrayList<Sense> childUnits = new ArrayList<>();
//        for (Sense unit : RemoteUtils.lexicalUnitRemote.dbFullGetUnits(s, 0,
//                LexiconManager.getInstance().getLexicons())) {
//            childUnits.add(unit);
//        }
//        if (childUnits.isEmpty()) {
//            DialogBox.showError("Synset docelowy nie zawiera jednostek.");
//            return;
//        }
//
//        RelationArgument objectType = null;
//        IRelationType relType = null;
//        IRelationType suggestedRelType = null;
//        Sense suggestedUnit = null;
//
//        if (synset instanceof ViwnNodeCandExtension) {
//            ViwnNodeCandExtension ext = (ViwnNodeCandExtension) synset;
//            suggestedRelType = ext.getExtGraphExtension().getRelationType();
//            suggestedUnit = ext.getExtGraphExtension().getLexicalUnit();
//        }
//
//        Domain domain = DomainManager.getInstance().getByID(0);
//        if (childUnits.size() > 0) {
//            domain = childUnits.get(0).getDomain();
//        }
//
//        // Changed to Pos of candidate instead of existing synset's
//        PartOfSpeech rightPos = word.getPos();
//
//        // New lexical unit dialog
//        Sense newUnit = NewLexicalUnitFrame.showModal(workbench,
//                workbench.getFrame(), word.getLabel(), rightPos, domain);
//        if (newUnit == null) {
//            return;
//        }
//
//        // New synset relation dialog
//        if (synset instanceof ViwnNodeCandExtension) {
//            if (!((ViwnNodeCandExtension) synset).getExtGraphExtension().getBase()) {
//                ArrayList<Sense> temp = childUnits;
//                childUnits = parentUnits;
//                parentUnits = temp;
//            }
//        }
//        IRelationType relationType = RelationTypeFrame.showModal(
//                workbench.getFrame(), objectType, rightPos, relType,
//                suggestedRelType, suggestedUnit, parentUnits, null, childUnits);
//        if (relationType == null) {
//            return;
//        }
//
//        if (evaluateProposedConnection(newUnit, relationType, synset, word)) {
//            getActiveGraphView().loadCandidate(word.getLabel(),
//                    word.getPackageNo(), word.getPos());
//        }
    }

    public void mergeSynsets(Object second) {
        if (first instanceof ViwnNodeSynset && second instanceof ViwnNodeSynset) {
            ViwnNodeSynset src = (ViwnNodeSynset) first;
            ViwnNodeSynset dst = (ViwnNodeSynset) second;

            if (src == null || dst == null || src.getId() == -1
                    || dst.getId() == -1) {
                setMergeSynsetsMode(false);
                return;
            }

            if (DialogBox.showYesNo(String.format(ToolbarViewUI.MERGE_SYNSETS,
                    src.getUnitsStr(), dst.getUnitsStr())) == DialogBox.YES) {
                RelationsDA.mergeSynsets(src.getSynset(), dst.getSynset(),
                        LexiconManager.getInstance().getLexiconsIds());
                for (ViwnGraphView gv : new ArrayList<>(graphViews)) {
                    gv.getUI().removeSynset(src);
                    gv.getUI().updateSynset(dst);
                }
                ViWordNetService s = (ViWordNetService) workbench
                        .getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService");
                src.getSynset().setId((long) -1);
                s.lockerView.refreshData();
            }
        }

        setMergeSynsetsMode(false);
    }

    /**
     * @param second object to make relation with first turns off make relation
     *               mode after it
     */
    public void makeRelation(Object second) {
        // make relation between synsets
        if (first instanceof ViwnNodeSynset && second instanceof ViwnNodeSynset) {
            ViwnNodeSynset src = (ViwnNodeSynset) first;
            ViwnNodeSynset dst = (ViwnNodeSynset) second;
            if (src == null || dst == null || src.getId() == -1 || dst.getId() == -1) {
                setMakeRelationMode(false);
                return;
            }

            if (MakeNewRelationWindow.showMakeSynsetRelationModal(workbench, src, dst)) {
                for (ViwnGraphView gv : graphViews) {
                    gv.getUI().relationAdded(src, dst);
                }
            }
        } else if (first instanceof ViwnNodeSynset
                && second instanceof ViwnNodeWord) {
            ViwnNodeWord word = (ViwnNodeWord) second;
            // if (tryLockMsg(word.getPackageNo(), word.getPartOfSpeach())) {
            newCandRelation((ViwnNodeSynset) first, word);
            // }
        } else if (second instanceof ViwnNodeSynset
                && first instanceof ViwnNodeWord) {
            ViwnNodeWord word = (ViwnNodeWord) first;
            // if (tryLockMsg(word.getPackageNo(), word.getPartOfSpeach())) {
            newCandRelation((ViwnNodeSynset) second, word);
            // }
        } // make relation between lexical units and one of lexical units from
        // synset
        else if (first instanceof Sense && second instanceof ViwnNodeSynset) {
            // show JPopupMenu with chose second lexical unit
            Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
            javax.swing.JPopupMenu jpm = new javax.swing.JPopupMenu();
//            List<Sense> senses = RemoteUtils.lexicalUnitRemote.dbFastGetUnits(
//                    ((ViwnNodeSynset) second).getSynset(), LexiconManager
//                    .getInstance().getLexicons());
//            for (final Sense lu : senses) {
//                jpm.add(new JMenuItem(new AbstractAction(lu.toString()) {
//
//                    private static final long serialVersionUID = 712639812536152L;
//
//                    @Override
//                    public void actionPerformed(ActionEvent ae) {
//                        if (MakeNewLexicalRelationFrame
//                                .showMakeLexicalRelationModal(workbench,
//                                        (Sense) first, lu)) {
//                            unitsRelationsView.refresh();
//                        }
//                    }
//                }));
//            }
            jpm.show(workbench.getFrame(), mouseLoc.x
                    - workbench.getFrame().getX(), mouseLoc.y
                    - workbench.getFrame().getY());
            // make relation between lexical units
        } else if (first instanceof Sense && second instanceof Sense) {
            if (MakeNewLexicalRelationWindow.showMakeLexicalRelationModal(
                    workbench, (Sense) first, (Sense) second)) {
                unitsRelationsView.refresh();
            }
        }
        setMakeRelationMode(false);
    }

    /**
     * @param relations collection of edges of relations to remove
     */
    public void removeRelation(
            Collection relations) {
        if (relations != null && !relations.isEmpty()) {
            if (relations.iterator().next() instanceof ViwnEdgeSynset) {
                Collection<ViwnEdge> c = DeleteRelationWindow
                        .showDeleteSynsetDialog(workbench.getFrame(), relations);
                for (ViwnGraphView vgv : graphViews) {
                    vgv.getUI().relationDeleted(c);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String graph_export_dir = "graph_export";
//        Collection<Integer> pkgs = RemoteUtils.extGraphRemote
//                .GetPackages(PosManager.getInstance().decode("rzeczownik"));
//
//        for (Integer pkg : pkgs) {
//            Collection<String> words = RemoteUtils.extGraphRemote
//                    .dbGetNewWords(pkg,
//                            PosManager.getInstance().decode("rzeczownik"));
//            String path = new File(graph_export_dir, pkg.toString()).toString();
//            new File(path).mkdirs();
//            for (String word : words) {
//                candidateSelection(new Pair<>(word,
//                        PosManager.getInstance().decode("rzeczownik")), pkg);
//                String fname = new File(path, word + ".png").toString();
//                getActiveGraphView().getUI().saveToFile(fname);
//                System.out.println("exported: " + fname);
//            }
//        }
    }

    public void clearAllViews() {
        graphViews.stream().map((vgv) -> {
            vgv.getUI().clear();
            return vgv;
        }).forEach((vgv) -> {
            vgv.getUI().getCriteria().setSense(new ArrayList<>());
        });
    }

    public void reloadCurrentListSelection() {
        luView.refreshData();
    }

    public SynsetView getSynsetView() {
        return synsetView;
    }

    public LexicalUnitsView getLexicalUnitsView() {
        return luView;
    }
}
