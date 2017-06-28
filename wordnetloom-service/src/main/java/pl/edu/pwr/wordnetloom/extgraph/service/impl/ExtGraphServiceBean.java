package pl.edu.pwr.wordnetloom.extgraph.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.extgraph.repository.ExtGraphRepository;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceLocal;

@Stateless
public class ExtGraphServiceBean implements ExtGraphServiceLocal {

    @Inject
    private ExtGraphRepository extGraphRepository;

    @Override
    public Collection<ExtGraph> dbFullGet() {
        return extGraphRepository.dbFullGet();
    }

    @Override
    public Collection<ExtGraph> dbFullGet(String word) {
        return extGraphRepository.dbFullGet(word);
    }

    @Override
    public Collection<ExtGraph> dbFullGet(Long[] extgraph_ids) {
        return extGraphRepository.dbFullGet(extgraph_ids);
    }

    @Override
    public Collection<ExtGraph> dbFullGet(String word, int packageno) {
        return extGraphRepository.dbFullGet(word, packageno);
    }

    @Override
    public Collection<ExtGraph> dbFastGet(String word, int packageno) {
        return extGraphRepository.dbFastGet(word, packageno);
    }

    @Override
    public Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos) {
        return extGraphRepository.dbGetNewWords(packageno, pos);
    }

    @Override
    public List<Integer> GetPackages(PartOfSpeech pos) {
        return extGraphRepository.GetPackages(pos);
    }

    @Override
    public int GetMaxPackageNo(PartOfSpeech pos) {
        return extGraphRepository.GetMaxPackageNo(pos);
    }

}
