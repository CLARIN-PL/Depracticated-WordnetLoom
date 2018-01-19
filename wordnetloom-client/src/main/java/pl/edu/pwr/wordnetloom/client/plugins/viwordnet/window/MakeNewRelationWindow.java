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
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
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

        init(RelationArgument.SYNSET_RELATION);

        ViwnNodeSynset fromNode = (ViwnNodeSynset)from[0];
        ViwnNodeSynset toNode = (ViwnNodeSynset)to[0];
        //TODO zobaczyć, czy nie można przekazać coś innego i w jaki sposób później będzie zapisywana relacja
        parentItem.addItem(fromNode.getSynset().getSenses().get(0));
        childItem.addItem(toNode.getSynset().getSenses().get(0));


//
////        List<Sense> senses = RemoteUtils.synsetRemote
////                .dbFastGetUnits(((ViwnNodeSynset) from[0]).getSynset(), lexicons);
////        for (Sense parent : senses) {
////            parentItem.addItem(parent.getLemma().getWord());
////        }
//
//        // middle element
//        middleItem = new MComboBox();
//        middleItem.setEnabled(false);
//
//        // description of relation
//        description = new MTextArea("");
//        description.setRows(6);
//        description.setEditable(false);
//
//        // list of tests
//        testsList = new JList();
//
//        // relation subtype
//        relationSubType = new MComboBox();
//        relationSubType.addKeyListener(this);
//        relationSubType.setEnabled(false);
//
//        // relation type
//        relationType = new MComboBox();
//        relationType.addKeyListener(this);
//
//        // show relations
//
//        mainRelations = RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION);
//        for(RelationType relType : mainRelations){
//            relationType.addItem(relType);
//        }
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
//        middleItem = new MComboBox();
//        parentItem.addKeyListener(this);
//        parentItem.addActionListener(this);
//        middleItem.addKeyListener(this);
//        middleItem.addActionListener(this);
//        childItem.addKeyListener(this);
//        childItem.addActionListener(this);

        // buttons
//        buttonChoose = MButton.buildSelectButton()
//                .withKeyListener(this)
//                .withActionListener(this);
//
//        buttonCancel = MButton.buildCancelButton()
//                .withKeyListener(this)
//                .withActionListener(this);

        buttonSwitch = new MButton(this)
                .withIcon(FontAwesome.EXCHANGE)
                .withMnemonic(KeyEvent.VK_Z)
                .withKeyListener(this);


        // if there are any relations
        if (mainRelations.size() > 0) {
            relationType.setSelectedIndex(0);
            buttonChoose.setEnabled(true);
        } else {
            buttonChoose.setEnabled(false);
        }

        initView();
        // build interfaceTEST_LABEL
//        add("",
//                new MLabel(Labels.RELATION_TYPE_COLON, 't', relationType));
//        add("tab hfill", relationType);
//        add("br", new MLabel(Labels.RELATION_SUBTYPE_COLON, 'y',
//                relationType));
//        add("tab hfill", relationSubType);
//        add("br", new MLabel(Labels.RELATION_DESC_COLON, '\0',
//                description));
//        add("br hfill", new JScrollPane(description));
//
//        jp = new WebPanel();
//        jp.setLayout(new RiverLayout());
//        jp.add("br", new MLabel(Labels.SOURCE_SYNSET_COLON, 'r', parentItem));
//        jp.add("tab hfill", parentItem);
//        jp.add("br", new MLabel(Labels.TARGET_SYNSET_COLON, 'd', childItem));
//        jp.add("tab hfill", childItem);
//
//        add("br hfill", jp);
//        add("", buttonSwitch);
//
//        add("br", new MLabel(Labels.TESTS_COLON, '\0', testsList));
//        add("br hfill vfill", new JScrollPane(testsList));
//        add("br center", buttonChoose);
//        add("", buttonCancel);
    }

    @Override
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

        jp = new WebPanel();
        jp.setLayout(new RiverLayout());
        jp.add("br", new MLabel(Labels.SOURCE_SYNSET_COLON, 'r', parentItem));
        jp.add("tab hfill", parentItem);
        jp.add("br", new MLabel(Labels.TARGET_SYNSET_COLON, 'd', childItem));
        jp.add("tab hfill", childItem);

        add("br hfill", jp);
        add("", buttonSwitch);

        add("br", new MLabel(Labels.TESTS_COLON, '\0', testsList));
        add("br hfill vfill", new JScrollPane(testsList));
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
            testsList.setListData(new String[]{});
//            IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }

            // relation type changed
//        } else if (event.getSource() == relationType) {
//            relationSubType.removeAllItems();
//            description.setText("");
//            testsList.setListData(new String[]{});
//            // read chosen function index
//            int index = relationType.getSelectedIndex();
////            for (IRelationType type : mainRelations) {
////                if (index-- == 0) {
////                    // refresh subrelation
////                    subRelations = new ArrayList<>();
////                    Collection<IRelationType> readRelations = LexicalDA
////                            .getChildren(type);
////                    for (IRelationType relType : readRelations) {
////                        if (fixedRelationType == null || fixedRelationType.getId().longValue() == relType.getId().longValue()) {
////                            relationSubType.addItem(RelationTypes.getFullNameFor(relType.getId()));
////                            subRelations.add(relType);
////                        }
////                    }
////                    if (subRelations.size() > 0) {
////                        relationSubType.setSelectedIndex(0);
////                    } else {
////                        loadTests(type);
////                    }
////                    description.setText(RelationTypes.get(type.getId())
////                            .getRelationType().getDescription().getText());
////                    break;
////                }
////            }
//            relationSubType.setEnabled(subRelations != null
//                    && subRelations.size() > 0);
//
//            // subtype changed
        } else if (event.getSource() == relationSubType
//                || event.getSource() == parentItem
//                || event.getSource() == childItem
                || event.getSource() == middleItem) {
            testsList.setListData(new String[]{});
//            IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }
        } else {
            super.actionPerformed(event);
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
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
