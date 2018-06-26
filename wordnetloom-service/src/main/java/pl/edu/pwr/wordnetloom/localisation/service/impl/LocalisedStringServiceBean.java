package pl.edu.pwr.wordnetloom.localisation.service.impl;

import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.localisation.exception.LocalisedStringNotFoundException;
import pl.edu.pwr.wordnetloom.localisation.exception.UnsupportedLanguageException;
import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import pl.edu.pwr.wordnetloom.localisation.repository.LocalisedStringRepository;
import pl.edu.pwr.wordnetloom.localisation.service.LocalisedStringServiceLocal;
import pl.edu.pwr.wordnetloom.localisation.service.LocalisedStringServiceRemote;
import sun.reflect.CallerSensitive;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;

@Stateless
@Remote(LocalisedStringServiceRemote.class)
@Local(LocalisedStringServiceLocal.class)
public class LocalisedStringServiceBean implements LocalisedStringServiceLocal {

    @Inject
    LocalisedStringRepository repository;

    @Inject
    Validator validator;

    @Override
    public List<ApplicationLabel> findLabelsByLanguage(String locale) {
        return repository.findAllLabels(locale);
    }

    @Override
    public LocalisedString findStringsByKey(LocalisedKey key) {
        LocalisedString string = repository.findByKey(key);
        if (string == null) {
            throw new LocalisedStringNotFoundException();
        }
        return string;
    }

    @Override
    public List<LocalisedString> findAllStringsByLanguage(String language) {
        return repository.findAllByLanguage(language);
    }

    @Override
    public Map<Long, String> findAllByLanguageAsMap(String language) {
        return repository.findAllByLanguageAsMap(language);
    }

    public LocalisedString add(LocalisedString entity) {
        ValidationUtils.validateEntityFields(validator, entity);
        return repository.persist(entity);
    }

    public void update(LocalisedString entity) {
        ValidationUtils.validateEntityFields(validator, entity);

        if (!repository.existsByKey(entity.getKey())) {
            throw new LocalisedStringNotFoundException();
        }

        repository.update(entity);
    }

    @Override
    public ApplicationLabel save(ApplicationLabel label){
        return repository.save(label);
    }

    @Override
    public ApplicationLabel find(String key, String language){
        return repository.find(key, language);
    }

    @Override
    public Map<String, Map<Long, String>> finaAll() {
        return repository.findAll();
    }

    @Override
    public List<ApplicationLabel> findStringInAllLanguages(String key){
        return repository.findStringsByKey(key);
    }
}
