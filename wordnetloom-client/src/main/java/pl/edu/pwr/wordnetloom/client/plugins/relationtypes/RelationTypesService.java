package pl.edu.pwr.wordnetloom.client.plugins.relationtypes;

import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window.RelationsEditorWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;

public class RelationTypesService extends AbstractService implements ActionListener {

    public RelationTypesService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installMenuItems() {
        JMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        other.add(new MenuItemExt(Labels.EDIT_RELATION_TYPES, KeyEvent.VK_Y, this));
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void installViews() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            RelationsEditorWindow.showModal(workbench);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
