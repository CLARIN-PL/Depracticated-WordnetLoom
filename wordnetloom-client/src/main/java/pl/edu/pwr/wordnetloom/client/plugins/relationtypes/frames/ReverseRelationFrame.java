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
package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.RelationTypesIM;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.da.RelationTypesDA;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTreeModel;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;

/**
 * okienko do wybierania relacji odwrotnej
 *
 * @author Max
 */
public class ReverseRelationFrame extends IconDialog implements ActionListener, TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    private ButtonExt buttonChoose, buttonCancel, buttonNoReverse;
    private RelationType lastReverse = null;
    private RelationType selectedRelation = null;
    private JTree tree;
    private JCheckBox autoReverse;

    /**
     * konstruktor
     *
     * @param owner - srodowisko
     */
    private ReverseRelationFrame(JFrame owner) {
        super(owner, Labels.REVERSE_RELATION, 400, 450);
        this.setResizable(false);
        //this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // drzewo relacji
        tree = new JTree();
        tree.setExpandsSelectedPaths(true);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(this);

        // ustawienie ikonek dla drzewa
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(RelationTypesIM.getOpenImage());
        renderer.setClosedIcon(RelationTypesIM.getClosedImage());
        renderer.setLeafIcon(RelationTypesIM.getLeafImage());
        tree.setCellRenderer(renderer);

        buttonChoose = new ButtonExt(Labels.SELECT, this, KeyEvent.VK_W);
        buttonChoose.setEnabled(false);
        buttonNoReverse = new ButtonExt(Labels.WITHOUT_REVERSE, this, KeyEvent.VK_B);
        buttonCancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);

        // automatyczna odwrotna
        autoReverse = new JCheckBox(Labels.AUTO_ADD_REVERSE);
        autoReverse.setSelected(false);

        // dodanie do zawartości okna
        this.add("", new LabelExt(Labels.RELATION_TYPES_COLON, 't', tree));
        this.add("br hfill vfill", new JScrollPane(tree));
        this.add("br left", autoReverse);
        this.add("br center", buttonChoose);
        this.add("", buttonNoReverse);
        this.add("", buttonCancel);
    }

    /**
     * odswiezenie danych w drzewie
     *
     */
    private void refreshTree() {
        Collection<RelationType> relations = RelationTypesDA.getHighestRelations(null, null);
        for (RelationType type : relations) {
            RelationTypesDA.getChildren(type);
            RelationTypesDA.getTests(type);
        }
        tree.setModel(new RelationTreeModel(relations));
        int count = tree.getRowCount();
        for (int i = 0; i < count; i++) {
            tree.expandRow(i);
        }
        tree.clearSelection();
    }

    /**
     * wyświetlenie okienka
     *
     * @param owner - srodowisko
     * @param lastReverse - aktualna relacja odwrotna
     * @param autoReverse - czy relacja odwrotna ma byc automatycznie tworzona
     * @return nowo wybrana relacja odwrotna
     */
    static public Pair<RelationType, Boolean> showModal(JFrame owner, RelationType lastReverse, Boolean autoReverse) {
        ReverseRelationFrame frame = new ReverseRelationFrame(owner);
        frame.lastReverse = lastReverse;
        frame.autoReverse.setSelected(autoReverse.booleanValue());
        frame.refreshTree();
        frame.setVisible(true);
        Pair<RelationType, Boolean> result = new Pair<RelationType, Boolean>(frame.lastReverse, new Boolean(frame.autoReverse.isSelected()));
        frame.dispose();
        return result;
    }

    /**
     * wciśnięto przycisk
     */
    public void actionPerformed(ActionEvent arg0) {
        // wcisnieto anuluj
        if (arg0.getSource() == buttonCancel) {
            setVisible(false);

            // wcisnieto wybierz
        } else if (arg0.getSource() == buttonChoose) {
            lastReverse = selectedRelation;
            setVisible(false);

            // wcisnieto bez relacji
        } else if (arg0.getSource() == buttonNoReverse) {
            lastReverse = null;
            setVisible(false);
        }
    }

    /**
     * zmienilo sie zaznaczenie w drzewie
     */
    public void valueChanged(TreeSelectionEvent arg0) {
        RelationType rel = null;
        if (arg0 != null && arg0.getNewLeadSelectionPath() != null) {
            Object lastElem = arg0.getNewLeadSelectionPath().getLastPathComponent();
            if (lastElem != null && lastElem instanceof RelationType) {
                rel = (RelationType) lastElem;
            }
        }
        buttonChoose.setEnabled(rel != null && RemoteUtils.relationTypeRemote.dbGetChildren(rel, LexiconManager.getInstance().getLexicons()).size() == 0);
        selectedRelation = rel;
    }

}
