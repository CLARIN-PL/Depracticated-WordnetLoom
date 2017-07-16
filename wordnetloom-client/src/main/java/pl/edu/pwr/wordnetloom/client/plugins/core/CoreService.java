package pl.edu.pwr.wordnetloom.client.plugins.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import pl.edu.pwr.wordnetloom.client.plugins.core.window.AboutWindow;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * klasa dostarczająca podsawową usługę - menu
 *
 */
public class CoreService extends AbstractService implements MenuListener {

    private static final String SHOW_TOOLTIPS_PARAM = "ShowTooltips";
    private JMenu settings;
    private JCheckBoxMenuItem showTooltips;

    public CoreService(Workbench workbench) {
        super(workbench);
        //Sets default Dialog window to this frame
        DialogBox.setParentWindow(workbench.getFrame());
    }

    @Override
    public void installMenuItems() {
        final Workbench w = super.workbench;

        JMenu file = new JMenu(Labels.FILE);
        file.setMnemonic(KeyEvent.VK_P);

        file.add(new MenuItemExt(Labels.EXIT, KeyEvent.VK_W, (ActionEvent arg0) -> {
            System.exit(0);
        }));

        JMenu help = new JMenu(Labels.HELP);
        help.setMnemonic(KeyEvent.VK_C);

        help.add(new MenuItemExt(Labels.ABOUT_APP, KeyEvent.VK_O, (ActionEvent e) -> {
            AboutWindow.showModal(w);
        }));

        // wyswietlanie tooltipow
        showTooltips = new JCheckBoxMenuItem(Labels.SHOW_TOOLTIPS);
        showTooltips.setMnemonic(KeyEvent.VK_D);

        showTooltips.addActionListener((ActionEvent e) -> {

            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            w.setParam(SHOW_TOOLTIPS_PARAM, item.isSelected() ? "1" : "0");
            ToolTipGenerator.getGenerator().setEnabledTooltips(item.isSelected());

        });

        settings = new JMenu(Labels.SETTINGS);
        settings.setMnemonic(KeyEvent.VK_S);

        // standardowe ustawienia okna
        settings.add(new MenuItemExt(Labels.DEFAULT_SETTINGS, KeyEvent.VK_S, (ActionEvent e) -> {
            w.getActivePerspective().resetViews();
        }));

        settings.addMenuListener(this);
        settings.add(showTooltips);

        workbench.installMenu(file);
        workbench.installMenu(settings);
        workbench.installMenu(help);
    }

    @Override
    public void menuSelected(MenuEvent arg0) {
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void installViews() {
    }

    @Override
    public void menuDeselected(MenuEvent arg0) {
    }

    @Override
    public void menuCanceled(MenuEvent arg0) {
    }
}
