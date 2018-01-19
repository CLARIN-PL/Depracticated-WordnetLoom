package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MakeNewLexicalRelationWindow extends RelationTypeFrame {

    private static final long serialVersionUID = 5479457915334417348L;

    protected MButton buttonSwitch;
    protected JPanel jp;
    protected Sense from, to;

    protected MakeNewLexicalRelationWindow(WebFrame frame, String type,
                                           PartOfSpeech pos, Sense[] from, Sense[] to) {
        super(frame, type, pos, null);

        this.from = from[0];
        this.to = to[0];

        // relation from:
        parentItem = new MComboBox();
        parentItem.addItem(this.from.getWord());

        // relation to:
        childItem = new MComboBox();
        childItem.addItem(this.to.getWord());

        // middle element
        middleItem = new MComboBox();
        middleItem.setEnabled(false);

        // description of relation
        description = new MTextArea("");
        description.setRows(6);
        description.setEditable(false);

        // list of tests
        testsList = new JList();

        // relation subtype
        relationSubType = new MComboBox();
        relationSubType.addKeyListener(this);
        relationSubType.setEnabled(false);

        // relation type
        relationType = new MComboBox();
        relationType.addKeyListener(this);

        // show relations
//        mainRelations = new ArrayList<>();
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
        // event listeners
        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        middleItem.addKeyListener(this);
        middleItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);

        // buttons
        buttonChoose = MButton.buildSelectButton(this).withKeyListener(this);

        buttonCancel = MButton.buildCancelButton().withKeyListener(this);

        buttonSwitch = new MButton(this)
                .withCaption(Labels.SWITCH)
                .withMnemonic(KeyEvent.VK_Z)
                .withKeyListener(this);

        relationSubType.addActionListener(this);
        relationType.addActionListener(this);

        // if there are any relations
//        if (mainRelations.size() > 0) {
//            relationType.setSelectedIndex(0);
//            buttonChoose.setEnabled(true);
//        } else {
//            buttonChoose.setEnabled(false);
//        }
        // build interface
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
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonChoose) {
            //      chosenType = getSelectedRelation();
            setVisible(false);

        } else if (event.getSource() == buttonCancel) {
            setVisible(false);

        } else if (event.getSource() == buttonSwitch) {
            // switch elements
            Sense pom = from;
            from = to;
            to = pom;
            // switch combo boxes
            ComboBoxModel cbm = parentItem.getModel();
            parentItem.setModel(childItem.getModel());
            childItem.setModel(cbm);

            // refresh tests
            testsList.setListData(new String[]{});
            //      IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }

            // relation type changed
        } else if (event.getSource() == relationType) {
            relationSubType.removeAllItems();
            description.setText("");
            testsList.setListData(new String[]{});

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
//            relationSubType.setEnabled(subRelations != null
//                    && subRelations.size() > 0);
//
//            // subtype changed
//        } else if (event.getSource() == relationSubType
//                || event.getSource() == parentItem
//                || event.getSource() == childItem
//                || event.getSource() == middleItem) {
//            testsList.setListData(new String[]{});
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

    /**
     * @param workbench <code>Workbench</code> to get JFrame
     * @param from      <code>ViwnNode</code> parent for relation
     * @param to        <code>ViwnNode</code> child for relation
     * @return true when relation was added successfully
     */
    public static boolean showMakeLexicalRelationModal(Workbench workbench,
                                                       Sense from, Sense to) {
        Sense[] from1 = new Sense[]{from};
        Sense[] to1 = new Sense[]{to};
        Sense sf = (Sense) from1[0];
        Sense st = (Sense) to1[0];
        // check if parent and child are different lexical units
        if (sf.getId().equals(st.getId())) {
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

}
