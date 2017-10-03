package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.yiddish.ItemToImport;

@Remote
public interface NativeServiceRemote {
	
	void setOwner(String owner);
	String getOwner();
	List<ItemToImport> getAllYiddishItemsToImport();
	
}
