package pl.edu.pwr.wordnetloom.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;

@Stateless
public class DAOBean implements DAOLocal {

    @PersistenceContext(unitName = "plWordnetPU")
    private EntityManager entityManager;

    public DAOBean() {
    }

    @Override
    public <E> void persistObject(E entity) {
        entityManager.persist(entity);
    }

    @Override
    public <E> E mergeObject(E entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public <E> void refresh(E entity) {
        entityManager.refresh(entity);
    }

    @Override
    public <E> void deleteObject(E entity) {
        entityManager.remove(entity);
    }

    @Override
    public <E, ID extends Serializable> void deleteObject(Class<E> entity, ID id) {
        entityManager.remove(entityManager.find(entity, id));
    }

    @Override
    public <E, ID extends Serializable> E getObject(Class<E> entity, ID id) {
        return entityManager.find(entity, id);
    }

    @Override
    public <E> List<E> getAllObjects(Class<E> entity) {
        return entityManager.createQuery(
                "SELECT obj FROM " + entity.getSimpleName() + " obj")
                .getResultList();
    }

    @Override
    public EntityManager getEM() {
        return entityManager;
    }
}
