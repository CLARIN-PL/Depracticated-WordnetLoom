package pl.edu.pwr.wordnetloom.lexicon.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

@Stateless
public class LexiconRepository extends GenericRepository<Lexicon> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected Class<Lexicon> getPersistentClass() {
        return Lexicon.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Lexicon> findByLexicons(List<Long> lexiconsIds) {
        Query query = em.createQuery("FROM Lexicon l WHERE l.id IN (:ids)");
        return query
                .setParameter("ids", lexiconsIds)
                .getResultList();
    }
    
    public List<Long> findAllLexiconIds() {
        return em.createQuery("SELECT l.id FROM Lexicon l").getResultList();
    }
}
