package pl.edu.pwr.wordnetloom.relationtest.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtest.model.SynsetRelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

@Stateless
public class SynsetRelationTestRepository extends GenericRepository<SynsetRelationTest> {

    @PersistenceContext
    EntityManager em;

    public void deleteByRelationType(SynsetRelationType relationType) {
        em.createQuery("DELETE FROM SynsetRelationTest r WHERE r.relationType.id = :relationType", SynsetRelationTest.class)
                .setParameter("relationType", relationType.getId())
                .executeUpdate();
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM SynsetRelationTest r", SynsetRelationTest.class)
                .executeUpdate();
    }

    public List<SynsetRelationTest> findByRelationType(SynsetRelationType relationType) {
        return em.createQuery("SELECT r FROM SynsetRelationTest r WHERE r.relationType.id = :relationType", SynsetRelationTest.class)
                .setParameter("relationType", relationType.getId())
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
