package pl.edu.pwr.wordnetloom.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.ExtGraph;
import pl.edu.pwr.wordnetloom.model.Synset;

@Local
// FIXME: refactor names
public interface ExtGraphDAOLocal {
	
	public Collection<ExtGraph> dbFullGet();
	public Collection<ExtGraph> dbFullGet(String word);
	public Collection<ExtGraph> dbFullGet(Long[] extgraph_ids);
	public Collection<ExtGraph> dbFullGet(String word, int packageno);
	public Collection<ExtGraph> dbFastGet(String word, int packageno);
	public List<Integer> GetPackages(PartOfSpeech pos);
	public int GetMaxPackageNo(PartOfSpeech pos);
	public List<Long> getIDsFromWord(String word);
	public List<Long> getIDsFromWord(String word, int packageno);
	public void deleteForSynset(Synset synset);
	Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos);
}
