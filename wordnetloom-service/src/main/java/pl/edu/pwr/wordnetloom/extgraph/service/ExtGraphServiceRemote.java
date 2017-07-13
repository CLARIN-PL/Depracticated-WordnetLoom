package pl.edu.pwr.wordnetloom.extgraph.service;

import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Remote
public interface ExtGraphServiceRemote {

    Collection<ExtGraph> findByWord(String word);

    Collection<ExtGraph> findByIds(Long[] extgraphIds);

    Collection<ExtGraph> findByWordAndPackageNo(String word, int packageno);

    Collection<String> findNewWordsByPackageNoAndPartOfSpeech(int packageno, PartOfSpeech pos);

    List<Long> findPackagesByPartOfSpeech(PartOfSpeech pos);

    int findMaxPackageNoByPartOfSpeech(PartOfSpeech pos);

    List<Long> findIDsByWord(String word);

    List<Long> findIDsByWordAndPackageNo(String word, int packageno);

    void deleteBySynset(Synset synset);

}
