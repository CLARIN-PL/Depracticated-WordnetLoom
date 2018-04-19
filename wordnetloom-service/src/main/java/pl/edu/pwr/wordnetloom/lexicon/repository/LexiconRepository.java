package pl.edu.pwr.wordnetloom.lexicon.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class LexiconRepository extends GenericRepository<Lexicon> {

    @Inject
    EntityManager em;

    @Override
    protected Class<Lexicon> getPersistentClass() {
        return Lexicon.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Long> findAllLexiconIds() {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT l.id FROM Lexicon l", Long.class);
        return query.getResultList();
    }


}
