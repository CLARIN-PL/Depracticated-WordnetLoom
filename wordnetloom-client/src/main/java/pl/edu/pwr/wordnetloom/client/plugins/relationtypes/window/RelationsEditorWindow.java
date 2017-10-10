package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.RelationTypesIM;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTreeModel;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTypeNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtest.model.SenseRelationTest;
import pl.edu.pwr.wordnetloom.relationtest.model.SynsetRelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.ERelationType;
import pl.edu.pwr.wordnetloom.relationtype.model.IRelationType;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import se.datadosen.component.RiverLayout;

import javax.management.relation.RelationType;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * okienko do zarządzania relacjami
 *
 * @author Max
 */
public class RelationsEditorWindow extends IconFrame implements ActionListener, TreeSelectionListener, CaretListener, ListSelectionListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private final int MIN_WINDOW_WIDTH = 200;
    private final int MIN_WINDOW_HEIGHT = 400;

    // komponenty panelu dolnego (propertiesPanel)
    private TextFieldPlain lexicon;
    private TextFieldPlain relationName;
    private JCheckBox multilingual;
    private TextFieldPlain relationDisplay;
    private TextFieldPlain relationShortcut;
    private DescriptionTextArea relationDescription;
    private TextFieldPlain relationPos;
    private TextFieldPlain relationReverse;
    private JFormattedTextField relationColor;
    private ComboBoxPlain<ViwnNode.Direction> relationDirection; //TODO chyba będzie trzeba dodać

    private ButtonExt buttonPos;
    private ButtonExt buttonReverse;
    private ButtonExt buttonColor;
    private ButtonExt buttonSave;
    private ButtonExt buttonCancel;

    private JPanel detailsPanel;
    private JTabbedPane relationsPanel;

    private JList testsList;

    private IRelationType currentEditedType;

    public static void showModal(Workbench workbench) throws ParseException {
        RelationsEditorWindow frame = new RelationsEditorWindow(workbench);
        frame.setLocationRelativeTo(workbench.getFrame());
        frame.setVisible(true);
        frame.showModal();
    }

    private RelationsEditorWindow(Workbench workbench) throws ParseException {
        super(workbench.getFrame(), Labels.EDIT_RELATION_TYPES, 550, 700);
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Dimension separatorSize = new Dimension(10, 10);
        init();
        createView();
    }

    private void init() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
    }

    /**Tworzy widok calego okna.*/
    private void createView() throws ParseException {
        relationsPanel = createTabbedPanel();
        detailsPanel = createDetailsPanel();
        this.add(createSplitPanel(relationsPanel, detailsPanel), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new RiverLayout());
        buttonSave = new ButtonExt(Labels.SAVE, e -> save());
        buttonCancel = new ButtonExt(Labels.CANCEL, e -> dispose());
        buttonsPanel.add(RiverLayout.LINE_BREAK + RiverLayout.CENTER, buttonSave);
        buttonsPanel.add(RiverLayout.TAB_STOP, buttonCancel);

        JButton testButton = new ButtonExt("test", e->clearDetails());
        buttonsPanel.add(RiverLayout.LINE_BREAK, testButton);
        JButton testButton2 = new ButtonExt("test2", e-> setDetailsPanelEnabled(true));
        buttonsPanel.add(RiverLayout.TAB_STOP, testButton2);
        this.add(buttonsPanel, BorderLayout.SOUTH);

        clearDetails();
    }

    private JTabbedPane createTabbedPanel() {
        JTabbedPane relationsTabbedPane = new JTabbedPane();
        //relationsTabbedPane.add("Synset", new RelationsTypePanel());
        //relationsTabbedPane.add("Sense",new RelationsTypePanel());
        relationsTabbedPane.add("Synset", new RelationsTypePanel(this, ERelationType.SYNSET));
        relationsTabbedPane.add("Sense", new RelationsTypePanel(this, ERelationType.SENSE));

        return relationsTabbedPane;
        //this.add(relationsTabbedPane, BorderLayout.CENTER);
    }

    /**
     * Tworzy podział pomiędzy dwoma panelami. Rozmiar paneli może być dzięki
     * temu zmieniany za pomocą przeciągnięcia podziału.
     *
     * @param topPanel    panel, którym ma znajdować się na górze
     * @param bottomPanel panel, który ma znajdować się na dole
     * @return widok zawierający dwa panele i przedział pobiędzy nimi
     */
    private SplitPaneExt createSplitPanel(JComponent topPanel, JComponent bottomPanel) {
        SplitPaneExt mainSplit = new SplitPaneExt(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        mainSplit.setStartDividerLocation(200);
        mainSplit.setResizeWeight(1.0f);
        return mainSplit;
    }

    private JPanel createDetailsPanel() throws ParseException {
        //utworzenie pola lexicon
        lexicon = new TextFieldPlain("");
        lexicon.addActionListener(this);

        // utworzenie pola zaznaczania wielojęzyczności
        multilingual = new JCheckBox(Labels.MULTILINGUAL);
        multilingual.addActionListener(this);

        // utworzenie pola nazwy relacji
        relationName = new TextFieldPlain("");
        relationName.addActionListener(this);

        // utworzenie pola formy do wyświetlenia
        relationDisplay = new TextFieldPlain("");
        relationDisplay.addCaretListener(this);

        // utworzenie skrótu relacji
        relationShortcut = new TextFieldPlain("");
        relationShortcut.addCaretListener(this);

        // utowrzenie opisu
        relationDescription = new DescriptionTextArea();
        relationDescription.setRows(2);
//        relationDescription.addCaretListener(this);


        // utworzenie pola części mowy oraz przycisku do wybierania części mowy
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        relationPos = new TextFieldPlain(PartOfSpeechManager.getInstance().getById(1L).getName(locale));

        buttonPos = new ButtonExt("...", e-> openPartOfSpeechDialog());
        buttonPos.setToolTipText(Hints.CHOOSE_POS_FOR_RELATION);

        // utworzenie relacji odwrotnej oraz przycisku do wybierania
        relationReverse = new TextFieldPlain(Labels.NO_VALUE);

        buttonReverse = new ButtonExt("...", e->openReverseRelationDialog());
        buttonReverse.setToolTipText(Hints.CHOOSE_REVERSE_RELATION);

        //utworzenie pola koloru i przycisku do wyboru koloru
        MaskFormatter colorFormatter = new MaskFormatter("'#HHHHHH");

        relationColor = new JFormattedTextField(colorFormatter);
        // TODO dodać możliwość wpisywania koloru w pole tekstowe

        buttonColor = new ButtonExt("...", this);
        buttonColor.setToolTipText(Hints.CHOOSE_COLOR_RELATION);

        // utowrzenie pola wyboru kierunku relacji
        relationDirection = new ComboBoxPlain<>();

        // utworzenie panelu z testami relacji
//        JPanel testsPanel = createTestsPanel();
        JPanel testsPanel = new TestsPanel(this);

        // utworzenie etykiet
        LabelExt lexiconLabel = new LabelExt(Labels.LEXICON_COLON, 'l', lexicon);
        LabelExt nameLabel = new LabelExt(Labels.RELATION_NAME_COLON, 'n', relationName);
        LabelExt displayLabel = new LabelExt(Labels.DISPALY_FORM_COLON, 'f', relationDisplay);
        LabelExt shortcutLabel = new LabelExt(Labels.SHORTCUT_COLON, 's', relationShortcut);
        LabelExt descriptionLabel = new LabelExt(Labels.DESCRIPTION_COLON, 'o', relationPos);
        LabelExt posLabel = new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 'c', relationPos);
        LabelExt reverseLabel = new LabelExt(Labels.REVERSE_RELATION, '\0', relationReverse);
        LabelExt colorLabel = new LabelExt(Labels.COLOR_COLON, 'q', relationColor);
        LabelExt directionLabel = new LabelExt(Labels.DIRECTION_COLON, 'd', relationDirection);
        LabelExt testsLabel = new LabelExt(Labels.TESTS_COLON, 't', testsPanel);

        final String LINE_BREAK = RiverLayout.LINE_BREAK;
        final String LEFT_BREAK = RiverLayout.LINE_BREAK + " " + RiverLayout.LEFT;
        final String LINE_FILL = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL;
        final String TAB_FILL = RiverLayout.TAB_STOP + " " + RiverLayout.HFILL;
        final String RIGHT = RiverLayout.RIGHT;

        // dodanie komponentów do panelu
        JPanel panel = new JPanel(new RiverLayout());

        panel = new JPanel(new RiverLayout());
        panel.add(LINE_BREAK, multilingual);
        panel.add(LINE_BREAK, lexiconLabel);
        panel.add(TAB_FILL, lexicon);
        JButton buttonLexicon = new ButtonExt("...", rel -> {
            Set<Lexicon> lexicons = new LexiconCheckBoxDialog(lexicon, "leksykon"
                    , currentEditedType != null ? currentEditedType.getLexicons() : null
                    , RemoteService.lexiconServiceRemote.findAll()
            ).showModal();
            setLexiconFieldText(lexicons);
        });
        panel.add(RIGHT, buttonLexicon);
        panel.add(LINE_BREAK, nameLabel);
        panel.add(TAB_FILL, relationName);
        panel.add(LINE_BREAK, displayLabel);
        panel.add(TAB_FILL, relationDisplay);
        panel.add(LINE_BREAK, shortcutLabel);
        panel.add(TAB_FILL, relationShortcut);
        panel.add(LINE_BREAK, descriptionLabel);
        panel.add(TAB_FILL, relationDescription);
        panel.add(LINE_BREAK, posLabel);
        panel.add(TAB_FILL, relationPos);
        panel.add(RIGHT, buttonPos);
        panel.add(LINE_BREAK, reverseLabel);
        panel.add(TAB_FILL, relationReverse);
        panel.add(RIGHT, buttonReverse);
        panel.add(LINE_BREAK, colorLabel);
        panel.add(TAB_FILL, relationColor);
        panel.add(RIGHT, buttonColor);
        panel.add(LINE_BREAK, directionLabel);
        panel.add(TAB_FILL, relationDirection);
        panel.add(LEFT_BREAK, testsLabel);
        panel.add(LINE_FILL, testsPanel);

        return panel;
    }

    private void openPartOfSpeechDialog(){
        Set<PartOfSpeech> partOfSpeeches  = new PartOfSpeechCheckBoxDialog(buttonPos,
                Labels.PARTS_OF_SPEECH_COLON,
                null,
                PartOfSpeechManager.getInstance().getAll()
        ).showModal();
        setPartOfSpeechFieldText(partOfSpeeches);
    }

    private void openReverseRelationDialog(){
        RelationTreeModel model = ((RelationsTypePanel)relationsPanel.getSelectedComponent()).getModel();
        Pair<IRelationType, Boolean> reverseRelationPair = ReverseRelationDialog.showModal(this, currentEditedType.getReverse(), currentEditedType.getAutoReverse(), model);
        if(reverseRelationPair.getA() != null){
            String locale = RemoteConnectionProvider.getInstance().getLanguage();
            relationReverse.setText(reverseRelationPair.getA().getName(locale));
        } else {
            relationReverse.setText("");
        }
        currentEditedType.setReverse(reverseRelationPair.getA());
        currentEditedType.setAutoReverse(reverseRelationPair.getB());
    }

    private void save() {

    }

    private void setLexiconFieldText(Set<Lexicon> lexicons) {
        if(lexicons == null){
            lexicon.setText("");
            return;
        }
        String lexiconText = "";
        if (currentEditedType != null) {
            currentEditedType.setLexicons(lexicons);

        }
        // ustawianie tekstu pola leksykon
        if (!lexicons.isEmpty()) {
            StringBuilder textBuilder = new StringBuilder();
            Iterator iterator = lexicons.iterator();
            while (iterator.hasNext()) {
                textBuilder.append(((Lexicon) iterator.next()).getName());
                if (iterator.hasNext()) {
                    textBuilder.append(", ");
                }
            }
            lexiconText = textBuilder.toString();
        }
        lexicon.setText(lexiconText);
    }

    private void setPartOfSpeechFieldText(Set<PartOfSpeech> pos) {
        assert currentEditedType != null; // okienko z wyborem części mowy nie powinno się otowrzyć, jeśli żaden typ relacji nie jest zaznaczony
        //TODO ustawić części mowy
        if(pos == null){
            relationPos.setText("");
            return;
        }
        String posText = "";
        if(!pos.isEmpty()){
            StringBuilder textBuilder = new StringBuilder();
            Iterator iterator = pos.iterator();
            String locale = RemoteConnectionProvider.getInstance().getLanguage();
            while(iterator.hasNext()){
                textBuilder.append(((PartOfSpeech)iterator.next()).getName(locale));
                if(iterator.hasNext()){
                    textBuilder.append(", ");
                }
            }
            posText = textBuilder.toString();
        }
        relationPos.setText(posText);

    }

    public void refreshDetails(IRelationType rel, ERelationType type) {
        currentEditedType = rel;
        if(rel == null){
            clearDetails();
            return;
        }
        setDetailsPanelEnabled(true);
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        multilingual.setSelected(rel.isMultilingual());
        setLexiconFieldText(rel.getLexicons());
        relationName.setText(rel.getName(locale));
        relationDisplay.setText(rel.getDisplayText(locale));
        relationShortcut.setText(rel.getShortDisplayText(locale));
        relationDescription.setText(rel.getDescription(locale));
//        relationPos.setText() ; //TODO ogarnąć jak to ustawić
        relationReverse.setText(rel.getReverse() != null ? rel.getReverse().getDisplayText(locale) : "");
        relationColor.setText(rel.getColor());
//        relationDirection.setSelectedItem(); //TODO ustawić to
    }

    public void clearDetails() {
        // ustawienie wartości
        multilingual.setSelected(false);
        lexicon.setText("");
        relationName.setText("");
        relationDisplay.setText("");
        relationShortcut.setText("");
        relationDescription.setText("");
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        relationPos.setText(PartOfSpeechManager.getInstance().getById(1L).getName(locale));
        relationReverse.setText("");
        relationColor.setText("");
        buttonColor.setBackground(null);
        relationDirection.setSelectedItem(null);

        setDetailsPanelEnabled(false);
    }

    private void setDetailsPanelEnabled(boolean enabled){
        for(Component component : detailsPanel.getComponents()){
            component.setEnabled(enabled);
        }
        // jeżeli włączamy pola, trzeba zablokować możliwość edytowania polom, których nie powinno się blokować
        if(enabled) {
            lexicon.setEnabled(false);
            relationPos.setEnabled(false);
            relationReverse.setEnabled(false);
            relationColor.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonColor) {
            Color newColor = JColorChooser.showDialog(null, "Wybierz kolor", buttonColor.getBackground());
            buttonColor.setBackground(newColor);
            String colorHexValue = "#" + Integer.toHexString(newColor.getRGB()).substring(2);
            relationColor.setText(colorHexValue);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

    }
}

