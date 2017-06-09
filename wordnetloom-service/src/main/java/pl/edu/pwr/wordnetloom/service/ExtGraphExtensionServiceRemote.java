package pl.edu.pwr.wordnetloom.service;

import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.ExtGraphExtension;

@Remote
public interface ExtGraphExtensionServiceRemote extends DAORemote {

    void dbSave(Collection<ExtGraphExtension> exts);

    Collection<ExtGraphExtension> dbFullGet();

    Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids);

    Collection<ExtGraphExtension> dbFullGet(String word);

    Collection<ExtGraphExtension> dbFullGet(String word, int packageno);

    List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts);

}
