package pl.edu.pwr.wordnetloom.extgraph.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.extgraph.repository.ExtGraphRepository;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceLocal;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class ExtGraphServiceBean implements ExtGraphServiceLocal {

    @Inject
    private ExtGraphRepository extGraphRepository;

    @Override
    public Collection<ExtGraph> findByWord(String word) {
        return extGraphRepository.findByWord(word);
    }

    @Override
    public Collection<ExtGraph> findByIds(Long[] extgraphIds) {
        return extGraphRepository.findByIds(extgraphIds);
    }

    @Override
    public Collection<ExtGraph> findByWordAndPackageNo(String word, int packageno) {
        return extGraphRepository.findByWordAndPackageNo(word, packageno);
    }

    @Override
    public Collection<String> findNewWordsByPackageNoAndPartOfSpeech(int packageno, PartOfSpeech pos) {
        return extGraphRepository.findNewWordsByPackageNoAndPartOfSpeech(packageno, pos);
    }

    @Override
    public List<Long> findPackagesByPartOfSpeech(PartOfSpeech pos) {
        return extGraphRepository.findPackagesByPartOfSpeech(pos);
    }

    @Override
    public int findMaxPackageNoByPartOfSpeech(PartOfSpeech pos) {
        return extGraphRepository.findMaxPackageNoByPartOfSpeech(pos);
    }

    @Override
    public List<Long> findIDsByWord(String word) {
        return extGraphRepository.findIDsByWord(word);
    }

    @Override
    public List<Long> findIDsByWordAndPackageNo(String word, int packageno) {
        return extGraphRepository.findIDsByWordAndPackageNo(word, packageno);
    }

    @Override
    public void deleteBySynset(Synset synset) {
        extGraphRepository.deleteBySynset(synset);
    }

}
