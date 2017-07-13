package pl.edu.pwr.wordnetloom.relationtest.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.relationtest.model.SenseRelationTest;
import pl.edu.pwr.wordnetloom.relationtest.repository.SenseRelationTestRepository;
import pl.edu.pwr.wordnetloom.relationtest.service.SenseRelationTestServiceLocal;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Stateless
public class SenseRelationTestServiceBean implements SenseRelationTestServiceLocal {

    @Inject
    private SenseRelationTestRepository relationTestRepository;

    @Override
    public void delete(SenseRelationTest relationTest) {
        relationTestRepository.delete(relationTest);
    }

    @Override
    public void delete(SenseRelationType relationType) {
        relationTestRepository.deleteByRelationType(relationType);
    }

    @Override
    public List<SenseRelationTest> findByRelationType(SenseRelationType relationType) {
        return relationTestRepository.findByRelationType(relationType);
    }

    @Override
    public List<SenseRelationTest> findAllSenseRelationTests() {
        return relationTestRepository.findAll("id");
    }

    @Override
    public SenseRelationTest save(SenseRelationTest test) {
        return relationTestRepository.persist(test);
    }

    @Override
    public SenseRelationTest update(SenseRelationTest test) {
        return relationTestRepository.update(test);
    }

    @Override
    public void deleteAllSenseRelationTests() {
        relationTestRepository.deleteAll();
    }

    @Override
    public void switchSenseRelationTestsIntoNewRelation(SenseRelationType oldRelation, SenseRelationType newRelation) {
        relationTestRepository.switchTestsIntoNewRelation(oldRelation, newRelation);
    }

}
