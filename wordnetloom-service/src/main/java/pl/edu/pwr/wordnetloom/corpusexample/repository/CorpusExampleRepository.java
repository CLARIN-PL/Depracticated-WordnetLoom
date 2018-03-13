package pl.edu.pwr.wordnetloom.corpusexample.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
public class CorpusExampleRepository extends GenericRepository<CorpusExample> {

    @Inject
    EntityManager em;

    @Override
    protected Class<CorpusExample> getPersistentClass() {
        return CorpusExample.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<CorpusExample> findCorpusExamplesByWord(String word) {
        return em.createQuery("SELECT c FROM CorpusExample c WHERE c.word = :word", CorpusExample.class)
                .setParameter("word", word)
                .getResultList();
    }

}
