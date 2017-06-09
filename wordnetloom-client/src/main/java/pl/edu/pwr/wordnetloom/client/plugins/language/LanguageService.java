package pl.edu.pwr.wordnetloom.client.plugins.language;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.Main;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class LanguageService extends AbstractService {

    private static final String EMBEDDED_EN_PROPERTIES = "lang_en.properties";
    private static final String EMBEDDED_PL_PROPERTIES = "lang_pl.properties";

    private final List<JMenuItem> menuItems = new ArrayList<>();
    private final Map<String, String> languages = new HashMap<>();
    private JMenuItem selectedItem;

    public LanguageService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installViews() {
    }

    @Override
    public void installMenuItems() {
        initLanguages();
        buildLanguagesMenu();

        menuItems.stream().forEach((menuItem) -> {
            workbench.installMenu(Labels.HELP, Labels.LANGUAGE, menuItem);
        });
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    private void buildLanguagesMenu() {
        Iterator<Entry<String, String>> it = languages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            final JMenuItem menuItem;
            if (workbench.getParam("InterfaceLanguage").equals(pairs.getKey())) {
                menuItem = new JCheckBoxMenuItem(pairs.getValue());
                menuItem.setLocale(new Locale(pairs.getKey()));
                menuItem.setSelected(true);
                selectedItem = menuItem;
            } else {
                menuItem = new JCheckBoxMenuItem(pairs.getValue());
                menuItem.setLocale(new Locale(pairs.getKey()));
            }

            menuItem.addActionListener((ActionEvent e) -> {
                workbench.setParam("InterfaceLanguage", menuItem.getLocale().getLanguage());
                menuItem.setSelected(true);
                selectedItem.setSelected(false);
                selectedItem = menuItem;
                if (JOptionPane.showOptionDialog(menuItem,
                        Messages.INFO_RESTART_APPLICATION, Labels.APPLICATION_RESTART,
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
                    restartApplication();
                }
            });
            menuItems.add(menuItem);
        }
    }

    private void initLanguages() {
        try {
            Properties properties = new Properties();
            properties.load(LanguageService.class.getClassLoader().getResourceAsStream(EMBEDDED_PL_PROPERTIES));
            languages.put(properties.getProperty("locale"), properties.getProperty("language"));

            properties = new Properties();
            properties.load(LanguageService.class.getClassLoader().getResourceAsStream(EMBEDDED_EN_PROPERTIES));
            languages.put(properties.getProperty("locale"), properties.getProperty("language"));
        } catch (IOException e) {
            Logger.getLogger(getClass()).log(Level.ERROR, "Language Service error " + e);
        }
    }

    private void restartApplication() {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        try {
            File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            if (!currentJar.getName().endsWith(".jar")) {
                System.exit(0);
            }

            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException e) {
            Logger.getLogger(getClass()).log(Level.ERROR, "Restart error " + e);
        }
        System.exit(0);
    }
}
