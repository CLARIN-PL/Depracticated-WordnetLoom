package pl.edu.pwr.wordnetloom.relationtest.service.impl;

import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtest.repository.RelationTestRepository;
import pl.edu.pwr.wordnetloom.relationtest.service.RelationTestServiceLocal;
import pl.edu.pwr.wordnetloom.relationtest.service.RelationTestServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Remote(RelationTestServiceRemote.class)
@Local(RelationTestServiceLocal.class)
public class RelationTestServiceBean implements RelationTestServiceLocal {

    @Inject
    private RelationTestRepository relationTestRepository;

    @Override
    public void delete(RelationTest relationTest) {
        relationTestRepository.delete(relationTest);
    }

    @Override
    public void delete(RelationType relationType) {
        relationTestRepository.deleteByRelationType(relationType);
    }

    @Override
    public List<RelationTest> findByRelationType(RelationType relationType) {
        return relationTestRepository.findByRelationType(relationType);
    }

    @Override
    public List<RelationTest> findAllSenseRelationTests() {
        return relationTestRepository.findAll("id");
    }

    @Override
    public RelationTest save(RelationTest test) {
        return relationTestRepository.persist(test);
    }

    @Override
    public RelationTest update(RelationTest test) {
        return relationTestRepository.update(test);
    }

    @Override
    public void deleteAllSenseRelationTests() {
        relationTestRepository.deleteAll();
    }

    @Override
    public void switchSenseRelationTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation) {
        relationTestRepository.switchTestsIntoNewRelation(oldRelation, newRelation);
    }

}
