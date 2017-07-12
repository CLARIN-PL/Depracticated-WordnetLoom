package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.Dictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.LanguageVariantDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;

@Remote
public interface DictionaryServiceRemote {

    List<StatusDictionary> findAllStatusDictionary();

    List<LanguageVariantDictionary> findAllLanguageVariantDictionary();

    LanguageVariantDictionary findDefaultLanguageVariantDictionaryValue();

    StatusDictionary findDefaultStatusDictionaryValue();

    List<String> findAllDictionaryNames();

    void saveOrUpdate(Dictionary dic);

    void remove(Dictionary dic);
}
