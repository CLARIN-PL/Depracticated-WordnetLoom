package pl.edu.pwr.wordnetloom.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.ExtGraph;

@Remote
//FIXME: refactor names
public interface ExtGraphServiceRemote {
	
	public Collection<ExtGraph> dbFullGet();
	public Collection<ExtGraph> dbFullGet(String word);
	public Collection<ExtGraph> dbFullGet(Long[] extgraph_ids);
	public Collection<ExtGraph> dbFullGet(String word, int packageno);
	public Collection<ExtGraph> dbFastGet(String word, int packageno);
	public List<Integer> GetPackages(PartOfSpeech pos);
	public int GetMaxPackageNo(PartOfSpeech pos);
	Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos);
	
}
