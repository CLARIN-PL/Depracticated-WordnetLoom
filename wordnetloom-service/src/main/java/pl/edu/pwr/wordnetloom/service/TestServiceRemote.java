package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.CorpusExample;
import pl.edu.pwr.wordnetloom.model.RelationTest;
import pl.edu.pwr.wordnetloom.model.Word;

@Remote
public interface TestServiceRemote {

	public void removeRelationTest(RelationTest relationTest);
	public void removeRelationTestsFor(RelationType relationType);
	public void removeAllRelationTests();
	
	public List<RelationTest> getRelationTestsFor(RelationType relationType);
	public List<RelationTest> getAllRelationTests();
	
	public void switchTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation);
	
	public List<CorpusExample> getCorpusExamplesFor(Word word);
	
	public void persist(RelationTest test);
	public void merge(RelationTest test);

}
