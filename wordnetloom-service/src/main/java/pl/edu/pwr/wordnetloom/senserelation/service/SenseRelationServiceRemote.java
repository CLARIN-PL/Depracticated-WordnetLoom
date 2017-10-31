package pl.edu.pwr.wordnetloom.senserelation.service;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;

import java.util.List;
import java.util.Set;

public interface SenseRelationServiceRemote {

    void delete(SenseRelation rel);

    void delete(RelationType relation);

    void deleteAll();

    SenseRelation findById(Long id);

    List<SenseRelation> findSubRelations(Sense sense, RelationType relationType);

    List<SenseRelation> findRelations(RelationType relationType);

    Set<Long> findSelfRelations();

    List<SenseRelation> findUpperRelations(Sense sense, RelationType relationType);

    List<SenseRelation> findFullRelations(Sense parent);

    List<SenseRelation> findRelations(Sense parent, Sense child, RelationType relationType);

    SenseRelation findRelation(Sense parent, Sense child, RelationType relationType);

    boolean makeRelation(Sense parent, Sense child, RelationType relation);

    void deleteConnection(Sense sense);

    List<SenseRelation> dbFullGetRelations();

    Long findRelationsCount();

    Long findRelationUseCount(RelationType relation);

    void move(RelationType oldRelation, RelationType newRelation);

    boolean relationExists(Sense parent, Sense child, RelationType relationType);

    List<RelationType> findSenseRelationTypesBySense(Sense sense);

    Long relationCountBySense(Sense sense);

    Long deleteImproper();

    List<SenseRelation> findRelations(Sense unit, RelationType templateType, Boolean asParent, boolean hideAutoReverse);

}
