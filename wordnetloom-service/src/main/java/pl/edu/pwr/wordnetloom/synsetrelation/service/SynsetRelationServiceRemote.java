package pl.edu.pwr.wordnetloom.synsetrelation.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

@Remote
public interface SynsetRelationServiceRemote {

    boolean makeRelation(Synset parent, Synset child, SynsetRelationType rel);

    boolean delete(Synset parent, Synset child, SynsetRelationType relationType);

    void delete(SynsetRelationType relationType);

    void deleteAll();

    List<SynsetRelation> findSubRelations(Synset synset, SynsetRelationType relationType);

    List<SynsetRelation> findUpperRelations(Synset synset, SynsetRelationType relationType);

    void deleteConnection(Synset synset);

    Long findAllRelationsCount();

    Long findRelationTypeUseCount(SynsetRelationType relation);

    void move(SynsetRelationType oldRelation, SynsetRelationType newRelation);

    boolean checkRelationExists(Synset parent, Synset child, SynsetRelationType relation);

    List<SynsetRelationType> findtRelationTypesBySynset(Synset synset);

    int deleteImproper();

    List<SynsetRelation> findRelations(Synset parent, Synset child, SynsetRelationType relation);

    SynsetRelation findRelation(Synset parent, Synset child, SynsetRelationType relation);

    Long findRelationCountBySynset(Synset synset);

    List<Long> findTopPath(Synset synset, Long rtype);

    List<Synset> findTopPathInSynsets(Synset synset, Long rtype);

    List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset);

    List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset);
}
