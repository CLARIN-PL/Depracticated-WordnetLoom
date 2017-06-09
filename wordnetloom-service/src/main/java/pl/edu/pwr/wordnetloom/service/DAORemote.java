package pl.edu.pwr.wordnetloom.service;

import java.io.Serializable;

public interface DAORemote {

    <E> void persistObject(E entity);

    <E> E mergeObject(E entity);

    void flush();

    <E> void refresh(E entitie);

    <E> void deleteObject(E entite);

    <E, ID extends Serializable> void deleteObject(Class<E> entite, ID id);

    <E, ID extends Serializable> E getObject(Class<E> entitie, ID id);
}
