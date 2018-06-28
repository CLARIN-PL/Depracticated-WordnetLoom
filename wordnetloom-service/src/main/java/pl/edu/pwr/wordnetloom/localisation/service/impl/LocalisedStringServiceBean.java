package pl.edu.pwr.wordnetloom.localisation.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
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

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(LocalisedStringServiceRemote.class)
@Local(LocalisedStringServiceLocal.class)
public class LocalisedStringServiceBean implements LocalisedStringServiceLocal {

    @Inject
    LocalisedStringRepository repository;

    @Inject
    Validator validator;

    @PermitAll
    @Override
    public List<ApplicationLabel> findLabelsByLanguage(String locale) {
        return repository.findAllLabels(locale);
    }

    @PermitAll
    @Override
    public LocalisedString findStringsByKey(LocalisedKey key) {
        LocalisedString string = repository.findByKey(key);
        if (string == null) {
            throw new LocalisedStringNotFoundException();
        }
        return string;
    }

    @PermitAll
    @Override
    public List<LocalisedString> findAllStringsByLanguage(String language) {
        return repository.findAllByLanguage(language);
    }

    @PermitAll
    @Override
    public Map<Long, String> findAllByLanguageAsMap(String language) {
        return repository.findAllByLanguageAsMap(language);
    }

    @RolesAllowed("ADMIN")
    public LocalisedString add(LocalisedString entity) {
        ValidationUtils.validateEntityFields(validator, entity);
        return repository.persist(entity);
    }

    @RolesAllowed("ADMIN")
    public void update(LocalisedString entity) {
        ValidationUtils.validateEntityFields(validator, entity);

        if (!repository.existsByKey(entity.getKey())) {
            throw new LocalisedStringNotFoundException();
        }

        repository.update(entity);
    }

    @RolesAllowed("ADMIN")
    @Override
    public void remove(ApplicationLabel label){
        repository.remove(find(label.getKey(), label.getLanguage()));
    }

    @RolesAllowed("ADMIN")
    @Override
    public ApplicationLabel save(ApplicationLabel label){
        return repository.save(label);
    }

    @PermitAll
    @Override
    public ApplicationLabel find(String key, String language){
        return repository.find(key, language);
    }

    @PermitAll
    @Override
    public Map<String, Map<Long, String>> finaAll() {
        return repository.findAll();
    }

    @PermitAll
    @Override
    public List<ApplicationLabel> findStringInAllLanguages(String key){
        return repository.findStringsByKey(key);
    }
}
