package pl.edu.pwr.wordnetloom.commontests.utils;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@Ignore
@Stateless
public class TestRepositoryEJB {

    @PersistenceContext
    private EntityManager em;

    private static final List<Class<?>> ENTITIES_TO_REMOVE = Arrays.asList(Lexicon.class,
            PartOfSpeech.class, Domain.class, RelationType.class);

    public void deleteAll() {
        for (Class<?> entityClass : ENTITIES_TO_REMOVE) {
            deleteAllForEntity(entityClass);
        }
    }

    private void deleteAllForEntity(Class<?> entityClass) {
        List<Object> rows = em.createQuery("Select e From " + entityClass.getSimpleName() + " e").getResultList();
        for (Object row : rows) {
            em.remove(row);
        }
    }

    public void add(Object entity) {
        em.persist(entity);
    }

}
