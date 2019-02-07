package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryManager {

    private static DictionaryManager instance;
    List<Dictionary> dictionaries;

    public static DictionaryManager getInstance() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    private DictionaryManager() {
        dictionaries = RemoteService.dictionaryServiceRemote.findAll();
    }

    public <T> List<T> getDictionaryByClassName(Class<T> clazz) {
        return dictionaries.stream()
                .filter(d -> clazz.isInstance(d))
                .map(d -> (T) d)
                .collect(Collectors.toList());
    }

    public<T> List<T> getDictionaryById(Long id) {
        return dictionaries.stream()
                .filter(d->d.getId().equals(id))
                .map(d->(T)d)
                .collect(Collectors.toList());
    }

}
