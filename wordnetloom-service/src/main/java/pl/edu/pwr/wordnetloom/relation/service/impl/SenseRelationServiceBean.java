package pl.edu.pwr.wordnetloom.relation.service.impl;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.relation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.relation.repository.SenseRelationRepository;

@Stateless
public class SenseRelationServiceBean implements SenseRelationServiceLocal {

    @Inject
    private SenseRelationRepository senseRelationRepository;

    @Override
    public void dbDelete(SenseRelation rel) {
        senseRelationRepository.dbDelete(rel);
    }

    @Override
    public void dbDelete(RelationType relation) {
        senseRelationRepository.dbDelete(relation);
    }

    @Override
    public void dbDeleteAll() {
        senseRelationRepository.dbDeleteAll();
    }

    @Override
    public List<SenseRelation> dbGetSubRelations(Sense sense,
            RelationType relationType) {
        return senseRelationRepository.dbGetSubRelations(sense, relationType);
    }

    @Override
    public List<SenseRelation> dbFastGetRelations(RelationType relationType) {
        return senseRelationRepository.dbFastGetRelations(relationType);
    }

    @Override
    public Set<Long> dbSelftRelations() {
        return senseRelationRepository.dbSelftRelations();
    }

    @Override
    public List<SenseRelation> dbGetUpperRelations(Sense sense,
            RelationType relationType) {
        return senseRelationRepository.dbGetUpperRelations(sense, relationType);
    }

    @Override
    public List<SenseRelation> dbGetFullRelations(Sense parent) {
        return senseRelationRepository.dbGetFullRelations(parent);
    }

    @Override
    public List<SenseRelation> dbGetRelations(Sense parent, Sense child,
            RelationType relationType) {
        return senseRelationRepository.dbGetRelations(parent, child, relationType);
    }

    @Override
    public SenseRelation dbGetRelation(Sense parent, Sense child,
            RelationType relationType) {
        return senseRelationRepository.dbGetRelation(parent, child, relationType);
    }

    @Override
    public boolean dbMakeRelation(Sense parent, Sense child, RelationType relation) {
        return senseRelationRepository.dbMakeRelation(parent, child, relation);
    }

    @Override
    public void dbDeleteConnection(Sense sense) {
        senseRelationRepository.dbDeleteConnection(sense);
    }

    @Override
    public List<SenseRelation> dbFullGetRelations() {
        return senseRelationRepository.dbFullGetRelations();
    }

    @Override
    public int dbGetRelationsCount() {
        return senseRelationRepository.dbGetRelationsCount();
    }

    @Override
    public int dbGetRelationUseCount(RelationType relation) {
        return senseRelationRepository.dbGetRelationUseCount(relation);
    }

    @Override
    public void dbMove(RelationType oldRelation, RelationType newRelation) {
        senseRelationRepository.dbMove(oldRelation, newRelation);
    }

    @Override
    public boolean dbRelationExists(Sense parent, Sense child,
            RelationType relationType) {
        return senseRelationRepository.dbRelationExists(parent, child, relationType);
    }

    @Override
    public List<RelationType> dbGetRelationTypesOfUnit(Sense sense) {
        return senseRelationRepository.dbGetRelationTypesOfUnit(sense);
    }

    @Override
    public int dbGetRelationCountOfUnit(Sense sense) {
        return senseRelationRepository.dbGetRelationCountOfUnit(sense);
    }

    @Override
    public int dbDeleteImproper() {
        return senseRelationRepository.dbDeleteImproper();
    }

    @Override
    public SenseRelation dbGet(Long id) {
        return senseRelationRepository.dbGet(id);
    }

    @Override
    public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse) {
        return senseRelationRepository.dbGetRelations(unit, templateType, unitType, hideAutoReverse);
    }
}
