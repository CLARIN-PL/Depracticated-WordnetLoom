package pl.edu.pwr.wordnetloom.localisation.repository;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.dto.DataMap;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.dictionary.model.RegisterDictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import pl.edu.pwr.wordnetloom.localisation.model.RegisterType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class LocalisedStringRepository extends GenericRepository<LocalisedString> {

    @Inject
    EntityManager em;

    public Map<String, String> findAllLabels(String locale) {
        Map<String, String> map = new HashMap<>();

        final List<Object[]> list = em.createNativeQuery("SELECT label_key, value FROM application_labels WHERE language = :locale")
                .setParameter("locale", locale).getResultList();
        list.forEach(i -> map.put(i[0].toString(), i[1].toString()));
        return map;
    }

    public LocalisedString findByKey(LocalisedKey key) {
        TypedQuery<LocalisedString> s = em.createQuery("SELECT s FROM  LocalisedString s WHERE s.key = :key", LocalisedString.class)
                .setParameter("key", key);
        return s.getSingleResult();
    }

    public List<LocalisedString> findAllByLanguage(String language) {
        TypedQuery<LocalisedString> s = em.createQuery("SELECT s FROM  LocalisedString s WHERE s.key.language = :lang", LocalisedString.class)
                .setParameter("lang", language);
        return s.getResultList();
    }

    public Map<Long, String> findAllByLanguageAsMap(String language) {
        final Map<Long, String> map = new HashMap<>();
        List<LocalisedString> list = findAllByLanguage(language);
        list.forEach(i -> map.put(i.getKey().getId(), i.getValue()));
        return map;
    }

    private Long findNextId() {
        Long next = (Long) em.createQuery("SELECT max(s.key.id) FROM LocalisedString s")
                .getSingleResult();
        return next != null ? next + 1 : 1;
    }

    public boolean existsByKey(LocalisedKey key) {
        return getEntityManager().createQuery(
                "SELECT 1 FROM LocalisedString s WHERE s.key = :key")
                .setParameter("key", key)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }

    public Map<Long, String> findAllRegisterTypes(String language) {

        List<RegisterDictionary> list = getEntityManager().createQuery("FROM RegisterDictionary").getResultList();
        Map<Long, String> resultMap = new HashMap<>();
        for(RegisterDictionary register : list){
            resultMap.put(register.getId(), String.valueOf(register.getName())); //TODO zmienić tekst
        }
        return resultMap;
    }

    @Override
    public LocalisedString persist(LocalisedString s) {
        if (s.getKey().getId() == null) {
            s.getKey().setId(findNextId());
        }
        getEntityManager().persist(s);
        return s;
    }


    @Override
    protected Class<LocalisedString> getPersistentClass() {
        return LocalisedString.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
