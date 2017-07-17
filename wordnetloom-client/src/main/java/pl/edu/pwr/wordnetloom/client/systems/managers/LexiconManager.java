package pl.edu.pwr.wordnetloom.client.systems.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

public final class LexiconManager {

    private static final Logger LOGGER = Logger.getLogger(LexiconManager.class);
    private static volatile LexiconManager instance = null;
    private List<Long> cachedLexicons;
    private List<Lexicon> cachedFullLexicons;
    private boolean lexiconMarker;
    private final ConfigurationManager config = new ConfigurationManager(PanelWorkbench.WORKBENCH_CONFIG_FILE);

    private LexiconManager() {
        loadLexicons();
        loadLexiconMarker();
        loadFullLexicons(cachedLexicons);
    }

    public static LexiconManager getInstance() {
        if (instance == null) {
            synchronized (LexiconManager.class) {
                instance = new LexiconManager();
            }
        }
        return instance;
    }

    private void loadFullLexicons(List<Long> lexicons) {
        cachedFullLexicons = RemoteService.lexiconServiceRemote.findAllByLexicon(lexicons);
    }

    private void loadLexicons() {
        try {
            cachedLexicons = Collections.unmodifiableList(Collections.synchronizedList(readLexiconsFromFile()));
        } catch (InvalidAttributeValueException e) {
            LOGGER.debug("Problem while loading lexicon string from file", e);
        }
    }

    private void loadLexiconMarker() {
        config.loadConfiguration();
        String marker = config.get("LexiconMarker");
        if (!marker.equals("") && marker != null && marker.equals("on")) {
            lexiconMarker = true;
        } else {
            lexiconMarker = !(!marker.equals("") && marker != null && marker.equals("off"));
        }
    }

    public List<Lexicon> getFullLexicons() {
        return cachedFullLexicons;
    }

    public boolean getLexiconMarker() {
        return lexiconMarker;
    }

    public void setLexiconMarkerOn() {
        lexiconMarker = true;
        saveLexiconMarker("on");
    }

    public void setLexiconMarkerOff() {
        lexiconMarker = false;
        saveLexiconMarker("off");
    }

    private void saveLexiconMarker(String option) {
        config.loadConfiguration();
        config.set("LexiconMarker", option);
        config.saveConfiguration();
        refresh();
    }

    private List<Long> readLexiconsFromFile() throws InvalidAttributeValueException {
        config.loadConfiguration();
        List<Long> list = new ArrayList<>();
        String[] lexiconArray = config.get("Lexicons").split(",");
        for (String lexiconArray1 : lexiconArray) {
            try {
                Long id = Long.parseLong(lexiconArray1);
                list.add(id);
            } catch (NumberFormatException ex) {
                throw new InvalidAttributeValueException("Invalid character in lexicon string");
            }
        }
        return list;
    }

    public List<Long> getLexicons() {
        return cachedLexicons;
    }

    public void save(String lexicons) {
        config.loadConfiguration();
        config.set("Lexicons", lexicons);
        config.saveConfiguration();
        refresh();
    }

    public void refresh() {
        loadLexicons();
        loadFullLexicons(cachedLexicons);
    }

    public ConfigurationManager getActualConfig() {
        return config;
    }
}
