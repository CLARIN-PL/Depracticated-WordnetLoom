package pl.edu.pwr.wordnetloom.dictionary.servcie;

import java.util.List;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

public interface DictionaryServiceRemote {

    void saveOrUpdate(Dictionary dic);

    void remove(Dictionary dic);

    <T> List<T> findDictionaryByClass(Class<T> clazz);

    List<String> findAllDictionaryNames();
}
