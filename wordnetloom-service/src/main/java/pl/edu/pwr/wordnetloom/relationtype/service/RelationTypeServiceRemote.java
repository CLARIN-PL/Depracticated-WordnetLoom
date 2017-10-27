package pl.edu.pwr.wordnetloom.relationtype.service;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.Collection;
import java.util.List;

public interface RelationTypeServiceRemote {

    RelationType findById(Long id);

    Long findReverseId(RelationType relationType);

    RelationType findFullRelationType(RelationType rt);

    RelationType findReverseByRelationType(RelationType relationType);

    RelationType save(RelationType rel);

    List<RelationType> findtHighest(List<Long> lexicons);

    List<RelationType> findLeafs(List<Long> lexicons);

    List<RelationType> findFullRelationTypes(List<Long> lexicons);

    List<RelationType> findChildren(RelationType relation, List<Long> lexicons);

    void delete(RelationType relation);

    void deleteAll(RelationType type);

    boolean isReverseRelation(RelationType[] relations, RelationType test);

    boolean isReverseRelation(Collection<RelationType> relations, RelationType test);
}
