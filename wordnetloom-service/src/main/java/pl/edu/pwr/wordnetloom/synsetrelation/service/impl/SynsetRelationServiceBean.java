package pl.edu.pwr.wordnetloom.synsetrelation.service.impl;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.synsetrelation.repository.SynsetRelationRepository;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceLocal;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceRemote;

@Stateless
@Local(SynsetRelationServiceLocal.class)
@Remote(SynsetRelationServiceRemote.class)
public class SynsetRelationServiceBean implements SynsetRelationServiceLocal {

    @Inject
    private SynsetRelationRepository synsetRelationRepository;

    @Override
    public boolean makeRelation(Synset parent, Synset child, SynsetRelationType rel) {
        SynsetRelation s = new SynsetRelation();
        s.setRelationType(rel);
        s.setParent(parent);
        s.setChild(child);
        synsetRelationRepository.persist(s);
        return true;
    }

    @Override
    public boolean delete(Synset parent, Synset child, SynsetRelationType relationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(SynsetRelationType relationType) {
//        SynsetRelationType relation = rel.getRelationType();
//
//        if (relation.getAutoReverse()) {
//            IRelationType reverse = relationType.dbGetReverseByRelationType(relation);
//            dbDelete(rel.getSynsetTo(), rel.getSynsetFrom(), reverse);
//        }
//
//        try {
//            local.deleteObject(SynsetRelation.class, rel.getId());
//        } catch (Exception e) {
//            System.err.println(this.getClass() + ": WARRNING: " + e.getLocalizedMessage());
//        }
//        synsetRelationRepository.dbDelete(rel);
    }

    @Override
    public void deleteAll() {
        synsetRelationRepository.deleteAll();
    }

    @Override
    public List<SynsetRelation> findSubRelations(Synset synset, SynsetRelationType relationType) {
        return synsetRelationRepository.findSubRelations(synset, relationType);
    }

    @Override
    public List<SynsetRelation> findUpperRelations(Synset synset, SynsetRelationType relationType) {
        return synsetRelationRepository.findUpperRelations(synset, relationType);
    }

    @Override
    public void deleteConnection(Synset synset) {
        synsetRelationRepository.deleteConnection(synset);
    }

    @Override
    public Long findAllRelationsCount() {
        return synsetRelationRepository.findAllRelationsCount();
    }

    @Override
    public Long findRelationTypeUseCount(SynsetRelationType relation) {
        return synsetRelationRepository.findRelationTypeUseCount(relation);
    }

    @Override
    public void move(SynsetRelationType oldRelation, SynsetRelationType newRelation) {
        synsetRelationRepository.move(oldRelation, newRelation);
    }

    @Override
    public boolean checkRelationExists(Synset parent, Synset child, SynsetRelationType relation) {
        return synsetRelationRepository.checkRelationExists(parent, child, relation);
    }

    @Override
    public List<SynsetRelationType> findtRelationTypesBySynset(Synset synset) {
        return synsetRelationRepository.findtRelationTypesBySynset(synset);
    }

    @Override
    public int deleteImproper() {
        return synsetRelationRepository.deleteImproper();
    }

    @Override
    public List<SynsetRelation> findRelations(Synset parent, Synset child, SynsetRelationType relation) {
        return synsetRelationRepository.findRelations(parent, child, relation);
    }

    @Override
    public SynsetRelation findRelation(Synset parent, Synset child, SynsetRelationType relation) {
        return synsetRelationRepository.findRelation(parent, child, relation);
    }

    @Override
    public Long findRelationCountBySynset(Synset synset) {
        return synsetRelationRepository.findRelationCountBySynset(synset);
    }

    @Override
    public List<Long> findTopPath(Synset synset, Long rtype) {
        return synsetRelationRepository.findTopPath(synset, rtype);
    }

    @Override
    public List<Synset> findTopPathInSynsets(Synset synset, Long rtype) {
        return synsetRelationRepository.findTopPathInSynsets(synset, rtype);
    }

    @Override
    public List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset) {
        return synsetRelationRepository.findRelationsWhereSynsetIsChild(synset);
    }

    @Override
    public List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset) {
        return synsetRelationRepository.findRelationsWhereSynsetIsParent(synset);
    }
}
