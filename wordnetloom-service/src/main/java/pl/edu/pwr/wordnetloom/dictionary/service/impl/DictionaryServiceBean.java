package pl.edu.pwr.wordnetloom.dictionary.service.impl;

import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;
import pl.edu.pwr.wordnetloom.dictionary.repository.DictionaryRepository;
import pl.edu.pwr.wordnetloom.dictionary.service.DictionaryServiceLocal;
import pl.edu.pwr.wordnetloom.dictionary.service.DictionaryServiceRemote;
import pl.edu.pwr.wordnetloom.localisation.repository.LocalisedStringRepository;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Local(DictionaryServiceLocal.class)
@Remote(DictionaryServiceRemote.class)
public class DictionaryServiceBean implements  DictionaryServiceLocal{

    @Inject
    DictionaryRepository dictionaryRepository;

    @Inject
    LocalisedStringRepository localisedStringRepository;

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
        List<Long> localisationIds = new ArrayList<>();
        try {
            localisationIds = getLocalisationIds(dic);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        dictionaryRepository.delete(dic.getId());
        for(Long id : localisationIds){
            localisedStringRepository.removeLocalisedString(id);
        }
    }

    private List<Long> getLocalisationIds(Dictionary dictionary) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = dictionary.getClass().getMethods();
        String name;
        List<Long> resultIds = new ArrayList<>();
        for(Method method : methods){
            name = method.getName();
            if(method.getReturnType() == Long.class
                    &&name.contains("get")
                    && !name.equals("getId")
                    && !name.equals("getClass")){
                Long id = (Long) method.invoke(dictionary);
                resultIds.add(id);
            }
        }
        return resultIds;
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

    @Override
    public Status findStatusDefaultValue() {
        return dictionaryRepository.findStatusDefaultValue();
    }
}
