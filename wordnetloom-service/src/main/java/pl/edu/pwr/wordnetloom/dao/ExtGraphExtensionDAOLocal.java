package pl.edu.pwr.wordnetloom.dao;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

@Local
public interface ExtGraphExtensionDAOLocal {

    void dbSave(Collection<ExtGraphExtension> exts);

    Collection<ExtGraphExtension> dbFullGet();

    Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids);

    Collection<ExtGraphExtension> dbFullGet(String word);

    Collection<ExtGraphExtension> dbFullGet(String word, int packageno);

    List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts);

    void dbDeleteForSynset(Synset s);

}
