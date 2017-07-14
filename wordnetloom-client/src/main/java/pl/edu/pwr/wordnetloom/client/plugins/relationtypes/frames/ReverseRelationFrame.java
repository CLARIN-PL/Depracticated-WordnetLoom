package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

/**
 * okienko do wybierania relacji odwrotnej
 *
 * @author Max
 */
public class ReverseRelationFrame extends IconDialog implements ActionListener, TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    private final ButtonExt buttonChoose, buttonCancel, buttonNoReverse;
    private SynsetRelationType lastReverse = null;
    private SynsetRelationType selectedRelation = null;
    private final JTree tree;
    private final JCheckBox autoReverse;

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
        Collection<SynsetRelationType> relations = new ArrayList<>(); //RelationTypesDA.getHighestRelations(null, null);
        for (SynsetRelationType type : relations) {
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
     * @param owner - srodowisko
     * @param lastReverse - aktualna relacja odwrotna
     * @param autoReverse - czy relacja odwrotna ma byc automatycznie tworzona
     * @return nowo wybrana relacja odwrotna
     */
    static public Pair<SynsetRelationType, Boolean> showModal(JFrame owner, SynsetRelationType lastReverse, Boolean autoReverse) {
        ReverseRelationFrame frame = new ReverseRelationFrame(owner);
        frame.lastReverse = lastReverse;
        frame.autoReverse.setSelected(autoReverse);
        frame.refreshTree();
        frame.setVisible(true);
        Pair<SynsetRelationType, Boolean> result = new Pair<>(frame.lastReverse, frame.autoReverse.isSelected());
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
        SynsetRelationType rel = null;
        if (arg0 != null && arg0.getNewLeadSelectionPath() != null) {
            Object lastElem = arg0.getNewLeadSelectionPath().getLastPathComponent();
            if (lastElem != null && lastElem instanceof SynsetRelationType) {
                rel = (SynsetRelationType) lastElem;
            }
        }
        // buttonChoose.setEnabled(rel != null && RemoteUtils.relationTypeRemote.dbGetChildren(rel, LexiconManager.getInstance().getLexicons()).isEmpty());
        selectedRelation = rel;
    }

}
