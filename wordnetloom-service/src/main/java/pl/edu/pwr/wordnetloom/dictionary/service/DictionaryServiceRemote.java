package pl.edu.pwr.wordnetloom.dictionary.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;

public interface DictionaryServiceRemote {

    Dictionary save(Dictionary dic);

    void remove(Dictionary dic);

    <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(Class<T> clazz);

    <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(String className);

    List<String> findAllDictionaryNames();

    List<Dictionary> findAll();

    Status findStatusDefaultValue();
}
