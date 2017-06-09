package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.CorpusExample;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationTest;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;

@Remote
public interface TestServiceRemote {

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
