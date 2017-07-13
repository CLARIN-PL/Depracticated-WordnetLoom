package pl.edu.pwr.wordnetloom.extgraph.service.impl;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.extgraph.repository.ExtGraphExtensionRepository;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceLocal;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class ExtGraphExtensionServiceBean implements ExtGraphExtensionServiceLocal {

    @Inject
    private ExtGraphExtensionRepository extGraphExtensionRepository;

    @Override
    public void persist(Collection<ExtGraphExtension> exts) {
        extGraphExtensionRepository.persist(exts);
    }

    @Override
    public Collection<ExtGraphExtension> findAll() {
        return extGraphExtensionRepository.findAll("id");
    }

    @Override
    public Collection<ExtGraphExtension> findByWord(String word) {
        return extGraphExtensionRepository.findByWord(word);
    }

    @Override
    public Collection<ExtGraphExtension> findByWordAndPAckageNo(String word, int packageno) {
        return extGraphExtensionRepository.findByWordAndPackageNo(word, packageno);
    }

    @Override
    public void deleteBySynset(Synset s) {
        extGraphExtensionRepository.deleteBySynset(s);
    }

    @Override
    public Collection<ExtGraphExtension> findByIds(Long[] extgraphIds) {
        return extGraphExtensionRepository.finaAll(extgraphIds);
    }
}
