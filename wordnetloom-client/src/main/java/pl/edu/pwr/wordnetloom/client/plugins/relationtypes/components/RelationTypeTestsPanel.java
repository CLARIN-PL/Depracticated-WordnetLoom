package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.google.common.eventbus.Subscribe;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events.ShowRelationTestsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window.TestEditorWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButtonPanel;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class RelationTypeTestsPanel extends WebPanel {

    private final WebList tests;
    private final DefaultListModel<RelationTest> model = new DefaultListModel<>();
    private final MButton moveUpButton = MButton.buildUpButton()
            .withToolTip(Hints.MOVE_TEST_UP)
            .withDefaultIconSize()
            .withActionListener(e -> moveTestUp());
    private final MButton moveDownButton = MButton.buildDownButton()
            .withToolTip(Hints.MOVE_TEST_DOWN)
            .withDefaultIconSize()
            .withActionListener(e -> moveTestDown());
    private final MButton editButton = new MButton()
            .withToolTip(Hints.EDIT_SELECTED_TEST)
            .withIcon(FontAwesome.PENCIL)
            .withDefaultIconSize()
            .withActionListener(e -> editTest());
    private final MButton removeButton = MButton.buildDeleteButton()
            .withToolTip(Hints.REMOVE_SELECTED_TEST)
            .withDefaultIconSize()
            .withActionListener(e -> removeTest());
    private RelationType relationType;
    private final MButton addButton = MButton.buildAddButton()
            .withToolTip(Hints.CREATE_NEW_TEST)
            .withDefaultIconSize()
            .withActionListener(e -> addTest());

    public RelationTypeTestsPanel() {

        Application.eventBus.register(this);

        setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Relation tests", TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(51, 51, 51)));

        setLayout(new BorderLayout());

        tests = new WebList(model);
        tests.addListSelectionListener(l -> {
            if (tests.isSelectionEmpty()) {
                adjustButtonSelection(false);
            } else {
                adjustButtonSelection(true);
            }
        });

        MButtonPanel buttonsPanel = new MButtonPanel(moveUpButton, moveDownButton,
                addButton, editButton, removeButton)
                .withVerticalLayout()
                .withAllButtonsEnabled(true)
                .withMargin(10);

        add(new WebScrollPane(tests), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
    }

    public void setRelationTests(List<RelationTest> tests) {
        model.clear();
        tests.forEach(model::addElement);
    }

    @Subscribe
    public void onShowRelationTests(ShowRelationTestsEvent event) {
        relationType = event.getRelationType();
        setRelationTests(relationType.getRelationTests());
    }

    private RelationTest getSelected() {
        return model.elementAt(tests.getSelectedIndex());
    }

    private void moveTestUp() {
    }

    private void moveTestDown() {
    }

    private void removeTest() {
        RelationTest selected = getSelected();
        model.removeElement(selected);
        RemoteService.relationTestRemote.delete(selected);
    }

    private void addTest() {
        RelationTest nt = new RelationTest(relationType);
        nt.setPosition(model.getSize() + 1);

        RelationTest rt = TestEditorWindow.showModal(null, nt);
        rt = RemoteService.relationTestRemote.save(rt);
        model.addElement(rt);
    }

    private void editTest() {
        RelationTest rt = TestEditorWindow.showModal(null, getSelected());
        RemoteService.relationTestRemote.save(rt);
    }

    private void adjustButtonSelection(boolean activate) {
        moveDownButton.setEnabled(activate);
        moveUpButton.setEnabled(activate);
        editButton.setEnabled(activate);
        removeButton.setEnabled(activate);
    }

}
