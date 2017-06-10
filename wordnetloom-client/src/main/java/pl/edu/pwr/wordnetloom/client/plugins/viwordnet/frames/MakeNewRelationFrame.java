/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.

    See the LICENSE and COPYING files for more details.
 */
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.frames;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.plugins.relations.da.RelationsDA;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextAreaPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationArgument;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import se.datadosen.component.RiverLayout;

/**
 * @author amusial
 *
 */
public class MakeNewRelationFrame extends RelationTypeFrame {

    private static final long serialVersionUID = 5479457915334417348L;

    protected ButtonExt buttonSwitch;
    protected JPanel jp;
    protected List<Long> lexicons;
    protected ViwnNode from[], to[];

    protected MakeNewRelationFrame(JFrame frame, RelationArgument type,
            PartOfSpeech pos, ViwnNode[] from, ViwnNode[] to, Workbench workbench) {
        super(frame, type, pos, null);

        this.from = from;
        this.to = to;

        lexicons = LexiconManager.getInstance().getLexicons();

        // relation from:
        parentItem = new ComboBoxPlain();
        List<Sense> senses = RemoteUtils.synsetRemote
                .dbFastGetUnits(((ViwnNodeSynset) from[0]).getSynset(), lexicons);
        for (Sense parent : senses) {
            parentItem.addItem(parent.getLemma().getWord());
        }

        // relation to:
        childItem = new ComboBoxPlain();
        senses = RemoteUtils.synsetRemote
                .dbFastGetUnits(((ViwnNodeSynset) to[0]).getSynset(), lexicons);
        for (Sense child : senses) {
            childItem.addItem(child.getLemma().getWord());
        }

        // middle element
        middleItem = new ComboBoxPlain();
        middleItem.setEnabled(false);

        // description of relation
        description = new TextAreaPlain("");
        description.setRows(6);
        description.setEditable(false);

        // list of tests
        testsLit = new JList();

        // relation subtype
        relationSubType = new ComboBoxPlain();
        relationSubType.addKeyListener(this);
        relationSubType.setEnabled(false);

        // relation type
        relationType = new ComboBoxPlain();
        relationType.addKeyListener(this);

        // show relations
        mainRelations = new ArrayList<>();
        Collection<RelationType> readRelations = LexicalDA.getHighestRelations(
                type, pos);
        for (RelationType relType : readRelations) {
            if (fixedRelationType == null
                    || relType.getId().longValue() == fixedRelationType.getId()
                    .longValue()
                    || (fixedRelationType.getParent() != null && relType
                    .getId().longValue() == fixedRelationType
                    .getParent().getId())) {
                relationType.addItem(RelationTypes.getFullNameFor(relType
                        .getId()));
                mainRelations.add(relType);
            }
        }

        // System.out.println("POS: "+PosManager.getInstance().getNormalized(pos).getName());
        // for(RelationType rt : mainRelations){
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
        buttonChoose = new ButtonExt(Labels.SELECT, this, KeyEvent.VK_W);
        buttonChoose.addKeyListener(this);
        buttonCancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);
        buttonCancel.addKeyListener(this);
        buttonSwitch = new ButtonExt(Labels.SWITCH, this, KeyEvent.VK_Z);
        buttonSwitch.addKeyListener(this);

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
        this.add("",
                new LabelExt(Labels.RELATION_TYPE_COLON, 't', relationType));
        this.add("tab hfill", relationType);
        this.add("br", new LabelExt(Labels.RELATION_SUBTYPE_COLON, 'y',
                relationType));
        this.add("tab hfill", relationSubType);
        this.add("br", new LabelExt(Labels.RELATION_DESC_COLON, '\0',
                description));
        this.add("br hfill", new JScrollPane(description));

        jp = new JPanel();
        jp.setLayout(new RiverLayout());
        jp.add("br", new LabelExt(Labels.SOURCE_SYNSET_COLON, 'r', parentItem));
        jp.add("tab hfill", parentItem);
        jp.add("br", new LabelExt(Labels.TARGET_SYNSET_COLON, 'd', childItem));
        jp.add("tab hfill", childItem);

        this.add("br hfill", jp);
        this.add("", buttonSwitch);

