package pl.edu.pwr.wordnetloom.relationtest.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.relationtest.model.SynsetRelationTest;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

@Remote
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
