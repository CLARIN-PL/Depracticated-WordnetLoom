package pl.edu.pwr.wordnetloom.senserelation.service.impl;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationRepository;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceLocal;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Stateless
@Remote(SenseRelationServiceRemote.class)
@Local(SenseRelationServiceLocal.class)
public class SenseRelationServiceBean implements SenseRelationServiceLocal {

    @Inject
    private SenseRelationRepository senseRelationRepository;

    @Override
    public void delete(SenseRelation rel) {
        senseRelationRepository.delete(rel);
    }

    @Override
    public void delete(RelationType relation) {
        senseRelationRepository.delete(relation);
    }

    @Override
    public void deleteAll() {
        senseRelationRepository.deleteAll();
    }

    @Override
    public SenseRelation findById(Long id) {
        return senseRelationRepository.findById(id);
    }

    @Override
    public List<SenseRelation> findSubRelations(Sense sense, RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

    @Override
    public boolean makeRelation(Sense parent, Sense child, RelationType relation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

    @Override
    public boolean relationExists(Sense parent, Sense child, RelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public List<SenseRelation> findRelations(Sense unit, RelationType templateType, Boolean asParent, boolean hideAutoReverse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
