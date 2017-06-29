package pl.edu.pwr.wordnetloom.synsetrelation.service.impl;

import pl.edu.pwr.wordnetloom.relation.service.impl.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.relation.model.RelationTest;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
public class RelationTestServiceBean implements RelationTestServiceLocal {

    @Inject
    private RelationTestRepository relationTestRepository;

    @Override
    public void removeRelationTest(RelationTest relationTest) {
        relationTestRepository.removeRelationTest(relationTest);
    }

    @Override
    public void removeRelationTestsFor(RelationType relationType) {
        relationTestRepository.removeRelationTestsFor(relationType);
    }

    @Override
    public void removeAllRelationTests() {
        relationTestRepository.removeAllRelationTests();
    }

    @Override
    public List<RelationTest> getRelationTestsFor(RelationType relationType) {
        return relationTestRepository.getRelationTestsFor(relationType);
    }

    @Override
    public List<RelationTest> getAllRelationTests() {
        return relationTestRepository.getAllRelationTests();
    }

    @Override
    public void switchTestsIntoNewRelation(RelationType oldRelation,
            RelationType newRelation) {
        relationTestRepository.switchTestsIntoNewRelation(oldRelation, newRelation);
    }

    @Override
    public List<CorpusExample> getCorpusExamplesFor(Word word) {
        return corpusExampleLocal.getCorpusExamplesFor(word);
    }

    @Override
    public void persist(RelationTest test) {
        relationTestRepository.persist(test);
    }

    @Override
    public void merge(RelationTest test) {
        relationTestRepository.merge(test);
    }

}
