package pl.edu.pwr.wordnetloom.client.plugins.relationtypes;

import com.alee.laf.menu.WebMenu;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window.RelationsEditorWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;

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
        try {
            RelationsEditorWindow.showModal(workbench);
        } catch (ParseException e) {
            logger().error("Unable to parse", e);
        }
    }
}
