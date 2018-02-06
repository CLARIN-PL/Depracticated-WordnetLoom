package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tree.WebTree;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events.ShowRelationTypeEvent;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTypeNode;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
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
import java.util.List;
import java.util.Optional;

/**
 * Window for choosing revers relation
 */
public class ReverseRelationWindow extends DialogWindow implements ActionListener, TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    private final RelationTypeNode root = new RelationTypeNode("Relations");

    private final MButton buttonChoose, buttonCancel, buttonNoReverse;
    private RelationType lastReverse = null;
    private RelationType selectedRelation = null;
    private final JTree tree;
    private final JCheckBox autoReverse;

    private ReverseRelationWindow(WebFrame owner) {
        super(owner, Labels.REVERSE_RELATION, 400, 450);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tree = new WebTree(root);
        tree.setToggleClickCount(2);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(this);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        tree.setCellRenderer(renderer);
        tree.setCellRenderer(renderer);

        buttonChoose = MButton.buildSelectButton()
                .withEnabled(false)
                .withActionListener(this);

        buttonNoReverse = new MButton()
                .withActionListener(this)
                .withCaption(Labels.WITHOUT_REVERSE)
                .withMnemonic(KeyEvent.VK_B);

        buttonCancel = MButton.buildCancelButton().withActionListener(this);

        autoReverse = new JCheckBox(Labels.AUTO_ADD_REVERSE);
        autoReverse.setSelected(false);

        add("", new MLabel(Labels.RELATION_TYPES_COLON, 't', tree));
        add("br hfill vfill", new JScrollPane(tree));
        add("br left", autoReverse);
        add("br center", buttonChoose);
        add("", buttonNoReverse);
        add("", buttonCancel);
    }

    private void buildTree(final List<RelationType> list) {
        root.removeAllChildren();

        list.forEach(e -> {
            final RelationTypeNode parent = new RelationTypeNode(e);
            List<RelationType> children = RelationTypeManager.getInstance().getChildren(e.getId());
            children.forEach(child -> {
                RelationTypeNode childNode = new RelationTypeNode(child);
                parent.add(childNode);
            });
            root.add(parent);
        });
    }

    static public Pair<RelationType, Boolean> showModal(WebFrame owner, RelationType lastReverse, Boolean autoReverse, final List<RelationType> list) {
        ReverseRelationWindow frame = new ReverseRelationWindow(owner);
        frame.lastReverse = lastReverse;
        frame.autoReverse.setSelected(autoReverse);
        frame.buildTree(list);
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
            setVisible(false);

        } else if (event.getSource() == buttonNoReverse) {
            lastReverse = null;
            setVisible(false);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        getSelectedRelationType()
                .ifPresent(relation -> {
                    lastReverse = relation;
                    buttonChoose.setEnabled(true);
                });
    }

    private Optional<RelationType> getSelectedRelationType() {
        if (tree.getLastSelectedPathComponent() != null) {
            return Optional.of(tree.getLastSelectedPathComponent())
                    .filter(RelationTypeNode.class::isInstance)
                    .map(RelationTypeNode.class::cast)
                    .filter(node -> node.getUserObject() instanceof RelationType)
                    .map(relation -> (RelationType) relation.getUserObject());

        }
        return Optional.empty();
    }

}
