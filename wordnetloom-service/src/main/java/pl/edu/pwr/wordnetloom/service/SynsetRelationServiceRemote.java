package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetRelation;

@Remote
// FIXME: refactor names
public interface SynsetRelationServiceRemote {
	
	public void dbDelete(SynsetRelation rel);
	public void dbDelete(RelationType relType);
	public SynsetRelation dbGet(Long id);
	public boolean dbMakeRelation(Synset parent, Synset child, RelationType rel);
	public void dbDeleteAll();
	public void dbDeleteConnection(Synset template);
	public List<SynsetRelation> dbFullGetRelations();
	public List<SynsetRelation> dbFastGetRelations(RelationType templateType);
	public int dbGetRelationsCount();
	public int dbGetRelationUseCount(RelationType relType);
	public void dbMove(RelationType oldRel, RelationType newRel);
	public boolean dbRelationExists(Synset parent, Synset child, RelationType rel);
	public List<RelationType> dbGetRelationTypesOfSynset(Synset synset);
	public int dbDeleteImproper();
	public List<SynsetRelation> dbGetRelations(Synset parent,Synset child, RelationType templateType);
	public List<SynsetRelation> dbGetRelations(Long id);
	public SynsetRelation dbGetRelation(Synset parent,Synset child, RelationType templateType);
	public int dbGetRelationCountOfSynset(Synset synset);
	public List<Long> dbGetTopPath(Synset synset, Long rtype);
	public List<Synset> dbGetTopPathInSynsets(Synset synset, Long rtype);
	List<SynsetRelation> dbGetSubRelations(Synset synset,RelationType templateType, List<Long> lexicons);
	List<SynsetRelation> dbGetUpperRelations(Synset synset,	RelationType templateType, List<Long> lexicons);
	List<SynsetRelation> dbGetRelationsSynsetTo(Synset synset);
	
}