class TestsPanel extends JPanel {
    private final int ADD_TEST = 0;
    private final int EDIT_TEST = 1;
    private final int REMOVE_TEST = 2;
    private final int MOVE_TEST_UP = 3;
    private final int MOVE_TEST_DOWN = 4;

    private JFrame owner;
    private JList testsList;
    private GenericListModel<RelationTest> testsModel;
    private ButtonsPanel testButtonsPanel;
    private IRelationType currentRelationType;

    public TestsPanel(JFrame owner) {
        this.owner = owner;
        setLayout(new BorderLayout());

        // utworzenie listy testów

        RelationTest test1 = new SynsetRelationTest();
        test1.setTest("Test pierwszy");
        RelationTest test2 = new SynsetRelationTest();
        test2.setTest("Test drugi");
        RelationTest[] tests = {test1, test2};
        testsList = new JList(tests);
        testsList.setCellRenderer(new TestListRenderer());
        add(new JScrollPane(testsList), BorderLayout.CENTER);

        // utworzenie przycisków
        testButtonsPanel = createTestButtonsPanel();
        add(testButtonsPanel, BorderLayout.EAST);

        testsModel = new GenericListModel<>();
        //TODO
    }

    public void updatePanel(IRelationType relationType){
        //TODO pobranie testów z bazy i ich wyświetlenie
        currentRelationType = relationType;
    }

