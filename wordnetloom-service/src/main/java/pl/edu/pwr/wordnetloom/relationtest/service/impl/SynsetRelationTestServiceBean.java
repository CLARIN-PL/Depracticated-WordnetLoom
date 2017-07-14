package pl.edu.pwr.wordnetloom.relationtest.service.impl;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.relationtest.model.SynsetRelationTest;
import pl.edu.pwr.wordnetloom.relationtest.repository.SynsetRelationTestRepository;
import pl.edu.pwr.wordnetloom.relationtest.service.SynsetRelationTestServiceLocal;
import pl.edu.pwr.wordnetloom.relationtest.service.SynsetRelationTestServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

@Stateless
@Remote(SynsetRelationTestServiceRemote.class)
@Local(SynsetRelationTestServiceLocal.class)
public class SynsetRelationTestServiceBean implements SynsetRelationTestServiceLocal {

    @Inject
    private SynsetRelationTestRepository relationTestRepository;

    @Override
    public void delete(SynsetRelationTest relationTest) {
        relationTestRepository.delete(relationTest);
    }

    @Override
    public void delete(SynsetRelationType relationType) {
        relationTestRepository.deleteByRelationType(relationType);
    }

    @Override
    public void deleteAllSynsetRelationTests() {
        relationTestRepository.deleteAll();
    }

    @Override
    public List<SynsetRelationTest> findByRelationType(SynsetRelationType relationType) {
        return relationTestRepository.findByRelationType(relationType);
    }

    @Override
    public List<SynsetRelationTest> findAllSynsetRelationTests() {
        return relationTestRepository.findAll("id");
    }

    @Override
    public void switchSynsetRelationTestsIntoNewRelation(SynsetRelationType oldRelation, SynsetRelationType newRelation) {
        relationTestRepository.switchTestsIntoNewRelation(oldRelation, newRelation);
    }

    @Override
    public SynsetRelationTest save(SynsetRelationTest test) {
        return relationTestRepository.persist(test);
    }

    @Override
    public SynsetRelationTest update(SynsetRelationTest test) {
        return relationTestRepository.update(test);
    }

}
