package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.RelationTest;
import pl.edu.pwr.wordnetloom.model.RelationType;

import javax.ejb.Local;
import java.util.Collection;
import java.util.List;

@Local
public interface RelationTypeDAOLocal {

    RelationType save(RelationType rel);

    RelationType dbGet(Long id);

    boolean isReverseRelation(RelationType[] relations, RelationType test);

    boolean isReverseRelation(Collection<RelationType> relations, RelationType test);

    @Deprecated
    List<RelationTest> dbGetTests(RelationType relation);

    void dbDeleteAll();

    Long getReverseID(RelationType relationType);

    RelationType getEagerRelationTypeByID(RelationType rt);

    RelationType dbGetReverseByRelationType(RelationType relationType);

    List<RelationType> dbGetHighest(RelationArgument argument, PartOfSpeech pos, List<Long> lexicons);

    List<RelationType> dbGetLeafs(RelationArgument argument, List<Long> lexicons);

    List<RelationType> dbGetChildren(RelationType relation, List<Long> lexicons);

    void dbDelete(RelationType relation, List<Long> lexicons);

    List<RelationType> dbFullGetRelationTypes(List<Long> lexicons);

    List<RelationType> dbGetChildrenFull(RelationType relation, List<Long> lexicons);
}
