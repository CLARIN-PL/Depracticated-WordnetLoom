package pl.edu.pwr.wordnetloom.word.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
public class WordRepository extends GenericRepository<Word> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Word> getPersistentClass() {
        return Word.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Word> findByWord(Word word) {
        return em.createQuery("SELECT w FROM Word w WHERE w.word = :word", Word.class)
                .setParameter("word", word.getWord())
                .getResultList();
    }

    public Integer countByWord(Word word) {
        return em.createQuery("SELECT COUNT(w.word) FROM Word w WHERE w.word = :word", Integer.class)
                .setParameter("word", word.getWord())
                .getSingleResult();
    }
}
