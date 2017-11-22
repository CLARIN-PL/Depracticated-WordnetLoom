package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButtonPanel;
import pl.edu.pwr.wordnetloom.client.utils.Hints;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RelationTypeTestsPanel extends WebPanel {

    private final WebList tests;

    public RelationTypeTestsPanel() {

        setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Relation tests", TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(51, 51, 51)));

        setLayout(new BorderLayout());

        tests = new WebList();
        //tests.setCellRenderer(new TestListRenderer());

        MButtonPanel buttonsPanel = new MButtonPanel(moveUpButton, moveDownButton,
                addButton, addEditButton, removeButton)
                .withVerticalLayout()
                .withAllButtonsEnabled(true)
                .withMargin(10);

        add(new WebScrollPane(tests), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
    }


    private void moveTestUp() {
    }

    private void removeTest() {
    }

    private void addTest() {
    }

    private void addEditTest() {
    }

    private void moveTestDown() {
    }


    private final MButton moveUpButton = MButton.buildUpButton()
            .withToolTip(Hints.MOVE_TEST_UP)
            .withDefaultIconSize()
            .withActionListener(e -> moveTestUp());

    private final MButton moveDownButton = MButton.buildDownButton()
            .withToolTip(Hints.MOVE_TEST_DOWN)
            .withDefaultIconSize()
            .withActionListener(e -> moveTestDown());

    private final MButton addButton = MButton.buildAddButton()
            .withToolTip(Hints.CREATE_NEW_TEST)
            .withDefaultIconSize()
            .withActionListener(e -> addTest());

    private final MButton addEditButton = new MButton()
            .withToolTip(Hints.EDIT_SELECTED_TEST)
            .withIcon(FontAwesome.PLUS_SQUARE)
            .withDefaultIconSize()
            .withActionListener(e -> addEditTest());

    private final MButton removeButton = MButton.buildDeleteButton()
            .withToolTip(Hints.REMOVE_SELECTED_TEST)
            .withDefaultIconSize()
            .withActionListener(e -> removeTest());


}
