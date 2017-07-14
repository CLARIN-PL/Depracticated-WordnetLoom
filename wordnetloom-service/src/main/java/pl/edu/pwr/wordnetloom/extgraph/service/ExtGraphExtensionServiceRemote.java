package pl.edu.pwr.wordnetloom.extgraph.service;

import java.util.Collection;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

public interface ExtGraphExtensionServiceRemote {

    void persist(Collection<ExtGraphExtension> exts);

    Collection<ExtGraphExtension> findAll();

    Collection<ExtGraphExtension> findByIds(Long[] extgraph_ids);

    Collection<ExtGraphExtension> findByWord(String word);

    Collection<ExtGraphExtension> findByWordAndPAckageNo(String word, int packageno);

    void deleteBySynset(Synset s);
}
