package pl.edu.pwr.wordnetloom.client.plugins.language;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class LanguageService extends AbstractService {

    private static final String EMBEDDED_EN_PROPERTIES = "lang_en.properties";
    private static final String EMBEDDED_PL_PROPERTIES = "lang_pl.properties";

    private final Map<String, String> languages = new HashMap<>();

    public LanguageService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installViews() {
    }

    @Override
    public void installMenuItems() {
        initLanguages();
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
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
}
