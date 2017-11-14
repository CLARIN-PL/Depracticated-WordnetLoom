package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.RelationTypesIM;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 * okienko do wybierania relacji odwrotnej
 *
 * @author Max
 */
public class ReverseRelationWindow extends DialogWindow implements ActionListener, TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    private final MButton buttonChoose, buttonCancel, buttonNoReverse;
    private RelationType lastReverse = null;
    private RelationType selectedRelation = null;
    private final JTree tree;
    private final JCheckBox autoReverse;

    /**
     * konstruktor
     *
     * @param owner - srodowisko
     */
    private ReverseRelationWindow(WebFrame owner) {
        super(owner, Labels.REVERSE_RELATION, 400, 450);
        setResizable(false);
        //this.setAlwaysOnTop(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        buttonChoose = MButton.buildSelectButton()
                .withEnabled(false)
                .withActionListener(this);

        buttonNoReverse = new MButton()
                .withActionListener(this)
                .withCaption(Labels.WITHOUT_REVERSE)
                .withMnemonic(KeyEvent.VK_B);

        buttonCancel = MButton.buildCancelButton().withActionListener(this);

        // automatyczna odwrotna
        autoReverse = new JCheckBox(Labels.AUTO_ADD_REVERSE);
        autoReverse.setSelected(false);

        // dodanie do zawartości okna
        add("", new MLabel(Labels.RELATION_TYPES_COLON, 't', tree));
        add("br hfill vfill", new JScrollPane(tree));
        add("br left", autoReverse);
        add("br center", buttonChoose);
        add("", buttonNoReverse);
        add("", buttonCancel);
    }

    /**
     * odswiezenie danych w drzewie
     */
    private void refreshTree() {
        Collection<RelationType> relations = new ArrayList<>(); //RelationTypesDA.getHighestRelations(null, null);
        for (RelationType type : relations) {
//            RelationTypesDA.getChildren(type);
//            RelationTypesDA.getTests(type);
        }
        // tree.setModel(new RelationTreeModel(relations));
        int count = tree.getRowCount();
        for (int i = 0; i < count; i++) {
            tree.expandRow(i);
        }
        tree.clearSelection();
    }

    /**
     * wyświetlenie okienka
     *
     * @param owner       - srodowisko
     * @param lastReverse - aktualna relacja odwrotna
     * @param autoReverse - czy relacja odwrotna ma byc automatycznie tworzona
     * @return nowo wybrana relacja odwrotna
     */
    static public Pair<RelationType, Boolean> showModal(WebFrame owner, RelationType lastReverse, Boolean autoReverse) {
        ReverseRelationWindow frame = new ReverseRelationWindow(owner);
        frame.lastReverse = lastReverse;
        frame.autoReverse.setSelected(autoReverse);
        frame.refreshTree();
        frame.setVisible(true);
        Pair<RelationType, Boolean> result = new Pair<>(frame.lastReverse, frame.autoReverse.isSelected());
        frame.dispose();
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonCancel) {
            setVisible(false);

        } else if (event.getSource() == buttonChoose) {
            lastReverse = selectedRelation;
            setVisible(false);

        } else if (event.getSource() == buttonNoReverse) {
            lastReverse = null;
            setVisible(false);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        RelationType rel = null;
        if (arg0 != null && arg0.getNewLeadSelectionPath() != null) {
            Object lastElem = arg0.getNewLeadSelectionPath().getLastPathComponent();
            if (lastElem != null) {
                rel = (RelationType) lastElem;
            }
        }
        // buttonChoose.setEnabled(rel != null && RemoteUtils.relationTypeRemote.dbGetChildren(rel, LexiconManager.getInstance().getLexicons()).isEmpty());
        selectedRelation = rel;
    }

}
