package pl.edu.pwr.wordnetloom.relationtest.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.relationtest.model.SenseRelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

public interface SenseRelationTestServiceRemote {

    void delete(SenseRelationTest relationTest);

    void delete(SenseRelationType relationType);

    void deleteAllSenseRelationTests();

    List<SenseRelationTest> findByRelationType(SenseRelationType relationType);

    List<SenseRelationTest> findAllSenseRelationTests();

    void switchSenseRelationTestsIntoNewRelation(SenseRelationType oldRelation, SenseRelationType newRelation);

    SenseRelationTest save(SenseRelationTest test);

    SenseRelationTest update(SenseRelationTest test);

}
