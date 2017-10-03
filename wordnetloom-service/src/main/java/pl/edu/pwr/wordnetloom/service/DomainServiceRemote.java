package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.Domain;

@Remote
public interface DomainServiceRemote {
	
	public List<Domain> getAllDomains(List<Long> lexicons);
	public Domain getDomainByID(Long id);
	
}
