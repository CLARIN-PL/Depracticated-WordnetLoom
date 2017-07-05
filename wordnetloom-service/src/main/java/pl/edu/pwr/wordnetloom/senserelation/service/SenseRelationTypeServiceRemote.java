package pl.edu.pwr.wordnetloom.senserelation.service;

import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Remote
public interface SenseRelationTypeServiceRemote {

    SenseRelationType findById(Long id);

    boolean isReverseRelation(SenseRelationType[] relations, SenseRelationType test);

    boolean isReverseRelation(Collection<SenseRelationType> relations, SenseRelationType test);

    void deleteAll();

    Long findReverseId(SenseRelationType relationType);

    SenseRelationType getEagerRelationTypeByID(SenseRelationType rt);

    SenseRelationType findReverseByRelationType(SenseRelationType relationType);

    SenseRelationType save(SenseRelationType rel);

    List<SenseRelationType> findtHighest(List<Long> lexicons);

    List<SenseRelationType> findLeafs(List<Long> lexicons);

    List<SenseRelationType> dbFullGetRelationTypes(List<Long> lexicons);

    List<SenseRelationType> findChildren(SenseRelationType relation, List<Long> lexicons);

    void delete(SenseRelationType relation, List<Long> lexicons);
}
