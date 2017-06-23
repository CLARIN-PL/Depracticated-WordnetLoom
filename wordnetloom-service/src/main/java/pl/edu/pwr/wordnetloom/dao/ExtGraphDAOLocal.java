package pl.edu.pwr.wordnetloom.dao;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Local
public interface ExtGraphDAOLocal {

    Collection<ExtGraph> dbFullGet();

    Collection<ExtGraph> dbFullGet(String word);

    Collection<ExtGraph> dbFullGet(Long[] extgraph_ids);

    Collection<ExtGraph> dbFullGet(String word, int packageno);

    Collection<ExtGraph> dbFastGet(String word, int packageno);

    List<Integer> GetPackages(PartOfSpeech pos);

    int GetMaxPackageNo(PartOfSpeech pos);

    List<Long> getIDsFromWord(String word);

    List<Long> getIDsFromWord(String word, int packageno);

    void deleteForSynset(Synset synset);

    Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos);
}
