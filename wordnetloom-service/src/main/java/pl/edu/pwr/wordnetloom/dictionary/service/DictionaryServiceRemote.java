package pl.edu.pwr.wordnetloom.dictionary.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

public interface DictionaryServiceRemote {

    Dictionary save(Dictionary dic);

    void remove(Dictionary dic);

    <T> List<T> findDictionaryByClass(Class<T> clazz);

    List<String> findAllDictionaryNames();

    List<Dictionary> findAll();
}
