package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MakeNewLexicalRelationWindow extends RelationTypeFrame {

    private static final long serialVersionUID = 5479457915334417348L;

    protected Sense from, to;
    private SenseRelation senseRelation;

    public MakeNewLexicalRelationWindow(WebFrame frame,
                                           PartOfSpeech pos, Sense senseFrom, Sense senseTo) {
        super(frame, RelationArgument.SENSE_RELATION, pos, null);

        from = RemoteService.senseRemote.fetchSense(senseFrom.getId());
        to = RemoteService.senseRemote.fetchSense(senseTo.getId());

//        buildUI();
        init(RelationArgument.SENSE_RELATION);
        super.initView();
        addListeners();
        loadRelations(from, to);

        parentItem.addItem(from);
        childItem.addItem(to);

        if(mainRelations.size() > 0){
            relationType.setSelectedIndex(0);
            buttonChoose.setEnabled(true);
        } else {
            buttonChoose.setEnabled(false);
        }
    }

    private void loadRelations(Sense from, Sense to) {
        if(from.getLexicon().equals(to.getLexicon())){
            loadParentRelation(relationsType);
        } else {
            loadParentRelation(relationsType, MULTILINGUAL_RELATIONS);
        }
    }


    private void createUIComponents() {
        parentItem = new MComboBox();
        parentItem.addItem(from);

        childItem = new MComboBox();
        childItem.addItem(to);

        // description of relation
        description = new MTextArea("");
        description.setRows(6);
        description.setEditable(false);

        buttonChoose = MButton.buildSelectButton(this).withKeyListener(this);

        buttonCancel = MButton.buildCancelButton().withKeyListener(this);

        buttonSwitch = new MButton(this)
                .withCaption(Labels.SWITCH)
                .withMnemonic(KeyEvent.VK_Z)
                .withKeyListener(this);
    }

    private void addListeners() {
        // event listeners
        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonChoose) {
            senseRelation = getSenseRelation();
            setVisible(false);
        } else if (event.getSource() == buttonCancel) {
            setVisible(false);
        } else if (event.getSource() == buttonSwitch) {
            switchSenses();
        } else {
            super.actionPerformed(event);
        }
    }

    private SenseRelation getSenseRelation()
    {
        chosenType = getSelectedRelation();
        return new SenseRelation(chosenType, from, to);
    }

    private void switchSenses() {
        Sense temp = from;
        from = to;
        to = temp;

        swapParentAndChildrenModels();
    }

    @Override
    protected void swapParentAndChild() {
        Sense temp = from;
        from = to;
        to = temp;

        swapParentAndChildrenModels();
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    private static RelationType chooseRelationType(Workbench workbench, PartOfSpeech partOfSpeech, Sense senseFrom, Sense senseTo){
        MakeNewLexicalRelationWindow relationWindow = new MakeNewLexicalRelationWindow(workbench.getFrame(), partOfSpeech, senseFrom, senseTo);
        relationWindow.setVisible(true);
        return relationWindow.chosenType;
    }

    /**
     * @param workbench <code>Workbench</code> to get JFrame
     * @param senseFrom      <code>ViwnNode</code> parent for relation
     * @param senseTo        <code>ViwnNode</code> child for relation
     * @return true when relation was added successfully
     */
    public static boolean showModalAndSaveRelation(Workbench workbench,
                                                   Sense senseFrom, Sense senseTo) {
        // check if parent and child are different lexical units
        if (senseFrom.getId().equals(senseTo.getId())) {
            DialogBox.showInformation(Messages.FAILURE_SOURCE_UNIT_SAME_AS_TARGET);
            return false;
        }


//        MakeNewLexicalRelationFrame framew = new MakeNewLexicalRelationFrame(
//                workbench.getFrame(), RelationArgument.LEXICAL,
//                from1[0].getPartOfSpeech(), from1, to1);
//        framew.setVisible(true);
//        if (framew.chosenType != null) {
//
//            // check if relation exists
//            if (!RemoteUtils.lexicalRelationRemote.dbRelationExists(from, to,
//                    framew.chosenType)) {
//                RemoteUtils.lexicalRelationRemote.dbMakeRelation(from, to,
//                        framew.chosenType);
//
//                // if reverse relation exists
//                if (framew.chosenType.getReverse() != null) {
//                    IRelationType reverse = framew.chosenType.getReverse();
//                    // add reversed relation
//                    if (!RemoteUtils.lexicalRelationRemote.dbRelationExists(to,
//                            from, reverse)
//                            && (framew.chosenType.isAutoReverse() || DialogBox
//                            .showYesNo(String
//                                    .format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION,
//                                            LexicalDA
//                                            .getRelationName(reverse))) == DialogBox.YES)) {
//                        RemoteUtils.lexicalRelationRemote.dbMakeRelation(to,
//                                from, reverse);
//                    }
//                }
//
//                // show confirmation dialog
//                DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
//                return true;
//
//            } else {
//                // show error dialog
//                DialogBox.showError(Messages.FAILURE_RELATION_EXISTS);
//            }
//        }
        return false;
    }

    public Sense getParentSense(){
        return from;
    }

    public Sense getChildSense(){
        return to;
    }

    public RelationType getChosenType(){
        return chosenType;
    }

    public SenseRelation getChoosenRelation(){
        return senseRelation;
    }
}
