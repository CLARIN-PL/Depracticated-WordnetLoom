package pl.edu.pwr.wordnetloom.relation.service;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.relation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Remote
public interface SenseRelationServiceRemote {

    void dbDelete(SenseRelation rel);

    void dbDelete(RelationType relation);

    void dbDeleteAll();

    SenseRelation dbGet(Long id);

    List<SenseRelation> dbGetSubRelations(Sense sense, RelationType relationType);

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

    List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse);

}
