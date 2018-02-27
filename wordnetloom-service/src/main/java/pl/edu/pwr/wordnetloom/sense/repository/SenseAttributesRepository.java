package pl.edu.pwr.wordnetloom.sense.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class SenseAttributesRepository extends GenericRepository<SenseAttributes>{

    @Inject
    EntityManager em;

    @Override
    protected Class<SenseAttributes> getPersistentClass() {
        return SenseAttributes.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
