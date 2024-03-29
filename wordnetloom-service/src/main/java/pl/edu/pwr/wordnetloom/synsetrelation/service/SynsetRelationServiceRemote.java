package pl.edu.pwr.wordnetloom.synsetrelation.service;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.util.List;

public interface SynsetRelationServiceRemote {

    boolean makeRelation(Synset parent, Synset child, RelationType rel);

    boolean delete(Synset parent, Synset child, RelationType relationType);

    void delete(RelationType relationType);

    void delete(SynsetRelation relation);

    void deleteAll();

    List<SynsetRelation> findSubRelations(Synset synset, RelationType relationType);

    List<SynsetRelation> findUpperRelations(Synset synset, RelationType relationType);

    void deleteConnection(Synset synset);

    Long findAllRelationsCount();

    Long findRelationTypeUseCount(RelationType relation);

    void move(RelationType oldRelation, RelationType newRelation);

    boolean checkRelationExists(Synset parent, Synset child, RelationType relation);

    List<RelationType> findRelationTypesBySynset(Synset synset);

    int deleteImproper();

    List<SynsetRelation> findRelations(Synset parent, Synset child, RelationType relation);

    SynsetRelation findRelation(Synset parent, Synset child, RelationType relation);



    List<DataEntry> findPath(Synset synset, RelationType relationType, List<Long> lexiconsIds);

    List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons, NodeDirection[] directions);

    List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons, NodeDirection[] directions);

    List<SynsetRelation> findSimpleRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons);

    List<SynsetRelation> findSimpleRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons);

    void create(Synset parent, Synset child, RelationType relationType);
}
