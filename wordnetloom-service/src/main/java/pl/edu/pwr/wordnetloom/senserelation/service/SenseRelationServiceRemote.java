package pl.edu.pwr.wordnetloom.senserelation.service;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationType;

@Remote
public interface SenseRelationServiceRemote {

    void delete(SenseRelation rel);

    void delete(SenseRelationType relation);

    void deleteAll();

    SenseRelation findById(Long id);

    List<SenseRelation> findSubRelations(Sense sense, SenseRelationType relationType);

    List<SenseRelation> findRelations(SenseRelationType relationType);

    Set<Long> findSelfRelations();

    List<SenseRelation> findUpperRelations(Sense sense, SenseRelationType relationType);

    List<SenseRelation> findFullRelations(Sense parent);

    List<SenseRelation> findRelations(Sense parent, Sense child, SenseRelationType relationType);

    SenseRelation findRelation(Sense parent, Sense child, SenseRelationType relationType);

    boolean makeRelation(Sense parent, Sense child, SenseRelationType relation);

    void deleteConnection(Sense sense);

    List<SenseRelation> dbFullGetRelations();

    Long findRelationsCount();

    Long findRelationUseCount(SenseRelationType relation);

    void move(SenseRelationType oldRelation, SenseRelationType newRelation);

    boolean relationExists(Sense parent, Sense child, SenseRelationType relationType);

    List<SenseRelationType> findSenseRelationTypesBySense(Sense sense);

    Long relationCountBySense(Sense sense);

    Long deleteImproper();

    List<SenseRelation> findRelations(Sense unit, SenseRelationType templateType, Boolean asParent, boolean hideAutoReverse);

}
