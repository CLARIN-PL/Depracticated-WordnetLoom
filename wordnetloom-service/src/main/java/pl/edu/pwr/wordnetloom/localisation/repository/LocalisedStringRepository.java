package pl.edu.pwr.wordnetloom.localisation.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
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

    public Map<String, String> findAllLabels(String locale) {
        Map<String, String> map = new HashMap<>();

        List<Object[]> list = em.createNativeQuery("SELECT label_key, value FROM application_labels WHERE language = :locale")
                .setParameter("locale", locale).getResultList();

        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }

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