    @Override
    public void setEnabled(boolean enabled){
        testsList.setEnabled(enabled);
        testButtonsPanel.enableAllButtons(enabled);
    }

    private ButtonsPanel createTestButtonsPanel() {
        ButtonsPanel buttonsPanel = new ButtonsPanel.Builder(ButtonsPanel.VERTICAL)
                .addButton(ADD_TEST, new ButtonExt(RelationTypesIM.getNewImage(), e -> createTest()), Hints.CREATE_NEW_TEST)
                .addButton(EDIT_TEST, new ButtonExt(RelationTypesIM.getEditImage(), e -> editTest()), Hints.EDIT_SELECTED_TEST)
                .addButton(REMOVE_TEST, new ButtonExt(RelationTypesIM.getDeleteImage(), e -> deleteTest()), Hints.REMOVE_SELECTED_TEST)
                .addButton(MOVE_TEST_UP, new ButtonExt(RelationTypesIM.getMoveUpImage(), e -> moveTestUp()), Hints.MOVE_TEST_UP)
                .addButton(MOVE_TEST_DOWN, new ButtonExt(RelationTypesIM.getMoveDownImage(), e -> moveTestDown()), Hints.MOVE_TEST_DOWN)
                .setButtonsEnabled(true)
                .build();
        return buttonsPanel;
    }

