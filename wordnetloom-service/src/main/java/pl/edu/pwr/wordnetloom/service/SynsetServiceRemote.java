package pl.edu.pwr.wordnetloom.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.dto.DataEntry;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.Synset;

@Remote 
public interface SynsetServiceRemote extends DAORemote {

	public boolean dbDelete(Synset synset);
	public List<Synset> dbGetUnits(List<Synset> synsets);
	public void dbSaveConnections(HashMap<Synset, List<Sense>> map);
	public List<Synset> dbFullGet(Long[] synset_ids);
	public int dbDeleteEmpty();
	public List<Synset> dbFullGetSynsets(String filter);
	public List<Synset> dbGetNotEmptySynsets(String filter);
	public int dbGetSynsetsCount(Sense unit);
	public Synset dbGet(Long id);
	public Synset dbGetSynsetRels(Synset synset);
	public List<Synset> dbGetSynsetsRels(List<Synset> synsets);
	public List<Synset> dbGetSynsetsUnitsRels(Collection<Long> ind);
	public int dbGetSynsetsCount();
	public int dbGetSimilarityCount(Synset a,Synset b);
	public String getSynsetAtrribute(Synset synset, String nazwaPola);
	public void setSynsetAtrribute(Synset synset, String key, String value);
	public int dbGetUnitsCount(Synset synset);
	public Synset updateSynset(Synset synset);
	public List<SenseToSynset> getSenseToSynsetBySynset(Synset synset);
	public Long fastGetPOSID(Synset synset);
	List<Synset> dbFastGetSynsets(String filter, List<Long> lexicons);
	List<Synset> dbFastGetSynsets(String filter, Domain domain,
			RelationType relationType, int limitSize, List<Long> lexicons);
	List<Synset> dbFastGetSynsets(String filter, Domain domain,
			RelationType relationType, int limitSize, long posIndex,
			List<Long> lexicons);
	List<Synset> dbFastGetSynsets(String filter, Sense filterObject,List<Long> lexicons);
	Map<Long, String> dbGetSynsetsDescriptionIdx(List<Long> idx, List<Long> lexicons);
	List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);
	String dbRebuildUnitsStr(Synset synset, List<Long> lexicons);
	PartOfSpeech dbGetPos(Synset synset, List<Long> lexicons);
	Domain dbGetDomain(Synset synset, List<Long> lexicons);
	List<Sense> dbGetUnits(Long synsetId, List<Long> lexicons);
	Synset fetchSynsetForSense(Sense sense, List<Long> lexicons);
	List<Synset> dbFastGetSynsets(Sense unit, List<Long> lexicons);
	Synset dbGetUnit(Synset synset, List<Long> lexicons);
	void dbClone(Synset synset, List<Long> lexicons);
	HashMap<Long, DataEntry> prepareCacheForRootNode(Synset synset,
			List<Long> lexicons);
	List<Sense> dbFastGetSenseBySynset(String filter, Domain domain,
			RelationType relationType, String definition, String comment,
			String artificial, int limitSize, long posIndex, List<Long> lexicons);
	List<Sense> dbFastGetSenseBySynsetUbyPose(String filter, Domain domain,
			RelationType relationType, String definition, String comment,
			String artificial, int limitSize,
			pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos,
			List<Long> lexicons);
	Boolean areSynsetsInSameLexicon(long synset1, long synset2);
}
