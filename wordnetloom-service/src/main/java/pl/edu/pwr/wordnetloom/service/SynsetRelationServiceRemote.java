package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetRelation;

@Remote
public interface SynsetRelationServiceRemote {
	
	void dbDelete(SynsetRelation rel);
	void dbDelete(RelationType relType);
	SynsetRelation dbGet(Long id);
	boolean dbMakeRelation(Synset parent, Synset child, RelationType rel);
	void dbDeleteAll();
	void dbDeleteConnection(Synset template);
	List<SynsetRelation> dbFullGetRelations();
	List<SynsetRelation> dbFastGetRelations(RelationType templateType);
	int dbGetRelationsCount();
	int dbGetRelationUseCount(RelationType relType);
	void dbMove(RelationType oldRel, RelationType newRel);
	boolean dbRelationExists(Synset parent, Synset child, RelationType rel);
	List<RelationType> dbGetRelationTypesOfSynset(Synset synset);
	int dbDeleteImproper();
	List<SynsetRelation> dbGetRelations(Synset parent,Synset child, RelationType templateType);
	List<SynsetRelation> dbGetRelations(Long id);
	SynsetRelation dbGetRelation(Synset parent,Synset child, RelationType templateType);
	int dbGetRelationCountOfSynset(Synset synset);
	List<Long> dbGetTopPath(Synset synset, Long rtype);
	List<Synset> dbGetTopPathInSynsets(Synset synset, Long rtype);
	List<SynsetRelation> dbGetSubRelations(Synset synset,RelationType templateType, List<Long> lexicons);
	List<SynsetRelation> dbGetUpperRelations(Synset synset,	RelationType templateType, List<Long> lexicons);
	List<SynsetRelation> dbGetRelationsSynsetTo(Synset synset);
	
}
