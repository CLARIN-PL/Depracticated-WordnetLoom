package pl.edu.pwr.wordnetloom.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.ExtGraphDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.ExtGraph;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.service.ExtGraphServiceRemote;

@Stateless
//FIXME: refactor names
public class ExtGraphServiceBean implements ExtGraphServiceRemote {

    @EJB
    private ExtGraphDAOLocal extGraph;

    @Override
    public Collection<ExtGraph> dbFullGet() {
        return extGraph.dbFullGet();
    }

    @Override
    public Collection<ExtGraph> dbFullGet(String word) {
        return extGraph.dbFullGet(word);
    }

    @Override
    public Collection<ExtGraph> dbFullGet(Long[] extgraph_ids) {
        return extGraph.dbFullGet(extgraph_ids);
    }

    @Override
    public Collection<ExtGraph> dbFullGet(String word, int packageno) {
        return extGraph.dbFullGet(word, packageno);
    }

    @Override
    public Collection<ExtGraph> dbFastGet(String word, int packageno) {
        return extGraph.dbFastGet(word, packageno);
    }

    @Override
    public Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos) {
        return extGraph.dbGetNewWords(packageno, pos);
    }

    @Override
    public List<Integer> GetPackages(PartOfSpeech pos) {
        return extGraph.GetPackages(pos);
    }

    @Override
    public int GetMaxPackageNo(PartOfSpeech pos) {
        return extGraph.GetMaxPackageNo(pos);
    }

}
