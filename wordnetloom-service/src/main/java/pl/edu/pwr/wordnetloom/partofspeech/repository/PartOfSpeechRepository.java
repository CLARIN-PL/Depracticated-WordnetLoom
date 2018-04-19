package pl.edu.pwr.wordnetloom.partofspeech.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PartOfSpeechRepository extends GenericRepository<PartOfSpeech> {

    @Inject
    EntityManager em;

    @Override
    protected Class<PartOfSpeech> getPersistentClass() {
        return PartOfSpeech.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<PartOfSpeech> findByLexicon(Lexicon lexicon) {
        TypedQuery<PartOfSpeech> query = em.createQuery(
                "SELECT DISTINCT lapd.partOfSpeech " +
                   "FROM LexiconAllowedPartOfSpeech lapd WHERE lapd.lexicon.id = :id", PartOfSpeech.class);
        return query
                .setParameter("id", lexicon.getId())
                .getResultList();
    }

}
