package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.Synset;

@Remote
public interface UnitAndSynsetServiceRemote extends DAORemote{
	
//	public Collection<String[]> dbGetUnitsHistogram();
//	public Collection<String[]> dbGetSynsetsHistogram();
	public boolean dbAddConnection(Sense unit,Synset synset,boolean rebuildUnitsStr);
	public Synset dbAddConnection(Sense unit,Synset synset);
	public void dbDeleteConnection(Sense template);
	public void dbDeleteConnection(Synset template,boolean rebuildUnitsStr);
	public Synset dbDeleteConnection(Sense unit,Synset synset);
	public boolean dbExchangeUnits(Synset synset, Sense firstUnit, Sense secondUnit);
	public List<SenseToSynset> dbGetConnections(Synset synset);
	public List<SenseToSynset> dbFullGetConnections();
	public int dbGetSimilarityCount(Synset a, Synset b);
	public int dbGetUsedUnitsCount();
	public int dbGetUsedSynsetsCount();
	public int dbGetConnectionsCount();
	
}
