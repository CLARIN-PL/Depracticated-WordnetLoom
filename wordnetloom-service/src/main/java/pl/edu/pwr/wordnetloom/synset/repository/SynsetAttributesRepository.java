package pl.edu.pwr.wordnetloom.synset.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class SynsetAttributesRepository extends GenericRepository<SynsetAttributes>{

    @Inject
    EntityManager em;

    @Override
    protected Class<SynsetAttributes> getPersistentClass() {
        return SynsetAttributes.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
