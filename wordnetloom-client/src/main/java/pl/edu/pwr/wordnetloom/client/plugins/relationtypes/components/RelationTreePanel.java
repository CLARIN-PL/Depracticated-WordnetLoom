package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.WebTree;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events.ShowRelationTypeEvent;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTypeNode;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButtonPanel;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class RelationTreePanel extends WebPanel implements TreeSelectionListener {

    private WebTree tree;

    private final RelationArgument relationArgument;

    private final RelationTypeNode root = new RelationTypeNode("Relations");

    private final MButton moveUpButton = MButton.buildUpButton()
            .withToolTip(Hints.MOVE_RELATION_UP)
            .withActionListener(e -> moveRelationUp());


    private final MButton moveDownButton = MButton.buildDownButton()
            .withToolTip(Hints.MOVE_RELATION_DOWN)
            .withActionListener(e -> moveRelationDown());

    private final MButton addButton = MButton.buildAddButton()
            .withToolTip(Hints.CREATE_RELATION_TYPE)
            .withActionListener(e -> addRelation());

    private final MButton addSubRelationButton = new MButton()
            .withToolTip(Hints.CREATE_NEW_RELATION_SUBTYPE)
            .withIcon(FontAwesome.PLUS_SQUARE)
            .withActionListener(e -> addSubRelation());

    private final MButton removeButton = MButton.buildDeleteButton()
            .withToolTip(Hints.REMOVE_SELECTED_REL_AND_SUBRELATION)
            .withActionListener(e -> removeRelation());

    public RelationTreePanel(RelationArgument argument) {
        relationArgument = argument;
        init();
    }

    private void init() {

        setLayout(new BorderLayout());

        tree = new WebTree(root);
        tree.setToggleClickCount(2);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(this);

        WebScrollPane treeScrollWrapper = new WebScrollPane(tree);

        WebPanel wrapper = new WebPanel(treeScrollWrapper);
        wrapper.setMargin(5, 10, 10, 0);

        MButtonPanel buttonPanel = new MButtonPanel(moveUpButton, moveDownButton,
                addButton, addSubRelationButton, removeButton)
                .withVerticalLayout()
                .withAllButtonsEnabled(true)
                .withMargin(10);

        add(wrapper, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

    }

    public void setRelationsTypes(final List<RelationType> list) {
        buildTree(list);
        tree.expandAll();
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

    private void moveRelationDown() {

        getSelectedNode().ifPresent(node -> {

            RelationTypeNode parent = (RelationTypeNode) node.getParent();

            if (parent.getIndex(node) < parent.getIndex(parent.getLastChild())) {
                moveWithChildren(node, (RelationTypeNode) parent.getChildAfter(node), parent, parent.getIndex(node));
            }
        });

    }

    private void moveRelationUp() {
        getSelectedNode().ifPresent(node -> {

            RelationTypeNode parent = (RelationTypeNode) node.getParent();

            if (parent.getIndex(node) > parent.getIndex(parent.getFirstChild())) {
                moveWithChildren(node, (RelationTypeNode) node.getPreviousNode(), parent, parent.getIndex(node));
            }
        });
    }

    private void moveWithChildren(RelationTypeNode node, RelationTypeNode siblingNode, RelationTypeNode parent, int index) {

        final List<RelationTypeNode> children = new ArrayList<>();

        if (siblingNode.getChildCount() > 0) {

            Enumeration en = siblingNode.breadthFirstEnumeration();

            while (en.hasMoreElements()) {

                RelationTypeNode item = (RelationTypeNode) en.nextElement();
                children.add(item);
            }

            siblingNode.removeAllChildren();
        }

        parent.insert(siblingNode, index);

        children.forEach(i -> siblingNode.add(i));

        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload(parent);

        tree.setSelectedNode(node);
    }


    private void addRelation() {
    }

    private void addSubRelation() {
    }

    private void removeRelation() {
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        getSelectedRelationType()
                .ifPresent(relation -> Application.eventBus.post(new ShowRelationTypeEvent(relation, relationArgument)));
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

    private Optional<RelationTypeNode> getSelectedNode() {

        return Optional.of(tree.getLastSelectedPathComponent())
                .filter(RelationTypeNode.class::isInstance)
                .map(RelationTypeNode.class::cast);
    }
}
