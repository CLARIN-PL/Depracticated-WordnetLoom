package pl.edu.pwr.wordnetloom.relationtest.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.relationtest.model.SynsetRelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

public interface SynsetRelationTestServiceRemote {

    void delete(SynsetRelationTest relationTest);

    void delete(SynsetRelationType relationType);

    void deleteAllSynsetRelationTests();

    List<SynsetRelationTest> findByRelationType(SynsetRelationType relationType);

    List<SynsetRelationTest> findAllSynsetRelationTests();

    void switchSynsetRelationTestsIntoNewRelation(SynsetRelationType oldRelation, SynsetRelationType newRelation);

    SynsetRelationTest save(SynsetRelationTest test);

    SynsetRelationTest update(SynsetRelationTest test);

}
