package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.AbstractListFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.UnitsListFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.models.UnitsInSynsetListModel;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetPerspective;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.ValueContainer;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenersContainer;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.SenseTooltipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.exception.InvalidLexiconException;
import pl.edu.pwr.wordnetloom.synset.exception.InvalidPartOfSpeechException;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * klasa opisujacy wyglada okienka z strukutrą synsetu
 *
 * @author Max
 */
public class SynsetStructureViewUI extends AbstractViewUI implements
        ActionListener, ListSelectionListener, CaretListener, MouseListener, Loggable {

    /**
     * usunieto jednostke z synsetu
     */
    public static final int UNIT_REMOVED = 3;
    /**
     * nowa jednostka zostala stworzona (dodane do systemu)
     */
    public static final int UNIT_CREATED = 4;
    /**
     * zmienilo sie zaznaczenie na liscie
     */
    private static final int LIST_SELECTION_CHANGED = 2;
    private final SimpleListenersContainer clickListeners = new SimpleListenersContainer();
    private final SimpleListenersContainer synsetUpdateListeners = new SimpleListenersContainer();
    private final UnitsInSynsetListModel listModel = new UnitsInSynsetListModel();
    private final ViwnGraphViewUI graphUI;
    ArrayList<Sense> lastSelectedUnits = new ArrayList<>();
    private ViWordNetService viWordNetService;
    private WebList unitsList;
    private WebTextField synsetID;
    private WebTextField princentonID;
    private WebTextField iliID;
    private WebLabel synsetOwner;
    private WebLabel isAbstract;
    private MButton buttonUp, buttonDown, buttonAdd, buttonDelete,
            buttonRelations, buttonSwitchToLexicalPerspective, buttonToNew;
    private Collection<Sense> lastUnits = null;
    private MTextArea commentValue = null;
    private Synset lastSynset = null;


    public SynsetStructureViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
    }

    /**
     * formatowanie wartości, tak aby nie bylo nulla
     *
     * @param value - wartość wejsciowa
     * @return wartośc wjesciowa lub "brak danych" gdy był null
     */
    private static String formatValue(String value) {
        return (value == null || value.length() == 0) ? Labels.VALUE_UNKNOWN
                : value;
    }

    /**
     * odczytanie synsetu
     *
     * @return ostatni synset
     */
    public Synset getLastSynset() {
        return lastSynset;
    }

    @Override
    protected void initialize(WebPanel content) {
        // ustawienie layoutu
        content.setLayout(new RiverLayout());

        synsetOwner = new WebLabel("");

        // synset id
        synsetID = new WebTextField("");
        synsetID.setEditable(false);
        synsetID.setDrawBackground(false);
        synsetID.setDrawBorder(false);
        synsetID.setDrawShade(false);

        princentonID = new WebTextField("");
        princentonID.setEditable(false);
        princentonID.setDrawBackground(false);
        princentonID.setDrawBorder(false);
        princentonID.setDrawShade(false);

        iliID = new WebTextField("");
        iliID.setEditable(false);
        iliID.setDrawBorder(false);
        iliID.setDrawBackground(false);
        iliID.setDrawShade(false);

        refreshData(null);

        unitsList = new ToolTipList(workbench, listModel, new SenseTooltipGenerator());
        unitsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        unitsList.addListSelectionListener(this);
        unitsList.addMouseListener(this);
        unitsList.setEnabled(false);

        commentValue = new MTextArea("");
        commentValue.addCaretListener(this);
        commentValue.setRows(3);
        commentValue.setEnabled(false);

        isAbstract = new WebLabel();

        buttonUp = MButton.buildUpButton()
                .withToolTip(Hints.MOVE_UNIT_UP)
                .withEnabled(false)
                .withActionListener(this);

        installViewScopeShortCut(buttonUp, KeyEvent.CTRL_MASK, KeyEvent.VK_UP);

        buttonDown = MButton.buildDownButton()
                .withToolTip(Hints.MOVE_UNIT_DOWN)
                .withEnabled(false)
                .withActionListener(this);

        installViewScopeShortCut(buttonDown, KeyEvent.CTRL_MASK,
                KeyEvent.VK_DOWN);

        buttonAdd = MButton.buildAddButton()
                .withEnabled(false)
                .withToolTip(Hints.ADD_UNITS)
                .withActionListener(this);

        installViewScopeShortCut(buttonAdd, 0, KeyEvent.VK_INSERT);

        buttonDelete = MButton.buildDeleteButton()
                .withToolTip(Hints.DETACH_UNIT_FORM_SYNSET)
                .withEnabled(false)
                .withActionListener(this);

        installViewScopeShortCut(buttonDelete, 0, KeyEvent.VK_DELETE);

        buttonToNew = new MButton(this)
                .withIcon(FontAwesome.SIGN_IN)
                .withEnabled(false)
                .withToolTip(Hints.CREATE_SYNSET_AND_MOVE_SELECTED_UNITS);

        installViewScopeShortCut(buttonToNew, 0, KeyEvent.VK_N);

        buttonRelations = new MButton(this)
                .withIcon(FontAwesome.REFRESH)
                .withToolTip(Hints.ADD_RELATION)
                .withEnabled(false);

        installViewScopeShortCut(buttonRelations, KeyEvent.CTRL_MASK,
                KeyEvent.VK_R);

        buttonSwitchToLexicalPerspective = new MButton(this)
                .withEnabled(false)
                .withToolTip(Hints.SWITCH_TO_UNIT_PERSPECTIVE)
                .withIcon(FontAwesome.EXCHANGE);

        installViewScopeShortCut(buttonSwitchToLexicalPerspective,
                KeyEvent.CTRL_MASK, KeyEvent.VK_L);

        MButtonPanel buttonsPanel = new MButtonPanel(buttonUp, buttonDown, buttonAdd, buttonDelete, buttonToNew)
                .withVerticalLayout();

        // dodanie do okna
        content.add("", new MLabel(Labels.LEXICAL_UNITS_IN_SYSET_COLON, 'y',
                unitsList));

        WebPanel synsetContentPanel = new WebPanel();
        synsetContentPanel.setLayout(new RiverLayout(0, 0));

        WebScrollPane scroll = new WebScrollPane(unitsList);
        scroll.setMaximumSize(new Dimension(0, 140));
        scroll.setMinimumSize(new Dimension(0, 140));
        scroll.setPreferredSize(new Dimension(0, 140));

        synsetContentPanel.add("hfill", scroll);
        synsetContentPanel.add("", buttonsPanel);

        content.add("br hfill", synsetContentPanel);

        WebPanel properties = new WebPanel();
        properties.setLayout(new RiverLayout(0, 0));

        properties.add("br vtop", new JLabel(Labels.COMMENT_COLON));
        properties.add("br", commentValue);

        properties.add("br", isAbstract);

        properties.add("br", new WebLabel("Synset Id:"));
        properties.add("", synsetID);

        properties.add("br", new WebLabel("Princeton Id:"));
        properties.add("", princentonID);

        properties.add("br", new WebLabel("ILI Id:"));
        properties.add("", iliID);

        properties.add("br", new WebLabel(Labels.OWNER_COLON));
        properties.add("", synsetOwner);

        WebScrollPane scr = new WebScrollPane(properties);
        scr.setDrawBorder(false);

        content.add("br vfill hfill", scr);
    }

    private void setSplitPosition(int newSplitPosition) {
        lastSynset.setSplit(newSplitPosition);
        lastSynset = RemoteService.synsetRemote.save(lastSynset);
        listModel.setSplitPosition(newSplitPosition);
    }

    private void moveUnitUp(int senseIndex) {
        moveUnit(senseIndex, senseIndex - 1, true);
    }

    private void moveUnitDown(int senseIndex) {
        moveUnit(senseIndex, senseIndex + 1, false);
    }

    private void moveUnit(int sourceIndex, int destIndex, boolean moveUp) {
        int splitPosition = listModel.getLineSplitPosition();
        if (sourceIndex == splitPosition) {
            setSplitPosition(destIndex);
        } else if (destIndex == splitPosition) {
            int newSplitPosition = moveUp ? destIndex + 1 : destIndex - 1;
            setSplitPosition(newSplitPosition);
        } else {
            exchangeUnitsInSynset(sourceIndex, destIndex, splitPosition, listModel);
        }

        unitsList.updateUI();
        int newSelectedPosition = moveUp ? sourceIndex - 1 : sourceIndex + 1;
        unitsList.setSelectedIndex(newSelectedPosition);
        // if move element to first position, we must update synset name on the graph (replace label)
        if (sourceIndex == 0 || destIndex == 0) {
            updateNodeNameOnGraph();
        }
    }

    private void exchangeUnitsInSynset(int senseIndex1, int senseIndex2, int splitPosition, UnitsInSynsetListModel listModel) {
        assert senseIndex1 > 0 && senseIndex2 > 0;
        assert senseIndex1 < listModel.getSize() && senseIndex2 < listModel.getSize();

        Sense sense1 = listModel.getObjectAt(senseIndex1);
        Sense sense2 = listModel.getObjectAt(senseIndex2);

        int sense1Position = sense1.getSynsetPosition();
        int sense2Position = sense2.getSynsetPosition();
        // exchange position of units in synset and saving in database
        sense1.setSynsetPosition(sense2Position);
        sense2.setSynsetPosition(sense1Position);
        RemoteService.senseRemote.save(sense1);
        RemoteService.senseRemote.save(sense2);
        // exchange position on the list
        int indexOnList1 = senseIndex1 >= splitPosition ? senseIndex1 - 1 : senseIndex1;
        int indexOnList2 = senseIndex2 >= splitPosition ? senseIndex2 - 1 : senseIndex2;
        List<Sense> sensesList = new ArrayList<>(listModel.getCollection());
        Collections.swap(sensesList, indexOnList1, indexOnList2);
        listModel.setCollection(sensesList);
    }

    /**
     * Update synset label on the graph from name of first (head) unit in synset
     */
    private void updateNodeNameOnGraph() {
        ViwnNode node = graphUI.getSelectedNode();
        if (node != null && node instanceof ViwnNodeSynset) {
            ViwnNodeSynset s = (ViwnNodeSynset) node;
            // pobieramy głowę synsetu, ponieważ jednostki na liście nie mają zaadowanej domeny, która jest potrzebna do stworzenia opisu węza
            // we get head of synset(first sense), because we need domain to create label. Senses in list haven't domain
            Sense firstSense = RemoteService.senseRemote.findHeadSenseOfSynset(lastSynset.getId());
            getViWordNetService().getSynsetData().changeLabel(lastSynset.getId(), firstSense);
            s.setLabel(null);
            graphUI.updateSynset(s);
        }
    }

    private void addUnitToSynset() {
        Point location = buttonAdd.getLocationOnScreen();
        ValueContainer<Boolean> created = new ValueContainer<>(false);

        Rectangle r = workbench.getFrame().getBounds();
        int x = r.x + r.width - AbstractListFrame.WIDTH / 2 - 50;

        Collection<Sense> selectedUnits = UnitsListFrame.showModal(
                workbench, x, location.y, true,
                LexicalDA.getPos(lastSynset, LexiconManager.getInstance().getUserChosenLexiconsIds()), created);

        if (created.getValue()) {
            clickListeners.notifyAllListeners(lastSynset, UNIT_CREATED);
        }
        if (selectedUnits != null) {
            // okienko z testami dla relacji, nie wyswietla gdy nie ma
            // takich relacji w bazie
//                if (LexicalDA.areRelations(RelationArgument.LEXICAL_SPECIAL)) {
//                    IRelationType rel;
//                    rel = RelationTypeFrame.showModalAndSaveRelation(workbench,
//                            RelationArgument.LEXICAL_SPECIAL, LexicalDA.getPos(lastSynset, LexiconManager.getInstance().getLexicons()),
            //  RemoteUtils.lexicalUnitRemote.dbFastGetUnits(lastSynset, LexiconManager.getInstance().getLexicons()), selectedUnits);
            // if (rel == null) {
            //      return;
            // }
            for (Sense selectedUnit : selectedUnits) {
                // dodanie powiazanie
                selectedUnit.setSynset(lastSynset);

                try {
                    RemoteService.synsetRemote.addSenseToSynset(selectedUnit, lastSynset);
                } catch (InvalidPartOfSpeechException poe) {
                    logger().error("Error", poe);
                } catch (InvalidLexiconException lox) {
                    logger().error("Error", lox);
                }
            }
            Collection<Sense> sensesInSynset = RemoteService.senseRemote.findBySynset(lastSynset, LexiconManager.getInstance().getLexiconsIds());
            listModel.setCollection(sensesInSynset); //TODO sprawdzić, czy nie da rady zrobić tego bez pobierania jednostek
            unitsList.updateUI();
            getViWordNetService().refreshViews();
        }
    }

    private void deleteUnitFromSynset() {
        Collection<Sense> selectedUnits = getSelectedUnits();
        assert !selectedUnits.isEmpty();

        int answer;
        int unitsCount = lastSynset.getSenses().size();
        boolean deleteSynset;
        if (selectedUnits.size() == unitsCount) {
            answer = DialogBox.showYesNo(Messages.QUESTION_DELETE_SYNSET);
            deleteSynset = true;
        } else {
            answer = DialogBox.showYesNo(Messages.QUESTION_DETACH_LEXICAL_UNITS_FROM_SYNSET);
            deleteSynset = false;
        }

        if (answer == DialogBox.YES) {
            RemoteService.synsetRemote.deleteSensesFromSynset(selectedUnits, lastSynset);
            // usunięcie jednostki z listy
            Collection<Sense> sensesOnList = listModel.getCollection();
            sensesOnList.removeAll(selectedUnits);
            listModel.setCollection(sensesOnList);
            if (deleteSynset) {
                RemoteService.synsetRemote.delete(lastSynset);
                listModel.setCollection(null);
//                refreshData(lastSynset);
                //TODO zrobić usunięcie synsetu z grafu
                getViWordNetService().refreshViews();
            } else if (lastSynset.getSplit() > listModel.getSize()) {
                // jeżeli po usunięciu jednostek podział jest większy niż liczba elementów na liście, należy przesunąć podział na ostatnie miejsce
                updateSplitPosition(listModel.getSize());
            }
            unitsList.updateUI();
        }
    }

    private void updateSplitPosition(int newSplitPosition) {
        lastSynset.setSplit(newSplitPosition);
        listModel.setSplitPosition(newSplitPosition);
        RemoteService.synsetRemote.save(lastSynset);
    }

    private void moveToNewSynset() {
        //TODO można dodać ograniczniki, sprawdzające warunki wstępne
        int unitsInSynsetCount = listModel.getSize();
        Collection<Sense> selectedUnits = getSelectedUnits();
        // nie można przenieść wszystkich jednostek z synsetu
        if (selectedUnits.size() == unitsInSynsetCount) {
            DialogBox.showError(Messages.ERROR_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET);
            return;
        }
        // wyświetlenie okienka do ustalenia relacji między starym i nowym synsetem
        PartOfSpeech partOfSpeech = listModel.getObjectAt(0).getPartOfSpeech();

        RelationType relationType = RelationTypeFrame.showModal(workbench, RelationArgument.SYNSET_RELATION, partOfSpeech, lastSynset.getSenses(), selectedUnits);

        if (relationType == null) {
            return;
        }

        Synset synsetToSave = new Synset();
        Lexicon lexicon = selectedUnits.iterator().next().getLexicon();
        synsetToSave.setLexicon(lexicon);

        Synset newSynset = RemoteService.synsetRemote.save(synsetToSave);

        for (Sense sense : selectedUnits) {
            try {
                RemoteService.synsetRemote.addSenseToSynset(sense, newSynset);
            } catch (InvalidPartOfSpeechException poe) {
                logger().error("Error", poe);
            } catch (InvalidLexiconException lox) {
                logger().error("Error", lox);
            }
        }

        RemoteService.synsetRelationRemote.makeRelation(lastSynset, newSynset, relationType);
        // usunięcie przeniesionych jednostek z listy
        Collection<Sense> unitsInSynset = listModel.getCollection();
        unitsInSynset.removeAll(selectedUnits);
        listModel.setCollection(unitsInSynset);

        if (lastSynset.getSplit() > listModel.getSize()) {
            updateSplitPosition(listModel.getSize());
        }
        unitsList.updateUI();

        //TODO zrobić rysowanie grafu. Być może będzie zrobić ładowanie całęgo grafu od nowa
        getViWordNetService().getSynsetData().createData(newSynset);
        graphUI.refreshView(lastSynset); //TODO tutaj jakoś będzie trzeba chyba sprawdzić
        graphUI.recreateLayout();
        getViWordNetService().refreshViews();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int index = unitsList.getSelectedIndex(); // odczytanie zaznaczenia
        if (event.getSource() == buttonUp) { // czy przycisk W GÓRE
            moveUnitUp(index);
        } else if (event.getSource() == buttonDown) { // czy przycos W DÓŁ
            moveUnitDown(index);
        } else if (event.getSource() == buttonAdd) { // dodanie nowego leksemu
            addUnitToSynset();
        } else if (event.getSource() == buttonDelete) {
            deleteUnitFromSynset();
        } else if (event.getSource() == buttonToNew) {
            moveToNewSynset();
        }
        //TODO sprawdzić czy zostały jeszcze jakieś przyciski do obsłużenia
        // odswiezenie danych

//                refreshData(lastSynset);
//                getViWordNetService().refreshViews();
//        }
//        } else if (event.getSource() == buttonDelete) { // usuniecie zaznaczonej
//            // jednostki
//            Collection<Sense> selectedUnits = getSelectedUnits();
//            int result = DialogBox.YES;
//
//            // usuniecie wszystkich jednostek
//            List<SenseToSynset> senseToSynsetList = LexicalDA
//                    .getSenseToSynsetBySynset(lastSynset);
//            if (selectedUnits.size() == senseToSynsetList.size()
//                    && RemoteUtils.synsetRelationRemote
//                    .dbGetRelationCountOfSynset(lastSynset) > 0) {
//                result = DialogBox.showYesNo(Messages.QUESTION_DELETE_SYNSET);
//            } else {
//                result = DialogBox
//                        .showYesNo(Messages.QUESTION_DETACH_LEXICAL_UNITS_FROM_SYNSET);
//            }

//            if (result == DialogBox.YES) { // odpowiedziano twierdzaco
//                lastSynset = LexicalDA.deleteConnections(selectedUnits,
//                        lastSynset);
//                if (lastSynset != null) {
//                    refreshData(lastSynset);
//                }
//                ViwnNode node = getViWordNetService().getActiveGraphView()
//                        .getUI().getSelectedNode();
//                if (node != null && node instanceof ViwnNodeSynset) {
//                    ((ViwnNodeSynset) node).setLabel(null).getSynset()
//                            .setId((long) -1);
//                }
//            }
//            // powiadomienie o usunieciu jednostki
//            clickListeners.notifyAllListeners(lastSynset, UNIT_REMOVED);
//            refreshData(lastSynset);
//            getViWordNetService().refreshViews();
//        } else if (event.getSource() == buttonRelations) { // wywoalanie relacji
//            int[] selectedItems = unitsList.getSelectedIndices();
//            // odczytanie zaznaczonych jednostek
//            Collection<Sense> lexicalUnits = new ArrayList<>();
//            if (selectedItems != null) {
//                for (int i : selectedItems) {
//                    lexicalUnits.add(listModel.getObjectAt(i));
//                }
//            }
//            Object[] pairs = {lexicalUnits, lastSynset};
//
//            // powiadomienie zainteresowanych
//            clickListeners.notifyAllListeners(pairs, SYNSET_RELATIONS);
//        } else if (event.getSource() == buttonSwitchToLexicalPerspective) {
//            int[] selectedItems = unitsList.getSelectedIndices();
//            // odczytanie zaznaczonych jednostek
//            if (selectedItems != null) {
//                for (int i : selectedItems) {
//                    Sense unit = listModel.getObjectAt(i);
//                    // powiadomienie zainteresowanych
//                    clickListeners
//                            .notifyAllListeners(unit, LEXICAL_PERSPECTIVE);
//                }
//            }
//        } else if (event.getSource() == buttonToNew) {
//            lastSelectedUnits.removeAllNodes();
//            lastSelectedUnits.add(listModel.getObjectAt(index));
        // czy nie ma przypadkiem do przeniesienia wszystkich jednostek
//            List<Sense> list = RemoteUtils.lexicalUnitRemote
//                    .dbFastGetUnits(lastSynset, LexiconManager.getInstance().getLexicons());
//            if (lastSelectedUnits.size() == list.size()) {
//                DialogBox
//                        .showError(Messages.ERROR_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET);
//                return;
//            }
//            Collection<Sense> unitsOfMainSynset = RelationsDA.getUnits(
//                    lastSynset, lastSelectedUnits, LexiconManager.getInstance().getLexicons());
//            final IRelationType rel = RelationTypeFrame.showModalAndSaveRelation(workbench,
//                    RelationArgument.SYNSET, RelationsDA.getPos(lastSynset, LexiconManager.getInstance().getLexicons()),
//                    unitsOfMainSynset, lastSelectedUnits);
//            if (rel == null) {
//                return;
//            }
//            // utworzenie nowego synsetu
//            final Synset newSynset = RelationsDA.newSynset();
//
//            // utworzenie powiazan
//            new AbstractProgressThread(Tools.findFrame(getContent()),
//                    Labels.SYNSET_RELATIONS, null) {
//                /**
//                 * glowna procedura watku
//                 */
//                @Override
//                protected void mainProcess() {
//                    // ustawienie parametrow dla paska postepu
//                    progress.setGlobalProgressParams(1, 2);
//                    progress.setProgressParams(0, 0, Labels.MOVING_UNITS);
//                    RelationsDA.moveUnitsToExistenSynset(lastSynset,
//                            lastSelectedUnits, newSynset);
//                    progress.setGlobalProgressValue(2);
//                    progress.setProgressParams(0, 0, Labels.CREATING_CONNECTION);
//                    RelationsDA.makeRelation(lastSynset, newSynset, rel);
//                }
//            };
        // czy istieje relacja odwrotna
//            if (rel.getReverse() != null) {
//                // testy dla relacji odwrotnej
//                Collection<String> tests = LexicalDA.getTests(
//                        RelationsDA.getReverseRelation(rel),
//                        lastSynset.toString(), newSynset.toString(),
//                        RelationsDA.getPos(lastSynset, LexiconManager.getInstance().getLexicons()));
//                String test = "\n\n";
//                for (String i : tests) {
//                    test += i + "\n";
//                }
//                // czy utworzyc dla relacji odwrotnej
//                IRelationType reverse = RelationsDA.getReverseRelation(rel);
//                reverse = RelationTypes.get(reverse.getId()).getRelationType();
//
//                String reverseName = RelationsDA.getRelationName(reverse);
//                if (rel.isAutoReverse()
//                        || DialogBox
//                        .showYesNoCancel(String
//                                .format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION
//                                        + test, reverseName)) == DialogBox.YES) {
//                    RelationsDA.makeRelation(newSynset, lastSynset, reverse);
//                }
//
//            }
//
//            lastSynset = RelationsDA.refreshSynset(lastSynset);
//            ;
//            getViWordNetService().refreshViews();
//
//            final int returnValue = unitsList.getSelectedIndex();
//            Sense unit = listModel.getObjectAt(returnValue);
//            boolean superMode = workbench.getParam(SUPER_MODE) != null
//                    && workbench.getParam(SUPER_MODE).equals(SUPER_MODE_VALUE);
//            buttonDelete.setEnabled(unit != null ? superMode : false);
//
//            // powiadomienie zainteresowanych
//            listeners
//                    .notifyAllListeners(unitsList.getSelectedIndices().length == 1 ? unit
//                            : null);
//
//            refreshData(lastSynset);
//
//        }
//        unitsList.invalidate(); // odrysowanie listy
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        if (arg0 != null && arg0.getValueIsAdjusting()) {
            return;
        }

        int index = unitsList.getSelectedIndex(); // odczytanie indeksu
        boolean singleSelection = unitsList.getSelectedIndices() == null
                || unitsList.getSelectedIndices().length < 2;
        buttonUp.setEnabled(singleSelection && index > 0
                && (listModel.getLineSplitPosition() != index || index > 1));
        buttonDown.setEnabled(singleSelection && index != -1
                && index + 1 < listModel.getSize()
                && (index > 0 || index + 1 != listModel.getLineSplitPosition()));
        buttonToNew.setEnabled(null != lastSynset
                && listModel.getCollection().size() > 1);

        int selectionSize = unitsList.getSelectedIndices().length;
        // nie można usunąć linii podziału
        buttonDelete.setEnabled(selectionSize > 0
                && selectionSize < listModel.getSize());
        buttonSwitchToLexicalPerspective.setEnabled(singleSelection
                && index != -1 && index != listModel.getLineSplitPosition());

        // powiadomienie zainteresowanych
        listeners.notifyAllListeners(listModel.getObjectAt(index), LIST_SELECTION_CHANGED);
    }

    /**
     * odczytanie wybranych jednostek
     *
     * @return kolekcja zaznaczonych jednostek
     */
    public Collection<Sense> getSelectedUnits() {
        Collection<Sense> selectedUnits = new ArrayList<>();
        int[] selectedIndices = unitsList.getSelectedIndices();
        int size = listModel.getSize();
        if (selectedIndices != null && selectedIndices.length < size
                && listModel.getSize() > 1) { // synset moze być pusty
            for (int i : selectedIndices) {
                selectedUnits.add(listModel.getObjectAt(i));
            }
        }
        return selectedUnits;
    }

    /**
     * odswiezenie danych w widoku
     *
     * @param synset - powiazany senset
     */
    public void refreshData(Synset synset) {
        lastSynset = synset;
        int newSplitPoint = 0;
        List<Sense> units = null;

        if (synset != null) {
            // odczytanie punktu podzialu
            newSplitPoint = synset.getSplit();
            // odczytanie jednostek
            units = RemoteService.senseRemote.findBySynset(synset, LexiconManager.getInstance().getLexiconsIds());
            SynsetAttributes sa = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());

            commentValue.setText(sa.getComment() != null ? sa.getComment() : "");
            if (synset.getAbstract()) {
                isAbstract.setText(String.format("<html><font color=red>%s</font></html>", Labels.SYNSET_ARTIFICIAL));
            } else {
                isAbstract.setText("");
            }
            synsetID.setText(Long.toString(synset.getId()));
            princentonID.setText(sa.getPrincetonId());
            iliID.setText(sa.getIliId());

            synsetOwner.setText(sa.getOwner() != null ? sa.getOwner().getFullname() : "");
        }


        // odczytanie zaznaczonej jednostki
        Collection<Sense> selectedUnits = lastUnits;
        if (selectedUnits == null && unitsList != null
                && !unitsList.isSelectionEmpty()) {
            selectedUnits = new ArrayList<>();
            int[] values = unitsList.getSelectedIndices();
            for (int i : values) {
                selectedUnits.add(listModel.getObjectAt(i));
            }
        }
        lastUnits = null;

        listModel.setCollection(units, newSplitPoint);

        if (unitsList != null) {
            buttonAdd.setEnabled(listModel.getSize() > 1);
            unitsList.setEnabled(listModel.getSize() > 1);
            buttonRelations.setEnabled(listModel.getSize() > 1);
            // przywrocenie zaznaczenia
            Collection<Integer> indices = selectedUnits != null ? listModel
                    .getIndexesOfSelectedElements(selectedUnits) : null;
            // zaznaczenie odpowiedniego elementu
            if (indices != null && indices.size() > 0) {
                unitsList.setSelectedIndices(indices.stream().mapToInt(Integer::intValue).toArray());
                valueChanged(new ListSelectionEvent(
                        synset == null ? new Object() : synset, 0, 0, false));
            } else if (listModel.getSize() > 0) {
                // czyścimy zaznaczenie, aby podczas ustawienia pierwszego elementu zostało wywołane zdarzenie
                // które doprowadzi do załadowania relacji dla jednostki
                unitsList.clearSelection();
                unitsList.setSelectedIndex(0); // zaznaczenie pierwszej
            } else {
                unitsList.clearSelection();
            }
        }
    }

    /**
     * dodanie sluchacza dla zdarzenia klikniecia w przycisk
     *
     * @param newListener - sluchacz
     */
    public void addClickListener(SimpleListenerInterface newListener) {
        clickListeners.add(newListener);
    }

    public void addSynsetUpdateListener(SimpleListenerInterface newListener) {
        synsetUpdateListeners.add(newListener);
    }

    /**
     * ostatnio zaznaczone jednostki
     *
     * @param units - ostatnio zaznaczone jednostki
     */
    public void setLastUnits(Collection<Sense> units) {
        lastUnits = units;
    }

    @Override
    public JComponent getRootComponent() {
        return unitsList;
    }

    @Override
    public void caretUpdate(CaretEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int idx = unitsList.locationToIndex(e.getPoint());

        if (e.getButton() == MouseEvent.BUTTON3 && idx != -1
                && idx != listModel.getLineSplitPosition()) {

            Sense unit = listModel.getObjectAt(idx);
            unit = RemoteService.senseRemote.fetchSense(unit.getId());

            LexicalUnitPropertiesViewUI lui = new LexicalUnitPropertiesViewUI(graphUI);
            lui.init(workbench);

            DialogWindow dia = new DialogWindow(workbench.getFrame(), Labels.UNIT_PROPERTIES, 585, 520);
            WebPanel pan = new WebPanel();

            lui.initialize(pan);
            lui.refreshData(unit);
            lui.closeWindow((ActionEvent e1) -> {
                dia.dispose();
            });
            dia.setLocationRelativeTo(workbench.getFrame());
            dia.setContentPane(pan);
            dia.pack();
            dia.setVisible(true);
        }
    }

    private ViWordNetService getViWordNetService() {
        if (null == viWordNetService) {
            viWordNetService = ((ViWordNetPerspective) workbench
                    .getActivePerspective()).getViWordNetService();
        }
        return viWordNetService;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
