package pl.edu.pwr.wordnetloom.service.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.SynsetRelationDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import pl.edu.pwr.wordnetloom.model.wordnet.SynsetRelation;
import pl.edu.pwr.wordnetloom.service.SynsetRelationServiceRemote;

@Stateless
public class SynsetRelationServiceBean implements SynsetRelationServiceRemote {

    @EJB
    private SynsetRelationDAOLocal local;

    @Override
    public void dbDelete(SynsetRelation rel) {
        local.dbDelete(rel);
    }

    @Override
    public void dbDelete(RelationType relType) {
        local.dbDelete(relType);
    }

    public SynsetRelation dbGet(Long id) {
        return local.dbGet(id);
    }

    @Override
    public boolean dbMakeRelation(Synset parent, Synset child, RelationType rel) {
        return local.dbMakeRelation(parent, child, rel);
    }

    @Override
    public void dbDeleteAll() {
        local.dbDeleteAll();
    }

    @Override
    public List<SynsetRelation> dbGetSubRelations(Synset synset, RelationType templateType, List<Long> lexicons) {
        return local.dbGetSubRelations(synset, templateType, lexicons);
    }

    @Override
    public List<SynsetRelation> dbGetUpperRelations(Synset synset, RelationType templateType, List<Long> lexicons) {
        return local.dbGetUpperRelations(synset, templateType, lexicons);
    }

    @Override
    public void dbDeleteConnection(Synset template) {
        local.dbDeleteConnection(template);
    }

    @Override
    public List<SynsetRelation> dbFullGetRelations() {
        return local.dbFullGetRelations();
    }

    @Override
    public List<SynsetRelation> dbFastGetRelations(RelationType templateType) {
        return local.dbFastGetRelations(templateType);
    }

    @Override
    public int dbGetRelationsCount() {
        return local.dbGetRelationsCount();
    }

    @Override
    public int dbGetRelationUseCount(RelationType relType) {
        return local.dbGetRelationUseCount(relType);
    }

    @Override
    public void dbMove(RelationType oldRel, RelationType newRel) {
        local.dbMove(oldRel, newRel);
    }

    @Override
    public boolean dbRelationExists(Synset parent, Synset child, RelationType rel) {
        return local.dbRelationExists(parent, child, rel);
    }

    @Override
    public List<RelationType> dbGetRelationTypesOfSynset(Synset synset) {
        return local.dbGetRelationTypesOfSynset(synset);
    }

    @Override
    public int dbDeleteImproper() {
        return local.dbDeleteImproper();
    }

    @Override
    public List<SynsetRelation> dbGetRelations(Synset parent, Synset child, RelationType templateType) {
        return local.dbGetRelations(parent, child, templateType);
    }

    @Override
    public List<SynsetRelation> dbGetRelations(Long id) {
        return local.dbGetRelations(id);
    }

    @Override
    public SynsetRelation dbGetRelation(Synset parent, Synset child, RelationType templateType) {
        return local.dbGetRelation(parent, child, templateType);
    }

    @Override
    public int dbGetRelationCountOfSynset(Synset synset) {
        return local.dbGetRelationCountOfSynset(synset);
    }

    @Override
    public List<Long> dbGetTopPath(Synset synset, Long rtype) {
        return local.dbGetTopPath(synset, rtype);
    }

    @Override
    public List<Synset> dbGetTopPathInSynsets(Synset synset, Long rtype) {
        return local.dbGetTopPathInSynsets(synset, rtype);
    }

    @Override
    public List<SynsetRelation> dbGetRelationsSynsetTo(Synset synset) {
        return local.getRelationsSynsetTo(synset);
    }
}
