package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.tree.WebTree;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButtonPanel;
import pl.edu.pwr.wordnetloom.client.utils.Hints;

import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class RelationTreePanel extends WebPanel {

    private WebTree tree;

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

    private final MButton removeButton = MButton.buildRemoveButton()
            .withToolTip(Hints.REMOVE_SELECTED_REL_AND_SUBRELATION)
            .withActionListener(e -> removeRelation());

    public RelationTreePanel() {
        init();
    }

    private void init() {

        setLayout(new BorderLayout());


        tree = new WebTree();
        tree.setToggleClickCount(2);
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);

        WebPanel listPanelWrapper = new WebPanel();
        listPanelWrapper.setMargin(5, 10, 10, 0);
        listPanelWrapper.add(tree);

        MButtonPanel buttonPanel = new MButtonPanel(moveUpButton, moveDownButton,
                addButton, addSubRelationButton, removeButton)
                .withVeritcalLayout()
                .withAllButtonsEnabled(true)
                .withMargin(10);

        add(listPanelWrapper, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

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