    //TODO podorabiać
    private void createTest() {
        //TODO ustawić częśc mowy
        PartOfSpeech partOfSpeech = PartOfSpeechManager.getInstance().getById(1L);
//        Pair<String, PartOfSpeech> partOfSpeechPair = TestEditorWindow.showModal(owner, new SenseRelationTest());
        SenseRelationTest senseTest = new SenseRelationTest();
        senseTest.setSenseApartOfSpeech(PartOfSpeechManager.getInstance().getById(2L));
        senseTest.setTest("To jest jakiś test");
        RelationTest test = TestEditorWindow.showModal(owner, senseTest);
        //TODO operacja na bazie danych
        //TODO zaktualizowac element na liście

    }

    private void editTest() {
        JOptionPane.showMessageDialog(null, "Edit");
    }

    private void deleteTest() {
        JOptionPane.showMessageDialog(null, "Delete");
    }

    private void moveTestUp() {
        JOptionPane.showMessageDialog(null, "moveUp");
    }

    private void moveTestDown() {
        JOptionPane.showMessageDialog(null, "moveDown");
    }
}

class TestListRenderer extends JPanel implements ListCellRenderer<RelationTest>
{
    private JLabel posLabel;
    private JLabel testLabel;

    public TestListRenderer(){

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends RelationTest> list, RelationTest value, int index, boolean isSelected, boolean cellHasFocus) {

        return this;
    }
}

