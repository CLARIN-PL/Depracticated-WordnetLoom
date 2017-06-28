package pl.edu.pwr.wordnetloom.extgraph.service;

import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Remote
public interface ExtGraphServiceRemote {

    Collection<ExtGraph> dbFullGet();

    Collection<ExtGraph> dbFullGet(String word);

    Collection<ExtGraph> dbFullGet(Long[] extgraph_ids);

    Collection<ExtGraph> dbFullGet(String word, int packageno);

    Collection<ExtGraph> dbFastGet(String word, int packageno);

    List<Integer> GetPackages(PartOfSpeech pos);

    int GetMaxPackageNo(PartOfSpeech pos);

    Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos);

}
