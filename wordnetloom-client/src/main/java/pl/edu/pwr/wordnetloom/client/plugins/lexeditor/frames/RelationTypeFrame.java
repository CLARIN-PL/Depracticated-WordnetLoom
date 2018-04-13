package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import com.alee.laf.rootpane.WebFrame;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RelationTypeFrame extends DialogWindow implements ActionListener, KeyListener {

    protected static final long serialVersionUID = 1L;

    protected MComboBox relationType;
    protected MComboBox relationSubType;
    protected MButton buttonChoose, buttonCancel;
    protected MTextArea description;
    protected JList testsList;
    protected static MComboBox parentItem;
    protected MComboBox middleItem;
    protected static MComboBox childItem;
    private RelationType fixedRelationType;
    protected RelationType chosenType = null;
    protected List<RelationType> mainRelations = null;
    protected static PartOfSpeech pos;

    private Sense parentSense;
    private Sense childSense;

    private static boolean testsPositive;

    private class PartOfSpeechRenderer implements ListCellRenderer {

        DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(value != null){
                RelationType relationType = (RelationType)value;
                String text = LocalisationManager.getInstance().getLocalisedString(relationType.getName());
                renderer.setText(text);
            }
            return renderer;
        }
    }

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
        RelationTypeFrame.pos = pos;
        this.fixedRelationType = fixedRelationType;
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        init(type);

        // element nadrzedny
//        parentItem = new MComboBox();
        for (Sense parent : parentUnits) {
            parentItem.addItem(parent.getWord().getWord());
        }

        // element podrzedny
