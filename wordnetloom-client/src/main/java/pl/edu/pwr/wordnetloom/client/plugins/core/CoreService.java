package pl.edu.pwr.wordnetloom.client.plugins.core;

import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor.DictionaryEditorWindow;
import pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor.LabelEditorWindow;
import pl.edu.pwr.wordnetloom.client.plugins.core.window.AboutWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.ChangePasswordWindow;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.user.model.Role;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;

/**
 * Class is handling basic menu interactions
 */
public class CoreService extends AbstractService implements MenuListener {

    public static final String APP_SETTINGS ="Application settings";
    public static final String ACC_SETTINGS ="Account settings";
    private final int FONT_SIZE = 12;
    private final String LEFT_ALIGNMENT = "left";
    private final String RIGTH_ALIGNMENT = "right";

    public CoreService(Workbench workbench) {
        super(workbench);
        Application.eventBus.register(this);
        DialogBox.setParentWindow(workbench.getFrame());
    }

    @Override
    public void installMenuItems() {

        final Workbench w = super.workbench;

        WebMenu help = createHelpMenu(w);
        WebMenu settings = createMenu(Labels.SETTINGS, FontAwesome.COGS, KeyEvent.VK_S);
        WebMenu user = createMenu(UserSessionContext.getInstance().getFullName(), FontAwesome.USER);
        WebMenu account = createAccountMenu();
        if(UserSessionContext.getInstance().hasRole(Role.ADMIN)){
            WebMenu appSettings = createAppSettingMenu(w);
            user.add(appSettings);
        }
        if(!UserSessionContext.getInstance().hasRole(Role.ANONYMOUS)){
            user.add(account);
        }
        user.addSeparator();
        user.add(createSignOutMenuItem());

        workbench.installMenu(help, LEFT_ALIGNMENT);
        if(UserSessionContext.getInstance().hasRole(Role.ADMIN)) {
            workbench.installMenu(settings, LEFT_ALIGNMENT);
        }
        workbench.installMenu(user, RIGTH_ALIGNMENT);
    }

    private WebMenu createHelpMenu(Workbench w) {
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
        return help;
    }

    private WebCheckBoxMenuItem createShowTooltipsMenuItem() {
        WebCheckBoxMenuItem showTooltips = new WebCheckBoxMenuItem(Labels.SHOW_TOOLTIPS);
        showTooltips.setMnemonic(KeyEvent.VK_D);

        showTooltips.addChangeListener(e -> {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            UserSessionContext.getInstance().getUserSettings().setShowToolTips(item.isSelected());
            RemoteService.userServiceRemote.update(UserSessionContext.getInstance().getUserSettings());
        });

        return showTooltips;
    }

    private MMenuItem createSignOutMenuItem() {
        // TODO dorobić etykietę
        return new MMenuItem("Sign out")
                .withMnemonic(KeyEvent.VK_P)
                .withIcon(FontAwesome.SIGN_OUT)
                .withActionListener(e ->
                        System.exit(0)
                );
    }

    private WebMenu createMenu(String name, FontAwesome font, Integer mnemonic){
        WebMenu menu = new WebMenu(name);
        Icon menuIcon = IconFontSwing.buildIcon(font, FONT_SIZE);
        menu.setIcon(menuIcon);
        if(mnemonic != null){
            menu.setMnemonic(mnemonic);
        }
        return menu;
    }

    private WebMenu createMenu(String name, FontAwesome font){
        return createMenu(name, font, null);
    }

    private WebMenu createAccountMenu() {
        WebMenu account = createAccountMenu(ACC_SETTINGS, FontAwesome.ADDRESS_CARD);

        // TODO dorobić etykietę
        account.add(new MMenuItem("Change password")
                .withMnemonic(KeyEvent.VK_P)
                .withIcon(FontAwesome.EXCHANGE)
                .withActionListener(e ->
                        ChangePasswordWindow.showModal(workbench)
                ));
        return account;
    }

    private WebMenu createAccountMenu(String accSettings, FontAwesome addressCard) {
        WebMenu account = new WebMenu(accSettings);
        Icon accIcon = IconFontSwing.buildIcon(addressCard, 12);
        account.setIcon(accIcon);
        return account;
    }

    private WebMenu createAppSettingMenu(Workbench w) {
        WebMenu appSettings = createMenu(APP_SETTINGS, FontAwesome.COG, FONT_SIZE);

        appSettings.add(new MMenuItem(Labels.DEFAULT_SETTINGS)
                .withMnemonic(KeyEvent.VK_S).withActionListener(e -> w.getActivePerspective().resetViews()));
        appSettings.addMenuListener(this);
        WebCheckBoxMenuItem showTooltips = createShowTooltipsMenuItem();
        appSettings.add(showTooltips);

        // TODO dorobić etykietę
        WebMenuItem labelEditorItem = new MMenuItem("Edytor etykiet");
        labelEditorItem.addActionListener(e -> LabelEditorWindow.showModal(workbench.getFrame()));
        appSettings.add(labelEditorItem);

        // TODO dorobić etykietę
        WebMenuItem dictionaryEditorItem = new MMenuItem("Edytor słówników");
        dictionaryEditorItem.addActionListener(e -> DictionaryEditorWindow.showModal(workbench.getFrame()));
        appSettings.add(dictionaryEditorItem);

        return appSettings;
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
