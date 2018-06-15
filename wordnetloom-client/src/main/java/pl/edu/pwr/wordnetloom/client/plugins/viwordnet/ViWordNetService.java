package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import com.alee.laf.menu.WebMenu;
import com.google.common.eventbus.Subscribe;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.LexicalUnitsView;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.SynsetPropertiesView;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.SynsetStructureView;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.SynsetView;
import pl.edu.pwr.wordnetloom.client.plugins.relations.da.RelationsDA;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateGraphEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateSynsetPropertiesEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateSynsetUnitsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.LockerChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.DeleteRelationWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.MakeNewLexicalRelationWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.MakeNewRelationWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.misc.SimpleListenerWrapper;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
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

    private final static int GRAPH_VIEWS_LIMIT = 6;

    private static final Cursor MAKE_RELATION_CURSOR = Cursor
            .getPredefinedCursor(Cursor.HAND_CURSOR);

    private static final Cursor MERGE_SYNSETS_CURSOR = Cursor
            .getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

    private static final Cursor DEFAULT_CURSOR = Cursor
            .getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private final List<ViwnGraphView> graphViews = new ArrayList<>(GRAPH_VIEWS_LIMIT);

    private SynsetData synsetData = new SynsetData();
    private String perspectiveName = null;
    private ViWordNetPerspective perspective = null;
    private ViwnGraphView activeGraphView = null;
    private LexicalUnitsView luView = null;
    private SynsetView synsetView = null;

    private ViwnLexicalUnitRelationsView unitsRelationsView = null;
    private SynsetStructureView synsetStructureView = null;
    private SynsetPropertiesView synsetPropertiesView = null;
    private ViwnSatelliteGraphView satelliteGraphView = null;
    private ViwnExamplesView examplesView = null;
    private ViwnExampleKPWrView kpwrExamples = null;

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
     * @param workbench       <code>Workbench</code> of this
     * @param perspectiveName perspective name
     * @param perspective     <code>ViWordNetPerspective</code> of this
     */
    public ViWordNetService(Workbench workbench, String perspectiveName,
                            ViWordNetPerspective perspective) {
        super(workbench);
        this.perspectiveName = perspectiveName;
        this.perspective = perspective;

        Application.eventBus.register(this);
    }

    public SynsetData getSynsetData() {
        return synsetData;
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

        satelliteGraphView = new ViwnSatelliteGraphView(workbench, Labels.PREVIEW, graphUI);
        workbench.installView(satelliteGraphView, ViWordNetPerspective.SPLIT_RIGHT_TOP_VIEW, perspectiveName);
        activeGraphView.addGraphChangeListener(satelliteGraphView);

        examplesView = new ViwnExamplesView(workbench, Labels.EXAMPLES);
        kpwrExamples = new ViwnExampleKPWrView(workbench, Labels.KPWR, graphUI);
        workbench.installView(examplesView, ViWordNetPerspective.SPILT_LOCKER_VIEW, perspectiveName);
        workbench.installView(kpwrExamples, ViWordNetPerspective.SPILT_LOCKER_VIEW, perspectiveName);

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

    private void loadPartOfSpeechBackgroundColors() {
        ViwnNodeSynset.PosBgColors = PartOfSpeechManager.getInstance().getBackgroundColors();
    }


    private void loadRelationsColors() {
        ViwnEdgeSynset.relsColors = RelationTypeManager.getInstance().getRelationsColors();
    }

    @Override
    public void onStart() {

        new Thread(() -> {
            loadRelationsColors();
            loadPartOfSpeechBackgroundColors();
            workbench.setBusy(false);
        }, "Starting").start();
        workbench.setBusy(true);

    }

    @Override
    public void synsetSelectionChangeListener(ViwnNode node) {
        if (node != null && node instanceof ViwnNodeSynset) {
            ViwnNodeSynset synset = (ViwnNodeSynset) node;
            getActiveGraphView().getUI().setSelectedNode(synset);

            sendSelectionChangeEvents(synset.getSynset());
        }
    }

    private void sendSelectionChangeEvents(Synset synset) {
        Application.eventBus.post(new UpdateSynsetUnitsEvent(synset));
        Application.eventBus.post(new UpdateSynsetPropertiesEvent(synset));
    }

    @Override
    public void doAction(Object object, int tag) {
        getActiveGraphView().getUI().setCriteria(luView.getCriteria());
        if (object instanceof Sense) {
            new LoadSenseTask((Sense) object).execute();
        } else if (object instanceof Synset) {
            new LoadSynsetTask((Synset) object).execute();
        }
    }

    @Subscribe
    public void updateGraph(UpdateGraphEvent event) {
        if (event.getSense() == null) {
            for(ViwnGraphView gv : graphViews){
                Synset synset = gv.getUI().getRootSynset();
                new LoadSynsetTask(synset).execute();
            }
//            Synset synset = getActiveGraphView().getUI().getRootSynset();
//            new LoadSynsetTask(synset).execute();
        } else {
            new LoadSenseTask(event.getSense()).execute();
        }
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
        // in case of failure, recreate the visualisation
        if (graphViews.size() < GRAPH_VIEWS_LIMIT) {
            if (DialogBox.showYesNo("Nie znaleziono źródłowej wizualizacji synsetu, otworzyć nową?") == DialogBox.YES) {
                addGraphView();
                // reload active visualisation view
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
     * Add new visualisation view tab (<code>ViwnGraphView</code>), and set it to active
     * visualisation view tab refresh other views of this perspective
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
            activeGraphView.getUI().getLayout().setSize(new Dimension(500, 500));
            workbench.installView(activeGraphView, 1, perspectiveName);
            graphViews.add(activeGraphView);
            activeGraphView.getUI().center();
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
     * sets activeGraphView to visualisation view which <code>ViwnGraphViewUI</code>
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

    /* MAKE AND DELETE RELATION SECTION */
    // //////////////////////////////////////////////////////////////

    /**
     * @return <code>ViwnGraphView</code> currently set as active
     */
    public ViwnGraphView getActiveGraphView() {
        return activeGraphView;
    }

    /**
     * gets visualisation view which <code>ViwnGraphViewUI</code> produces panel jp
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
     * @return number of visualisation views
     */
    public int graphViewsCount() {
        return graphViews.size();
    }

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
        makeRelationMode = set;
        setCursor(MAKE_RELATION_CURSOR, makeRelationMode);
    }

    private void setCursor(Cursor cursorType, boolean value) {
        Cursor cursor = value ? cursorType : DEFAULT_CURSOR;
        for (ViwnGraphView viwnGraphView : graphViews) {
            viwnGraphView.getUI().setCursor(cursor);
        }
    }

    /**
     * @param set true - switch to synsets merge mode, false - normal mode
     */
    private void setMergeSynsetsMode(boolean set) {
        mergeSynsetsMode = set;
        setCursor(MERGE_SYNSETS_CURSOR, mergeSynsetsMode);
    }

    public void mergeSynsets(Object second) {
        if (first instanceof ViwnNodeSynset && second instanceof ViwnNodeSynset) {
            ViwnNodeSynset src = (ViwnNodeSynset) first;
            ViwnNodeSynset dst = (ViwnNodeSynset) second;

            if (src.getId() == -1 || dst.getId() == -1) {
                setMergeSynsetsMode(false);
                return;
            }

            final String MERGE_SYNSETS = "<html>Czy na pewno chcesz połączyć synsety:<br>"
                    + "1. <font color=\"blue\">%s</font><br>"
                    + "2. <font color=\"blue\">%s</font> ?</html>";

            if (DialogBox.showYesNo(String.format(MERGE_SYNSETS,
                    src.getUnitsStr(), dst.getUnitsStr())) == DialogBox.YES) {

//                RelationsDA.mergeSynsets(src.getSynset(), dst.getSynset(),
//                        LexiconManager.getInstance().getLexiconsIds());

                RemoteService.synsetRemote.merge(dst.getSynset(), src.getSynset());

                Application.eventBus.post(new UpdateGraphEvent(null));
                for(ViwnGraphView gv: graphViews){
                    gv.getUI().removeSynset(src);
                }
//                for (ViwnGraphView gv : new ArrayList<>(graphViews)) {
//                    gv.getUI().removeSynset(src);
//                    gv.getUI().updateSynset(dst);
//                }
//
//
//                ViWordNetService s = ServiceManager.getViWordNetService(workbench);
//                src.getSynset().setId((long) -1);
            }
        }
        setMergeSynsetsMode(false);
    }

    /**
     * @param second object to make relation with first turns off make relation
     *               mode after it
     */
    public void makeRelation(Object second) {
        if (first instanceof ViwnNodeSynset && second instanceof ViwnNodeSynset) {
            makeSynsetRelation((ViwnNodeSynset) second);
        } else if (first instanceof Sense && second instanceof ViwnNodeSynset) {
            makeUnitRelation((Sense) first, (ViwnNodeSynset) second);
        } else if (first instanceof Sense && second instanceof Sense) {
            makeUnitRelation((Sense) first, (Sense) second);
        }
        setMakeRelationMode(false);
    }

    /**
     * @param relations collection of edges of relations to remove
     */
    public void removeRelation(Collection relations) {
        if (relations != null && !relations.isEmpty()) {
            if (relations.iterator().next() instanceof ViwnEdgeSynset) {
                Collection<ViwnEdge> c = DeleteRelationWindow
                        .showDeleteSynsetDialog(workbench.getFrame(), relations);
                ViwnEdgeSynset edgeSynset;
                for (ViwnEdge edge : c) {
                    edgeSynset = (ViwnEdgeSynset) edge;
                    synsetData.removeRelation(edgeSynset.getSynsetRelation());
                }

                for (ViwnGraphView vgv : graphViews) {
                    vgv.getUI().relationDeleted(c);
                }
            }
        }
    }

    private void makeSynsetRelation(ViwnNodeSynset second) {
        ViwnNodeSynset src = (ViwnNodeSynset) first;
        ViwnNodeSynset dst = second;
        if (src == null || dst == null || src.getId() == -1 || dst.getId() == -1) {
            setMakeRelationMode(false);
            return;
        }

        if (MakeNewRelationWindow.showModalAndSaveRelation(workbench, src, dst)) {
            for (ViwnGraphView gv : graphViews) {
                gv.getUI().relationAdded(src, dst);
            }
        }
    }

    private void makeUnitRelation(Sense sense, ViwnNodeSynset nodeSynset) {
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
//                                .showModalAndSaveRelation(workbench,
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
    }

    private void makeUnitRelation(Sense sense1, Sense sense2) {
        if (MakeNewLexicalRelationWindow.showModalAndSaveRelation(
                workbench, sense1, sense2)) {
            unitsRelationsView.refresh();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

    public void reloadCurrentListSelection() {
        luView.refreshData();
    }

    public SynsetView getSynsetView() {
        return synsetView;
    }

    public LexicalUnitsView getLexicalUnitsView() {
        return luView;
    }

    class LoadSynsetTask extends SwingWorker<Void, Void> {

        protected Synset synset;

        public LoadSynsetTask(Synset synset) {
            this.synset = synset;
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (synset == null) {
                getActiveGraphView().getUI().clear();
                return null;
            }
            workbench.setBusy(true);
            loadSynset(synset);
            return null;
        }

        @Override
        public void done() {
            workbench.setBusy(false);
            Application.eventBus.post(new UpdateSynsetPropertiesEvent(synset));
            Application.eventBus.post(new UpdateSynsetUnitsEvent(synset));
        }

        protected void loadSynset(Synset synset) {
            getActiveGraphView().getUI().clearNodeCache();
            if (synset != null) {
                synsetData.load(synset, LexiconManager.getInstance().getUserChosenLexiconsIds());
                // loading full object. Original rootSynset does't have partOfSpeech and other required data
                synset = synsetData.getSynsetById(synset.getId());
                loadGraph(synset);
            }
        }

        private void loadGraph(Synset rootSynset) {
            activeGraphView.loadSynset(rootSynset);
            Sense unit = rootSynset.getSenses().get(0);
            examplesView.load_examples(unit.getWord().getWord());
            ((ViWordNetPerspective) workbench.getActivePerspective())
                    .setTabTitle("<html><font color=green>" + unit.getWord()
                            + "</font> #"
//                            + (my_tag == 0 ? unit.getVariant() : my_tag)
                            + "</html>");
            kpwrExamples.load_examples(unit);
        }
    }

    class LoadSenseTask extends LoadSynsetTask {

        private Sense sense;

        public LoadSenseTask(Sense sense) {
            super(null);
            this.sense = sense;
        }

        @Override
        protected Void doInBackground() {
            if (sense == null) {
                getActiveGraphView().getUI().clear();
                return null;
            }
            workbench.setBusy(true);
            synset = RemoteService.synsetRemote.findSynsetBySense(sense, LexiconManager.getInstance().getLexiconsIds());
            loadSynset(synset);
            return null;
        }

    }
}
