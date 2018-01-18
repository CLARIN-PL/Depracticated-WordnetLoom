package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MakeNewRelationWindow extends RelationTypeFrame {

    private static final long serialVersionUID = 5479457915334417348L;

    protected MButton buttonSwitch;
    protected WebPanel jp;
    protected List<Long> lexicons;
    protected ViwnNode from[], to[];

    protected MakeNewRelationWindow(WebFrame frame, PartOfSpeech pos, ViwnNode[] from, ViwnNode[] to, Workbench workbench) {
        super(frame, "", pos, null);

        this.from = from;
        this.to = to;

        lexicons = LexiconManager.getInstance().getUserChosenLexiconsIds();

        ViwnNodeSynset fromNode = (ViwnNodeSynset)from[0];
        ViwnNodeSynset toNode = (ViwnNodeSynset)to[0];
        // relation from:
        parentItem = new MComboBox();
        //TODO sprawdzić, czy rzeczywiście o jednostki chodzi
        List<Sense> parentSenses = RemoteService.senseRemote.findBySynset(fromNode.getSynset(), lexicons);
        for(Sense parent : parentSenses){
            parentItem.addItem(parent.getWord().getWord());
        }
        childItem = new MComboBox();
        List<Sense> childSenses = RemoteService.senseRemote.findBySynset(toNode.getSynset(), lexicons);
        for(Sense child : childSenses){
            childItem.addItem(child.getWord().getWord());
        }

//        List<Sense> senses = RemoteUtils.synsetRemote
//                .dbFastGetUnits(((ViwnNodeSynset) from[0]).getSynset(), lexicons);
//        for (Sense parent : senses) {
//            parentItem.addItem(parent.getLemma().getWord());
//        }

        // middle element
        middleItem = new MComboBox();
        middleItem.setEnabled(false);

        // description of relation
        description = new MTextArea("");
        description.setRows(6);
        description.setEditable(false);

        // list of tests
        testsLit = new JList();

        // relation subtype
        relationSubType = new MComboBox();
        relationSubType.addKeyListener(this);
        relationSubType.setEnabled(false);

        // relation type
        relationType = new MComboBox();
        relationType.addKeyListener(this);

        // show relations

        mainRelations = RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION);
        for(RelationType relType : mainRelations){
            relationType.addItem(relType);
        }
//        mainRelations = new ArrayList<>();
//        List<RelationType> relationTypes = RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION);
//        for(RelationType relType : relationTypes) {
//            relationType.add(relType);
//        }
//        Collection<IRelationType> readRelations = LexicalDA.getHighestRelations(
//                type, pos);
//        for (IRelationType relType : readRelations) {
//            if (fixedRelationType == null
//                    || relType.getId().longValue() == fixedRelationType.getId()
//                    .longValue()
//                    || (fixedRelationType.getParent() != null && relType
//                    .getId().longValue() == fixedRelationType
//                    .getParent().getId())) {
//                relationType.addItem(RelationTypes.getFullNameFor(relType
//                        .getId()));
//                mainRelations.add(relType);
//            }
//        }

        // System.out.println("POS: "+PosManager.getInstance().getNormalized(pos).getName());
        // for(IRelationType rt : mainRelations){
        // System.out.println("ID: "+rt.getId()+"\t"+RelationTypes.getFullNameFor(rt.getId()));
        // }
        // event listeners
        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        middleItem.addKeyListener(this);
        middleItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);

        // buttons
        buttonChoose = MButton.buildSelectButton()
                .withKeyListener(this)
                .withActionListener(this);

        buttonCancel = MButton.buildCancelButton()
                .withKeyListener(this)
                .withActionListener(this);

        buttonSwitch = new MButton(this)
                .withIcon(FontAwesome.EXCHANGE)
                .withMnemonic(KeyEvent.VK_Z)
                .withKeyListener(this);

        relationSubType.addActionListener(this);
        relationType.addActionListener(this);

        // if there are any relations
        if (mainRelations.size() > 0) {
            relationType.setSelectedIndex(0);
            buttonChoose.setEnabled(true);
        } else {
            buttonChoose.setEnabled(false);
        }

        // build interfaceTEST_LABEL
        add("",
                new MLabel(Labels.RELATION_TYPE_COLON, 't', relationType));
        add("tab hfill", relationType);
        add("br", new MLabel(Labels.RELATION_SUBTYPE_COLON, 'y',
                relationType));
        add("tab hfill", relationSubType);
        add("br", new MLabel(Labels.RELATION_DESC_COLON, '\0',
                description));
        add("br hfill", new JScrollPane(description));

        jp = new WebPanel();
        jp.setLayout(new RiverLayout());
        jp.add("br", new MLabel(Labels.SOURCE_SYNSET_COLON, 'r', parentItem));
        jp.add("tab hfill", parentItem);
        jp.add("br", new MLabel(Labels.TARGET_SYNSET_COLON, 'd', childItem));
        jp.add("tab hfill", childItem);

        add("br hfill", jp);
        add("", buttonSwitch);

        add("br", new MLabel(Labels.TESTS_COLON, '\0', testsLit));
        add("br hfill vfill", new JScrollPane(testsLit));
        add("br center", buttonChoose);
        add("", buttonCancel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonChoose) {
            chosenType = getSelectedRelation();
            setVisible(false);

        } else if (event.getSource() == buttonCancel) {
            setVisible(false);

        } else if (event.getSource() == buttonSwitch) {
            // switch elements
            ViwnNode pom = from[0];
            from[0] = to[0];
            to[0] = pom;
            // switch combo boxes
            ComboBoxModel cbm = parentItem.getModel();
            parentItem.setModel(childItem.getModel());
            childItem.setModel(cbm);

            // refresh tests
            testsLit.setListData(new String[]{});
//            IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }

            // relation type changed
        } else if (event.getSource() == relationType) {
            relationSubType.removeAllItems();
            description.setText("");
            testsLit.setListData(new String[]{});

            // read chosen function index
            int index = relationType.getSelectedIndex();
//            for (IRelationType type : mainRelations) {
//                if (index-- == 0) {
//                    // refresh subrelation
//                    subRelations = new ArrayList<>();
//                    Collection<IRelationType> readRelations = LexicalDA
//                            .getChildren(type);
//                    for (IRelationType relType : readRelations) {
//                        if (fixedRelationType == null || fixedRelationType.getId().longValue() == relType.getId().longValue()) {
//                            relationSubType.addItem(RelationTypes.getFullNameFor(relType.getId()));
//                            subRelations.add(relType);
//                        }
//                    }
//                    if (subRelations.size() > 0) {
//                        relationSubType.setSelectedIndex(0);
//                    } else {
//                        loadTests(type);
//                    }
//                    description.setText(RelationTypes.get(type.getId())
//                            .getRelationType().getDescription().getText());
//                    break;
//                }
//            }
            relationSubType.setEnabled(subRelations != null
                    && subRelations.size() > 0);

            // subtype changed
        } else if (event.getSource() == relationSubType
                || event.getSource() == parentItem
                || event.getSource() == childItem
                || event.getSource() == middleItem) {
            testsLit.setListData(new String[]{});
//            IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    private static List<String> getTests(RelationType relation, String parent, String child, PartOfSpeech partOfSpeechA, PartOfSpeech partOfSpeechB) {
        List<String> result = new ArrayList<>();
        List<RelationTest> tests = RemoteService.relationTestRemote.findByRelationType(relation);
        String text;
        Collection<String> defOfUnitA = new ArrayList<>();
        Collection<String> defOfUnitB = new ArrayList<>();
        String[] parseResult;
        boolean found = true;
        int testIndex = 1;
        for(RelationTest test : tests) {
            if(test.getSenseApartOfSpeech().equals(partOfSpeechA) && test.getSenseBpartOfSpeech().equals(partOfSpeechB)) {
                text = test.getTest();
                while(found){
                    parseResult = extractUnitDefinition(text, "<x#", "<x" + defOfUnitA.size() + ">");
                    found = parseResult[0] != null;
                    if(found){
                        defOfUnitA.add(parseResult[0]);
                    }
                    text = parseResult[1];
                }
                found = true;
                while(found) {
                    parseResult = extractUnitDefinition(text, "<y#", "<y" + defOfUnitB.size() + ">");
                    found = parseResult[0] != null;
                    if(found){
                        defOfUnitB.add(parseResult[0]);
                    }
                    text = parseResult[1];
                }

                Collection<String> formsOfUnitA = getForms(defOfUnitA, parent);
                Collection<String> formsOfUnitB = getForms(defOfUnitB, child);

                int index = 0;
                for(String f : formsOfUnitA) {
                    text = text.replace("<x" + index + ">", "<font color=\"blue\">" + (f == null || "null".equals(f) || "null się".equals(f) ? parent : f) + "</font>");
                    index++;
                }
                index = 0;
                for(String f : formsOfUnitB) {
                    text = text.replace("<y" + index + ">", "<font color=\"blue\">" + (f == null || "null".equals(f) || "null się".equals(f) ? child : f) + "</font>");
                    index++;
                }
                result.add("<html>" + (testIndex++) + ". " + text + "</html>");
            }
        }
        return result;
    }

    private static Collection<String> getForms(Collection<String> defs, String unit) {
        Collection<String> forms = new ArrayList<>();
        // ustawieni suffixu
        String suffix = "";
        if (unit.endsWith("się")) { // konczy sie z się, a ma to zostać odcięte
            unit = unit.substring(0, unit.length() - 4);
            suffix = " się"; // ustawienie sufixu
        }
        for (String def : defs) {
            List<String> splits = Arrays.asList(def.split("\\|"));
            //forms.add(RemoteUtils.wordFormsRemote.getFormFor(new Word(unit), splits) + suffix);
        }
        return forms;
    }

    private static String[] extractUnitDefinition(String text, String code, String replace) {
        String[] result = new String[2];
        int pos = text.indexOf(code);
        if (pos != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(text.substring(0, pos));
            int endPos = text.indexOf(">", pos);

            result[0] = text.substring(pos + code.length(), endPos);

            sb.append(replace).append(text.substring(endPos + 1));
            result[1] = sb.toString();
        } else {
            result[0] = null;
            result[1] = text;
        }

        return result;
    }

    /**
     * @param workbench <code>Workbench</code> to get JFrame
     * @param from      <code>ViwnNode</code> parent for relation
     * @param to        <code>ViwnNode</code> child for relation
     * @return true when relation was added successfully
     */
    public static boolean showMakeSynsetRelationModal(Workbench workbench,
                                                      ViwnNode from, ViwnNode to) {
        boolean saveResult = false;
        ViwnNodeSynset nodeFrom = (ViwnNodeSynset) from;
        ViwnNodeSynset nodeTo = (ViwnNodeSynset) to;
        if(nodeFrom.getId().equals(nodeTo.getId())){ // nie można stworzyć relacji z samym sobą
            DialogBox.showInformation(Messages.FAILURE_SOURCE_SYNSET_SAME_AS_TARGET);
            return false;
        }
        //TODO przetestować to wszystko
        PartOfSpeech partOfSpeech = nodeFrom.getPos();
        ViwnNode[] nodeFromArray = {from};
        ViwnNode[] nodeToArray = {to};
        MakeNewRelationWindow relationWindow = new MakeNewRelationWindow(workbench.getFrame(), partOfSpeech, nodeFromArray, nodeToArray, workbench);
        relationWindow.setVisible(true);
        RelationType relationType = relationWindow.chosenType;

        if(relationType != null) {
            Synset parent = nodeFrom.getSynset();
            Synset child = nodeTo.getSynset();
            // sprawdzenie, czy taka relacja już istnieje w bazie danych
            if(RemoteService.synsetRelationRemote.checkRelationExists(parent, child, relationType)){
                DialogBox.showInformation(Messages.FAILURE_RELATION_EXISTS);
                return false;
            }
            // zapisanie relacji w bazie danych
            saveResult = RemoteService.synsetRelationRemote.makeRelation(parent, child, relationType);

            // TODO ogarnąć o co tutaj chodzi
            if(saveResult && relationType.isAutoReverse() && RemoteService.relationTypeRemote.findReverseByRelationType(relationType.getId()) != null){
                String parentText = (String) parentItem.getItemAt(parentItem.getSelectedIndex());
                String childText = (String) childItem.getItemAt(childItem.getSelectedIndex());
                List<String> tests = getTests(relationType, parentText, childText, nodeFrom.getPos(), nodeTo.getPos());
                String test = "\n\n";
                test = tests.stream().map((i) -> i + "\n").reduce(test, String::concat);
                boolean hasReversRelation = relationType.getReverse() != null;
                final String relationTypeName = LocalisationManager.getInstance().getLocalisedString(relationType.getName());
                if(hasReversRelation){
                    int reverseRelationAnswer = DialogBox.showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION + test), relationTypeName);
                    if(reverseRelationAnswer == DialogBox.YES){ //TODO sprawdzić, czy rzeczywiście o taką chodzi
                        RelationType reverseRelationType = RemoteService.relationTypeRemote.findReverseByRelationType(relationType.getId());
                        saveResult= RemoteService.synsetRelationRemote.makeRelation(child, parent, reverseRelationType);
                    }
                }
            }
            if(saveResult){
                DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
                //TODO w przypadku pomyślnego zapisania relacji w bazie danych, zaktualizować graf
            } else {
                DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
            }
        }
        return saveResult;