class DescriptionTextArea extends JScrollPane{

    private JTextArea textArea;

    public DescriptionTextArea(){
        textArea = new TextAreaPlain("");
        getViewport().setView(textArea);
    }

    @Override
    public void setEnabled(boolean enabled){

        super.setEnabled(enabled);
        textArea.setEnabled(enabled);
        textArea.setEditable(enabled);
    }
    public void setRows(final int rows){
        textArea.setRows(rows);
    }

    public void setText(final String text) {
        textArea.setText(text);
    }
}

class RelationsTypePanel extends JPanel {
    private static final long serialVersionUID = 4649824763750406980L;

    private RelationsEditorWindow parentWindow;
    private ERelationType relationType;


    private final int ADD_RELATION = 0;
    private final int ADD_SUBRELATION = 1;
    private final int REMOVE_RELATION = 2;
    private final int MOVE_RELATION_UP = 3;
    private final int MOVE_RELATION_DOWN = 4;

    private JTree tree;
    private RelationTreeModel model;

    public RelationsTypePanel(RelationsEditorWindow parentWindow, ERelationType relationType) {
        this.parentWindow = parentWindow;
        init();
        createView();
    }

    private void createView() {
        createRelationsTree(BorderLayout.CENTER);
        createRelationButtons(BorderLayout.EAST);
    }

