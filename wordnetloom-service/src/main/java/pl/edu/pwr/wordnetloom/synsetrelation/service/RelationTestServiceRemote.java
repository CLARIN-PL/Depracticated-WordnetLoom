package pl.edu.pwr.wordnetloom.synsetrelation.service;

import pl.edu.pwr.wordnetloom.relation.service.*;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.relation.model.RelationTest;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Remote
public interface RelationTestServiceRemote {

    void removeRelationTest(RelationTest relationTest);

    void removeRelationTestsFor(RelationType relationType);

    void removeAllRelationTests();

    List<RelationTest> getRelationTestsFor(RelationType relationType);

    List<RelationTest> getAllRelationTests();

    void switchTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation);

    List<CorpusExample> getCorpusExamplesFor(Word word);

    void persist(RelationTest test);

    void merge(RelationTest test);

}
