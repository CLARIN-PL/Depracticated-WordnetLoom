package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.RelationTestDaoLocal;
import pl.edu.pwr.wordnetloom.dao.RelationTypeDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationTest;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;

@Stateless
public class RelationTestDaoBean implements RelationTestDaoLocal {

    @EJB
    private DAOLocal local;

    @EJB
    private RelationTypeDAOLocal relationDao;

    @Override
    public void removeRelationTest(RelationTest relationTest) {
        local.deleteObject(local.getObject(RelationTest.class, relationTest.getId()));
    }

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
    public void persist(RelationTest test) {
        local.persistObject(test);
    }

    @Override
    public void merge(RelationTest test) {
        local.mergeObject(test);
    }

}
