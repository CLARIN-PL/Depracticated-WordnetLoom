package pl.edu.pwr.wordnetloom.client.systems.managers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

public class DomainManager {

    private static volatile DomainManager defaultDomainManager;
    private static String defaultLanguageCode;

    // just to be sure
    private static volatile Map<String, DomainManager> managers
            = Collections.synchronizedMap(new HashMap<String, DomainManager>());

    private List<Domain> cache;
    private String languageCode;

    /**
     * Private constructor. We cannot let others to initialize it on their own.
     *
     * @param languageCode language code for this instance of manager.
     */
    private DomainManager(String languageCode) {
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
            List<Domain> list = RemoteService.domainServiceRemote.findAll();
            cache = Collections.unmodifiableList(Collections.synchronizedList(list));

            synchronized (DomainManager.class) {
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
        // refresh data in cache with new one from database
        if (cache != null) {
            synchronized (languageCode) {
                fetchDataFromDB();
            }
        }
    }

    /**
     * Simple getInstance method.
     *
     * @return default {@link DomainManager} instance if exists, throws
     * exception otherwise.
     */
    public static DomainManager getInstance() {
        DomainManager defaultDM = DomainManager.defaultDomainManager;
        if (defaultDM == null) {
            synchronized (DomainManager.class) {
                defaultDM = DomainManager.defaultDomainManager;
                // we want to believe
                if (defaultDM == null) {
                    defaultLanguageCode = "pl";
                    DomainManager.defaultDomainManager = defaultDM = new DomainManager(defaultLanguageCode);
                }
            }
        }
        return defaultDM;
    }

    /**
     * Simple getInstance method with parameter.
     *
     * @param languageCode language code to fetch from database
     * @return specific {@link DomainManager}, this method won't throw exception
     * if no manager is found, manager will be created.
     */
    public static DomainManager getInstance(String languageCode) {
        DomainManager toReturn;
        synchronized (DomainManager.class) {
            if (defaultDomainManager != null && DomainManager.getDefaultLanguageCode().equals(languageCode)) {
                toReturn = defaultDomainManager;
            } else if (managers.containsKey(languageCode)) {
                toReturn = managers.get(languageCode);
            } else {
                toReturn = new DomainManager(languageCode);
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
        DomainManager.defaultLanguageCode = defaultLanguageCode;
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

    public Domain getByID(int id) {
        Domain domain = null;
        for (int i = 0; i < cache.size() && domain == null; i++) {
            if (cache.get(i).getId().intValue() == id) {
                domain = cache.get(i);
            }
        }
        return domain;
    }

    public Domain getNormalized(Domain dd) {
        for (Domain d : cache) {
            if (d.getId().equals(dd.getId())) {
                return d;
            }
        }
        return null;
    }

    public List<Domain> getAllDomains() {
        return cache;
    }

    public List<Domain> getAllDomainsSorted() {
        return sortDomains(getAllDomains());
    }

    public List<Domain> sortDomains(List<Domain> doaminsToSort) {
        List<Domain> toReturn = Arrays.asList(doaminsToSort.toArray(new Domain[]{}));
        Collections.sort(toReturn, (Domain a, Domain b) -> a.getName(languageCode).compareTo(b.getName(languageCode)));
        return toReturn;
    }

    public Domain[] sortedDomainsAsArray() {
        return getAllDomainsSorted().toArray(new Domain[]{});
    }

    @Deprecated
    public Domain decode(String s) {

        Domain toReturn = null;

        if (s == null || s.equals("default")) {
            toReturn = cache.get(0);
        }

        for (int i = 0; i < cache.size() && toReturn == null; i++) {
            if (cache.get(i).getName(defaultLanguageCode).equals(s)) {
                toReturn = cache.get(i);
            }
        }
        return toReturn;
    }

    public Domain[] sortedDomainsAsArrayByNumber() {
        long max = 0;
        for (Domain d : cache) {
            max = Math.max(max, d.getId());
        }

        Domain[] toReturn = new Domain[(int) max];
        Arrays.fill(toReturn, cache.get(0));

        cache.stream().forEach((d) -> {
            toReturn[(int) (d.getId() + 0)] = d;
        });

        return toReturn;
    }
}
