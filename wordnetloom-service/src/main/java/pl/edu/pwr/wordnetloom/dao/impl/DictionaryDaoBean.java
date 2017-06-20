package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.DictionaryDaoLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.Dictionary;

@Stateless
public class DictionaryDaoBean implements DictionaryDaoLocal {

    @EJB
    private DAOLocal local;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void saveOrUpdate(Dictionary dic) {
        EntityManager em = local.getEM();

        if (null != dic.getId()) {
            em.merge(dic);
        } else {
            em.persist(dic);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void remove(Dictionary dic) {
        Dictionary d = local.getEM().getReference(Dictionary.class, dic.getId());
        local.getEM().remove(d);
    }

    @Override
    public <T> List<T> findDictionaryByClass(Class<T> clazz) {
        return local.getAllObjects(clazz);
    }

    @Override
    public List<String> findAllDictionaryNames() {
        return local.getEM().createNativeQuery("SELECT DISTINCT dtype FROM dictionaries")
                .getResultList();
    }

}
