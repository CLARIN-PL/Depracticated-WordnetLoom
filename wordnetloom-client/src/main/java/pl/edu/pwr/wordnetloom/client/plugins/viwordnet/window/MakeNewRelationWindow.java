package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.utils.RelationTypeFormat;
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
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MakeNewRelationWindow extends RelationTypeFrame {

    private static final long serialVersionUID = 5479457915334417348L;

    protected MButton buttonSwitch;
    protected WebPanel jp;
    protected List<Long> lexicons;
    protected ViwnNode from[], to[];

    protected MakeNewRelationWindow(WebFrame frame, PartOfSpeech pos, ViwnNode[] from, ViwnNode[] to) {
        super(frame, RelationArgument.SYNSET_RELATION, pos, null);

        this.from = from;
        this.to = to;

        lexicons = LexiconManager.getInstance().getUserChosenLexiconsIds();

        init(RelationArgument.SYNSET_RELATION);
        super.initView();


        ViwnNodeSynset fromNode = (ViwnNodeSynset)from[0];
        ViwnNodeSynset toNode = (ViwnNodeSynset)to[0];
        loadRelations(fromNode, toNode);

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

    private void loadRelations(ViwnNodeSynset fromNode, ViwnNodeSynset toNode){
        if(!fromNode.getSynset().getLexicon().equals(toNode.getSynset().getLexicon())){
            loadParentRelation(relationsType, MULTILINGUAL_RELATIONS);
        } else {
            loadParentRelation(relationsType);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonChoose) {
            choose();
        } else if (event.getSource() == buttonCancel) {
            setVisible(false);
        } else if (event.getSource() == buttonSwitch) {
            switchSynset();
        }  else {
            super.actionPerformed(event);
        }
    }

    private void choose(){
        chosenType = getSelectedRelation();
        setVisible(false);
    }

    private void switchSynset(){
        ViwnNode pom = from[0];
        from[0] = to[0];
        to[0] = pom;

        swapParentAndChildrenModels();
    }

    @Override
    protected void swapParentAndChild() {
        ViwnNode temp = from[0];
        from[0] = to[0];
        to[0] = temp;

        swapParentAndChildrenModels();
    }

    /**
     * @param workbench <code>Workbench</code> to get JFrame
     * @param from      <code>ViwnNode</code> parent for relation
     * @param to        <code>ViwnNode</code> child for relation
     * @return true when relation was added successfully
     */
    public static boolean showModalAndSaveRelation(Workbench workbench,
                                                   ViwnNode from, ViwnNode to) {
        boolean saveResult = false;
        ViwnNodeSynset nodeFrom = (ViwnNodeSynset) from;
        ViwnNodeSynset nodeTo = (ViwnNodeSynset) to;
        if(checkIsRelationWithHimself(nodeFrom, nodeTo)){
            DialogBox.showInformation(Messages.FAILURE_SOURCE_SYNSET_SAME_AS_TARGET);
            return false;
        }

        PartOfSpeech partOfSpeech = nodeFrom.getPos();
        ViwnNode[] nodeFromArray = {from};
        ViwnNode[] nodeToArray = {to};

        RelationType relationType = chooseRelationType(workbench, partOfSpeech, nodeFromArray, nodeToArray);

        if(relationType != null) { // if choose relation type
            Synset parent = nodeFrom.getSynset();
            Synset child = nodeTo.getSynset();
            if(checkRelationExist(relationType, parent, child)){
                DialogBox.showInformation(Messages.FAILURE_RELATION_EXISTS);
                return false;
            }
            saveResult = saveRelationInDatabase(parent, child, relationType);
            if(saveResult){
                saveResult = saveReverseRelation(saveResult, relationType, parent, child);
            }
            showRelationSavingResult(saveResult, nodeFrom, nodeTo);
        }
        return saveResult;
    }

    private static void showRelationSavingResult(boolean saveResult, ViwnNodeSynset nodeFrom, ViwnNodeSynset nodeTo) {
        if(saveResult){
            DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
            nodeFrom.refresh();
            nodeTo.refresh();
            //TODO w przypadku pomyślnego zapisania relacji w bazie danych, zaktualizować graf
        } else {
            DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
        }
    }

    private static boolean saveReverseRelation(boolean saveResult, RelationType relationType, Synset parent, Synset child) {
        RelationType reverseRelationType = RemoteService.relationTypeRemote.findReverseByRelationType(relationType.getId());
        if(relationType.isAutoReverse() ) {
            saveResult = RemoteService.synsetRelationRemote.makeRelation(child, parent, reverseRelationType); // tworzenie relacji odwrotnej
        } else if(reverseRelationType != null){
            String reverseRelationTypeName = RelationTypeFormat.getText(reverseRelationType);
            if(DialogBox.showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION, reverseRelationTypeName)) == DialogBox.YES) {
                saveResult = RemoteService.synsetRelationRemote.makeRelation(child, parent, reverseRelationType);
            }
        }
        return saveResult;
    }

    private static boolean checkRelationExist(RelationType relationType, Synset parent, Synset child) {
        return RemoteService.synsetRelationRemote.checkRelationExists(parent, child, relationType);
    }

    private static RelationType chooseRelationType(Workbench workbench, PartOfSpeech partOfSpeech, ViwnNode[] nodeFromArray, ViwnNode[] nodeToArray) {
        MakeNewRelationWindow relationWindow = new MakeNewRelationWindow(workbench.getFrame(), partOfSpeech, nodeFromArray, nodeToArray);
        relationWindow.setVisible(true);
        return relationWindow.chosenType;
    }

    private static boolean checkIsRelationWithHimself(ViwnNodeSynset nodeFrom, ViwnNodeSynset nodeTo) {
        return nodeFrom.getId().equals(nodeTo.getId());
    }

    private static boolean saveRelationInDatabase(Synset parent, Synset child, RelationType relationType)
    {
        boolean saveResult = RemoteService.synsetRelationRemote.makeRelation(parent, child, relationType);
        if(!saveResult){
            //TODO wyświetlić komunikat o niepowodzeniu zapisu
            return false;
        }
        return true;
    }

}
