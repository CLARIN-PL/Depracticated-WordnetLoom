package pl.edu.pwr.wordnetloom.client.plugins.core;

import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenu;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.core.window.AboutWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * klasa dostarczająca podsawową usługę - menu
 */
public class CoreService extends AbstractService implements MenuListener {

    private WebMenu settings;
    private WebCheckBoxMenuItem showTooltips;

    public CoreService(Workbench workbench) {
        super(workbench);
        DialogBox.setParentWindow(workbench.getFrame());
    }

    @Override
    public void installMenuItems() {

        final Workbench w = super.workbench;

        WebMenu file = new WebMenu(Labels.FILE);
        file.setMnemonic(KeyEvent.VK_P);

        file.add(new MMenuItem(Labels.EXIT)
                .withIcon(FontAwesome.SIGN_OUT)
                .withMnemonic(KeyEvent.VK_W)
                .withActionListener(e -> System.exit(0)));

        WebMenu help = new WebMenu(Labels.HELP);
        help.setMnemonic(KeyEvent.VK_C);

        help.add(new MMenuItem(Labels.ABOUT_APP)
                .withMnemonic(KeyEvent.VK_O)
                .withActionListener(e -> AboutWindow.showModal(w)));

        // wyswietlanie tooltipow
        showTooltips = new WebCheckBoxMenuItem(Labels.SHOW_TOOLTIPS);
        showTooltips.setMnemonic(KeyEvent.VK_D);

        showTooltips.addChangeListener( e -> {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            RemoteConnectionProvider.getInstance().getUser().getSettings().setShowToolTips(item.isSelected());
            RemoteService.userServiceRemote.update(RemoteConnectionProvider.getInstance().getUser().getSettings());
        });

        settings = new WebMenu(Labels.SETTINGS);
        settings.setMnemonic(KeyEvent.VK_S);

        // standardowe ustawienia okna
        settings.add(new MMenuItem(Labels.DEFAULT_SETTINGS)
                .withMnemonic(KeyEvent.VK_S).withActionListener(e -> w.getActivePerspective().resetViews()));

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
