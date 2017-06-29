package pl.edu.pwr.wordnetloom.synsetrelation.service;

import pl.edu.pwr.wordnetloom.relation.service.*;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;

@Remote
public interface RelationTypeServiceRemote {

    RelationType dbGet(Long id);

    boolean isReverseRelation(RelationType[] relations, RelationType test);

    boolean isReverseRelation(Collection<RelationType> relations, RelationType test);

    void dbDeleteAll();

    Long getReverseID(RelationType relationType);

    RelationType getEagerRelationTypeByID(RelationType rt);

    RelationType dbGetReverseByRelationType(RelationType relationType);

    RelationType save(RelationType rel);

    List<RelationType> dbGetHighest(RelationArgument argument, PartOfSpeech pos, List<Long> lexicons);

    List<RelationType> dbGetLeafs(RelationArgument argument, List<Long> lexicons);

    List<RelationType> dbFullGetRelationTypes(List<Long> lexicons);

    List<RelationType> dbGetChildren(RelationType relation, List<Long> lexicons);

    void dbDelete(RelationType relation, List<Long> lexicons);
}
