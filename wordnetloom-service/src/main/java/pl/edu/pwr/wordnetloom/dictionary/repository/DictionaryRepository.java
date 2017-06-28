package pl.edu.pwr.wordnetloom.dictionary.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

@Stateless
public class DictionaryRepository extends GenericRepository<Dictionary> {

    @PersistenceContext
    EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveOrUpdate(Dictionary dic) {
        if (null != dic.getId()) {
            em.merge(dic);
        } else {
            em.persist(dic);
        }
    }

    public <T> List<T> findDictionaryByClass(Class<T> clazz) {
        return null;
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
