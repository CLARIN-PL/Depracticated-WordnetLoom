package pl.edu.pwr.wordnetloom.relation.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relation.model.RelationTest;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;

@Stateless
public class RelationTestRepository extends GenericRepository<RelationTest> {

    @PersistenceContext
    EntityManager em;

    @Override
    public void removeRelationTestsFor(RelationType relationType) {
        local.getEM().createNamedQuery("RelationTest.removeRelationTestsFor", RelationTest.class)
                .setParameter("relationType", relationType)
                .executeUpdate();
    }

    @Override
    public void removeAllRelationTests() {
        local.getEM().createNamedQuery("RelationTest.removeAllRelationTests", RelationTest.class)
                .executeUpdate();
    }

    @Override
    public List<RelationTest> getRelationTestsFor(RelationType relationType) {
        return local.getEM().createNamedQuery("RelationTest.getRelationTestsFor", RelationTest.class)
                .setParameter("relationType", relationType)
                .getResultList();
    }

    @Override
    public List<RelationTest> getAllRelationTests() {
        return local.getEM().createNamedQuery("RelationTest.getAllRelationTests", RelationTest.class)
                .getResultList();
    }

    @Override
    public void switchTestsIntoNewRelation(RelationType oldRelation,
            RelationType newRelation) {
        newRelation = relationDao.getEagerRelationTypeByID(newRelation);
        oldRelation = relationDao.getEagerRelationTypeByID(oldRelation);

        for (RelationTest test : oldRelation.getRelationTests()) {
            test.setRelationType(newRelation);
            local.mergeObject(test);
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
