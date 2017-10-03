package pl.edu.pwr.wordnetloom.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.ExtGraphExtension;

@Remote
// FIXME: refactor names
public interface ExtGraphExtensionServiceRemote extends DAORemote{
	
	public void dbSave(Collection<ExtGraphExtension> exts);
	public Collection<ExtGraphExtension> dbFullGet();
	public Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids);
	public Collection<ExtGraphExtension> dbFullGet(String word);
	public Collection<ExtGraphExtension> dbFullGet(String word, int packageno);
	public List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts);
	
}
