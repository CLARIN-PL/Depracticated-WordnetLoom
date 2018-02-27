package pl.edu.pwr.wordnetloom.relationtest.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class RelationTestRepository extends GenericRepository<RelationTest> {

    @Inject
    EntityManager em;

    @Override
    public void delete(RelationTest test) {

        RelationTest t = findById(test.getId());

        if(t != null) {
            super.delete(t);
        }
    }

    public void deleteByRelationType(RelationType relationType) {
        em.createQuery("DELETE FROM RelationTest r WHERE r.relationType.id = :relationType", RelationTest.class)
                .setParameter("relationType", relationType.getId())
                .executeUpdate();
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM RelationTest r", RelationTest.class)
                .executeUpdate();
    }

    public List<RelationTest> findByRelationType(RelationType relationType) {
        return em.createQuery("SELECT r FROM RelationTest r WHERE r.relationType.id = :relationType", RelationTest.class)
                .setParameter("relationType", relationType.getId())
                .getResultList();
    }

    public void switchTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation) {
        for (RelationTest test : oldRelation.getRelationTests()) {
            test.setRelationType(newRelation);
            update(test);
        }
    }

    @Override
    protected Class<RelationTest> getPersistentClass() {
        return RelationTest.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