    public RelationTreeModel getModel(){
        return model;
    }

    private void init() {
        setLayout(new BorderLayout());
    }

    private void createRelationsTree(String constrains) {
        // TEST--------------------------------------------
        model = new RelationTreeModel();

        List<Lexicon> allLexicons = RemoteService.lexiconServiceRemote.findAll();
        Set<Lexicon> allowedLexicons = new HashSet<>();
        allowedLexicons.add(allLexicons.get(0));
        allowedLexicons.add(allLexicons.get(2));

        IRelationType type1 = new SynsetRelationType();
        final String locale = RemoteConnectionProvider.getInstance().getLanguage();
        type1.setName(locale, "Jeden");
        type1.setDisplayText(locale, "Jedd");
        type1.setDescription(locale, "To jest jakiś opis");
        type1.setShortDisplayText(locale, "j");
        type1.setColor("FFFFF");
        type1.setMultilingual(true);
        type1.setLexicons(allowedLexicons);
        IRelationType type2 = new SynsetRelationType();
        type2.setName("pl", "Dwa");
        List<IRelationType> relationTypes = new ArrayList<>();
        relationTypes.add(type1);
        relationTypes.add(type2);
        model.setRelationTypeNodes(relationTypes);
        IRelationType type3 = new SynsetRelationType();
        type3.setName("pl", "Trzy");
        List<IRelationType> children = new ArrayList<>();
        children.add(type3);
        model.addChildren(0, children);

        // ------------------------------------------------

        tree = new JTree();
        tree.setModel(model);
        tree.setToggleClickCount(2); //rozwijanie za pomocą dwukliku
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(e -> {
            RelationTypeNode node = (RelationTypeNode)tree.getLastSelectedPathComponent();
            if(node == null){
                parentWindow.clearDetails();
                return;
            }
            parentWindow.refreshDetails(node.getValue(), relationType);
        });
        tree.setExpandsSelectedPaths(false);

        // ustawienie ikonek dla drzewa relacji
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(RelationTypesIM.getOpenImage());
        renderer.setClosedIcon(RelationTypesIM.getClosedImage());
        renderer.setLeafIcon(RelationTypesIM.getLeafImage());
        tree.setCellRenderer(renderer);

        this.add(new JScrollPane(tree), constrains);
    }

    private void createRelationButtons(String constrains) {
        JButton moveUpButton = new ButtonExt(RelationTypesIM.getMoveUpImage(), e -> moveRelationUp());
        JButton moveDownButton = new ButtonExt(RelationTypesIM.getMoveDownImage(), e -> moveRelationDown());
        JButton addButton = new ButtonExt(RelationTypesIM.getNewImage(), e -> addRelation());
        JButton addSubRelationButton = new ButtonExt(RelationTypesIM.getNewSubImage(), e -> addSubRelation());
        JButton removeButton = new ButtonExt(RelationTypesIM.getDeleteImage(), e -> removeRelation());
        ButtonsPanel buttonsPanel = new ButtonsPanel.Builder(ButtonsPanel.VERTICAL)
                .addButton(MOVE_RELATION_UP, moveUpButton, Hints.MOVE_RELATION_UP)
                .addButton(MOVE_RELATION_DOWN, moveDownButton, Hints.MOVE_RELATION_DOWN)
                .addButton(ADD_RELATION, addButton, Hints.CREATE_RELATION_TYPE)
                .addButton(ADD_SUBRELATION, addSubRelationButton, Hints.CREATE_NEW_RELATION_SUBTYPE)
                .addButton(REMOVE_RELATION, removeButton, Hints.REMOVE_SELECTED_REL_AND_SUBRELATION)
                .setButtonsEnabled(true)
                .build();

        this.add(buttonsPanel, constrains);
    }

