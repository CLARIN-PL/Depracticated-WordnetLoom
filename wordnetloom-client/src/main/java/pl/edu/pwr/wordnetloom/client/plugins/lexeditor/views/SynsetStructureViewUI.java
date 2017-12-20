package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.panel.WebPanel;
import jiconfont.icons.FontAwesome;
import org.jboss.naming.remote.client.ejb.RemoteNamingStoreEJBClientHandler;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.AbstractListFrame;
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
import pl.edu.pwr.wordnetloom.client.systems.misc.Tools;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
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
import java.util.List;

/**
 * klasa opisujacy wyglada okienka z strukutrą synsetu
 *
 * @author Max
 */
public class SynsetStructureViewUI extends AbstractViewUI implements
        ActionListener, ListSelectionListener, CaretListener, MouseListener {

    private ViWordNetService viWordNetService;

    private static final String SUPER_MODE = "SuperMode";
    private static final String SUPER_MODE_VALUE = "1";

    /**
     * relacja dla synsetow
     */
    public static final int SYNSET_RELATIONS = 0;
    /**
     * przejscie do perspektywy leksyklanej
     */
    public static final int LEXICAL_PERSPECTIVE = 1;
    /**
     * zmienilo sie zaznaczenie na liscie
     */
    private static final int LIST_SELECTION_CHANGED = 2;
    /**
     * usunieto jednostke z synsetu
     */
    public static final int UNIT_REMOVED = 3;
    /**
     * nowa jednostka zostala stworzona (dodane do systemu)
     */
    public static final int UNIT_CREATED = 4;

    private JList unitsList;
    private JTextField synsetID;
    private JLabel synsetOwner;
    private JLabel isAbstract;
    private MButton buttonUp, buttonDown, buttonAdd, buttonDelete,
            buttonRelations, buttonSwitchToLexicalPerspective, buttonToNew;
    private Collection<Sense> lastUnits = null;
    private MTextArea commentValue = null;
    ArrayList<Sense> lastSelectedUnits = new ArrayList<>();

    private final SimpleListenersContainer clickListeners = new SimpleListenersContainer();
    private final SimpleListenersContainer synsetUpdateListeners = new SimpleListenersContainer();

    private final UnitsInSynsetListModel listModel = new UnitsInSynsetListModel();
    private Synset lastSynset = null;
    private final boolean showRelations;
    private final boolean showSwitch;
    private final boolean bottomButtons;
    private final ViwnGraphViewUI graphUI;

    /**
     * konstruktor
     *
     * @param showRelations - czy pokazać przycisk z relacjami
     * @param showSwitch    - czy pokazać przycisk do przelaczania sie do jednostek
     * @param bottomButtons - czy przyciski maja byc na dole (true) czy z boku
     *                      (false)
     * @param graphUI
     */
    public SynsetStructureViewUI(boolean showRelations, boolean showSwitch,
                                 boolean bottomButtons, ViwnGraphViewUI graphUI) {
        this.showRelations = showRelations;
        this.showSwitch = showSwitch;
        this.bottomButtons = bottomButtons;
        this.graphUI = graphUI;
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

        synsetOwner = new JLabel("");

        // synset id
        synsetID = new JTextField("");
        synsetID.setEditable(false);
        synsetID.setBackground(null); //this is the same as a JLabel
        synsetID.setBorder(null); //remove the border

        refreshData(null);
        unitsList = new ToolTipList(workbench, listModel,
                ToolTipGenerator.getGenerator());
        unitsList
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        unitsList.addListSelectionListener(this);
        unitsList.addMouseListener(this);
        unitsList.setEnabled(false);

        commentValue = new MTextArea(Labels.VALUE_UNKNOWN);
        commentValue.addCaretListener(this);
        commentValue.setRows(3);
        commentValue.setEnabled(false);

        isAbstract = new JLabel();

        // dodanie przyciskow
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

        // panel dolny z przyciskami
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new RiverLayout(0, 0));
        if (bottomButtons) {
            buttonsPanel.add("", buttonUp);
            buttonsPanel.add("", buttonAdd);
            if (showRelations) {
                buttonsPanel.add("", buttonRelations);
            }
            buttonsPanel.add("br", buttonDown);
            buttonsPanel.add("", buttonDelete);
            buttonsPanel.add("", buttonToNew);
            if (showSwitch) {
                buttonsPanel.add("", buttonSwitchToLexicalPerspective);
            }
        } else {
            buttonsPanel.add("", buttonUp);
            buttonsPanel.add("br", buttonDown);
            buttonsPanel.add("br", buttonAdd);
            buttonsPanel.add("br", buttonDelete);
            buttonsPanel.add("br", buttonToNew);
            if (showRelations) {
                buttonsPanel.add("br", buttonRelations);
            }
            if (showSwitch) {
                buttonsPanel.add("br", buttonSwitchToLexicalPerspective);
            }
        }

        // dodanie do okna
        content.add("", new MLabel(Labels.LEXICAL_UNITS_IN_SYSET_COLON, 'y',
                unitsList));
        content.add("br hfill vfill", new JScrollPane(unitsList));
        if (!bottomButtons) {
            content.add("", buttonsPanel);
            content.add("br", new JLabel(Labels.COMMENT_COLON));
            content.add("br", commentValue);

        }
        content.add("br", isAbstract);

        content.add("br", new JLabel("Synset Id:"));
        content.add("", synsetID);

        content.add("br", new JLabel(Labels.OWNER_COLON));
        content.add("", synsetOwner);
        if (bottomButtons) {
            content.add("br vtop", new JLabel(Labels.COMMENT_COLON));
            content.add("", commentValue);
            content.add("br center", buttonsPanel);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int index = unitsList.getSelectedIndex(); // odczytanie zaznaczenia

        if (event.getSource() == buttonUp) { // czy przycisk W GÓRE
            // odczytanie punktu podzialu
            int splitPosition = listModel.getSplitPosition();
            // zamiana danych
            LexicalDA.exchangeUnitsInSynset(lastSynset,
                    listModel.getObjectAt(index - 1),
                    listModel.getObjectAt(index), index - 1, splitPosition);
            refreshData(lastSynset);
            unitsList.setSelectedIndex(index - 1);

            ViwnNode node = graphUI.getSelectedNode();
            if (node != null && node instanceof ViwnNodeSynset) {
                ViwnNodeSynset s = (ViwnNodeSynset) node;
                s.setLabel(null);
                graphUI.graphChanged();
            } else {
                // dodano nowy synset, nie istnieje on nigdzie w grafie
                graphUI.graphChanged();
            }
        } else if (event.getSource() == buttonDown) { // czy przycos W DÓŁ
            // odczytanie punktu podzialu
            int splitPosition = listModel.getSplitPosition();
            // zamiana danych
            LexicalDA.exchangeUnitsInSynset(lastSynset,
                    listModel.getObjectAt(index),
                    listModel.getObjectAt(index + 1), index, splitPosition);
            refreshData(lastSynset);
            unitsList.setSelectedIndex(index + 1);

            ViwnNode node = graphUI.getSelectedNode();
            if (node != null && node instanceof ViwnNodeSynset) {
                ViwnNodeSynset s = (ViwnNodeSynset) node;
                s.setLabel(null);
                graphUI.graphChanged();
            } else {
                // dodano nowy synset, nie istnieje on nigdzie w grafie
                graphUI.graphChanged();
            }
        } else if (event.getSource() == buttonAdd) { // dodanie nowego leksemu
            Point location = buttonAdd.getLocationOnScreen();
            ValueContainer<Boolean> created = new ValueContainer<>(false);

            Rectangle r = workbench.getFrame().getBounds();
            int x = r.x + r.width - AbstractListFrame.WIDTH / 2 - 50;

            Collection<Sense> selectedUnits = new ArrayList<>();
            selectedUnits = UnitsListFrame.showModal(
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
//                    rel = RelationTypeFrame.showModal(workbench,
//                            RelationArgument.LEXICAL_SPECIAL, LexicalDA.getPos(lastSynset, LexiconManager.getInstance().getLexicons()),
                //  RemoteUtils.lexicalUnitRemote.dbFastGetUnits(lastSynset, LexiconManager.getInstance().getLexicons()), selectedUnits);
                // if (rel == null) {
                //      return;
                // }
            }

            for (Sense selectedUnit : selectedUnits) {
                // dodanie powiazanie
                lastSynset = LexicalDA.addConnection(selectedUnit,
                        lastSynset);
            }
            // odswiezenie danych

//                refreshData(lastSynset);
//                getViWordNetService().refreshViews();
        }
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
//            final IRelationType rel = RelationTypeFrame.showModal(workbench,
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
                && (listModel.getSplitPosition() != index || index > 1));
        buttonDown.setEnabled(singleSelection && index != -1
                && index + 1 < listModel.getSize()
                && (index > 0 || index + 1 != listModel.getSplitPosition()));
        buttonToNew.setEnabled(null != lastSynset
                && listModel.getCollection().size() > 1);

        int selectionSize = unitsList.getSelectedIndices().length;
        // nie można usunąć linii podziału
        buttonDelete.setEnabled(selectionSize > 0
                && selectionSize < listModel.getSize());
        buttonSwitchToLexicalPerspective.setEnabled(singleSelection
                && index != -1 && index != listModel.getSplitPosition());

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
//            units = RemoteUtils.lexicalUnitRemote.dbFastGetUnits(synset, LexiconManager.getInstance().getLexicons());
//            if (units == null) {
//                units = LexicalDA.getLexicalUnits(synset, LexiconManager.getInstance().getLexicons());
//            }
//            commentValue.setText(synset != null ? formatValue(Common.getSynsetAttribute(synset, Synset.COMMENT)) : formatValue(null));
//            if (Synset.isAbstract(Common.getSynsetAttribute(synset, Synset.ISABSTRACT))) {
//                isAbstract.setText(String.format("<html><font color=red>%s</font></html>", Labels.SYNSET_ARTIFICIAL));
//            } else {
//                isAbstract.setText("");
//            }
            synsetID.setText(Long.toString(synset.getId()));
            //  synsetOwner.setText(synset != null ? formatValue(Common.getSynsetAttribute(synset, Synset.OWNER)) : formatValue(null));
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
                    .getIndices(selectedUnits) : null;
            // zaznaczenie odpowiedniego elementu
            if (indices != null && indices.size() > 0) {
                unitsList.setSelectedIndices(Tools.getInstance().collectionToArray(indices));
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
                && idx != listModel.getSplitPosition()) {
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
