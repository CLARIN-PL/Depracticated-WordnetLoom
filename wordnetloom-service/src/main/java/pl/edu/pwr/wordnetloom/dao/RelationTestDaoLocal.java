package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.relation.model.RelationTest;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;

@Local
public interface RelationTestDaoLocal {

    void removeRelationTest(RelationTest relationTest);

    void removeRelationTestsFor(RelationType relationType);

    void removeAllRelationTests();

    List<RelationTest> getRelationTestsFor(RelationType relationType);

    List<RelationTest> getAllRelationTests();

    void switchTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation);

    void persist(RelationTest test);

    void merge(RelationTest test);

}
