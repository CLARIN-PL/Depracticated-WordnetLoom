package pl.edu.pwr.wordnetloom.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.model.Synset;

@Local
// FIXME: refactor names
public interface ExtGraphExtensionDAOLocal {
	
	public void dbSave(Collection<ExtGraphExtension> exts);
	public Collection<ExtGraphExtension> dbFullGet();
	public Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids);
	public Collection<ExtGraphExtension> dbFullGet(String word);
	public Collection<ExtGraphExtension> dbFullGet(String word, int packageno);
	public List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts);
	public void dbDeleteForSynset(Synset s);
	
}
