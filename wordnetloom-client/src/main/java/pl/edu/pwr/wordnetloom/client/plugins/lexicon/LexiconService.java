package pl.edu.pwr.wordnetloom.client.plugins.lexicon;

import com.alee.laf.menu.WebMenu;
import pl.edu.pwr.wordnetloom.client.plugins.core.CoreService;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.window.LexiconsWindow;
import pl.edu.pwr.wordnetloom.client.plugins.login.data.UserSessionData;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class LexiconService extends AbstractService {

    private final JMenuItem lexiconItem = new MMenuItem(Labels.LEXICON);

    public LexiconService(final Workbench workbench) {
        super(workbench);

        lexiconItem.addActionListener((ActionEvent e) -> {
            showLexiconWindow();
            ViWordNetService s = ServiceManager.getViWordNetService(workbench);
            s.getLexicalUnitsView().refreshLexicons();
            s.getSynsetView().refreshLexicons();
            s.clearAllViews();
            s.reloadCurrentListSelection();
        });
    }

    @Override
    public void installViews() {
    }

    @Override
    public void installMenuItems() {
        WebMenu user = workbench.getMenu(RemoteConnectionProvider.getInstance().getUser().getFullname());
        WebMenu help = findMenu(CoreService.APP_SETTINGS, user);
        if (help == null) {
            return;
        }
        help.add(lexiconItem);
    }

    public WebMenu findMenu(String name, WebMenu menu) {
        Component[] components = menu.getMenuComponents();

        for (Component component : components) {
            if (component instanceof WebMenu
                    && ((WebMenu) component).getText().equals(name)) {
                return (WebMenu) component;
            }
        }
        return null;
    }
    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    private void showLexiconWindow() {
        Set<Lexicon> lexicons = LexiconsWindow.showModal(workbench.getFrame(), LexiconManager.getInstance().getUserChosenLexicons());
        User user = RemoteConnectionProvider.getInstance().getUser();
        user.getSettings().setChosenLexicons(LexiconManager.getInstance().lexiconIdToString(lexicons));
        RemoteService.userServiceRemote.save(user);

        UserSessionData data = RemoteConnectionProvider.getInstance().getUserSessionData();
        UserSessionData current = new UserSessionData(data.getUsername(), data.getPassword(), data.getLanguage() , user);
        RemoteConnectionProvider.getInstance().setUserSessionData(current);
    }
}
