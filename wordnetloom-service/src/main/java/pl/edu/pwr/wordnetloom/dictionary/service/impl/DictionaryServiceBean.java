package pl.edu.pwr.wordnetloom.dictionary.service.impl;

import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.repository.DictionaryRepository;
import pl.edu.pwr.wordnetloom.dictionary.service.DictionaryServiceLocal;
import pl.edu.pwr.wordnetloom.dictionary.service.DictionaryServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
@Local(DictionaryServiceLocal.class)
@Remote(DictionaryServiceRemote.class)
public class DictionaryServiceBean implements  DictionaryServiceLocal{

    @Inject
    DictionaryRepository dictionaryRepository;

    @Inject
    Validator validator;

    @Override
    public Dictionary save(Dictionary dic) {
        ValidationUtils.validateEntityFields(validator, dic);
        return dictionaryRepository.save(dic);
    }

    @Override
    public void remove(Dictionary dic) {

    }

    @Override
    public <T> List<T> findDictionaryByClass(Class<T> clazz) {
        return dictionaryRepository.findDictionaryByClass(clazz);
    }

    @Override
    public List<String> findAllDictionaryNames() {
        return dictionaryRepository.findAllDictionaryNames();
    }

    @Override
    public List<Dictionary> findAll() {
        return dictionaryRepository.findAll("id");
    }
}
