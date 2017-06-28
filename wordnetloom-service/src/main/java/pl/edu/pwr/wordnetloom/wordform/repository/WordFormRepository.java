package pl.edu.pwr.wordnetloom.wordform.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.wordform.model.WordForm;

@Stateless
public class WordFormRepository extends GenericRepository<WordForm> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<WordForm> getPersistentClass() {
        return WordForm.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM WordForm w", WordForm.class)
                .executeUpdate();
    }

    public WordForm findByWordAndTags(Word word, List<String> tags) {
        List<WordForm> list = em.createQuery("SELECT w FROM WordForm w WHERE w.word LIKE :word OR w.form IN (:tags)", WordForm.class)
                .setParameter("word", word.getWord())
                .setParameter("tags", tags)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    public List<Sense> findSensesWithoutForm() {
        //return lexUnit.dbGetUnitsWithoutForms();
        return null;
    }

}
