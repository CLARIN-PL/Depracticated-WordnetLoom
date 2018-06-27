package pl.edu.pwr.wordnetloom.localisation.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class LocalisedStringRepository extends GenericRepository<LocalisedString> {

    @Inject
    EntityManager em;

    public List<ApplicationLabel> findAllLabels(String locale) {
        return em.createQuery("FROM ApplicationLabel a WHERE a.language=:language")
                .setParameter("language", locale)
                .getResultList();
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

    public Map<String, Map<Long, String>> findAll() {

        final List<LocalisedString> list = em.createQuery("SELECT s FROM  LocalisedString s", LocalisedString.class)
                .getResultList();

        Map<String, Map<Long, String>> map = new HashMap<>();

        list.forEach(i -> {
            String locale = i.getKey().getLanguage();
            if (!map.containsKey(locale)) {
                map.put(i.getKey().getLanguage(), new HashMap<>());
            }
             map.get(locale).put(i.getKey().getId(), i.getValue());
        });
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
        List<Register> list = getEntityManager().createQuery("FROM Register").getResultList();
        Map<Long, String> resultMap = new HashMap<>();
        for (Register register : list) {
            resultMap.put(register.getId(), String.valueOf(register.getName())); //TODO zmienić tekst
        }
        return resultMap;
    }

    public List<ApplicationLabel> findStringsByKey(String key){
        return em.createQuery("FROM ApplicationLabel a WHERE a.key =:key")
                .setParameter("key", key)
                .getResultList();
    }

    public ApplicationLabel save(ApplicationLabel label){
        if(em.contains(label)){
            em.persist(label);
            return label;
        } else {
            return em.merge(label);
        }
    }

    public ApplicationLabel find(String key, String language){
        return (ApplicationLabel) em.createQuery("FROM ApplicationLabel a WHERE a.key = :key AND a.language = :language")
                .setParameter("key", key)
                .setParameter("language", language)
                .getSingleResult();
    }

    public void remove(ApplicationLabel label){
        em.remove(label);
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
