package pl.edu.pwr.wordnetloom.client.systems.managers;

import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LexiconManager {

    private static final Logger LOGGER = Logger.getLogger(LexiconManager.class);
    private static volatile LexiconManager instance = null;
    private List<Long> cachedLexicons;
    private List<Lexicon> cachedFullLexicons;
    private boolean lexiconMarker;

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
        lexiconMarker = RemoteConnectionProvider.getInstance().getUser().getSettings().getLexionMarker();
    }

    public List<Lexicon> getFullLexicons() {
        return cachedFullLexicons;
    }

    public boolean getLexiconMarker() {
        return lexiconMarker;
    }

    public void setLexiconMarkerOn() {
        lexiconMarker = true;
    }

    public void setLexiconMarkerOff() {
        lexiconMarker = false;
    }


    private List<Long> readLexiconsFromFile() throws InvalidAttributeValueException {
        List<Long> list = new ArrayList<>();
        String[] lexiconArray = RemoteConnectionProvider.getInstance().getUser().getSettings().getChosenLexicons().split(";");
        for (String element : lexiconArray) {
            try {
                Long id = Long.parseLong(element);
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

    public List<Long> setLexicons(List<Long> lexicons) {
        cachedLexicons = lexicons;
        return cachedLexicons;
    }

    public void refresh() {
        loadLexicons();
        loadFullLexicons(cachedLexicons);
    }

}
