package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.DomainDaoLocal;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

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

}
