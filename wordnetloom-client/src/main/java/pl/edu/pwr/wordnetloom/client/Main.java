package pl.edu.pwr.wordnetloom.client;

import de.sic.main.SingleInstanceController;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.client.systems.managers.ConfigurationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);
    private static final String ERROR_MESSAGE = "W systemie może być uruchomiona tylko jedna aplikacja plWordNet.";
    private static final boolean ONE_INSTANCE = true;

    private static final String PROGRAM_VERSION = "plWordNet 2.0.9";
    private static final String PROGRAM_NAME = "WordnetLoom";

    private static ResourceBundle resource;
    private static SingleInstanceController sic = null;

    public static void main(String[] args) {

        Thread instancer = new Thread(() -> {

            if (Main.ONE_INSTANCE) {
                sic = new SingleInstanceController(new File(System.getProperty("java.io.tmpdir")
                        + Main.PROGRAM_NAME + ".file"), Main.class.getName());
                LOGGER.info("Is other instance running... " + sic.isOtherInstanceRunning());
                if (sic.isOtherInstanceRunning()) {
                    JOptionPane.showMessageDialog(null, Main.ERROR_MESSAGE,
                            Main.PROGRAM_NAME, JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else {
                    LOGGER.info("Registered... " + sic.registerApplication());
                }
            }
        }, "Instancer Thread");

        instancer.start();
        initializLanguage();

        Thread managers = new Thread(() -> {
            // Must be first other Managers depend on it
            LexiconManager.getInstance();
            PosManager.getInstance();
            DomainManager.getInstance();
            RelationTypes.loadRels();
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

        managers.start();
        CommandLine cl;
        Options options = createOptions();

        try {
            cl = new GnuParser().parse(options, args);
        } catch (ParseException pe) {
            System.err.println(pe.getMessage());
            return;
        }

        if (cl.hasOption("help")) {
            HelpFormatter form = new HelpFormatter();
            form.printHelp("wordnetloom-client.jar", options);
            return;
        }

        if (cl.hasOption("version")) {
            System.out.println(PROGRAM_VERSION);
            return;
        }

        try {
            instancer.join();
            managers.join();
        } catch (Exception e) {
            Logger.getLogger(Main.class).log(Level.ERROR,
                    "Error while joini instancer and managers" + e);
            System.exit(1);
        }

        Workbench workbench;

        try {
            workbench = new PanelWorkbench(PROGRAM_NAME + " " + PROGRAM_VERSION.split(" ")[1]);
            workbench.setVisible(true);
        } catch (Exception ex) {
            LOGGER.error("Workbench initialization error:", ex);
        }

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
    }

    static private void initializLanguage() {
        Locale locale;
        ConfigurationManager config = new ConfigurationManager(PanelWorkbench.WORKBENCH_CONFIG_FILE);
        config.loadConfiguration();
        String interfaceLanguage = config.get("InterfaceLanguage");
        if (interfaceLanguage == null) {
            locale = new Locale("en");
        } else {
            locale = new Locale(interfaceLanguage);
        }
        if ("pl".equals(interfaceLanguage) || "en".equals(interfaceLanguage)) {
            resource = ResourceBundle.getBundle("lang", locale);
        }
    }

    public static String getResouce(String key) {
        return resource.getString(key);
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption("help", false, "print this help message");
        options.addOption("version", false, "print program version");
        return options;
    }

}