        this.add("br", new LabelExt(Labels.TESTS_COLON, '\0', testsLit));
        this.add("br hfill vfill", new JScrollPane(testsLit));
        this.add("br center", this.buttonChoose);
        this.add("", this.buttonCancel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonChoose) {
            chosenType = getSelectedRelation();
            this.setVisible(false);

        } else if (event.getSource() == buttonCancel) {
            this.setVisible(false);

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
            RelationType relation = getSelectedRelation();
            if (relation != null) {
                loadTests(relation);
            }

            // relation type changed
        } else if (event.getSource() == relationType) {
            relationSubType.removeAllItems();
            description.setText("");
            testsLit.setListData(new String[]{});

            // read chosen function index
            int index = relationType.getSelectedIndex();
            for (RelationType type : mainRelations) {
                if (index-- == 0) {
                    // refresh subrelation
                    subRelations = new ArrayList<>();
                    Collection<RelationType> readRelations = LexicalDA
                            .getChildren(type);
                    for (RelationType relType : readRelations) {
                        if (fixedRelationType == null || fixedRelationType.getId().longValue() == relType.getId().longValue()) {
                            relationSubType.addItem(RelationTypes.getFullNameFor(relType.getId()));
                            subRelations.add(relType);
                        }
                    }
                    if (subRelations.size() > 0) {
                        relationSubType.setSelectedIndex(0);
                    } else {
                        loadTests(type);
                    }
                    description.setText(RelationTypes.get(type.getId())
                            .getRelationType().getDescription().getText());
                    break;
                }
            }
            relationSubType.setEnabled(subRelations != null
                    && subRelations.size() > 0);

            // subtype changed
        } else if (event.getSource() == relationSubType
                || event.getSource() == parentItem
                || event.getSource() == childItem
                || event.getSource() == middleItem) {
            testsLit.setListData(new String[]{});
            RelationType relation = getSelectedRelation();
            if (relation != null) {
                loadTests(relation);
            }
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
     * @param from <code>ViwnNode</code> parent for relation
     * @param to <code>ViwnNode</code> child for relation
     * @return true when relation was added successfully
     */
    public static boolean showMakeSynsetRelationModal(Workbench workbench,
            ViwnNode from, ViwnNode to) {
        ViwnNode[] from1 = new ViwnNode[]{from};
        ViwnNode[] to1 = new ViwnNode[]{to};
        ViwnNodeSynset sf = (ViwnNodeSynset) from1[0], st = (ViwnNodeSynset) to1[0];
        // check if parent and child are different synsets
        if (sf.getId().equals(st.getId())) {
            DialogBox
                    .showInformation(Messages.FAILURE_SOURCE_SYNSET_SAME_AS_TARGET);
            return false;
        }
        MakeNewRelationFrame framew = null;
        framew = new MakeNewRelationFrame(
                workbench.getFrame(), RelationArgument.SYNSET,
                RemoteUtils.synsetRemote.dbGetPos(((ViwnNodeSynset) from)
                        .getSynset(), LexiconManager.getInstance().getLexicons()), from1, to1, workbench);
        framew.setVisible(true);
        if (framew.chosenType != null) {
            sf = (ViwnNodeSynset) from1[0];
            st = (ViwnNodeSynset) to1[0];

            // check if such relation already exists
            if (RelationsDA.checkIfRelationExists(sf.getSynset(),
                    st.getSynset(), framew.chosenType)) {
                DialogBox.showInformation(Messages.FAILURE_RELATION_EXISTS);
            } // make relation
            else if (RemoteUtils.synsetRelationRemote.dbMakeRelation(
                    sf.getSynset(), st.getSynset(), framew.chosenType)) {
                // make reverse relation
                if (framew.chosenType.isAutoReverse()
                        || (RelationsDA.getReverseRelation(framew.chosenType) != null)) {
                    // Pobierz testy dla relacji odwrotnej
                    Collection<String> tests = LexicalDA.getTests(RelationsDA
                            .getReverseRelation(framew.chosenType),
                            (String) childItem.getItemAt(childItem
                                    .getSelectedIndex()), (String) parentItem
                            .getItemAt(parentItem.getSelectedIndex()),
                            pos);
                    String test = "\n\n";
                    test = tests.stream().map((i) -> i + "\n").reduce(test, String::concat);
                    boolean hasReversRelation = framew.chosenType.getReverse() != null;

                    if (framew.chosenType.isAutoReverse()
                            || hasReversRelation && (DialogBox.showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION + test,
                                    LexicalDA.getRelationName(RelationsDA.getReverseRelation(framew.chosenType)))) == DialogBox.YES)) {
                        RemoteUtils.synsetRelationRemote.dbMakeRelation(st.getSynset(), sf.getSynset(), RelationsDA
                                .getReverseRelation(framew.chosenType));
                    }
                }
                DialogBox.showInformation(Messages.SUCCESS_RELATION_ADDED);
                return true;
            } else {
                DialogBox.showInformation(Messages.FAILURE_UNABLE_TO_ADD_RELATION);
            }
        }
        return false;
    }

}
