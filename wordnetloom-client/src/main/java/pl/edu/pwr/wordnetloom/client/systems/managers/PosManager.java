package pl.edu.pwr.wordnetloom.client.systems.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.edu.pwr.wordnetloom.client.utils.RMIUtils;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.service.POSServiceRemote;

public class PosManager {

    private static volatile PosManager defaultPosManager;
    private static String defaultLanguageCode;

    // just to be sure
    private static volatile Map<String, PosManager> managers
            = Collections.synchronizedMap(new HashMap<String, PosManager>());

    private List<PartOfSpeech> cache;
    private String languageCode;

    /**
     * Private constructor. We cannot let others to initialize it on their own.
     *
     * @param languageCode language code for this instance of manager.
     */
    private PosManager(String languageCode) {
        if (languageCode == null) {
            throw new IllegalArgumentException("languageCode cannot be null!");
        }

        // recent request is the most important!
        setDefaultLanguageCode(languageCode);

        // fetch data from database and cache it
        fetchDataFromDB(languageCode);
    }

    private void fetchDataFromDB() {
        fetchDataFromDB(defaultLanguageCode);
    }

    /**
     * The most important method, it fetches data from database and puts it in
     * the cashe to later use. Another task for this method is to update
     * managers' keys.
     *
     * @param languageCode
     */
    private void fetchDataFromDB(String languageCode) {
        if (languageCode == null) {
            throw new IllegalArgumentException("languageCode cannot be null!");
        }

        synchronized (languageCode) {
            cache = null;

            /**
             * currently fetching only default language, despite the argument.
             *
             * fixme
             */
            POSServiceRemote service = RMIUtils.lookupForService(POSServiceRemote.class);

            List<PartOfSpeech> list;
            list = service.getAllPartsOfSpeech(LexiconManager.getInstance().getLexicons());
            cache = Collections.unmodifiableList(Collections.synchronizedList(list));
            synchronized (PosManager.class) {
                if (!managers.containsKey(languageCode)) {
                    managers.put(languageCode, this);
                }
                setLanguageCode(languageCode);
            }
        }
    }

    /**
     * Method to refresh current manager data with current language code from
     * database.
     */
    public void refresh() {
        if (cache != null) {
            synchronized (languageCode) {
                fetchDataFromDB();
            }
        }
    }

    /**
     * Simple getInstance method.
     *
     * @return default {@link PosManager} instance if exists, throws exception
     * otherwise.
     */
    public static PosManager getInstance() {
        if (defaultPosManager == null) {
            synchronized (PosManager.class) {
                // we want to believe
                if (defaultPosManager == null) {
                    defaultLanguageCode = "PL";
                    defaultPosManager = new PosManager(defaultLanguageCode);
                }
            }
        }
        return defaultPosManager;
    }

    /**
     * Simple getInstance method with parameter.
     *
     * @param languageCode language code to fetch from database
     * @return specific {@link PosManager}, this method won't throw exception if
     * no manager is found, manager will be created.
     */
    public static PosManager getInstance(String languageCode) {
        PosManager toReturn;
        synchronized (PosManager.class) {
            if (defaultPosManager != null && PosManager.getDefaultLanguageCode().equals(languageCode)) {
                toReturn = defaultPosManager;
            } else if (managers.containsKey(languageCode)) {
                toReturn = managers.get(languageCode);
            } else {
                toReturn = new PosManager(languageCode);
            }
        }
        return toReturn;
    }

    /**
     * Getting last used (default) language code.
     *
     * @return default language code or null, if manager was not initialized.
     */
    public static String getDefaultLanguageCode() {
        return defaultLanguageCode;
    }

    private static void setDefaultLanguageCode(String defaultLanguageCode) {
        PosManager.defaultLanguageCode = defaultLanguageCode;
    }

    /**
     * Gets current language code for this instance of manager.
     *
     * @return language code.
     */
    public String getLanguageCode() {
        return languageCode;
    }

    private void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * Simple init, without return. Use it, preferably in other thread, if you
     * do not want to hinder and slow down (suspend even) Swing's Event
     * Dispatcher Thread. Don't worry about concurrency, this whole manager is
     * as thread safe as it could possibly get.
     *
     * @param languageCode language code to init.
     */
    public static void initializeWithCode(String languageCode) {
        getInstance(languageCode);
    }

    /**
     * Check, if manager is initialized.
     *
     * @return true, if someone initialized at least one manager already.
     */
    public static boolean isInitialized() {
        return getDefaultLanguageCode() != null;
    }

    public PartOfSpeech getFromID(int id) {
        PartOfSpeech pos = null;
        for (int i = 0; i < cache.size() && pos == null; i++) {
            if (cache.get(i).getId().intValue() == id) {
                pos = cache.get(i);
            }
        }
        return pos;
    }

    public PartOfSpeech getNormalized(PartOfSpeech pos) {
        if (pos == null) {
            return null;
        }

        for (PartOfSpeech p : cache) {
            if (p.getId().equals(pos.getId())) {
                return p;
            }
        }
        return null;
    }

    @Deprecated
    public PartOfSpeech decode(String s) {
        PartOfSpeech pos = null;
        if (s == null || s.equals("default")) {
            pos = cache.get(0);
        }

        for (int i = 0; i < cache.size() && pos == null; i++) {
            if (cache.get(i).getName().toString().equals(s)) {
                pos = cache.get(i);
            }
        }
        return pos;
    }

    public List<PartOfSpeech> getPOSForLexicon(long id) {
        List<PartOfSpeech> filtred = new ArrayList<>();
        cache.stream().filter((pos) -> (pos.getLexicon().getId() == id)).forEach((pos) -> {
            filtred.add(pos);
        });
        return filtred;
    }

    public List<PartOfSpeech> getAllPOSes() {
        return cache;
    }
}