    private void refreshTree() {
        List<TreePath> expandedPaths = new ArrayList<>();
        for(int i=0; i<tree.getRowCount() - 1; i++){
            if(tree.getPathForRow(i).isDescendant(tree.getPathForRow(i+1))){
                expandedPaths.add(tree.getPathForRow(i));
            }
        }
        RelationTreeModel typeModel = (RelationTreeModel)tree.getModel();
        typeModel.reload();
//        tree.updateUI();
        for(TreePath path : expandedPaths){
            tree.expandPath(path);
        }
    }

    // poniższe metody zmieniają tylko pozycje na liście
    // usuwaniem z bazy danych powiny zajmowac się przeciążone metody klas dziedziczących
    private void moveRelationUp() {
        //TODO operacja na bazie danych
        RelationTypeNode node = (RelationTypeNode)tree.getLastSelectedPathComponent();
        model.moveNodeUp(node);
        tree.updateUI();
    }

    private void moveRelationDown() {
        //TODO operacja na bazie danych
        RelationTypeNode node = (RelationTypeNode)tree.getLastSelectedPathComponent();
        model.moveNodeDown(node);
        tree.updateUI();
    }

    private void addRelation() {
        String relationName = JOptionPane.showInputDialog(Labels.RELATION_NAME_COLON);
        //TODO operacja na bazie danych
        IRelationType type = new SynsetRelationType(); //TODO znaleźć sposób, aby utworzyć odpowiedni obiekt, zrobić jakiś kreator czy coś
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        type.setName(locale, relationName);
        model.addNode(type);
//        tree.updateUI();
        refreshTree();
    }

    private void addSubRelation() {
        RelationTypeNode node = (RelationTypeNode)tree.getLastSelectedPathComponent();
        if(node.getLevel() == RelationTypeNode.SUBTYPE_LEVEL){
            node = node.getParent();
        }
        if(node == null){
            return;
        }
        String subRelationName = JOptionPane.showInputDialog(Labels.SUBTYPE_RELATION_NAME_COLON);
        //TODO operacja na bazie danych
        IRelationType child = new SynsetRelationType();
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        child.setName(locale, subRelationName);
        model.addChild(node, child);
//        model.nodeChanged(node);
//        refreshTree();
        tree.updateUI();
    }

    private void removeRelation() {
        RelationTypeNode node = (RelationTypeNode)tree.getLastSelectedPathComponent();
        //TODO operacja na bazie danych
        model.removeChild(node.getParent(), node);
        tree.updateUI();
    }
}

abstract class CheckBoxDialog<T> extends JDialog {
    private JCheckBox[] checkBoxes;
    private java.util.List<T> elementsList;

    public CheckBoxDialog(Component owner, final String title, Set<T> selectedElement, List<T> allElements){
        final int EMPTY_BORDER_SIZE = 10;
        final int OK_BUTTON_WIDTH = 100;
        final int OK_BUTTON_HEIGHT = 25;
        setupFrame(owner, title);
        JPanel panel = getElementsPanel(selectedElement, allElements);
        JButton okButton = createOkButton(OK_BUTTON_WIDTH, OK_BUTTON_HEIGHT);
        panel.add(okButton, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE));
        this.add(panel);
        this.pack();
    }

    private void setupFrame(Component owner, final String title){
        setLocationRelativeTo(owner);
        setTitle(title);
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JPanel getElementsPanel(Set<T> selectedElements, List<T> allElements){
        elementsList = allElements;
        final int elementsSize = elementsList.size();
        checkBoxes = new JCheckBox[elementsSize];
        T element;
        boolean selected;
        JCheckBox checkBox;
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for(int i=0; i<elementsSize; i++){
            element = elementsList.get(i);
            checkBox = new JCheckBox();
            selected = selectedElements != null && selectedElements.contains(element);
            checkBox.setText(getName(element));
            checkBox.setSelected(selected);
            checkBoxes[i] = checkBox;
            panel.add(checkBoxes[i]);
        }
        return panel;
    }

    public abstract String getName(T element);

    private JButton createOkButton(int buttonWidth, int buttonHeight) {
        JButton okButton = new JButton();
        okButton.setText("OK");
        okButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        okButton.addActionListener(e->dispose());
        return okButton;
    }

    public Set<T> showModal() {
        setVisible(true);
        return getSelectedElements();
    }

    private Set<T> getSelectedElements(){
        Set<T> selectedElements = new LinkedHashSet<>();
        for(int i=0; i<checkBoxes.length; i++){
            if(checkBoxes[i].isSelected()){
                selectedElements.add(elementsList.get(i));
            }
        }
        return selectedElements;
    }
}

