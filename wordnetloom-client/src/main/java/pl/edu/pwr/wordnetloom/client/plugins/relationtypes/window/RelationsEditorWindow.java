package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;

import javax.management.relation.RelationType;
import javax.swing.*;
import java.text.ParseException;


public class RelationsEditorWindow extends MFrame { //implements ActionListener, TreeSelectionListener, CaretListener, ListSelectionListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private final int MIN_WINDOW_WIDTH = 200;
    private final int MIN_WINDOW_HEIGHT = 400;

    // komponenty panelu dolnego (propertiesPanel)
    private MTextField lexicon;
    private MTextField relationName;
    private JCheckBox multilingual;
    private MTextField relationDisplay;
    private MTextField relationShortcut;
    //private DescriptionTextArea relationDescription;
    private MTextField relationPos;
    private MTextField relationReverse;
    private JFormattedTextField relationColor;
    private MComboBox<NodeDirection> relationDirection; //TODO chyba będzie trzeba dodać

    private MButton buttonPos;
    private MButton buttonReverse;
    private MButton buttonColor;
    private MButton buttonSave;
    private MButton buttonCancel;

    private JPanel detailsPanel;
    private JTabbedPane relationsPanel;

    private JList testsList;

    private RelationType currentEditedType;

    public static void showModal(Workbench workbench) throws ParseException {
        RelationsEditorWindow frame = new RelationsEditorWindow(workbench);
        // frame.setLocationRelativeTo(workbench.getFrame());
        // frame.setVisible(true);
        frame.showModal();
    }

    private RelationsEditorWindow(Workbench workbench) throws ParseException {
        //  super(workbench.getFrame(), Labels.EDIT_RELATION_TYPES, 550, 700);
        // setResizable(true);
        // setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Dimension separatorSize = new Dimension(10, 10);
        init();
        //       createView();
    }

    private void init() {
        //setLayout(new BorderLayout());
        //setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
    }
}
    /*
/**
 * Tworzy widok calego okna.
 * <p>
 * Tworzy podział pomiędzy dwoma panelami. Rozmiar paneli może być dzięki
 * temu zmieniany za pomocą przeciągnięcia podziału.
 *
 * @param topPanel    panel, którym ma znajdować się na górze
 * @param bottomPanel panel, który ma znajdować się na dole
 * @return widok zawierający dwa panele i przedział pobiędzy nimi
 * <p>
 * Tworzy podział pomiędzy dwoma panelami. Rozmiar paneli może być dzięki
 * temu zmieniany za pomocą przeciągnięcia podziału.
 * @param topPanel    panel, którym ma znajdować się na górze
 * @param bottomPanel panel, który ma znajdować się na dole
 * @return widok zawierający dwa panele i przedział pobiędzy nimi
 * <p>
 * Tworzy podział pomiędzy dwoma panelami. Rozmiar paneli może być dzięki
 * temu zmieniany za pomocą przeciągnięcia podziału.
 * @param topPanel    panel, którym ma znajdować się na górze
 * @param bottomPanel panel, który ma znajdować się na dole
 * @return widok zawierający dwa panele i przedział pobiędzy nimi
 *//*

    private void createView() throws ParseException {
        relationsPanel = createTabbedPanel();
        detailsPanel = createDetailsPanel();
        add(createSplitPanel(relationsPanel, detailsPanel), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new RiverLayout());
        buttonSave = new MButton(Labels.SAVE, e -> save());
        buttonCancel = new MButton(Labels.CANCEL, e -> dispose());
        buttonsPanel.add(RiverLayout.LINE_BREAK + RiverLayout.CENTER, buttonSave);
        buttonsPanel.add(RiverLayout.TAB_STOP, buttonCancel);

        JButton testButton = new MButton("test", e -> clearDetails());
        buttonsPanel.add(RiverLayout.LINE_BREAK, testButton);
        JButton testButton2 = new MButton("test2", e -> setDetailsPanelEnabled(true));
        buttonsPanel.add(RiverLayout.TAB_STOP, testButton2);
        add(buttonsPanel, BorderLayout.SOUTH);

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

    */
