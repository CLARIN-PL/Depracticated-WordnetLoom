package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.CorpusExampleDaoLocal;
import pl.edu.pwr.wordnetloom.dao.RelationTestDaoLocal;
import pl.edu.pwr.wordnetloom.model.CorpusExample;
import pl.edu.pwr.wordnetloom.model.RelationTest;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Word;

@Stateless
public class TestServiceBean implements TestServiceRemote {

	@EJB private RelationTestDaoLocal relationTestLocal;
	@EJB private CorpusExampleDaoLocal corpusExampleLocal;
	
	@Override
	public void removeRelationTest(RelationTest relationTest) {
		relationTestLocal.removeRelationTest(relationTest);
	}

	@Override
	public void removeRelationTestsFor(RelationType relationType) {
		relationTestLocal.removeRelationTestsFor(relationType);
	}

	@Override
	public void removeAllRelationTests() {
		relationTestLocal.removeAllRelationTests();
	}

	@Override
	public List<RelationTest> getRelationTestsFor(RelationType relationType) {
		return relationTestLocal.getRelationTestsFor(relationType);
	}

	@Override
	public List<RelationTest> getAllRelationTests() {
		return relationTestLocal.getAllRelationTests();
	}

	@Override
	public void switchTestsIntoNewRelation(RelationType oldRelation,
			RelationType newRelation) {
		relationTestLocal.switchTestsIntoNewRelation(oldRelation, newRelation);
	}

	@Override
	public List<CorpusExample> getCorpusExamplesFor(Word word) {
		return corpusExampleLocal.getCorpusExamplesFor(word);
	}

	@Override
	public void persist(RelationTest test) {
		relationTestLocal.persist(test);
	}

	@Override
	public void merge(RelationTest test) {
		relationTestLocal.merge(test);
	}

}