class PartOfSpeechCheckBoxDialog extends CheckBoxDialog<PartOfSpeech>{

    PartOfSpeechCheckBoxDialog(Component owner, String title, Set<PartOfSpeech> selectedElement, List<PartOfSpeech> allElements) {
        super(owner, title, selectedElement, allElements);
    }

    @Override
    public String getName(PartOfSpeech element) {
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        return element.getName(locale);
    }
}

class LexiconCheckBoxDialog extends CheckBoxDialog<Lexicon>{

    LexiconCheckBoxDialog(Component owner, String title, Set<Lexicon> selectedElement, List<Lexicon> allElements) {
        super(owner, title, selectedElement, allElements);
    }

    @Override
    public String getName(Lexicon element) {
        return element.toString();
    }
}

class ReverseRelationDialog extends JDialog implements ActionListener {

    private ButtonExt chooseButton;
    private ButtonExt cancelButton;
    private ButtonExt noReverseButton;
    private IRelationType lastRelation;
    private IRelationType selectedRelation;
    private JTree tree;
    private JCheckBox autoReverse;

    private ReverseRelationDialog(JFrame owner, RelationTreeModel model){
        super(owner, Labels.REVERSE_RELATION);
        setSize(new Dimension(400, 450));
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setLayout(new RiverLayout());
        setLocationRelativeTo(owner);

        tree = createTree();
        tree.setModel(model);
        initButtons();
        addContentToWindow();
    }

    private JTree createTree(){
        JTree tree = new JTree();
        tree.setExpandsSelectedPaths(true);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(e -> {
            RelationTypeNode node = (RelationTypeNode)tree.getLastSelectedPathComponent();
            if(node != null){
                selectedRelation = node.getValue();
                chooseButton.setEnabled(true);
            } else {
                chooseButton.setEnabled(false);
            }
            // TODO zrobić zabezpieczenie przed wybraniem tego samego typu
        });

        //TODO przenieść to to do klasy
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(RelationTypesIM.getOpenImage());
        renderer.setClosedIcon(RelationTypesIM.getClosedImage());
        renderer.setLeafIcon(RelationTypesIM.getLeafImage());
        tree.setCellRenderer(renderer);

        return tree;
    }

    private void initButtons(){
        chooseButton = new ButtonExt(Labels.SELECT, this, KeyEvent.VK_B);
        chooseButton.setEnabled(false);
        noReverseButton = new ButtonExt(Labels.WITHOUT_REVERSE, this, KeyEvent.VK_B);
        cancelButton = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);

        autoReverse = new JCheckBox(Labels.AUTO_ADD_REVERSE);
        autoReverse.setSelected(false);
    }

    private void addContentToWindow(){
        this.add(new LabelExt(Labels.RELATION_TYPE_COLON, 't', tree));
        this.add("br hfill vfill",new JScrollPane(tree));
        this.add("br left",autoReverse);
        this.add("br center",chooseButton);
        this.add("",noReverseButton);
        this.add("",cancelButton);
    }

    public static Pair<IRelationType, Boolean> showModal(JFrame owner, IRelationType lastReverse, Boolean autoReverse, RelationTreeModel treeModel){
        ReverseRelationDialog dialog = new ReverseRelationDialog(owner, treeModel);
        dialog.lastRelation = lastReverse;
        dialog.autoReverse.setSelected(autoReverse != null ? autoReverse : false);
        // TODO ustawienie wartośći
        dialog.setVisible(true);
        Pair<IRelationType, Boolean> result = new Pair<>(dialog.lastRelation, dialog.autoReverse.isSelected());
        dialog.dispose();
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == chooseButton){
            lastRelation = selectedRelation;
        } else if(e.getSource() == noReverseButton){
            lastRelation = null;
        }
        dispose();
    }
}