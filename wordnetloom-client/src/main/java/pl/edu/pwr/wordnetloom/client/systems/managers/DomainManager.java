package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DomainManager {

    private static volatile DomainManager instance;

    private final List<Domain> cache;

    private DomainManager() {
        List<Domain> list = RemoteService.domainServiceRemote.findAll();
        cache = list;
    }

    public static DomainManager getInstance() {
        if (instance == null) {
            synchronized (DomainManager.class) {
                if (instance == null) {
                    instance = new DomainManager();
                }
            }
        }
        return instance;
    }

    public Domain getById(int id) {
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
        String lang = RemoteConnectionProvider.getInstance().getLanguage();
        // toReturn.sort(Comparator.comparing(a -> a.getName(lang)));
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

        String lang = RemoteConnectionProvider.getInstance().getLanguage();
        for (int i = 0; i < cache.size() && toReturn == null; i++) {
            // if (cache.get(i).getName(lang).equals(s)) {
            //     toReturn = cache.get(i);
            // }
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
            toReturn[Math.toIntExact(d.getId())] = d;
        });

        return toReturn;
    }
}
