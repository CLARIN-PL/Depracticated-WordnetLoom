package pl.edu.pwr.wordnetloom.senserelation.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationTest;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationType;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationTestRepository;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationTestServiceLocal;

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
    public void deleteAll() {
        relationTestRepository.deleteAll();
    }

    @Override
    public List<SenseRelationTest> findByRelationType(SenseRelationType relationType) {
        return relationTestRepository.findByRelationType(relationType);
    }

    @Override
    public List<SenseRelationTest> findAll() {
        return relationTestRepository.findAll("id");
    }

    @Override
    public void switchTestsIntoNewRelation(SenseRelationType oldRelation, SenseRelationType newRelation) {
        relationTestRepository.switchTestsIntoNewRelation(oldRelation, newRelation);
    }

    @Override
    public SenseRelationTest save(SenseRelationTest test) {
        return relationTestRepository.save(test);
    }

    @Override
    public SenseRelationTest update(SenseRelationTest test) {
        return relationTestRepository.update(test);
    }

}
