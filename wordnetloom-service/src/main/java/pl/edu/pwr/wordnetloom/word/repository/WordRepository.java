package pl.edu.pwr.wordnetloom.word.repository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
public class WordRepository extends GenericRepository<Word> {

    @Inject
    EntityManager em;

    @Override
    protected Class<Word> getPersistentClass() {
        return Word.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Word findByWord(String word) {
        try{
            return em.createQuery("SELECT w FROM Word w WHERE CONVERT(w.word, BINARY) = :word", Word.class)
                    .setParameter("word", word)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }

    }

    public Integer countByWord(String word) {
        return em.createQuery("SELECT COUNT(w.word) FROM Word w WHERE w.word = :word", Integer.class)
                .setParameter("word", word)
                .getSingleResult();
    }
}
