package pl.edu.pwr.wordnetloom.synsetrelation.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetRepository;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.synsetrelation.repository.SynsetRelationRepository;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceLocal;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceRemote;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Local(SynsetRelationServiceLocal.class)
@Remote(SynsetRelationServiceRemote.class)
public class SynsetRelationServiceBean implements SynsetRelationServiceLocal {

    @Inject
    private SynsetRelationRepository synsetRelationRepository;

    @Inject
    private SynsetRepository synsetRepository;

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public boolean makeRelation(Synset parent, Synset child, RelationType rel) {
        SynsetRelation s = new SynsetRelation();
        s.setRelationType(rel);
        s.setParent(parent);
        s.setChild(child);
        synsetRelationRepository.persist(s);
        return true;
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public boolean delete(Synset parent, Synset child, RelationType relationType) {
        return synsetRelationRepository.delete(parent, child, relationType);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void delete(RelationType relationType) {
        synsetRelationRepository.delete(relationType);
//        SynsetRelationType relation = rel.getRelationType();
//
//        if (relation.isAutoReverse()) {
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

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void delete(SynsetRelation relation) {
        synsetRelationRepository.delete(relation);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void deleteAll() {
        synsetRelationRepository.deleteAll();
    }

    @PermitAll
    @Override
    public List<SynsetRelation> findSubRelations(Synset synset, RelationType relationType) {
        return synsetRelationRepository.findSubRelations(synset, relationType);
    }

    @PermitAll
    @Override
    public List<SynsetRelation> findUpperRelations(Synset synset, RelationType relationType) {
        return synsetRelationRepository.findUpperRelations(synset, relationType);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void deleteConnection(Synset synset) {
        synsetRelationRepository.deleteConnection(synset);
    }

    @PermitAll
    @Override
    public Long findAllRelationsCount() {
        return synsetRelationRepository.findAllRelationsCount();
    }

    @PermitAll
    @Override
    public Long findRelationTypeUseCount(RelationType relation) {
        return synsetRelationRepository.findRelationTypeUseCount(relation);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void move(RelationType oldRelation, RelationType newRelation) {
        synsetRelationRepository.move(oldRelation, newRelation);
    }

    @PermitAll
    @Override
    public boolean checkRelationExists(Synset parent, Synset child, RelationType relation) {
        return synsetRelationRepository.checkRelationExists(parent, child, relation);
    }

    @PermitAll
    @Override
    public List<RelationType> findRelationTypesBySynset(Synset synset) {
        return synsetRelationRepository.findtRelationTypesBySynset(synset);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public int deleteImproper() {
        return synsetRelationRepository.deleteImproper();
    }

    @PermitAll
    @Override
    public List<SynsetRelation> findRelations(Synset parent, Synset child, RelationType relation) {
        return synsetRelationRepository.findRelations(parent, child, relation);
    }

    @PermitAll
    @Override
    public SynsetRelation findRelation(Synset parent, Synset child, RelationType relation) {
        return synsetRelationRepository.findRelation(parent, child, relation);
    }

   @PermitAll
   @Override
   public List<DataEntry> findPath(Synset synset, RelationType relationType, List<Long> lexiconsIds){
        List<DataEntry> dataEntries = new ArrayList<>();
        List<Synset> synsets = synsetRelationRepository.findPath(synset, relationType);
        synsets.forEach(syn -> dataEntries.add(synsetRepository.findSynsetDataEntry(syn.getUuid(), lexiconsIds)));
        return dataEntries;
   }

    @PermitAll
    @Override
    public List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        return synsetRelationRepository.findRelationsWhereSynsetIsChild(synset, lexicons, directions);
    }

    @PermitAll
    @Override
    public List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        return synsetRelationRepository.findRelationsWhereSynsetIsParent(synset, lexicons, directions);
    }

    @PermitAll
    @Override
    public List<SynsetRelation> findSimpleRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons) {
        return synsetRelationRepository.findSimpleRelationsWhereSynsetIsParent(synset, lexicons);
    }

    @PermitAll
    @Override
    public List<SynsetRelation> findSimpleRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons) {
        return synsetRelationRepository.findSimpleRelationsWhereSynsetIsChild(synset, lexicons);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void create(Synset parent, Synset child, RelationType relationType){
        synsetRelationRepository.create(parent, child, relationType);
    }
}
