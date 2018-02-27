package pl.edu.pwr.wordnetloom.senserelation.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationRepository;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceLocal;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceRemote;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(SenseRelationServiceRemote.class)
@Local(SenseRelationServiceLocal.class)
public class SenseRelationServiceBean implements SenseRelationServiceLocal {

    @Inject
    private SenseRelationRepository senseRelationRepository;

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public void delete(SenseRelation rel) {
        senseRelationRepository.delete(rel);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public void delete(RelationType relation) {
        senseRelationRepository.delete(relation);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public void deleteAll() {
        senseRelationRepository.deleteAll();
    }

    @PermitAll
    @Override
    public SenseRelation findById(Long id) {
        return senseRelationRepository.findById(id);
    }

    @PermitAll
    @Override
    public List<SenseRelation> findSubRelations(Sense sense, RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @PermitAll
    @Override
    public List<SenseRelation> findRelations(RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Long> findSelfRelations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SenseRelation> findUpperRelations(Sense sense, RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SenseRelation> findFullRelations(Sense parent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SenseRelation> findRelations(Sense parent, Sense child, RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SenseRelation findRelation(Sense parent, Sense child, RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public boolean makeRelation(Sense parent, Sense child, RelationType relation) {
        return senseRelationRepository.makeRelation(parent, child, relation);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public void deleteConnection(Sense sense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SenseRelation> dbFullGetRelations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long findRelationsCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long findRelationUseCount(RelationType relation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(RelationType oldRelation, RelationType newRelation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @PermitAll
    @Override
    public boolean relationExists(Sense parent, Sense child, RelationType relationType) {
        return senseRelationRepository.relationExists(parent, child, relationType);
    }

    @Override
    public List<RelationType> findSenseRelationTypesBySense(Sense sense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long relationCountBySense(Sense sense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long deleteImproper() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @PermitAll
    @Override
    public List<SenseRelation> findRelations(Sense unit, RelationType templateType, Boolean asParent, boolean hideAutoReverse) {
        return senseRelationRepository.findRelations(unit, templateType, asParent, hideAutoReverse);
    }

}
