package pl.edu.pwr.wordnetloom.relationtype.service;

import java.util.Collection;
import java.util.List;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

public interface SenseRelationTypeServiceRemote {

    SenseRelationType findById(Long id);

    boolean isReverseRelation(SenseRelationType[] relations, SenseRelationType test);

    boolean isReverseRelation(Collection<SenseRelationType> relations, SenseRelationType test);

    void deleteAll();

    Long findReverseId(SenseRelationType relationType);

    SenseRelationType findFullRelationType(SenseRelationType rt);

    SenseRelationType findReverseByRelationType(SenseRelationType relationType);

    SenseRelationType save(SenseRelationType rel);

    List<SenseRelationType> findtHighest(List<Long> lexicons);

    List<SenseRelationType> findLeafs(List<Long> lexicons);

    List<SenseRelationType> findFullRelationTypes(List<Long> lexicons);

    List<SenseRelationType> findChildren(SenseRelationType relation, List<Long> lexicons);

    void delete(SenseRelationType relation);
}
