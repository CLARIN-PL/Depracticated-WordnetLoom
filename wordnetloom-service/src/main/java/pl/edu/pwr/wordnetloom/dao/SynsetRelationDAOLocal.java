package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.relation.model.SynsetRelation;

@Local
public interface SynsetRelationDAOLocal {

    void dbDelete(SynsetRelation rel);

    boolean dbDelete(Synset parent, Synset child, RelationType relation);

    void dbDelete(RelationType relType);

    SynsetRelation dbGet(Long id);

    boolean dbMakeRelation(Synset parent, Synset child, RelationType rel);

    void dbDeleteAll();

    List<SynsetRelation> dbGetSubRelations(Synset synset, RelationType templateType, List<Long> lexicons);

    List<SynsetRelation> dbGetUpperRelations(Synset synset, RelationType templateType, List<Long> lexicons);

    void dbDeleteConnection(Synset template);

    List<SynsetRelation> dbFullGetRelations();

    List<SynsetRelation> dbFastGetRelations(RelationType templateType);

    int dbGetRelationsCount();

    int dbGetRelationUseCount(RelationType relType);

    void dbMove(RelationType oldRel, RelationType newRel);

    boolean dbRelationExists(Synset parent, Synset child, RelationType rel);

    List<RelationType> dbGetRelationTypesOfSynset(Synset synset);

    int dbDeleteImproper();

    List<SynsetRelation> dbGetRelations(Synset parent, Synset child, RelationType templateType);

    List<SynsetRelation> dbGetRelations(Long id);

    SynsetRelation dbGetRelation(Synset parent, Synset child, RelationType templateType);

    int dbGetRelationCountOfSynset(Synset synset);

    List<Long> dbGetTopPath(Synset synset, Long rtype);

    List<Synset> dbGetTopPathInSynsets(Synset synset, Long rtype);

    List<SynsetRelation> getRelatedRelations(Synset synset, List<Long> lexicons);

    List<SynsetRelation> getRelatedRelations(Set<Long> synsetIDs);

    List<SynsetRelation> getRelationsSynsetTo(Synset synset);

}
