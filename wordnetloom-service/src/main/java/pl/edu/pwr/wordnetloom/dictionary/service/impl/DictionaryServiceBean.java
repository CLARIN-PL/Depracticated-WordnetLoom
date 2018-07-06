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
        // TODO
        if(dic.getId() != null){
            ValidationUtils.validateEntityFields(validator, dic);
        }
        return dictionaryRepository.save(dic);
    }

    @Override
    public void remove(Dictionary dic) {

    }

    @Override
    public <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(Class<T> clazz) {
        return dictionaryRepository.findDictionaryByClass(clazz);
    }

    @Override
    public <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(String className) {
        return dictionaryRepository.findDictionaryByClass(className);
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
