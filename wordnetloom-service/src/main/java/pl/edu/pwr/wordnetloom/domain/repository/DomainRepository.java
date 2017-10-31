package pl.edu.pwr.wordnetloom.domain.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.lexicon.model.LexiconAllowedPartOfSpeech;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DomainRepository extends GenericRepository<Domain> {

    @Inject
    EntityManager em;

    @Override
    protected Class<Domain> getPersistentClass() {
        return Domain.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Domain> findByLexiconAndPartOfSpeech(Lexicon lexicon, PartOfSpeech pos) {
        Query query = em.createQuery(
                "SELECT lap FROM LexiconAllowedPartOfSpeech lap " +
                        "JOIN FETCH lap.domain " +
                        "WHERE lap.lexicon.id = :lexicon AND lap.partOfSpeech.id = :pos", LexiconAllowedPartOfSpeech.class);
        LexiconAllowedPartOfSpeech lap = (LexiconAllowedPartOfSpeech) query
                .setParameter("lexicon", lexicon.getId())
                .setParameter("pos", pos.getId())
                .getSingleResult();
        return new ArrayList(lap.getDomain());
    }

    public List<Domain> findAllWithFullNames() {
        return em.createQuery("FROM Domain d JOIN FETCH d.nameStrings JOIN FETCH d.descriptionStrings")
                .getResultList();
    }

}
