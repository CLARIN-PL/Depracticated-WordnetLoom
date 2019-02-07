package pl.edu.pwr.wordnetloom.client.plugins.relationtypes;

import com.alee.laf.menu.WebMenu;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window.RelationsEditorWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class RelationTypesService extends AbstractService implements ActionListener, Loggable {

    public RelationTypesService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installMenuItems() {
        WebMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        other.add(new MMenuItem(Labels.EDIT_RELATION_TYPES)
                .withIcon(FontAwesome.PENCIL_SQUARE)
                .withMnemonic(KeyEvent.VK_Y)
                .withActionListener(this));
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

        RelationsEditorWindow.showModal(workbench.getFrame());

    }
}
