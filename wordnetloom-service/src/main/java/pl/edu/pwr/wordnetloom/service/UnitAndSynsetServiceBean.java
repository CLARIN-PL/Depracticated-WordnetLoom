/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
*/

package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.UnitAndSynsetDAOLocal;
import pl.edu.pwr.wordnetloom.dao.DAOBean;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.Synset;

/**
 * klasa odpowiadajaca za zarzadzanie danymi o polaczeniach
 * @author Max
 *
 */
@Stateless
public class UnitAndSynsetServiceBean extends DAOBean implements UnitAndSynsetServiceRemote {

	@EJB private UnitAndSynsetDAOLocal local;
	
	public UnitAndSynsetServiceBean() {}
	
//	/**
//	 * odczytanie histogramu jednostek leksykalnych
//	 * @return dane do histogramu
//	 */
//	public Collection<String[]> dbGetUnitsHistogram() {
//		return local.dbGetUnitsHistogram();
//	}
//	
//	/**
//	 * odczytanie histogramu synsetow
//	 * @return histogram synsetow
//	 */
//	public Collection<String[]> dbGetSynsetsHistogram() {
//		return local.dbGetSynsetsHistogram();
//	}

	/**
	 * dodanie nowego połączenia jednostki i synsetu
	 * @param unit - jednostka
	 * @param synset - synset
	 * @param rebuildUnitsStr - czy odbudować opis jakie jednostki sa w synsecie 
	 * @return TRUE jesli sie udalo
	 */
	@Override
	public boolean dbAddConnection(Sense unit,Synset synset,boolean rebuildUnitsStr) {
		return local.dbAddConnection(unit, synset, rebuildUnitsStr);
	}
	
	/**
	 * dodanie nowego połączenia jednostki i synsetu
	 * @param unit - jednostka
	 * @param synset - synset
	 * @return synset
	 */
	@Override
	public Synset dbAddConnection(Sense unit,Synset synset) {
		return local.dbAddConnection(unit, synset);
	}

	/**
	 * usuniecie powiazan jednostka - synset z bazy danych
	 * @param template - wzor jednostki
	 */
	@Override
	public void dbDeleteConnection(Sense template) {
		local.dbDeleteConnection(template);
	}
	
	/**
	 * usuniecie powiazan jednostka - synset z bazy danych
	 * @param template - wzor synsetu
	 * @param rebuildUnitsStr - TRUE jesli odbudowac opis synsetu
	 */
	@Override
	public void dbDeleteConnection(Synset template,boolean rebuildUnitsStr) {
		local.dbDeleteConnection(template, rebuildUnitsStr);
	}
	
	/**
	 * usuniecie powiazan jednostka - synset z bazy danych
	 * @param unit - jednostka do usuniecia
	 * @param synset - synset do usuniecia
	 */
	@Override
	public Synset dbDeleteConnection(Sense unit,Synset synset) {
		return local.dbDeleteConnection(unit, synset);
	}

	/**
	 * zamienienie jednostek w synsecie 
	 * @param synset - synset 
	 * @param firstUnit - pierwsza jednostka
	 * @param secondUnit - druga jednostka 
	 * @return TRUE jesli sie udalo
	 */
	@Override
	public boolean dbExchangeUnits(Synset synset, Sense firstUnit, Sense secondUnit) {
		return local.dbExchangeUnits(synset, firstUnit, secondUnit);
	}
	
	/**
	 * odczytanie połączeń
	 * @param synset - synset dla ktorego maja zostać pobrane połączenia
	 * @return połączenia
	 */
	@Override
	public List<SenseToSynset> dbGetConnections(Synset synset) {
		return local.dbGetConnections(synset);
	}

	/**
	 * odczytanie wszystkich powiazan
	 * @return lista powiazan
	 */
	@Override
	public List<SenseToSynset> dbFullGetConnections() {
		return local.dbFullGetConnections();
	}

	/**
	 * odczytanie liczby identycznych jednostek w synsetach
	 * @param a - synset A
	 * @param b - synset B
	 * @return liczba identycznych jednostek
	 */
	@Override
	public int dbGetSimilarityCount(Synset a, Synset b) {
		return local.dbGetSimilarityCount(a, b);
	}
	
	/**
	 * odczytanie liczby wykorzystanych jednostek
	 * @return liczba wykorzystanych jednostek
	 */
	@Override
	public int dbGetUsedUnitsCount() {
		return local.dbGetUsedUnitsCount();
	}
	
	/**
	 * odczytanie liczby wykorzystanych synset
	 * @return liczba wykorzystanych synsetow
	 */
	@Override
	public int dbGetUsedSynsetsCount() {
		return local.dbGetUsedSynsetsCount();
	}
	
	/**
	 * odczytanie liczby wpisow w tabeli
	 * @return liczba wpisow
	 */
	@Override
	public int dbGetConnectionsCount() {
		return local.dbGetConnectionsCount();
	}
	
}
