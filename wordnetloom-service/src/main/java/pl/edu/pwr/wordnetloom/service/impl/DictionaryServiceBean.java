package pl.edu.pwr.wordnetloom.service.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DictionaryDaoLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.Dictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.LanguageVariantDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;
import pl.edu.pwr.wordnetloom.service.DictionaryServiceRemote;

@Stateless
public class DictionaryServiceBean implements DictionaryServiceRemote {

    @EJB
    private DictionaryDaoLocal local;

    @Override
    public List<StatusDictionary> findAllStatusDictionary() {
        return local.findDictionaryByClass(StatusDictionary.class);
    }

    public void saveOrUpdate(Dictionary dic) {
        local.saveOrUpdate(dic);
    }

    @Override
    public void remove(Dictionary dic) {
        local.remove(dic);
    }

    @Override
    public List<LanguageVariantDictionary> findAllLanguageVariantDictionary() {
        return local.findDictionaryByClass(LanguageVariantDictionary.class);
    }

    @Override
    public LanguageVariantDictionary findDefaultLanguageVariantDictionaryValue() {
        return local.findDefaultValueByClass(LanguageVariantDictionary.class);
    }

    @Override
    public StatusDictionary findDefaultStatusDictionaryValue() {
        return local.findDefaultValueByClass(StatusDictionary.class);
    }

    @Override
    public List<String> findAllDictionaryNames() {
        return local.findAllDictionaryNames();
    }

}
