package pl.edu.pwr.wordnetloom.systems.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.workbench.implementation.PanelWorkbench;

public final class LexiconManager {

	private static final Logger LOGGER = Logger.getLogger(LexiconManager.class);
	private static volatile LexiconManager instance = null;
	private List<Long> cachedLexicons;
	private List<Lexicon> cachedFullLexicons;
	private boolean lexiconMarker;
	private ConfigurationManager config = new ConfigurationManager(PanelWorkbench.WORKBENCH_CONFIG_FILE);

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
		cachedFullLexicons = RemoteUtils.lexicalUnitRemote.getLexiconsFromList(lexicons);
	}

	private void loadLexicons() {
		try {
			cachedLexicons = Collections.unmodifiableList(Collections.synchronizedList(readLexiconsFromFile()));
		} catch (InvalidAttributeValueException e) {
			LOGGER.debug("Problem while loading lexicon string from file", e);
		}
	}

	public Lexicon getLexiconById(Long id) {
		return cachedFullLexicons.stream().filter(l -> l.getId().equals(id)).findAny().get();
	}

	private void loadLexiconMarker() {
		config.loadConfig();
		String marker = config.get("LexiconMarker");
		if (!marker.equals("") && marker != null && marker.equals("on")) {
			lexiconMarker = true;
		} else if (!marker.equals("") && marker != null && marker.equals("off")) {
			lexiconMarker = false;
		} else {
			lexiconMarker = true;
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
		config.loadConfig();
		config.set("LexiconMarker", option);
		config.saveConfig();
		refresh();
	}

	private List<Long> readLexiconsFromFile() throws InvalidAttributeValueException {
		config.loadConfig();
		List<Long> list = new ArrayList<Long>();
		String[] lexiconArray = config.get("Lexicons").split(",");
		for (int i = 0; i < lexiconArray.length; i++) {
			try {
				Long id = Long.parseLong(lexiconArray[i]);
				list.add(id);
			} catch (Exception ex) {
				throw new InvalidAttributeValueException("Invalid character in lexicon string");
			}
		}
		return list;
	}

	public List<Long> getLexicons() {
		return cachedLexicons;
	}

	public void save(String lexicons) {
		config.loadConfig();
		config.set("Lexicons", lexicons);
		config.saveConfig();
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
