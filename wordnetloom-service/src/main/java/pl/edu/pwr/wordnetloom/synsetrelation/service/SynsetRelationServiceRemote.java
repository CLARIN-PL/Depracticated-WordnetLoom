package pl.edu.pwr.wordnetloom.synsetrelation.service;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.util.List;

public interface SynsetRelationServiceRemote {

    boolean makeRelation(Synset parent, Synset child, RelationType rel);

    boolean delete(Synset parent, Synset child, RelationType relationType);

    void delete(RelationType relationType);

    void deleteAll();

    List<SynsetRelation> findSubRelations(Synset synset, RelationType relationType);

    List<SynsetRelation> findUpperRelations(Synset synset, RelationType relationType);

    void deleteConnection(Synset synset);

    Long findAllRelationsCount();

    Long findRelationTypeUseCount(RelationType relation);

    void move(RelationType oldRelation, RelationType newRelation);

    boolean checkRelationExists(Synset parent, Synset child, RelationType relation);

    List<RelationType> findtRelationTypesBySynset(Synset synset);

    int deleteImproper();

    List<SynsetRelation> findRelations(Synset parent, Synset child, RelationType relation);

    SynsetRelation findRelation(Synset parent, Synset child, RelationType relation);

    Long findRelationCountBySynset(Synset synset);

    List<Long> findTopPath(Synset synset, Long rtype);

    List<Synset> findTopPathInSynsets(Synset synset, Long rtype);

    List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset);

    List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset);
}
