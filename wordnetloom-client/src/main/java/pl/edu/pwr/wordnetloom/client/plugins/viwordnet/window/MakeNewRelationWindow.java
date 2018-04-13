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
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
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
        initView();

        ViwnNodeSynset fromNode = (ViwnNodeSynset)from[0];
        ViwnNodeSynset toNode = (ViwnNodeSynset)to[0];
        loadRelations(RelationArgument.SYNSET_RELATION, fromNode, toNode);

        parentItem.addItem(fromNode.getSynset().getSenses().get(0));
        childItem.addItem(toNode.getSynset().getSenses().get(0));

        // if there are any relations
        if (mainRelations.size() > 0) {
            relationType.setSelectedIndex(0);
            buttonChoose.setEnabled(true);
        } else {
            buttonChoose.setEnabled(false);
        }
    }

    private void loadRelations(RelationArgument type, ViwnNodeSynset fromNode, ViwnNodeSynset toNode){
        if(fromNode.getSynset().getLexicon() != toNode.getSynset().getLexicon()){
            loadParentRelation(type, MULTILINGUAL_RELATIONS);
        } else {
            loadParentRelation(type);
        }
    }

    @Override
    protected void initView() {
        buttonSwitch = new MButton(this)
                .withIcon(FontAwesome.EXCHANGE)
                .withMnemonic(KeyEvent.VK_Z)
                .withKeyListener(this);
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
            choose();
        } else if (event.getSource() == buttonCancel) {
            setVisible(false);
        } else if (event.getSource() == buttonSwitch) {
            switchSynset();
        } else if (event.getSource() == relationSubType || event.getSource() == middleItem) {
            testsList.setListData(new String[]{});
//            IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }
        } else {
            super.actionPerformed(event);
        }
    }

    private void choose(){
        chosenType = getSelectedRelation();
        setVisible(false);
    }

    private void switchSynset(){
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

        PartOfSpeech partOfSpeech = nodeFrom.getPos();
        ViwnNode[] nodeFromArray = {from};
        ViwnNode[] nodeToArray = {to};

        MakeNewRelationWindow relationWindow = new MakeNewRelationWindow(workbench.getFrame(), partOfSpeech, nodeFromArray, nodeToArray, workbench);
        relationWindow.setVisible(true);
        RelationType relationType = relationWindow.chosenType;

        if(relationType != null) { // sprawdzanie, czy anulowano okno
            Synset parent = nodeFrom.getSynset();
            Synset child = nodeTo.getSynset();
            // sprawdzenie, czy taka relacja już istnieje w bazie danych
            if(RemoteService.synsetRelationRemote.checkRelationExists(parent, child, relationType)){
                DialogBox.showInformation(Messages.FAILURE_RELATION_EXISTS);
                return false;
            }
            // zapisanie relacji w bazie danych
            saveResult = RemoteService.synsetRelationRemote.makeRelation(parent, child, relationType);
            if(!saveResult){
                //TODO wyświetlić komunikat o niepowodzeniu zapisu
                return false;
            }
            if(saveResult){
                // znalezienie relacji odwrotnej
                RelationType reverseRelationType = RemoteService.relationTypeRemote.findReverseByRelationType(relationType.getId());
                if(relationType.isAutoReverse() ) {
                    saveResult = RemoteService.synsetRelationRemote.makeRelation(child, parent, reverseRelationType); // tworzenie relacji odwrotnej
                } else if(reverseRelationType != null){
                    String reverseRelationTypeName = LocalisationManager.getInstance().getLocalisedString(reverseRelationType.getName());
                    if(DialogBox.showYesNo(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION + " " + reverseRelationTypeName) == DialogBox.YES) {
                        saveResult = RemoteService.synsetRelationRemote.makeRelation(child, parent, reverseRelationType);
                    }
                }
            }
            if(saveResult){
                DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
                nodeFrom.refresh();
                nodeTo.refresh();
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