//        childItem = new MComboBox();
        for (Sense child : childUnits) {
            childItem.addItem(child.getWord().getWord());
        }
        //Wybranie zaproponowanej jednostki
        if (suggestedUnit != null) {

            childItem.setSelectedItem(suggestedUnit.getWord().getWord());
            parentItem.setSelectedItem(suggestedUnit.getWord().getWord());
        }

        // element posredni
        if (middleUnits != null && !middleUnits.isEmpty()) {
            for (Sense middle : middleUnits) {
                middleItem.addItem(middle.getWord());
            }
        } else {
            middleItem.setEnabled(false);
        }

        testsList = new JList();
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
                                String type,
                                PartOfSpeech pos,
                                RelationType fixedRelationType) {
        super(frame, Labels.RELATION_PARAMS, 650, 500);
        RelationTypeFrame.pos = pos;
        this.fixedRelationType = fixedRelationType;

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected static final int MULTILINGUAL_RELATIONS = 1;
    protected static final int ALL_RELATIONS = 0;

    protected void loadParentRelation(RelationArgument type, int filter){
        switch (filter){
            case ALL_RELATIONS:
                mainRelations = RelationTypeManager.getInstance().getParents(type);
                break;
            case MULTILINGUAL_RELATIONS:
                mainRelations = RelationTypeManager.getInstance().getMultilangualParents(type);
                break;
        }
        relationType.removeAllItems();
        for(RelationType relType : mainRelations){
            relationType.addItem(relType);
        }
    }

    protected void loadParentRelation(RelationArgument type){
        loadParentRelation(type, ALL_RELATIONS);
    }

    protected void init(RelationArgument type){
        relationType = new MComboBox();
        relationType.addKeyListener(this);
        relationType.addActionListener(this);
        relationType.setRenderer(new PartOfSpeechRenderer());

        relationSubType = new MComboBox();
        relationSubType.addKeyListener(this);
        relationSubType.addActionListener(this);
        relationSubType.setEnabled(false);
        relationSubType.setRenderer(new PartOfSpeechRenderer());

        description = new MTextArea("");
        description.setRows(6);
        description.setEditable(false);
        testsList = new JList();

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
        middleItem = new MComboBox();

        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);
        middleItem.addKeyListener(this);
        middleItem.addActionListener(this);
    }


    protected void initView(){
        add("", new MLabel(Labels.RELATION_TYPE_COLON, 't', relationType));
        add("tab hfill", relationType);
        add("br", new MLabel(Labels.RELATION_SUBTYPE_COLON, 'y', relationType));
        add("tab hfill", relationSubType);
        add("br", new MLabel(Labels.RELATION_DESC_COLON, '\0', description));
        add("br hfill", new JScrollPane(description));

        add("br", new MLabel(Labels.SOURCE_UNIT_COLON, 'r', parentItem));
        add("tab hfill", parentItem);

        if (middleItem.isEnabled()) {
            add("br", new MLabel(Labels.INTERMEDIATE_UNIT_COLON, 'p', parentItem));
            add("tab hfill", middleItem);
        }
        add("br", new MLabel(Labels.TARGET_UNIT_COLON, 'd', childItem));
        add("tab hfill", childItem);
        add("br", new MLabel(Labels.TESTS_COLON, '\0', testsList));
        add("br hfill vfill", new JScrollPane(testsList));
        add("br center", buttonChoose);
        add("", buttonCancel);
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
//        if (subRelations != null && subRelations.size() > 0) {
//            // jest pod typ
//            int index = relationSubType.getSelectedIndex();
//            for (RelationType type : subRelations) {
//                if (index-- == 0) {
//                    return type;
//                }
//            }
//        } else {
//            // brak podtypu
//            int index = relationType.getSelectedIndex();
//            for (RelationType type : mainRelations) {
//                if (index-- == 0) {
//                    return type;
//                }
//            }
//        }
//        return null;

        if(relationSubType.isEnabled()) { // relacja ma podtypy
           return (RelationType)relationSubType.getSelectedItem();
        } else {
            return (RelationType)relationType.getSelectedItem();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if(event.getSource() == buttonChoose) {
            chosenType = getSelectedRelation();
            this.setVisible(false);
        } else if(event.getSource() == buttonCancel) {
            this.setVisible(false);
        } else if(event.getSource() == relationType) {
            relationSubType.removeAllItems();
            testsList.setListData(new String[]{});
            RelationType parentRelation = (RelationType)relationType.getSelectedItem();
            if(parentRelation == null){
                relationSubType.setEnabled(false);
                return;
            }
            List<RelationType> relationsSubType = RelationTypeManager.getInstance().getChildren(parentRelation.getId(), RelationArgument.SYNSET_RELATION);
            if(!relationsSubType.isEmpty()) {
                for(RelationType relType : relationsSubType){
                    relationSubType.addItem(relType);
                }
                relationSubType.setEnabled(true);
            } else {
                relationSubType.setEnabled(false);
            }

            showDescription(parentRelation);
//            setTests(parentRelation);
            if(parentSense != null){
//                Sense parentSense = (Sense) parentItem.getSelectedItem();
//                Sense childSense = (Sense) childItem.getSelectedItem();
                String parentSenseName = parentSense.getWord().getWord();
                String childSenseName = childSense.getWord().getWord();
                PartOfSpeech parentSensePos = parentSense.getPartOfSpeech();
                PartOfSpeech childSensePos = childSense.getPartOfSpeech();
                List<String> tests = getTests(parentRelation, parentSenseName, childSenseName, parentSensePos, childSensePos);
                testsList.setListData(tests.toArray());
            }

        } else if(event.getSource() == parentItem){
            parentSense = (Sense) parentItem.getSelectedItem();
        } else if(event.getSource() == childItem) {
            childSense = (Sense) childItem.getSelectedItem();
        }
    }

    private void showDescription(RelationType relationType) {
        String descriptionText = LocalisationManager.getInstance().getLocalisedString(relationType.getDescription());
        description.setText(descriptionText);
    }

    private static String getMarker(final String startWith, final String text){
        int startIndex = text.indexOf(startWith);
        if(startIndex < 0){
            return null;
        }
        int endIndex = text.indexOf(">", startIndex);
        return text.substring(startIndex, endIndex+1);
    }

    protected static List<String> getTests(RelationType relation, String parent, String child, PartOfSpeech partOfSpeechA, PartOfSpeech partOfSpeechB) {
        List<String> result = new ArrayList<>();
        List<RelationTest> tests = RemoteService.relationTestRemote.findByRelationType(relation);
        String text;
        String markerX;
        String markerY;
        int testIndex = 1;
        testsPositive = true;
        for(RelationTest test : tests) {
            text = test.getTest();
            markerX = getMarker("<x#", text);
            markerY = getMarker("<y#", text);
            // zaznaczanie poprawności części mowy
            try{
                if ((test.getSenseApartOfSpeech() != null && !test.getSenseApartOfSpeech().equals(partOfSpeechA)) || (test.getSenseBpartOfSpeech() != null && !test.getSenseBpartOfSpeech().equals(partOfSpeechB))) {
                    text += "<font color=\"red\">[" + Labels.PARTS_OF_SPEECH_COLON + Labels.ERROR + "]</font>";
                    testsPositive = false;
                } else {
                    text += "<font color=\"green\">[" + Labels.PARTS_OF_SPEECH_COLON + Labels.OK + "]</font>";
                }
            } catch (Exception e){
                System.out.println();
            }


            if(markerX != null) {
                text = text.replace(markerX, "<font color=\"blue\">" + parent + "</font>");
            }
            if(markerY != null) {
                text = text.replace(markerY, "<font color=\"blue\">" + child + "</font>");
            }

            result.add("<html>" + (testIndex++) + ". " + text + "</html>");
        }
            return result;
    }

    private static Collection<String> getForms(Collection<String> defs, String unit) {
        Collection<String> forms = new ArrayList<>();
        // ustawieni suffixu
        String suffix = "";
        //TODO sprawdzić jak to powinno działać
        if (unit.endsWith("się")) { // konczy sie z się, a ma to zostać odcięte
            unit = unit.substring(0, unit.length() - 4);
            suffix = " się"; // ustawienie sufixu
        }
        for (String def : defs) {
            List<String> splits = Arrays.asList(def.split("\\|"));
            forms.add(unit);
            //forms.add(RemoteUtils.wordFormsRemote.getFormFor(new Word(unit), splits) + suffix);
        }
        return forms;
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
