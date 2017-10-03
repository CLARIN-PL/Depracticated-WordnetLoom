package pl.edu.pwr.wordnetloom.service;

import java.io.Serializable;

public interface DAORemote {
	public <E> void persistObject(E entity);

	public <E> E mergeObject(E entity);

	public void flush();

	public <E> void refresh(E entitie);

	public <E> void deleteObject(E entite);

	public <E, ID extends Serializable> void deleteObject(Class<E> entite, ID id);

	public <E, ID extends Serializable> E getObject(Class<E> entitie, ID id);
}
