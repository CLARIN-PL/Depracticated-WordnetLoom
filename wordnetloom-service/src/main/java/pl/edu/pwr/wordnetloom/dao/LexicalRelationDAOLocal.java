package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.dto.RelationDTO;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface LexicalRelationDAOLocal extends DAOLocal {

    void dbDelete(SenseRelation rel);

    void dbDelete(RelationType relation);

    void dbDeleteAll();

    SenseRelation dbGet(Long id);

    List<SenseRelation> dbGetSubRelations(Sense sense, RelationType relationType);

    Map<String, Set<RelationDTO>> dbGetSubRelations(Long senseId);

    Map<String, Set<RelationDTO>> dbGetUpperRelations(Long senseId);

    List<SenseRelation> dbFastGetRelations(RelationType relationType);

    Set<Long> dbSelftRelations();

    List<SenseRelation> dbGetUpperRelations(Sense sense, RelationType relationType);


    List<SenseRelation> dbGetFullRelations(Sense parent);

    List<SenseRelation> dbGetRelations(Sense parent, Sense child, RelationType relationType);

    SenseRelation dbGetRelation(Sense parent, Sense child, RelationType relationType);

    boolean dbMakeRelation(Sense parent, Sense child, RelationType relation);

    void dbDeleteConnection(Sense sense);

    List<SenseRelation> dbFullGetRelations();

    int dbGetRelationsCount();

    int dbGetRelationUseCount(RelationType relation);

    void dbMove(RelationType oldRelation, RelationType newRelation);

    boolean dbRelationExists(Sense parent, Sense child, RelationType relationType);

    List<RelationType> dbGetRelationTypesOfUnit(Sense sense);

    int dbGetRelationCountOfUnit(Sense sense);

    int dbDeleteImproper();

}
