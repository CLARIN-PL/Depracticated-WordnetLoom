package pl.edu.pwr.wordnetloom.client.plugins.lexicon;

import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.core.CoreService;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.events.SearchUnitsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.events.SetLexiconsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.window.LexiconManagerWindow;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.window.LexiconsWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateGraphEvent;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.user.model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class LexiconService extends AbstractService {

    // TODO przenieśc to w inne miejsce
    private final WebMenuItem lexiconItem = new MMenuItem(Labels.LEXICON)
            .withIcon(FontAwesome.BOOK);

    private final WebMenuItem lexiconManagerItem = new MMenuItem("Zarządzaj leksykonami")
            .withIcon(FontAwesome.ANGELLIST);

    public LexiconService(Workbench workbench) {
        super(workbench);

        lexiconItem.addActionListener((ActionEvent e) -> {
            showLexiconWindow();
            sendEvents();
            ViWordNetService s = ServiceManager.getViWordNetService(workbench);
            s.reloadCurrentListSelection();
        });

        lexiconManagerItem.addActionListener((ActionEvent e) -> {
            showLexiconManagerWindow();
        });
    }

    @Override
    public void installViews() {
    }

    @Override
    public void installMenuItems() {
        WebMenu user = workbench.getMenu(UserSessionContext.getInstance().getFullName());
        WebMenu help = findMenu(CoreService.APP_SETTINGS, user);
        if (help == null) {
            return;
        }
        help.add(lexiconItem);
        help.add(lexiconManagerItem);
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

        User user = UserSessionContext.getInstance().getUser();
        user.getSettings().setChosenLexicons(LexiconManager.getInstance().lexiconIdToString(lexicons));
        RemoteService.userServiceRemote.save(user);

        String language = UserSessionContext.getInstance().getLanguage();
        UserSessionContext.initialiseAndGetInstance(user, language);
    }

    private void showLexiconManagerWindow() {
        LexiconManagerWindow.showModal(workbench.getFrame());
    }

    private void sendEvents() {
        Application.eventBus.post(new SetLexiconsEvent());
        Application.eventBus.post(new SearchUnitsEvent());
        Application.eventBus.post(new UpdateGraphEvent(null));
    }
}
