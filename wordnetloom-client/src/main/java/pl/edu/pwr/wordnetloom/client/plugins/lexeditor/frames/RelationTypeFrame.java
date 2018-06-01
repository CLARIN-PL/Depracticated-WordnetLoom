package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import com.alee.laf.rootpane.WebFrame;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SenseFormat;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RelationTypeFrame extends DialogWindow implements ActionListener, KeyListener {

    protected static final long serialVersionUID = 1L;
    protected static final int MULTILINGUAL_RELATIONS = 1;
    protected static final int ALL_RELATIONS = 0;
    protected static MComboBox parentItem;
    protected static MComboBox childItem;
    protected static PartOfSpeech pos;
    private static boolean testsPositive;
    protected MComboBox relationType;
    protected MComboBox relationSubType;
    protected MButton buttonChoose, buttonCancel;
    protected MTextArea description;
    protected JList testsList;
    protected RelationType chosenType = null;
    protected List<RelationType> mainRelations = null;
    protected RelationArgument relationsType;

    protected MButton buttonSwitch;
    protected JPanel jp;
    private RelationType fixedRelationType;

    private RelationTypeFrame(WebFrame frame,
                              RelationArgument type,
                              PartOfSpeech pos,
                              RelationType fixedRelationType,
                              RelationType suggestedRelationType,
                              Sense suggestedUnit,
                              Collection<Sense> parentUnits,
                              Collection<Sense> middleUnits,
                              Collection<Sense> childUnits) {
        super(frame, Labels.RELATION_PARAMS, 650, 500);
        setLocationRelativeTo(frame);
        RelationTypeFrame.pos = pos;
        this.fixedRelationType = fixedRelationType;
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        init(type);

        parentUnits.forEach(parentItem::addItem);
        childUnits.forEach(childItem::addItem);

        //Wybranie zaproponowanej jednostki
        if (suggestedUnit != null) {
            childItem.setSelectedItem(suggestedUnit);
            parentItem.setSelectedItem(suggestedUnit);
        }
        initView();
    }

    /**
     * @param frame             parent component
     * @param type              relation type
     * @param pos               part of speech
     * @param fixedRelationType fixed relation type
     * @author amusial Constructor for derived classes
     */
    protected RelationTypeFrame(WebFrame frame,
                                RelationArgument type,
                                PartOfSpeech pos,
                                RelationType fixedRelationType) {
        super(frame, Labels.RELATION_PARAMS, 650, 500);
        RelationTypeFrame.pos = pos;
        this.fixedRelationType = fixedRelationType;
        relationsType = type;

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init(relationsType);
    }

    /**
     * wyswietlenie okienka dialogowego
     *
     * @param workbench   - srodowisko
     * @param type        - typ relacji
     * @param pos         - czesc mowy dla ktorej ma byc brana relacja
     * @param parentUnits - jednostki nadrzedne
     * @param childUnits  - jednostki podrzedne
     * @return typ relacji albo null
     */
    public static RelationType showModal(Workbench workbench,
//                                         String type,
                                         RelationArgument type,
                                         PartOfSpeech pos,
                                         Collection<Sense> parentUnits,
                                         Collection<Sense> childUnits) {
        return showModal(workbench.getFrame(), type, pos, null, null, null, parentUnits, null, childUnits);
    }

    /**
     * @param workbench
     * @param type
     * @param pos
     * @param fixedRelationType
     * @param parentUnits
     * @param middleUnits
     * @param childUnits
     * @return typ relacji albo null
     */
    public static RelationType showModal(Workbench workbench,
//                                         String type,
                                         RelationArgument type,
                                         PartOfSpeech pos,
                                         RelationType fixedRelationType,
                                         Collection<Sense> parentUnits,
                                         Collection<Sense> middleUnits,
                                         Collection<Sense> childUnits) {
        return showModal(workbench.getFrame(), type, pos, fixedRelationType, null, null, parentUnits, middleUnits, childUnits);
    }

    /**
     * konwersja jednostki na liste
     *
     * @param unit - jednostka
     * @return lista zawierajaca jednostek
     */
    public static Collection<Sense> unitToList(Sense unit) {
        Collection<Sense> list = new ArrayList<>();
        list.add(unit);
        return list;
    }

    /**
     * wyswietlenie okienka dialogowego
     *
     * @param frame                 - srodowisko
     * @param type                  - typ relacji
     * @param pos                   - czesc mowy dla ktorej ma byc brana relacja
     * @param relationType          - typ relacji jesli jest ustawiona na sztywno
     * @param suggestedRelationType - typ sugerowanej relacji, o ile istnieje
     * @param suggestedUnit
     * @param parentUnits           - jednostki nadrzedne
     * @param middleUnits           - jednostki posrednie
     * @param childUnits            - jednostki podrzedne
     * @return typ relacji albo null
     */
    public static RelationType showModal(WebFrame frame,
//                                         String type,
                                         RelationArgument type,
                                         PartOfSpeech pos,
                                         RelationType relationType,
                                         RelationType suggestedRelationType,
                                         Sense suggestedUnit,
                                         Collection<Sense> parentUnits,
                                         Collection<Sense> middleUnits,
                                         Collection<Sense> childUnits) {
        RelationTypeFrame framew = new RelationTypeFrame(frame, type, pos, relationType, suggestedRelationType, suggestedUnit, parentUnits, middleUnits, childUnits);
        framew.setVisible(true);
        return framew.chosenType;
    }

    private static String getMarker(String startWith, String text) {
        int startIndex = text.indexOf(startWith);
        if (startIndex < 0) {
            return null;
        }
        int endIndex = text.indexOf(">", startIndex);
        return text.substring(startIndex, endIndex + 1);
    }

    private static List<String> getTests(RelationType relation, String parent, String child, PartOfSpeech partOfSpeechA, PartOfSpeech partOfSpeechB) {
        List<String> result = new ArrayList<>();
        List<RelationTest> tests = RemoteService.relationTestRemote.findByRelationType(relation);
        String text;
        String markerX;
        String markerY;
        int testIndex = 1;
        testsPositive = true;
        for (RelationTest test : tests) {
            text = test.getTest();
            markerX = getMarker("<x#", text);
            markerY = getMarker("<y#", text);
            // zaznaczanie poprawności części mowy
            try {
                if ((test.getSenseApartOfSpeech() != null && !test.getSenseApartOfSpeech().equals(partOfSpeechA)) || (test.getSenseBpartOfSpeech() != null && !test.getSenseBpartOfSpeech().equals(partOfSpeechB))) {
                    text += "<font color=\"red\">[" + Labels.PARTS_OF_SPEECH_COLON + Labels.ERROR + "]</font>";
                    testsPositive = false;
                } else {
                    text += "<font color=\"green\">[" + Labels.PARTS_OF_SPEECH_COLON + Labels.OK + "]</font>";
                }
            } catch (Exception e) {
                System.out.println();
            }

            if (markerX != null) {
                String tag = markerX.replace("<x#","");
                tag = tag.replace("%>","");
                String form = findForm(parent, tag);
                text = text.replace(markerX, "<font color=\"blue\">" + form + "</font>");
            }
            if (markerY != null) {
                String tag = markerY.replace("<y#","");
                tag = tag.replace("%>","");
                String form = findForm(child, tag);
                text = text.replace(markerY, "<font color=\"blue\">" + form + "</font>");
            }

            result.add("<html>" + (testIndex++) + ". " + text + "</html>");
        }
        return result;
    }

    private static String findForm(String unit, String tag) {

        String suffix = "";
        if (unit.endsWith("się")) {
            unit = unit.substring(0, unit.length() - 4);
            suffix = " się";
        }

        String form = RemoteService.wordFormServiceRemote.findFormByLemmaAndTag(unit, tag);

        if (form != null) {
            return form + suffix;
        }
        return unit + suffix;
    }

    protected void loadParentRelation(RelationArgument type, int filter) {
        switch (filter) {
            case ALL_RELATIONS:
                mainRelations = RelationTypeManager.getInstance().getParents(type);
                break;
            case MULTILINGUAL_RELATIONS:
                mainRelations = RelationTypeManager.getInstance().getMultilingualParents(type);
                break;
        }
        relationType.removeAllItems();
        for (RelationType relType : mainRelations) {
            relationType.addItem(relType);
        }
    }

    protected void loadParentRelation(RelationArgument type) {
        loadParentRelation(type, ALL_RELATIONS);
    }

    protected void init(RelationArgument type) {

        PartOfSpeechRenderer posRenderer = new PartOfSpeechRenderer();
        relationsType = type;
        relationType = new MComboBox();
        relationType.addKeyListener(this);
        relationType.addActionListener(this);
        relationType.setRenderer(posRenderer);

        relationSubType = new MComboBox();
        relationSubType.addKeyListener(this);
        relationSubType.addActionListener(this);
        relationSubType.setEnabled(false);
        relationSubType.setRenderer(posRenderer);

        description = new MTextArea("");
        description.setRows(6);
        description.setEditable(false);
        testsList = new JList();

        buttonSwitch = new MButton(this)
                .withIcon(FontAwesome.EXCHANGE)
                .withCaption(Labels.SWITCH)
                .withMnemonic(KeyEvent.VK_Z)
                .withKeyListener(this);

        buttonChoose = MButton.buildSelectButton()
                .withMnemonic(KeyEvent.VK_ENTER)
                .withKeyListener(this)
                .withActionListener(this);

        buttonCancel = MButton.buildCancelButton()
                .withMnemonic(KeyEvent.VK_A)
                .withActionListener(this)
                .withKeyListener(this);

        parentItem = new MComboBox();
        childItem = new MComboBox();

        SenseRenderer senseRenderer = new SenseRenderer();
        parentItem.setRenderer(senseRenderer);
        childItem.setRenderer(senseRenderer);

        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);
    }

    protected void initView() {
        add("",
                new MLabel(Labels.RELATION_TYPE_COLON, 't', relationType));
        add("tab hfill", relationType);
        add("br", new MLabel(Labels.RELATION_SUBTYPE_COLON, 'y',
                relationType));
        add("tab hfill", relationSubType);
        add("br", new MLabel(Labels.RELATION_DESC_COLON, '\0',
                description));
        add("br hfill", new JScrollPane(description));

        jp = new JPanel();
        jp.setLayout(new RiverLayout());
        jp.add("br", new MLabel(Labels.SOURCE_UNIT_COLON, 'r', parentItem));
        jp.add("tab hfill", parentItem);
        jp.add("br", new MLabel(Labels.TARGET_UNIT_COLON, 'd', childItem));
        jp.add("tab hfill", childItem);

        add("br hfill", jp);
        add("", buttonSwitch);

        add("br", new MLabel(Labels.TESTS_COLON, '\0', testsList));
        add("br hfill vfill", new JScrollPane(testsList));
        add("br center", buttonChoose);
        add("", buttonCancel);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                event.consume();
                setVisible(false);
                break;
        }
    }

    /**
     * odczytanie zaznaczonej relacji
     *
     * @return zaznaczona relacja
     */
    protected RelationType getSelectedRelation() {
        if (relationSubType.isEnabled()) { // relacja ma podtypy
            return (RelationType) relationSubType.getSelectedItem();
        } else {
            return (RelationType) relationType.getSelectedItem();
        }
    }

    protected void swapParentAndChildrenModels() {
        ComboBoxModel cbm = parentItem.getModel();
        parentItem.setModel(childItem.getModel());
        childItem.setModel(cbm);
        refreshTests();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonChoose) {
            chosenType = getSelectedRelation();
            setVisible(false);
        } else if (event.getSource() == buttonCancel) {
            setVisible(false);
        } else if (event.getSource() == relationType) {
            changeRelation();
        } else if (event.getSource() == relationSubType) {
            if (relationSubType.getItemCount() > 0) {
                refreshTests((RelationType) relationSubType.getSelectedItem());
            }
        } else if (event.getSource() == buttonSwitch) {
            swapParentAndChild();
        } else if (event.getSource() == parentItem) {
//            refreshTests();
//            parentSense = (Sense) parentItem.getSelectedItem();
        } else //            childSense = (Sense) childItem.getSelectedItem();
            if (event.getSource() == childItem) {

            }
    }

    protected void swapParentAndChild() {
        //TODO
    }

    private void changeRelation() {
        relationSubType.removeAllItems();

        RelationType parentRelation = (RelationType) relationType.getSelectedItem();
        if (parentRelation == null) {
            relationSubType.setEnabled(false);
            return;
        }
        boolean hasSubrelation = loadSubtypeRelation(parentRelation);
        relationSubType.setEnabled(hasSubrelation);
        showDescription(parentRelation);

        Sense parentSense = (Sense) parentItem.getSelectedItem();
        Sense childSense = (Sense) childItem.getSelectedItem();
        if (parentSense != null) {
            if (hasSubrelation) {
                RelationType childRelation = (RelationType) relationSubType.getSelectedItem();
                refreshTests(childRelation, parentSense, childSense);
            } else {
                refreshTests(parentRelation, parentSense, childSense);
            }
        }
    }

    private void refreshTests(RelationType relation) {
        if (parentItem.getSelectedItem() != null) {
            refreshTests(relation, (Sense) parentItem.getSelectedItem(), (Sense) childItem.getSelectedItem());
        }
    }

    //TODO przejrzeć to i pousuwać niepotrzebne rzeczy
    private void refreshTests() {

        RelationType relation;
        if (relationSubType.getItemCount() > 0) {
            relation = (RelationType) relationSubType.getSelectedItem();
        } else {
            relation = (RelationType) relationType.getSelectedItem();
        }
        refreshTests(relation, (Sense) parentItem.getSelectedItem(), (Sense) childItem.getSelectedItem());
    }

    private void refreshTests(RelationType parentRelation, Sense parent, Sense child) {
        String parentName = parent.getWord().getWord();
        String childName = child.getWord().getWord();
        PartOfSpeech parentPos = parent.getPartOfSpeech();
        PartOfSpeech childPos = child.getPartOfSpeech();
        List<String> tests = getTests(parentRelation, parentName, childName, parentPos, childPos);
        testsList.setListData(tests.toArray());
    }

    private boolean loadSubtypeRelation(RelationType parentRelation) {
        List<RelationType> relationsSubType = RelationTypeManager.getInstance().getChildren(parentRelation.getId(), relationsType);
        if (!relationsSubType.isEmpty()) {
            for (RelationType relType : relationsSubType) {
                relationSubType.addItem(relType);
            }
            return true;
        } else {
            return false;
        }
    }

    private void showDescription(RelationType relationType) {
        String descriptionText = LocalisationManager.getInstance().getLocalisedString(relationType.getDescription());
        description.setText(descriptionText);
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    private class PartOfSpeechRenderer implements ListCellRenderer {

        DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                RelationType relationType = (RelationType) value;
                String text = LocalisationManager.getInstance().getLocalisedString(relationType.getName());
                renderer.setText(text);
            }
            return renderer;
        }
    }

    private class SenseRenderer implements ListCellRenderer {

        DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                Sense sense = (Sense) value;
                String text = SenseFormat.getText(sense);
                renderer.setText(text);
            }
            return renderer;
        }
    }
}