//        ViwnNode[] from1 = new ViwnNode[]{from};
//        ViwnNode[] to1 = new ViwnNode[]{to};
//        ViwnNodeSynset sf = (ViwnNodeSynset) from1[0];
//        ViwnNodeSynset st = (ViwnNodeSynset) to1[0];
//        // check if parent and child are different synsets
//        if (sf.getId().equals(st.getId())) {
//            DialogBox
//                    .showInformation(Messages.FAILURE_SOURCE_SYNSET_SAME_AS_TARGET);
//            return false;
//        }
//        MakeNewRelationFrame framew = null;
//        framew = new MakeNewRelationFrame(
//                workbench.getFrame(), RelationArgument.SYNSET,
//                RemoteUtils.synsetRemote.dbGetPos(((ViwnNodeSynset) from)
//                        .getSynset(), LexiconManager.getInstance().getLexicons()), from1, to1, workbench);
//        framew.setVisible(true);
//        if (framew.chosenType != null) {
//            sf = (ViwnNodeSynset) from1[0];
//            st = (ViwnNodeSynset) to1[0];
//
//            // check if such relation already exists
//            if (RelationsDA.checkIfRelationExists(sf.getSynset(),
//                    st.getSynset(), framew.chosenType)) {
//                DialogBox.showInformation(Messages.FAILURE_RELATION_EXISTS);
//            } // make relation
//            else if (RemoteUtils.synsetRelationRemote.dbMakeRelation(
//                    sf.getSynset(), st.getSynset(), framew.chosenType)) {
//                // make reverse relation
//                if (framew.chosenType.isAutoReverse()
//                        || (RelationsDA.getReverseRelation(framew.chosenType) != null)) {
//                    // Pobierz testy dla relacji odwrotnej
//                    Collection<String> tests = LexicalDA.getTests(RelationsDA
//                            .getReverseRelation(framew.chosenType),
//                            (String) childItem.getItemAt(childItem
//                                    .getSelectedIndex()), (String) parentItem
//                            .getItemAt(parentItem.getSelectedIndex()),
//                            pos);
//                    String test = "\n\n";
//                    test = tests.stream().map((i) -> i + "\n").reduce(test, String::concat);
//                    boolean hasReversRelation = framew.chosenType.getReverse() != null;
//
//                    if (framew.chosenType.isAutoReverse()
//                            || hasReversRelation && (DialogBox.showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION + test,
//                                    LexicalDA.getRelationName(RelationsDA.getReverseRelation(framew.chosenType)))) == DialogBox.YES)) {
//                        RemoteUtils.synsetRelationRemote.dbMakeRelation(st.getSynset(), sf.getSynset(), RelationsDA
//                                .getReverseRelation(framew.chosenType));
//                    }
//                }
//                DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
//                return true;
//            } else {
//                DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
//            }
//        }
//        return false;
    }

}
