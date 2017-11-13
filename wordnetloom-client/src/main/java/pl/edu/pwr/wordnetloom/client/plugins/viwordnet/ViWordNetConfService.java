package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import com.alee.laf.menu.WebMenu;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.RelationDisplayConfWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
        WebMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        other.add(new MMenuItem(Labels.RELATIONS_CONFIGURATION)
                .withMnemonic(KeyEvent.VK_K)
                .withActionListener(this));
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
