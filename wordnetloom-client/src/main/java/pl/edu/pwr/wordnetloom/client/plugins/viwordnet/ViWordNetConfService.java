package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.RelationDisplayConfWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/*
 * this is just a stub for gui relations configuration
 */
public class ViWordNetConfService extends AbstractService
        implements ActionListener {

    public ViWordNetConfService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installMenuItems() {
        JMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        other.add(new MenuItemExt(Labels.RELATIONS_CONFIGURATION, KeyEvent.VK_K, this));
    }

    @Override
    public void installViews() {
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RelationDisplayConfWindow.showModal(workbench);
    }
}
