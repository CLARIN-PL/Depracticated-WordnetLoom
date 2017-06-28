package pl.edu.pwr.wordnetloom.extgraph.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.extgraph.repository.ExtGraphExtensionRepository;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceLocal;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Stateless
public class ExtGraphExtensionServiceBean implements ExtGraphExtensionServiceLocal {

    @EJB
    private ExtGraphExtensionRepository extGraphExtensionRepository;

    @Override
    public void dbSave(Collection<ExtGraphExtension> exts) {
        extGraphExtensionRepository.dbSave(exts);
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet() {
        return extGraphExtensionRepository.dbFullGet();
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids) {
        return extGraphExtensionRepository.dbFullGet(extgraph_ids);
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(String word) {
        return extGraphExtensionRepository.dbFullGet(word);
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(String word, int packageno) {
        return extGraphExtensionRepository.dbFullGet(word, packageno);
    }

    @Override
    public List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts) {
        return extGraphExtensionRepository.dbGetRelation(exts);
    }

    @Override
    public Collection<ExtGraph> dbFastGet(String word, int packageno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> GetPackages(PartOfSpeech pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int GetMaxPackageNo(PartOfSpeech pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
