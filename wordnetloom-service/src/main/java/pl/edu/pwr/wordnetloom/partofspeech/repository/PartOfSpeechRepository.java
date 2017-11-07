package pl.edu.pwr.wordnetloom.partofspeech.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PartOfSpeechRepository extends GenericRepository<PartOfSpeech> {

    private final EntityManager em;

    @Inject
    public PartOfSpeechRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    protected Class<PartOfSpeech> getPersistentClass() {
        return PartOfSpeech.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<PartOfSpeech> findByLexicon(Lexicon lexicon) {
        Query query = em.createQuery("SELECT DISTINCT lapd.partOfSpeech FROM LexiconAllowedPartOfSpeech lapd WHERE lapd.lexicon.id = :id");
        return query
                .setParameter("id", lexicon.getId())
                .getResultList();
    }

    public List<PartOfSpeech> findAllWithName() {
        Query query = em.createQuery("SELECT pos FROM PartOfSpeech pos JOIN FETCH pos.nameStrings");
        return query
                .getResultList();
    }
}
