package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.WebTree;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models.RelationTreeModel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButtonPanel;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.List;

public class RelationTreePanel extends WebPanel {

    private WebTree tree;

    private final RelationTreeModel model = new RelationTreeModel();

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

    public RelationTreePanel() {
        init();
    }

    private void init() {

        setLayout(new BorderLayout());

        tree = new WebTree(model);
        tree.setToggleClickCount(2);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);

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
        model.setRelationTypes(list);
    }

    private void moveRelationDown() {
    }

    private void moveRelationUp() {
    }

    private void addRelation() {
    }

    private void addSubRelation() {
    }

    private void removeRelation() {
    }
}
