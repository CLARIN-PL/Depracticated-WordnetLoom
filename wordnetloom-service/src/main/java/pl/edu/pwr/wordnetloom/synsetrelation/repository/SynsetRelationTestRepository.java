package pl.edu.pwr.wordnetloom.synsetrelation.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationType;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelationTest;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelationType;

@Stateless
public class SynsetRelationTestRepository extends GenericRepository<SynsetRelationTest> {

    @PersistenceContext
    EntityManager em;

    public void deleteByRelationType(SenseRelationType relationType) {
        em.createQuery("DELETE FROM SynsetRelationTest r WHERE r.relationType = :relationType", SynsetRelationTest.class)
                .setParameter("relationType", relationType)
                .executeUpdate();
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM SynsetRelationTest r", SynsetRelationTest.class)
                .executeUpdate();
    }

    public List<SynsetRelationTest> findByRelationType(SynsetRelationType relationType) {
        return em.createQuery("SELECT r FROM SynsetRelationTest r WHERE r.relationType = :relationType", SynsetRelationTest.class)
                .setParameter("relationType", relationType)
                .getResultList();
    }

    public void switchTestsIntoNewRelation(SynsetRelationType oldRelation, SynsetRelationType newRelation) {
        for (SynsetRelationTest test : oldRelation.getRelationTests()) {
            test.setRelationType(newRelation);
            update(test);
        }
    }

    @Override
    protected Class<SynsetRelationTest> getPersistentClass() {
        return SynsetRelationTest.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
