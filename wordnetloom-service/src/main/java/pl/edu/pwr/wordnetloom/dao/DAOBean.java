package pl.edu.pwr.wordnetloom.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DAOBean implements DAOLocal {

    @PersistenceContext(unitName = "plWordnet")
	private EntityManager entityManager;
	
	public DAOBean(){}

	public <E> void persistObject(E object) {
		entityManager.persist(object);
	}

	public <E> E mergeObject(E object) {
		return entityManager.merge(object);
	}

	public void flush() {
		entityManager.flush();
	}

	public <E> void refresh(E entitie) {
		entityManager.refresh(entitie);
	}

	public <E> void deleteObject(E entite) {
		entityManager.remove(entite);
	}

	public <E, ID extends Serializable> void deleteObject(Class<E> entite, ID id) {
		entityManager.remove(entityManager.find(entite, id));
	}

	public <E, ID extends Serializable> E getObject(Class<E> entitie, ID id) {
		return entityManager.find(entitie, id);
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> getAllObjects(Class<E> entitie) {
		return entityManager.createQuery(
				"SELECT obj FROM " + entitie.getSimpleName() + " obj")
				.getResultList();
	}

	public EntityManager getEM() {
		return entityManager;
	}
}
