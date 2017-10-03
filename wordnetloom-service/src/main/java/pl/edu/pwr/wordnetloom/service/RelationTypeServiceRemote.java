package pl.edu.pwr.wordnetloom.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.RelationTest;
import pl.edu.pwr.wordnetloom.model.RelationType;

@Remote
public interface RelationTypeServiceRemote extends DAORemote{
	
	public RelationType dbGet(Long id);
	public boolean isReverseRelation(RelationType[] relations, RelationType test);
	public boolean isReverseRelation(Collection<RelationType> relations, RelationType test);
	@Deprecated
	public List<RelationTest> dbGetTests(RelationType relation);
	public void dbDeleteAll();
	public Long getReverseID(RelationType relationType);
	public RelationType getEagerRelationTypeByID(RelationType rt);
	public RelationType dbGetReverseByRelationType(RelationType relationType);
	RelationType save(RelationType rel);
	List<RelationType> dbGetHighest(RelationArgument argument, PartOfSpeech pos, List<Long> lexicons);
	List<RelationType> dbGetLeafs(RelationArgument argument, List<Long> lexicons);
	List<RelationType> dbFullGetRelationTypes(List<Long> lexicons);
	List<RelationType> dbGetChildren(RelationType relation, List<Long> lexicons);
	void dbDelete(RelationType relation, List<Long> lexicons);
}
