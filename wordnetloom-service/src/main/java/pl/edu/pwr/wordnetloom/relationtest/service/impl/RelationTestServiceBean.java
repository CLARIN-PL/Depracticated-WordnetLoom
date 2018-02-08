package pl.edu.pwr.wordnetloom.relationtest.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtest.repository.RelationTestRepository;
import pl.edu.pwr.wordnetloom.relationtest.service.RelationTestServiceLocal;
import pl.edu.pwr.wordnetloom.relationtest.service.RelationTestServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(RelationTestServiceRemote.class)
@Local(RelationTestServiceLocal.class)
public class RelationTestServiceBean implements RelationTestServiceLocal {

    @Inject
    private RelationTestRepository relationTestRepository;

    @RolesAllowed("ADMIN")
    @Override
    public void delete(RelationTest relationTest) {
        relationTestRepository.delete(relationTest);
    }

    @RolesAllowed("ADMIN")
    @Override
    public void delete(RelationType relationType) {
        relationTestRepository.deleteByRelationType(relationType);
    }

    @PermitAll
    @Override
    public List<RelationTest> findByRelationType(RelationType relationType) {
        return relationTestRepository.findByRelationType(relationType);
    }

    @PermitAll
    @Override
    public List<RelationTest> findAllSenseRelationTests() {
        return relationTestRepository.findAll("id");
    }

    @RolesAllowed("ADMIN")
    @Override
    public RelationTest save(RelationTest test) {
        return relationTestRepository.persist(test);
    }

    @RolesAllowed("ADMIN")
    @Override
    public RelationTest update(RelationTest test) {
        return relationTestRepository.update(test);
    }

    @RolesAllowed("ADMIN")
    @Override
    public void deleteAllSenseRelationTests() {
        relationTestRepository.deleteAll();
    }

    @RolesAllowed("ADMIN")
    @Override
    public void switchSenseRelationTestsIntoNewRelation(RelationType oldRelation, RelationType newRelation) {
        relationTestRepository.switchTestsIntoNewRelation(oldRelation, newRelation);
    }

}
