package pl.edu.pwr.wordnetloom.domain.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.LexiconAllowedPartOfSpeech;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    public List<Domain> findByLexiconAndPartOfSpeech(Long lexiconId, Long partOfSpeechId) {
        TypedQuery<LexiconAllowedPartOfSpeech> query = em.createQuery(
                "SELECT lap FROM LexiconAllowedPartOfSpeech lap " +
                        "JOIN FETCH lap.domain " +
                        "WHERE lap.lexicon.id = :lexiconId AND lap.partOfSpeech.id = :partOfSpeechId", LexiconAllowedPartOfSpeech.class);

        LexiconAllowedPartOfSpeech lap = query
                .setParameter("lexiconId", lexiconId)
                .setParameter("partOfSpeechId", partOfSpeechId)
                .getSingleResult();

        return new ArrayList(lap.getDomain());
    }

}
