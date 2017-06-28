package pl.edu.pwr.wordnetloom.relation.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.relation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.relation.repository.SynsetRelationRepository;
import pl.edu.pwr.wordnetloom.relation.service.SynsetRelationServiceLocal;

@Stateless
public class SynsetRelationServiceBean implements SynsetRelationServiceLocal {

    @Inject
    private SynsetRelationRepository synsetRelationRepository;

    @Override
    public void dbDelete(SynsetRelation rel) {
        synsetRelationRepository.dbDelete(rel);
    }

    @Override
    public void dbDelete(RelationType relType) {
        synsetRelationRepository.dbDelete(relType);
    }

    public SynsetRelation dbGet(Long id) {
        return synsetRelationRepository.dbGet(id);
    }

    @Override
    public boolean dbMakeRelation(Synset parent, Synset child, RelationType rel) {
        return synsetRelationRepository.dbMakeRelation(parent, child, rel);
    }

    @Override
    public void dbDeleteAll() {
        synsetRelationRepository.dbDeleteAll();
    }

    @Override
    public List<SynsetRelation> dbGetSubRelations(Synset synset, RelationType templateType, List<Long> lexicons) {
        return synsetRelationRepository.dbGetSubRelations(synset, templateType, lexicons);
    }

    @Override
    public List<SynsetRelation> dbGetUpperRelations(Synset synset, RelationType templateType, List<Long> lexicons) {
        return synsetRelationRepository.dbGetUpperRelations(synset, templateType, lexicons);
    }

    @Override
    public void dbDeleteConnection(Synset template) {
        synsetRelationRepository.dbDeleteConnection(template);
    }

    @Override
    public List<SynsetRelation> dbFullGetRelations() {
        return synsetRelationRepository.dbFullGetRelations();
    }

    @Override
    public List<SynsetRelation> dbFastGetRelations(RelationType templateType) {
        return synsetRelationRepository.dbFastGetRelations(templateType);
    }

    @Override
    public int dbGetRelationsCount() {
        return synsetRelationRepository.dbGetRelationsCount();
    }

    @Override
    public int dbGetRelationUseCount(RelationType relType) {
        return synsetRelationRepository.dbGetRelationUseCount(relType);
    }

    @Override
    public void dbMove(RelationType oldRel, RelationType newRel) {
        synsetRelationRepository.dbMove(oldRel, newRel);
    }

    @Override
    public boolean dbRelationExists(Synset parent, Synset child, RelationType rel) {
        return synsetRelationRepository.dbRelationExists(parent, child, rel);
    }

    @Override
    public List<RelationType> dbGetRelationTypesOfSynset(Synset synset) {
        return synsetRelationRepository.dbGetRelationTypesOfSynset(synset);
    }

    @Override
    public int dbDeleteImproper() {
        return synsetRelationRepository.dbDeleteImproper();
    }

    @Override
    public List<SynsetRelation> dbGetRelations(Synset parent, Synset child, RelationType templateType) {
        return synsetRelationRepository.dbGetRelations(parent, child, templateType);
    }

    @Override
    public List<SynsetRelation> dbGetRelations(Long id) {
        return synsetRelationRepository.dbGetRelations(id);
    }

    @Override
    public SynsetRelation dbGetRelation(Synset parent, Synset child, RelationType templateType) {
        return synsetRelationRepository.dbGetRelation(parent, child, templateType);
    }

    @Override
    public int dbGetRelationCountOfSynset(Synset synset) {
        return synsetRelationRepository.dbGetRelationCountOfSynset(synset);
    }

    @Override
    public List<Long> dbGetTopPath(Synset synset, Long rtype) {
        return synsetRelationRepository.dbGetTopPath(synset, rtype);
    }

    @Override
    public List<Synset> dbGetTopPathInSynsets(Synset synset, Long rtype) {
        return synsetRelationRepository.dbGetTopPathInSynsets(synset, rtype);
    }

    @Override
    public List<SynsetRelation> dbGetRelationsSynsetTo(Synset synset) {
        return synsetRelationRepository.getRelationsSynsetTo(synset);
    }
}
