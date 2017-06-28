package pl.edu.pwr.wordnetloom.corpusexample.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
public class CorpusExampleRepository extends GenericRepository<CorpusExample> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<CorpusExample> getPersistentClass() {
        return CorpusExample.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<CorpusExample> findCorpusExamplesByWord(Word word) {
        return em.createQuery("SELECT c FROM CorpusExample c WHERE c.word.word = :word", CorpusExample.class)
                .setParameter("word", word.getWord())
                .getResultList();
    }

}
