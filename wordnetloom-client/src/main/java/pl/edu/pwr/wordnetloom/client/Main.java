package pl.edu.pwr.wordnetloom.client;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import pl.edu.pwr.wordnetloom.client.plugins.login.window.LoginWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static final String PROGRAM_VERSION = "2.0";
    public static final String PROGRAM_NAME = "WordnetLoom";

    private static ResourceBundle resource;

    public static void main(String[] args) {

        Properties props = new Properties();
        try {
            props.load(Main.class.getResourceAsStream("/log4j.properties"));
        } catch (IOException ex) {
            LOGGER.error("No log4 properties file", ex);
        }
        PropertyConfigurator.configure(props);

        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            LOGGER.error("Uncaught exception", e);
        });

        LoginWindow dialog = new LoginWindow(new JFrame());
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        Thread managers = new Thread(() -> {
            // Must be first other Managers depend on it
            LexiconManager.getInstance();
            PartOfSpeechManager.getInstance();
            DomainManager.getInstance();
            RelationTypeManager.loadRels();
            Workbench workbench;
            try {
                workbench = new PanelWorkbench(PROGRAM_NAME + " " + PROGRAM_VERSION);
                workbench.refreshUserBar(RemoteConnectionProvider.getInstance().getUser());
                workbench.setVisible(true);

            } catch (Exception ex) {
                LOGGER.error("Workbench initialization error:", ex);
            }

        }, "Mangers Thread");

        managers.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            private boolean first = true;

            @Override
            public void uncaughtException(Thread t, final Throwable tt) {
                if (first) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                Messages.ERROR_UNABLE_TO_CONNECT_TO_SERVER,
                                Main.PROGRAM_NAME,
                                JOptionPane.ERROR_MESSAGE);
                    });
                    first = false;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                System.exit(1);
            }
        });

        dialog.setThreadOnSuccess(managers);
        dialog.setVisible(true);
    }

    public static void initializLanguage(Language lang) {
        Locale locale;
        if (lang == null) {
            locale = new Locale(Language.English.getAbbreviation());
        } else {
            locale = new Locale(lang.getAbbreviation());
        }
        resource = ResourceBundle.getBundle("lang", locale);
    }

    public static String getResouce(String key) {
        return resource.getString(key);
    }

}
