package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.DomainDaoLocal;
import pl.edu.pwr.wordnetloom.model.Domain;

@Stateless
public class DomainServiceBean implements DomainServiceRemote {

	@EJB private DomainDaoLocal local;
	
	@Override
	public List<Domain> getAllDomains(List<Long> lexicons) {
		return local.getAllDomains(lexicons);
	}
	
	@Override
	public Domain getDomainByID(Long id) {
		return local.getDomainByID(id);
	}

}
