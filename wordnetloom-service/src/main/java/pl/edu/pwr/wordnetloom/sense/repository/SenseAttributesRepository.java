package pl.edu.pwr.wordnetloom.sense.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class SenseAttributesRepository extends GenericRepository<SenseAttributes>{

    @Inject
    EntityManager em;

    @Override
    protected Class<SenseAttributes> getPersistentClass() {
        return SenseAttributes.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public List<SenseAttributes> findByLemmaWithSense(String lemma, List<Long> lexicons) {
        return getEntityManager().createQuery(
                "SELECT sa FROM SenseAttributes sa " +
                        "LEFT JOIN FETCH sa.sense s " +
                        "LEFT JOIN FETCH s.word w "+
                        "LEFT JOIN FETCH s.domain "+
                        "LEFT JOIN FETCH s.partOfSpeech "+
                        "LEFT JOIN FETCH s.lexicon " +
                        "WHERE s.lexicon.id IN (:lexicons) " +
                        "AND LOWER(w.word) LIKE :lemma ORDER BY w.word asc", SenseAttributes.class)
                .setParameter("lemma", lemma.toLowerCase())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

}
