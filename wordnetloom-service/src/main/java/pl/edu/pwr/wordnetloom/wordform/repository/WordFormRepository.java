package pl.edu.pwr.wordnetloom.wordform.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.wordform.model.WordForm;

@Stateless
public class WordFormRepository extends GenericRepository<WordForm> {

    @Inject
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

    public List<WordForm> findFormByLemmaAndTag(String lemma){
        return em.createNamedQuery(WordForm.FIND_FORM_BY_WORD, WordForm.class)
                .setParameter("word",lemma)
                .getResultList();
    }

}
