package pl.edu.pwr.wordnetloom.relationtype.service;

import java.util.Collection;
import java.util.List;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

public interface SynsetRelationTypeServiceRemote {

    SynsetRelationType findById(Long id);

    boolean isReverseRelation(SynsetRelationType[] relations, SynsetRelationType test);

    boolean isReverseRelation(Collection<SynsetRelationType> relations, SynsetRelationType test);

    void deleteAll();

    Long findReverseId(SynsetRelationType relationType);

    SynsetRelationType findFullRelationType(SynsetRelationType rt);

    SynsetRelationType findReverseByRelationType(SynsetRelationType relationType);

    SynsetRelationType save(SynsetRelationType rel);

    List<SynsetRelationType> findtHighest(List<Long> lexicons);

    List<SynsetRelationType> findLeafs(List<Long> lexicons);

    List<SynsetRelationType> findFullRelationTypes(List<Long> lexicons);

    List<SynsetRelationType> findChildren(SynsetRelationType relation, List<Long> lexicons);

    void delete(SynsetRelationType relation);
}
