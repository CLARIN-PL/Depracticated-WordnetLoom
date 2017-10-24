package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.dto.RelationDTO;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class LexicalRelationDAOBean extends DAOBean implements LexicalRelationDAOLocal {

    @EJB
    private DAOLocal local;

    @Override
    public void dbDelete(SenseRelation rel) {
        local.deleteObject(SenseRelation.class, rel.getId());
    }

    @Override
    public void dbDelete(RelationType relation) {
        local.getEM().createNamedQuery("LexicalRelation.dbDelete")
                .setParameter("relation", relation)
                .executeUpdate();

    }

    @Override
    public void dbDeleteAll() {
        local.getEM().createNamedQuery("LexicalRelation.dbDeleteAll", SenseRelation.class)
                .executeUpdate();
    }

    public SenseRelation dbGet(Long id) {
        List<SenseRelation> list = local.getEM()
                .createNamedQuery("SenseRelation.findSenseRelationByID", SenseRelation.class)
                .setParameter("id", id)
                .getResultList();
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    @Override
    public List<SenseRelation> dbGetSubRelations(Sense sense, RelationType relationType) {
        if (relationType == null)
            return local.getEM().createNamedQuery("LexicalRelation.dbGetSubRelations", SenseRelation.class)
                    .setParameter("parent", sense)
                    .getResultList();
        return local.getEM().createNamedQuery("LexicalRelation.dbGetSubRelationsWithRelation", SenseRelation.class)
                .setParameter("parent", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }


    @Override
    public Map<Long, Set<RelationDTO>> dbGetSubRelations(Long senseId) {
        List<RelationDTO> list = local.getEM().createQuery("SELECT NEW pl.edu.pwr.wordnetloom.dto.RelationDTO(s.senseTo.id, s.relation.id, CONCAT(s.senseTo.lemma.word, ' ', s.senseTo.senseNumber), s.senseTo.partOfSpeech.id) FROM SenseRelation s WHERE s.senseFrom.id  = :id", RelationDTO.class)
                .setParameter("id", senseId)
                .getResultList();
        return list.stream()
                .collect(
                        Collectors.groupingBy(
                                RelationDTO::getRelationTypeId,
                                Collectors.mapping(i -> i, Collectors.toSet())));
    }

    @Override
    public Map<Long, Set<RelationDTO>> dbGetUpperRelations(Long senseId) {
        List<RelationDTO> list = local.getEM().createQuery("SELECT NEW pl.edu.pwr.wordnetloom.dto.RelationDTO(s.senseFrom.id, s.relation.id,  CONCAT(s.senseFrom.lemma.word, ' ', s.senseFrom.senseNumber), s.senseFrom.partOfSpeech.id) FROM SenseRelation s WHERE s.senseTo.id  = :id", RelationDTO.class)
                .setParameter("id", senseId)
                .getResultList();
        return list.stream()
                .collect(
                        Collectors.groupingBy(
                                RelationDTO::getRelationTypeId,
                                Collectors.mapping(i -> i, Collectors.toSet())));
    }

    @Override
    public List<SenseRelation> dbFastGetRelations(RelationType relationType) {
        if (relationType == null)
            return local.getEM().createNamedQuery("LexicalRelation.dbFastGetRelations", SenseRelation.class)
                    .getResultList();
        return local.getEM().createNamedQuery("LexicalRelation.dbFastGetRelationsWithRelation", SenseRelation.class)
                .setParameter("relation", relationType)
                .getResultList();
    }

    @Override
    public Set<Long> dbSelftRelations() {
        List<SenseRelation> sr = local.getEM().createNamedQuery("LexicalRelation.dbSelftRelations", SenseRelation.class)
                .getResultList();
        Set<Long> ids = new HashSet<>();
        for (SenseRelation s : sr)
            ids.add(s.getSenseFrom().getId());
        return ids;
    }

    @Override
    public List<SenseRelation> dbGetUpperRelations(Sense sense, RelationType relationType) {
        if (relationType == null)
            return local.getEM().createNamedQuery("LexicalRelation.dbGetUpperRelations", SenseRelation.class)
                    .setParameter("child", sense)
                    .getResultList();
        return local.getEM().createNamedQuery("LexicalRelation.dbGetUpperRelationsWithRelation", SenseRelation.class)
                .setParameter("child", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }


    @Override
    public List<SenseRelation> dbGetFullRelations(Sense parent) {
        return dbGetSubRelations(parent, null);
    }

    @Override
    public List<SenseRelation> dbGetRelations(Sense parent, Sense child, RelationType relationType) {
        if (relationType == null)
            return local.getEM().createNamedQuery("LexicalRelation.dbGetRelations", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getResultList();
        return local.getEM().createNamedQuery("LexicalRelation.dbGetRelationsWithRelation", SenseRelation.class)
                .setParameter("parent", parent)
                .setParameter("child", child)
                .setParameter("relation", relationType)
                .getResultList();
    }

    @Override
    public SenseRelation dbGetRelation(Sense parent, Sense child, RelationType relationType) {
        List<SenseRelation> list = new ArrayList<>();
        if (relationType == null)
            list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelations", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getResultList();
        else
            list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationsWithRelation", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .setParameter("relation", relationType)
                    .getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return null;
        return list.get(0);
    }

    @Override
    public boolean dbMakeRelation(Sense parent, Sense child, RelationType relation) {
        SenseRelation rel = new SenseRelation();

        rel.setRelation(relation);
        rel.setSenseFrom(parent);
        rel.setSenseTo(child);

        local.persistObject(rel);

        return true;
    }

    @Override
    public void dbDeleteConnection(Sense sense) {
        local.getEM().createNamedQuery("LexicalRelation.dbDeleteConnection")
                .setParameter("senseID", sense.getId())
                .executeUpdate();
    }

    @Override
    public List<SenseRelation> dbFullGetRelations() {
        return dbFastGetRelations(null);
    }

    @Override
    public int dbGetRelationsCount() {
        List<Long> list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationsCount", Long.class).getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return 0;
        return list.get(0).intValue();
    }

    @Override
    public int dbGetRelationUseCount(RelationType relation) {
        List<Long> list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationUseCount", Long.class)
                .setParameter("relation", relation)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return 0;
        return list.get(0).intValue();
    }

    @Override
    public void dbMove(RelationType oldRelation, RelationType newRelation) {
        Query query = local.getEM().createQuery("UPDATE SenseRelation s SET s.relation = :newRelation WHERE s.relation = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    @Override
    public boolean dbRelationExists(Sense parent, Sense child, RelationType relationType) {
        return dbGetRelations(parent, child, relationType).size() > 0;
    }

    @Override
    public List<RelationType> dbGetRelationTypesOfUnit(Sense sense) {
        return local.getEM().createNamedQuery("LexicalRelation.dbGetRelationTypesOfUnit", RelationType.class)
                .setParameter("sense", sense)
                .getResultList();
    }

    @Override
    public int dbGetRelationCountOfUnit(Sense sense) {
        List<Long> list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationCountOfUnit", Long.class)
                .setParameter("sense", sense)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return 0;
        return list.get(0).intValue();
    }

    @Override
    public int dbDeleteImproper() {
        List<Sense> senses = local.getEM().createNamedQuery("Sense.findAll", Sense.class).getResultList();
        return local.getEM().createNamedQuery("LexicalRelation.dbDeleteImproper", SenseRelation.class)
                .setParameter("senses", senses)
                .executeUpdate();
    }

}
