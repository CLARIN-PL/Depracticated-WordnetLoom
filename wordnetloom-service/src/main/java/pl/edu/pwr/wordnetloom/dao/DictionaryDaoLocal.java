package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.Dictionary;

@Local
public interface DictionaryDaoLocal {

    void saveOrUpdate(Dictionary dic);

    void remove(Dictionary dic);

    <T> List<T> findDictionaryByClass(Class<T> clazz);

    List<String> findAllDictionaryNames();
}
