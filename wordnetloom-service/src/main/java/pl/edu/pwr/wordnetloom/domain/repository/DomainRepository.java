package pl.edu.pwr.wordnetloom.domain.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

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
        Query query = em.createQuery("SELECT lapd.partOfSpeech FROM LexiconAllowedPartOfSpeechDomain lapd WHERE lapd.lexicon = :lexicon AND lapd.partOfSpeech = :pos");
        return query
                .setParameter("lexicon", lexicon)
                .setParameter("pos", pos)
                .getResultList();
    }

}
