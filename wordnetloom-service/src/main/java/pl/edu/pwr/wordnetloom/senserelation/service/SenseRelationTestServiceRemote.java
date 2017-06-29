package pl.edu.pwr.wordnetloom.senserelation.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationTest;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationType;

@Remote
public interface SenseRelationTestServiceRemote {

    void delete(SenseRelationTest relationTest);

    void delete(SenseRelationType relationType);

    void deleteAll();

    List<SenseRelationTest> findByRelationType(SenseRelationType relationType);

    List<SenseRelationTest> findAll();

    void switchTestsIntoNewRelation(SenseRelationType oldRelation, SenseRelationType newRelation);

    SenseRelationTest save(SenseRelationTest test);

    SenseRelationTest update(SenseRelationTest test);

}
