package pl.edu.pwr.wordnetloom.dictionary.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

public interface DictionaryServiceRemote {

    Dictionary save(Dictionary dic);

    void remove(Dictionary dic);

    <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(Class<T> clazz);

    List<String> findAllDictionaryNames();

    List<Dictionary> findAll();
}
