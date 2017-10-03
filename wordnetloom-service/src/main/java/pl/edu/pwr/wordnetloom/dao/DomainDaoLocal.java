package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.Domain;

@Local
public interface DomainDaoLocal {

	public Domain getDomainByID(Long index);
	public List<Domain> getAllDomains(List<Long> lexicons);

}
