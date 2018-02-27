package pl.edu.pwr.wordnetloom.relationtest.service;

import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.List;

public interface RelationTestServiceRemote {

    void delete(RelationTest relationTest);

    void delete(RelationType relationType);

    void deleteAllSenseRelationTests();

    List<RelationTest> findByRelationType(RelationType relationType);

    List<RelationTest> findAllSenseRelationTests();

    void switchSenseRelationTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation);

    RelationTest save(RelationTest test);

}
