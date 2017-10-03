package pl.edu.pwr.wordnetloom.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

@Local
public interface DAOLocal {

	 <E> void persistObject(E entity);

	 <E> E mergeObject(E entity);

	 void flush();

	 <E> void refresh(E entitie);

 	 <E> void deleteObject(E entite);

	 <E, ID extends Serializable> void deleteObject(Class<E> entite, ID id);

	 <E, ID extends Serializable> E getObject(Class<E> entitie, ID id);

	 <E> List<E> getAllObjects(Class<E> entitie);

	 EntityManager getEM();
}
