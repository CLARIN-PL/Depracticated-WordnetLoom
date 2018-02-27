package pl.edu.pwr.wordnetloom.dictionary.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

@Stateless
public class DictionaryRepository extends GenericRepository<Dictionary> {

    @Inject
    EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Dictionary save(Dictionary dic) {
        if (null != dic.getId()) {
           return  em.merge(dic);
        }
        em.persist(dic);
        return  dic;
    }

    public <T> List<T> findDictionaryByClass(Class<T> clazz) {
        return getEntityManager().createQuery("FROM "+ clazz.getName()).getResultList();
    }

    public List<String> findAllDictionaryNames() {
        return em.createNativeQuery("SELECT DISTINCT dtype FROM dictionaries")
                .getResultList();
    }

    @Override
    protected Class<Dictionary> getPersistentClass() {
        return Dictionary.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
