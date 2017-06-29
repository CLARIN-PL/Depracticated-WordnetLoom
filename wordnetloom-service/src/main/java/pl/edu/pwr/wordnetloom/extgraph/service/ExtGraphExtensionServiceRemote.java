package pl.edu.pwr.wordnetloom.extgraph.service;

import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;

@Remote
public interface ExtGraphExtensionServiceRemote {

    void save(Collection<ExtGraphExtension> exts);

    Collection<ExtGraphExtension> dbFullGet();

    Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids);

    Collection<ExtGraphExtension> dbFullGet(String word);

    Collection<ExtGraphExtension> dbFullGet(String word, int packageno);

    List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts);

}