/**
 * Tworzy podział pomiędzy dwoma panelami. Rozmiar paneli może być dzięki
 * temu zmieniany za pomocą przeciągnięcia podziału.
 *
 * @param topPanel    panel, którym ma znajdować się na górze
 * @param bottomPanel panel, który ma znajdować się na dole
 * @return widok zawierający dwa panele i przedział pobiędzy nimi
 *//*

    private SplitPaneExt createSplitPanel(JComponent topPanel, JComponent bottomPanel) {
        SplitPaneExt mainSplit = new SplitPaneExt(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        mainSplit.setStartDividerLocation(200);
        mainSplit.setResizeWeight(1.0f);
        return mainSplit;
    }

    private JPanel createDetailsPanel() throws ParseException {
        //utworzenie pola lexicon
        lexicon = new MTextField("");
        lexicon.addActionListener(this);

        // utworzenie pola zaznaczania wielojęzyczności
        multilingual = new JCheckBox(Labels.MULTILINGUAL);
        multilingual.addActionListener(this);

        // utworzenie pola nazwy relacji
        relationName = new MTextField("");
        relationName.addActionListener(this);

        // utworzenie pola formy do wyświetlenia
        relationDisplay = new MTextField("");
        relationDisplay.addCaretListener(this);

        // utworzenie skrótu relacji
        relationShortcut = new MTextField("");
        relationShortcut.addCaretListener(this);

        // utowrzenie opisu
        relationDescription = new DescriptionTextArea();
        relationDescription.setRows(2);
//        relationDescription.addCaretListener(this);


        // utworzenie pola części mowy oraz przycisku do wybierania części mowy
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        relationPos = new MTextField(PartOfSpeechManager.getInstance().getById(1L).getName(locale));

        buttonPos = new MButton("...", e -> openPartOfSpeechDialog());
        buttonPos.setToolTipText(Hints.CHOOSE_POS_FOR_RELATION);

        // utworzenie relacji odwrotnej oraz przycisku do wybierania
        relationReverse = new MTextField(Labels.NO_VALUE);

        buttonReverse = new MButton("...", e -> openReverseRelationDialog());
        buttonReverse.setToolTipText(Hints.CHOOSE_REVERSE_RELATION);

        //utworzenie pola koloru i przycisku do wyboru koloru
        MaskFormatter colorFormatter = new MaskFormatter("'#HHHHHH");

        relationColor = new JFormattedTextField(colorFormatter);
        // TODO dodać możliwość wpisywania koloru w pole tekstowe

        buttonColor = new MButton("...", this);
        buttonColor.setToolTipText(Hints.CHOOSE_COLOR_RELATION);

        // utowrzenie pola wyboru kierunku relacji
        relationDirection = new MComboBox<>();

        // utworzenie panelu z testami relacji
//        JPanel testsPanel = createTestsPanel();
        JPanel testsPanel = new TestsPanel(this);

        // utworzenie etykiet
        MLabel lexiconLabel = new MLabel(Labels.LEXICON_COLON, 'l', lexicon);
        MLabel nameLabel = new MLabel(Labels.RELATION_NAME_COLON, 'n', relationName);
        MLabel displayLabel = new MLabel(Labels.DISPALY_FORM_COLON, 'f', relationDisplay);
        MLabel shortcutLabel = new MLabel(Labels.SHORTCUT_COLON, 's', relationShortcut);
        MLabel descriptionLabel = new MLabel(Labels.DESCRIPTION_COLON, 'o', relationPos);
        MLabel posLabel = new MLabel(Labels.PARTS_OF_SPEECH_COLON, 'c', relationPos);
        MLabel reverseLabel = new MLabel(Labels.REVERSE_RELATION, '\0', relationReverse);
        MLabel colorLabel = new MLabel(Labels.COLOR_COLON, 'q', relationColor);
        MLabel directionLabel = new MLabel(Labels.DIRECTION_COLON, 'd', relationDirection);
        MLabel testsLabel = new MLabel(Labels.TESTS_COLON, 't', testsPanel);

        String LINE_BREAK = RiverLayout.LINE_BREAK;
        String LEFT_BREAK = RiverLayout.LINE_BREAK + " " + RiverLayout.LEFT;
        String LINE_FILL = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL;
        String TAB_FILL = RiverLayout.TAB_STOP + " " + RiverLayout.HFILL;
        String RIGHT = RiverLayout.RIGHT;

        // dodanie komponentów do panelu
        JPanel panel = new JPanel(new RiverLayout());

        panel = new JPanel(new RiverLayout());
        panel.add(LINE_BREAK, multilingual);
        panel.add(LINE_BREAK, lexiconLabel);
        panel.add(TAB_FILL, lexicon);
        JButton buttonLexicon = new MButton("...", rel -> {
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

    private void openPartOfSpeechDialog() {
        Set<PartOfSpeech> partOfSpeeches = new PartOfSpeechCheckBoxDialog(buttonPos,
                Labels.PARTS_OF_SPEECH_COLON,
                null,
                PartOfSpeechManager.getInstance().getAll()
        ).showModal();
        setPartOfSpeechFieldText(partOfSpeeches);
    }

    private void openReverseRelationDialog() {
        RelationTreeModel model = ((RelationsTypePanel) relationsPanel.getSelectedComponent()).getModel();
        Pair<RelationType, Boolean> reverseRelationPair = ReverseRelationDialog.showModal(this, currentEditedType.getReverse(), currentEditedType.getAutoReverse(), model);
        if (reverseRelationPair.getA() != null) {
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
        if (lexicons == null) {
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
        if (pos == null) {
            relationPos.setText("");
            return;
        }
        String posText = "";
        if (!pos.isEmpty()) {
            StringBuilder textBuilder = new StringBuilder();
            Iterator iterator = pos.iterator();
            String locale = RemoteConnectionProvider.getInstance().getLanguage();
            while (iterator.hasNext()) {
                textBuilder.append(((PartOfSpeech) iterator.next()).getName(locale));
                if (iterator.hasNext()) {
                    textBuilder.append(", ");
                }
            }
            posText = textBuilder.toString();
        }
        relationPos.setText(posText);

    }

    public void refreshDetails(RelationType rel, ERelationType type) {
        currentEditedType = rel;
        if (rel == null) {
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

    private void setDetailsPanelEnabled(boolean enabled) {
        for (Component component : detailsPanel.getComponents()) {
            component.setEnabled(enabled);
        }
        // jeżeli włączamy pola, trzeba zablokować możliwość edytowania polom, których nie powinno się blokować
        if (enabled) {
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

    private final JFrame owner;
    private final JList testsList;
    private final GenericListModel<RelationTest> testsModel;
    private final ButtonsPanel testButtonsPanel;
    private RelationType currentRelationType;

    public TestsPanel(JFrame owner) {
        this.owner = owner;
        setLayout(new BorderLayout());

        // utworzenie listy testów

        RelationTest test1 = new RelationTest();
        test1.setTest("Test pierwszy");
        RelationTest test2 = new RelationTest();
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

    public void updatePanel(RelationType relationType) {
        //TODO pobranie testów z bazy i ich wyświetlenie
        currentRelationType = relationType;
    }

    @Override
    public void setEnabled(boolean enabled) {
        testsList.setEnabled(enabled);
        testButtonsPanel.enableAllButtons(enabled);
    }

    private ButtonsPanel createTestButtonsPanel() {
        ButtonsPanel buttonsPanel = new ButtonsPanel.Builder(ButtonsPanel.VERTICAL)
                .addButton(ADD_TEST, new MButton(RelationTypesIM.getNewImage(), e -> createTest()), Hints.CREATE_NEW_TEST)
                .addButton(EDIT_TEST, new MButton(RelationTypesIM.getEditImage(), e -> editTest()), Hints.EDIT_SELECTED_TEST)
                .addButton(REMOVE_TEST, new MButton(RelationTypesIM.getDeleteImage(), e -> deleteTest()), Hints.REMOVE_SELECTED_TEST)
                .addButton(MOVE_TEST_UP, new MButton(RelationTypesIM.getMoveUpImage(), e -> moveTestUp()), Hints.MOVE_TEST_UP)
                .addButton(MOVE_TEST_DOWN, new MButton(RelationTypesIM.getMoveDownImage(), e -> moveTestDown()), Hints.MOVE_TEST_DOWN)
                .setButtonsEnabled(true)
                .build();
        return buttonsPanel;
    }

    //TODO podorabiać
    private void createTest() {
        //TODO ustawić częśc mowy
        PartOfSpeech partOfSpeech = PartOfSpeechManager.getInstance().getById(1L);
//        Pair<String, PartOfSpeech> partOfSpeechPair = TestEditorWindow.showModal(owner, new SenseRelationTest());
        RelationTest senseTest = new RelationTest();
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

class TestListRenderer extends JPanel implements ListCellRenderer<RelationTest> {
    private JLabel posLabel;
    private JLabel testLabel;

    public TestListRenderer() {

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends RelationTest> list, RelationTest value, int index, boolean isSelected, boolean cellHasFocus) {

        return this;
    }
}

class DescriptionTextArea extends JScrollPane {

    private final JTextArea textArea;

    public DescriptionTextArea() {
        textArea = new MTextArea("");
        getViewport().setView(textArea);
    }

    @Override
    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        textArea.setEnabled(enabled);
        textArea.setEditable(enabled);
    }

    public void setRows(int rows) {
        textArea.setRows(rows);
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}

class RelationsTypePanel extends JPanel {
    private static final long serialVersionUID = 4649824763750406980L;

    private final RelationsEditorWindow parentWindow;
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

    public RelationTreeModel getModel() {
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
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
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
        List<RelationType> children = new ArrayList<>();
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
            RelationTypeNode node = (RelationTypeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
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

        add(new JScrollPane(tree), constrains);
    }

    private void createRelationButtons(String constrains) {
        JButton moveUpButton = new MButton(RelationTypesIM.getMoveUpImage(), e -> moveRelationUp());
        JButton moveDownButton = new MButton(RelationTypesIM.getMoveDownImage(), e -> moveRelationDown());
        JButton addButton = new MButton(RelationTypesIM.getNewImage(), e -> addRelation());
        JButton addSubRelationButton = new MButton(RelationTypesIM.getNewSubImage(), e -> addSubRelation());
        JButton removeButton = new MButton(RelationTypesIM.getDeleteImage(), e -> removeRelation());
        ButtonsPanel buttonsPanel = new ButtonsPanel.Builder(ButtonsPanel.VERTICAL)
                .addButton(MOVE_RELATION_UP, moveUpButton, Hints.MOVE_RELATION_UP)
                .addButton(MOVE_RELATION_DOWN, moveDownButton, Hints.MOVE_RELATION_DOWN)
                .addButton(ADD_RELATION, addButton, Hints.CREATE_RELATION_TYPE)
                .addButton(ADD_SUBRELATION, addSubRelationButton, Hints.CREATE_NEW_RELATION_SUBTYPE)
                .addButton(REMOVE_RELATION, removeButton, Hints.REMOVE_SELECTED_REL_AND_SUBRELATION)
                .setButtonsEnabled(true)
                .build();

        add(buttonsPanel, constrains);
    }

    private void refreshTree() {
        List<TreePath> expandedPaths = new ArrayList<>();
        for (int i = 0; i < tree.getRowCount() - 1; i++) {
            if (tree.getPathForRow(i).isDescendant(tree.getPathForRow(i + 1))) {
                expandedPaths.add(tree.getPathForRow(i));
            }
        }
        RelationTreeModel typeModel = (RelationTreeModel) tree.getModel();
        typeModel.reload();
//        tree.updateUI();
        for (TreePath path : expandedPaths) {
            tree.expandPath(path);
        }
    }

    // poniższe metody zmieniają tylko pozycje na liście
    // usuwaniem z bazy danych powiny zajmowac się przeciążone metody klas dziedziczących
    private void moveRelationUp() {
        //TODO operacja na bazie danych
        RelationTypeNode node = (RelationTypeNode) tree.getLastSelectedPathComponent();
        model.moveNodeUp(node);
        tree.updateUI();
    }

    private void moveRelationDown() {
        //TODO operacja na bazie danych
        RelationTypeNode node = (RelationTypeNode) tree.getLastSelectedPathComponent();
        model.moveNodeDown(node);
        tree.updateUI();
    }

    private void addRelation() {
        String relationName = JOptionPane.showInputDialog(Labels.RELATION_NAME_COLON);
        //TODO operacja na bazie danych
        RelationType type = new RelationType(); //TODO znaleźć sposób, aby utworzyć odpowiedni obiekt, zrobić jakiś kreator czy coś
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        type.setName(locale, relationName);
        model.addNode(type);
//        tree.updateUI();
        refreshTree();
    }

    private void addSubRelation() {
        RelationTypeNode node = (RelationTypeNode) tree.getLastSelectedPathComponent();
        if (node.getLevel() == RelationTypeNode.SUBTYPE_LEVEL) {
            node = node.getParent();
        }
        if (node == null) {
            return;
        }
        String subRelationName = JOptionPane.showInputDialog(Labels.SUBTYPE_RELATION_NAME_COLON);
        //TODO operacja na bazie danych
        RelationType child = new RelationType();
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        child.setName(locale, subRelationName);
        model.addChild(node, child);
//        model.nodeChanged(node);
//        refreshTree();
        tree.updateUI();
    }

    private void removeRelation() {
        RelationTypeNode node = (RelationTypeNode) tree.getLastSelectedPathComponent();
        //TODO operacja na bazie danych
        model.removeChild(node.getParent(), node);
        tree.updateUI();
    }
}

abstract class CheckBoxDialog<T> extends JDialog {
    private JCheckBox[] checkBoxes;
    private java.util.List<T> elementsList;

    public CheckBoxDialog(Component owner, String title, Set<T> selectedElement, List<T> allElements) {
        int EMPTY_BORDER_SIZE = 10;
        int OK_BUTTON_WIDTH = 100;
        int OK_BUTTON_HEIGHT = 25;
        setupFrame(owner, title);
        JPanel panel = getElementsPanel(selectedElement, allElements);
        JButton okButton = createOkButton(OK_BUTTON_WIDTH, OK_BUTTON_HEIGHT);
        panel.add(okButton, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE));
        add(panel);
        pack();
    }

    private void setupFrame(Component owner, String title) {
        setLocationRelativeTo(owner);
        setTitle(title);
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JPanel getElementsPanel(Set<T> selectedElements, List<T> allElements) {
        elementsList = allElements;
        int elementsSize = elementsList.size();
        checkBoxes = new JCheckBox[elementsSize];
        T element;
        boolean selected;
        JCheckBox checkBox;
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < elementsSize; i++) {
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
        okButton.addActionListener(e -> dispose());
        return okButton;
    }

    public Set<T> showModal() {
        setVisible(true);
        return getSelectedElements();
    }

    private Set<T> getSelectedElements() {
        Set<T> selectedElements = new LinkedHashSet<>();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                selectedElements.add(elementsList.get(i));
            }
        }
        return selectedElements;
    }
}

class PartOfSpeechCheckBoxDialog extends CheckBoxDialog<PartOfSpeech> {

    PartOfSpeechCheckBoxDialog(Component owner, String title, Set<PartOfSpeech> selectedElement, List<PartOfSpeech> allElements) {
        super(owner, title, selectedElement, allElements);
    }

    @Override
    public String getName(PartOfSpeech element) {
        String locale = RemoteConnectionProvider.getInstance().getLanguage();
        return element.getName(locale);
    }
}

class LexiconCheckBoxDialog extends CheckBoxDialog<Lexicon> {

    LexiconCheckBoxDialog(Component owner, String title, Set<Lexicon> selectedElement, List<Lexicon> allElements) {
        super(owner, title, selectedElement, allElements);
    }

    @Override
    public String getName(Lexicon element) {
        return element.toString();
    }
}

class ReverseRelationDialog extends JDialog implements ActionListener {

    private MButton chooseButton;
    private MButton cancelButton;
    private MButton noReverseButton;
    private RelationType lastRelation;
    private RelationType selectedRelation;
    private final JTree tree;
    private JCheckBox autoReverse;

    private ReverseRelationDialog(JFrame owner, RelationTreeModel model) {
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

    private JTree createTree() {
        JTree tree = new JTree();
        tree.setExpandsSelectedPaths(true);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(e -> {
            RelationTypeNode node = (RelationTypeNode) tree.getLastSelectedPathComponent();
            if (node != null) {
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

    private void initButtons() {
        chooseButton = new MButton(Labels.SELECT, this, KeyEvent.VK_B);
        chooseButton.setEnabled(false);
        noReverseButton = new MButton(Labels.WITHOUT_REVERSE, this, KeyEvent.VK_B);
        cancelButton = new MButton(Labels.CANCEL, this, KeyEvent.VK_A);

        autoReverse = new JCheckBox(Labels.AUTO_ADD_REVERSE);
        autoReverse.setSelected(false);
    }

    private void addContentToWindow() {
        add(new MLabel(Labels.RELATION_TYPE_COLON, 't', tree));
        add("br hfill vfill", new JScrollPane(tree));
        add("br left", autoReverse);
        add("br center", chooseButton);
        add("", noReverseButton);
        add("", cancelButton);
    }

    public static Pair<RelationType, Boolean> showModal(JFrame owner, RelationType lastReverse, Boolean autoReverse, RelationTreeModel treeModel) {
        ReverseRelationDialog dialog = new ReverseRelationDialog(owner, treeModel);
        dialog.lastRelation = lastReverse;
        dialog.autoReverse.setSelected(autoReverse != null ? autoReverse : false);
        // TODO ustawienie wartośći
        dialog.setVisible(true);
        Pair<RelationType, Boolean> result = new Pair<>(dialog.lastRelation, dialog.autoReverse.isSelected());
        dialog.dispose();
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseButton) {
            lastRelation = selectedRelation;
        } else if (e.getSource() == noReverseButton) {
            lastRelation = null;
        }
        dispose();
    }
}*/
