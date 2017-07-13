package pl.edu.pwr.wordnetloom.relationtest.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtest.model.SenseRelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Stateless
public class SenseRelationTestRepository extends GenericRepository<SenseRelationTest> {

    @PersistenceContext
    EntityManager em;

    public void deleteByRelationType(SenseRelationType relationType) {
        em.createQuery("DELETE FROM SenseRelationTest r WHERE r.relationType.id = :relationType", SenseRelationTest.class)
                .setParameter("relationType", relationType.getId())
                .executeUpdate();
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM SenseRelationTest r", SenseRelationTest.class)
                .executeUpdate();
    }

    public List<SenseRelationTest> findByRelationType(SenseRelationType relationType) {
        return em.createQuery("SELECT r FROM SenseRelationTest r WHERE r.relationType.id = :relationType", SenseRelationTest.class)
                .setParameter("relationType", relationType.getId())
                .getResultList();
    }

    public void switchTestsIntoNewRelation(SenseRelationType oldRelation, SenseRelationType newRelation) {
        for (SenseRelationTest test : oldRelation.getRelationTests()) {
            test.setRelationType(newRelation);
            update(test);
        }
    }

    @Override
    protected Class<SenseRelationTest> getPersistentClass() {
        return SenseRelationTest.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
