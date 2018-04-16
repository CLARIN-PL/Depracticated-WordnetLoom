package pl.edu.pwr.wordnetloom.client.plugins.core;

import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenu;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.core.window.AboutWindow;
import pl.edu.pwr.wordnetloom.client.plugins.login.window.ChangePasswordWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.user.model.Role;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class is handling basic menu interactions
 */
public class CoreService extends AbstractService implements MenuListener {

    private WebMenu settings;
    private WebMenu user;
    private WebCheckBoxMenuItem showTooltips;
    public static final String APP_SETTINGS ="Application settings";
    public static final String ACC_SETTINGS ="Account settings";

    public CoreService(Workbench workbench) {
        super(workbench);
        Application.eventBus.register(this);
        DialogBox.setParentWindow(workbench.getFrame());
    }

    @Override
    public void installMenuItems() {

        final Workbench w = super.workbench;

        WebMenu help = new WebMenu(Labels.ABOUT_APP);
        help.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                AboutWindow.showModal(w);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Icon helpIcon = IconFontSwing.buildIcon(FontAwesome.INFO_CIRCLE, 12);
        help.setIcon(helpIcon);

        showTooltips = new WebCheckBoxMenuItem(Labels.SHOW_TOOLTIPS);
        showTooltips.setMnemonic(KeyEvent.VK_D);

        showTooltips.addChangeListener(e -> {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            RemoteConnectionProvider.getInstance().getUser().getSettings().setShowToolTips(item.isSelected());
            RemoteService.userServiceRemote.update(RemoteConnectionProvider.getInstance().getUser().getSettings());
        });

        settings = new WebMenu(Labels.SETTINGS);
        settings.setMnemonic(KeyEvent.VK_S);
        Icon settingsIcon = IconFontSwing.buildIcon(FontAwesome.COGS, 12);
        settings.setIcon(settingsIcon);

        user = new WebMenu(RemoteConnectionProvider.getInstance().getUser().getFullname());
        Icon userIcon = IconFontSwing.buildIcon(FontAwesome.USER, 12);
        user.setIcon(userIcon);

        WebMenu account = new WebMenu(ACC_SETTINGS);
        Icon accIcon = IconFontSwing.buildIcon(FontAwesome.ADDRESS_CARD, 12);
        account.setIcon(accIcon);

        account.add(new MMenuItem("Change password")
                .withMnemonic(KeyEvent.VK_P)
                .withIcon(FontAwesome.EXCHANGE)
                .withActionListener(e ->
                        ChangePasswordWindow.showModal(workbench)
                ));

        WebMenu appSettings = new WebMenu(APP_SETTINGS);
        Icon appIcon = IconFontSwing.buildIcon(FontAwesome.COG, 12);
        appSettings.setIcon(appIcon);

        appSettings.add(new MMenuItem(Labels.DEFAULT_SETTINGS)
                .withMnemonic(KeyEvent.VK_S).withActionListener(e -> w.getActivePerspective().resetViews()));
        appSettings.addMenuListener(this);
        appSettings.add(showTooltips);

        user.add(appSettings);
        user.add(account);
        user.addSeparator();
        user.add(new MMenuItem("Sign out")
                .withMnemonic(KeyEvent.VK_P)
                .withIcon(FontAwesome.SIGN_OUT)
                .withActionListener(e ->
                        System.exit(0)
                ));

        workbench.installMenu(help, "left");
        if(RemoteConnectionProvider
                .getInstance()
                .getUser().getRole().equals(Role.ADMIN)) {
            workbench.installMenu(settings, "left");
        }
        workbench.installMenu(user, "right");

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
