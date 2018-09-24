package pl.edu.pwr.wordnetloom.dictionary.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;

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
        return dic;
    }

    public <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(Class<T> clazz) {
        return findDictionaryByClass(clazz.getName());
    }

    public void delete (Dictionary dictionary) {
        getEntityManager().remove(dictionary);
    }

    public <T extends Dictionary> List<? extends Dictionary> findDictionaryByClass(String className) {
        return getEntityManager().createQuery("FROM " + className).getResultList();
    }

    public Status findStatusDefaultValue() {
        return (Status) getEntityManager().createQuery("FROM Status s WHERE s.isDefault = True")
                .getSingleResult();
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
