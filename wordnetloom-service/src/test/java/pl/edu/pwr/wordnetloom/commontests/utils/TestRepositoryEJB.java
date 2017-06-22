package pl.edu.pwr.wordnetloom.commontests.utils;

import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

@Ignore
@Stateless
public class TestRepositoryEJB {

    @PersistenceContext
    private EntityManager em;

    private static final List<Class<?>> ENTITIES_TO_REMOVE = Arrays.asList(Lexicon.class);

    public void deleteAll() {
        for (final Class<?> entityClass : ENTITIES_TO_REMOVE) {
            deleteAllForEntity(entityClass);
        }
    }

    @SuppressWarnings("unchecked")
    private void deleteAllForEntity(final Class<?> entityClass) {
        final List<Object> rows = em.createQuery("Select e From " + entityClass.getSimpleName() + " e").getResultList();
        for (final Object row : rows) {
            em.remove(row);
        }
    }

    public void add(final Object entity) {
        em.persist(entity);
    }

}
