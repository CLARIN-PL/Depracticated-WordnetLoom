package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.model.Domain;

@Stateless
public class DomainDaoBean implements DomainDaoLocal {

	@EJB
	private DAOLocal local;

	@Override
	public Domain getDomainByID(Long index) {
		return local.getObject(Domain.class, index);
	}

	@Override
	public List<Domain> getAllDomains(List<Long> lexicons) {
		return local.getEM().createNamedQuery("Domain.getAllDomains", Domain.class)
		.setParameter("lexicons", lexicons)
		.getResultList();
	}

	@Override
	public List<Domain> getAllDomains() {
		return local.getEM().createQuery("SELECT DISTINCT d FROM Domain d LEFT JOIN FETCH d.name LEFT JOIN FETCH d.description", Domain.class)
				.getResultList();
	}

}
