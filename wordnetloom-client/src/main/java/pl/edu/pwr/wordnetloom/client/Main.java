package pl.edu.pwr.wordnetloom.client;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import pl.edu.pwr.wordnetloom.client.plugins.login.window.LoginWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


public class Main {

    public static final String PROGRAM_VERSION = "2.0";
    public static final String PROGRAM_NAME = "WordnetLoom";
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    private static final Map<String, String> labelsMap = new HashMap<>();
    private static final Map<Long, String> localisedStringsMap = new HashMap<>();

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

        WebLookAndFeel.install();
        IconFontSwing.register(FontAwesome.getIconFont());


        LoginWindow dialog = new LoginWindow(new WebFrame());
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
            //RelationTypeManager.loadRels();

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
            public void uncaughtException(Thread t, Throwable tt) {
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
                    Thread.sleep(13000);
                } catch (InterruptedException e) {
                }
                System.exit(1);
            }
        });

        dialog.setThreadOnSuccess(managers);
        dialog.setVisible(true);
    }

    public static void initialiseLanguage(Language lang) {
        Locale locale;
        if (lang == null) {
            locale = new Locale(Language.English.getAbbreviation());
        } else {
            locale = new Locale(lang.getAbbreviation());
        }
        loadLabels(locale);
    }

    private static void loadLabels(Locale locale) {
        labelsMap.putAll(RemoteService.localisedStringServiceRemote.findLabelsByLanguage(locale.getLanguage()));
        localisedStringsMap.putAll(RemoteService.localisedStringServiceRemote.findAllByLanguageAsMap(locale.getLanguage()));
    }

    public static String getResource(String key) {
        if (labelsMap.containsKey(key)) {
            return labelsMap.get(key);
        }
        return "";
    }

    public static String getLocalisedString(Long key) {
        if (localisedStringsMap.containsKey(key)) {
            return localisedStringsMap.get(key);
        }
        return "";
    }
}
