package pl.edu.pwr.wordnetloom.relationtype.service;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.Collection;
import java.util.List;

public interface RelationTypeServiceRemote {

    RelationType findById(Long id);

    Long findReverseId(Long relationTypeId);

    RelationType findReverseByRelationType(Long relationTypeId);

    RelationType save(RelationType rel);

    List<RelationType> findHighest(RelationArgument argument);

    List<RelationType> findLeafs(RelationArgument argument);

    List<RelationType> findChildren(Long relationTypeId);

    List<RelationType> findAll();

    void delete(RelationType relation);

    void deleteAll(RelationType type);

    boolean isReverseRelation(Collection<RelationType> relations, RelationType test);

    RelationType findByIdWithDependencies(Long id);

    RelationType findByName(String name);
}
