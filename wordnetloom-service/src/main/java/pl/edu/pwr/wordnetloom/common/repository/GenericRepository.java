package pl.edu.pwr.wordnetloom.common.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class GenericRepository<T> {

    protected abstract Class<T> getPersistentClass();

    protected abstract EntityManager getEntityManager();

    public T save(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    public <E> E update(E entity) {
        return getEntityManager().merge(entity);
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    public void delete(T entity) {
        getEntityManager().remove(entity);
    }

    public void delete(final Long id) {
        if (id != null) {
            getEntityManager().remove(findById(id));
        }
    }

    public T findById(final Long id) {
        if (id == null) {
            return null;
        }
        return getEntityManager().find(getPersistentClass(), id);
    }

    public List<T> findAll(final String orderField) {
        return getEntityManager().createQuery(
                "Select e From " + getPersistentClass().getSimpleName() + " e Order by e." + orderField)
                .getResultList();
    }

    public boolean existsById(final Long id) {
        return getEntityManager()
                .createQuery("Select 1 From " + getPersistentClass().getSimpleName() + " e where e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }

    public boolean alreadyExists(final String propertyName, final String propertyValue, final Long id) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append("Select 1 From ")
                .append(getPersistentClass().getSimpleName())
                .append(" e where e.")
                .append(propertyName)
                .append(" = :propertyValue");

        if (id != null) {
            jpql.append(" and e.id != :id");
        }

        final Query query = getEntityManager().createQuery(jpql.toString());
        query.setParameter("propertyValue", propertyValue);
        if (id != null) {
            query.setParameter("id", id);
        }

        return query.setMaxResults(1).getResultList().size() > 0;
    }

}
