package pl.edu.pwr.wordnetloom.client;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.AuthenticateUserEvent;
import pl.edu.pwr.wordnetloom.client.security.AuthenticationSuccessEvent;
import pl.edu.pwr.wordnetloom.client.security.ErrorEvent;
import pl.edu.pwr.wordnetloom.client.security.LoginWindow;
import pl.edu.pwr.wordnetloom.client.systems.managers.*;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;


public class Application implements Loggable {

    public static final String PROGRAM_NAME_VERSION = "WordnetLoom 2.0";

    public static final EventBus eventBus = new EventBus();

    private LoginWindow login = new LoginWindow(new WebFrame());

    public Application() {
        eventBus.register(this);
    }

    public static void main(String... args) {

        WebLookAndFeel.install();
        IconFontSwing.register(FontAwesome.getIconFont());

        Application app = new Application();
        eventBus.post(new AuthenticateUserEvent());

    }

    private void start() {
        try {

            Workbench workbench = new PanelWorkbench(PROGRAM_NAME_VERSION);
            workbench.setVisible(true);

        } catch (Exception ex) {
            logger().error("Workbench initialization error:", ex);
        }
    }

    @Subscribe
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {

        Thread managers = new Thread(() -> {

            List<Lexicon> lexicons = RemoteService.lexiconServiceRemote.findAll();
            LexiconManager.getInstance().load(lexicons);
            PartOfSpeechManager.getInstance();
            DomainManager.getInstance();
            List<RelationType> relations = RemoteService.relationTypeRemote.findAll();
            RelationTypeManager.getInstance().load(relations);
            DictionaryManager.getInstance();
            start();
        }, "Mangers Thread");

        managers.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            private boolean first = true;

            @Override
            public void uncaughtException(Thread t, Throwable tt) {
                if (first) {
                    SwingUtilities.invokeLater(() -> {
                        tt.printStackTrace();
                        eventBus.post(new ApplicationErrorEvent(Messages.ERROR_UNABLE_TO_CONNECT_TO_SERVER));
                    });
                    first = false;
                }
                try {
                    Thread.sleep(13000);
                } catch (InterruptedException e) {
                }
                System.exit(1);
            }
        });

        managers.start();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        JOptionPane.showMessageDialog(null,
                event.getMessage(),
                Application.PROGRAM_NAME_VERSION,
                JOptionPane.ERROR_MESSAGE);
    }

}
