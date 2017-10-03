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

package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import pl.edu.pwr.wordnetloom.model.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;

/**
 * klasa odpowiadajaca za zarzadzanie danymi o polaczeniach
 * @author Max
 *
 */
@Stateless
public class UnitAndSynsetDAOBean extends DAOBean implements UnitAndSynsetDAOLocal {
	
	@EJB private DAOLocal dao;

	@EJB private SynsetDAOLocal synsetDAO;
	
	@EJB private SynsetAttributeDaoLocal saDAO;
	
	@EJB private ExtGraphDAOLocal exDAO;
	
	@EJB private ExtGraphExtensionDAOLocal exeDAO;
	
	@EJB private SynsetRelationDAOLocal srDAO;
	
	// histogramy
//	final static String UNITS_HISTOGRAM="SELECT w.`c`,COUNT(w.`c`) FROM (SELECT COUNT(`SYN_ID`) AS c FROM `unitandsynset` GROUP BY `LEX_ID`) as w GROUP BY w.`c`";
//	final static String SYNSETS_HISTOGRAM="SELECT w.`c`,COUNT(w.`c`) FROM (SELECT COUNT(`LEX_ID`) AS c FROM `unitandsynset` GROUP BY `SYN_ID`) as w GROUP BY w.`c`";
//	final static String SIMILAR_COUNT="SELECT COUNT(A.`LEX_ID`) FROM `unitandsynset` AS A, `unitandsynset` AS B WHERE A.`SYN_ID`=%s AND B.`SYN_ID`=%s AND A.`LEX_ID`=B.`LEX_ID`";

	public UnitAndSynsetDAOBean() {}
	
//	/**
//	 * odczytanie histogramu jednostek leksykalnych
//	 * @return dane do histogramu
//	 */
//	@Deprecated
//	public Collection<String[]> dbGetUnitsHistogram() {
//		return dbq.rawQuery(UNITS_HISTOGRAM).getItems();
//	}
//	
//	/**
//	 * odczytanie histogramu synsetow
//	 * @return histogram synsetow
//	 */
//	@Deprecated
//	public Collection<String[]> dbGetSynsetsHistogram() {
//		return dbq.rawQuery(SYNSETS_HISTOGRAM).getItems();
//	}
	
	/**
	 * odczytanie liczby identycznych jednostek w synsetach
	 * @param a - synset A
	 * @param b - synset B
	 * @return liczba identycznych jednostek
	 */
	@Override
	public int dbGetSimilarityCount(Synset a, Synset b) {
		String queryString = "select count(a.idSense) from SenseToSynset a,"
				+ " SenseToSynset b "
				+ "where a.synset = :a "
				+ "and b.idSynset = :b "
				+ "and a.idSense = b.idSense";
		TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);
		q.setParameter("a", a.getId());
		q.setParameter("b", b.getId());


		List<Long> list = q.getResultList();
		if(list.isEmpty() || list.get(0)==null)
			return 0;
		return list.get(0).intValue();
	}	
	
	/**
	 * dodanie nowego połączenia jednostki i synsetu
	 * @param unit - jednostka
	 * @param synset - synset
	 * @param rebuildUnitsStr - czy odbudować opis jakie jednostki sa w synsecie 
	 * @return TRUE jesli sie udalo
	 */
	@Override
	public boolean dbAddConnection(Sense unit, Synset synset,boolean rebuildUnitsStr) {
		
		// pobranie wszystkich elementow synsetu
		List<SenseToSynset> old = dbGetConnections(synset);
	
		// przeindeksowanie
		int index = 0;
		for (SenseToSynset synsetOld : old) {
			// czy nie sa identyczne
			if (synsetOld.getSynset().getId().equals(synset.getId()) &&
					synsetOld.getSense().getId().equals(unit.getId()))
				return false;
			synsetOld.setSenseIndex(index++);
			dao.mergeObject(synsetOld);
		}
		
		// dodanie nowego
		SenseToSynset newRel=new SenseToSynset();
		newRel.setSynset(synset);
		newRel.setSense(unit);
		newRel.setIdSense(unit.getId());
		newRel.setIdSynset(synset.getId());
		newRel.setSenseIndex(new Integer(index++));
		
		dao.persistObject(newRel);

		return true;
	}	
	
	/**
	 * dodanie nowego połączenia jednostki i synsetu
	 * @param unit - jednostka
	 * @param synset - synset
	 * @param rebuildUnitsStr - czy odbudować opis jakie jednostki sa w synsecie 
	 * @return TRUE jesli sie udalo
	 */
	@Override
	public Synset dbAddConnection(Sense unit,Synset synset) {
		
		if(dbAddConnection(unit, synset, false)){
			return getObject(Synset.class, synset.getId());
		}
		return dao.getObject(Synset.class, synset.getId()); 
	}
	
	/**
	 * usuniecie powiazan jednostka - synset z bazy danych
	 * @param template - wzor jednostki
	 */
	@Override
	public void dbDeleteConnection(Sense sense) {
		// pobranie wszystkich elementow w ktorych jest jednostka
		List<SenseToSynset> senseToSynsets = dao.getEM().createNamedQuery("SenseToSynset.findAllBySense", SenseToSynset.class)
			.setParameter("idSense", sense.getId())
			.getResultList();

		// usuniecie wszystki jednostek
		for (SenseToSynset sts : senseToSynsets) {
			dao.deleteObject(SenseToSynset.class, sts.getId());
		}
		
		// odbudowa synsetow - wylaczona odbudowa
//		for (SenseToSynset sts : sensetoSynsets) {
//			Synset synset = dao.getObject(Synset.class, sts.getIdSynset());
//			synset.rebuildUnitsStr();
//		}
	}	
	
	/**
	 * usuniecie powiazan jednostka - synset z bazy danych
	 * @param template - wzor synsetu
	 * @param rebuildUnitsStr - TRUE jesli odbudowac opis synsetu
	 */
	@Override
	public void dbDeleteConnection(Synset template, boolean rebuildUnitsStr) {
		
		dao.getEM()
			.createNamedQuery("SenseToSynset.DeleteBySynsetID")
				.setParameter("idSynset", template.getId())
					.executeUpdate();
	}	
	
	
	/**
	 * usuniecie powiazan jednostka - synset z bazy danych
	 * @param unit - jednostka do usuniecia
	 * @param synset - synset do usuniecia
	 */
	@Override
	public Synset dbDeleteConnection(Sense unit,Synset synset) {
		// usuniecie jednego powiazania
		dao.getEM()
		.createNamedQuery("SenseToSynset.DeleteBySynsetIdAndSenseId")
			.setParameter("idSynset", synset.getId())
				.setParameter("idSense", unit.getId())
					.executeUpdate();

		// pobranie wszystkich elementow dla synsetu
		List<SenseToSynset> rest=dbGetConnections(synset);

		// przeindeksowanie
		int index=0;
		for (SenseToSynset synsetDTO : rest) {
			synsetDTO.setSenseIndex(new Integer(index++));
			dao.mergeObject(synsetDTO);
		}
		
		// synset jest pusty, nastepuje usuniecie takiego synsetu z bazy danych
		if (rest.isEmpty()) {
			saDAO.deleteAttributesFor(synset);
			exeDAO.dbDeleteForSynset(synset);
			exDAO.deleteForSynset(synset);
			srDAO.dbDeleteConnection(synset);
			dao.deleteObject(Synset.class, synset.getId());
			return null;
		}
		
		return synset;
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
		// pobranie wszystkich elementow dla synsetu
		List<SenseToSynset> old=dbGetConnections(synset);

		// zamiana numerow indeksow
		for (SenseToSynset synsetDTO : old) {
			// zamienieni indeksow
			if (synsetDTO.getIdSense().longValue()==firstUnit.getId().longValue()) {
				synsetDTO.setSenseIndex(new Integer(synsetDTO.getSenseIndex()+1));
				dao.mergeObject(synsetDTO);
			} else if (synsetDTO.getIdSense().longValue()==secondUnit.getId().longValue()) {
				synsetDTO.setSenseIndex(new Integer(synsetDTO.getSenseIndex().intValue()-1));
				dao.mergeObject(synsetDTO);
			}
		}
		return true;
	}	
	
	/**
	 * odczytanie połączeń
	 * @param synset - synset dla ktorego maja zostać pobrane połączenia
	 * @return połączenia
	 */
	@Override
	public List<SenseToSynset> dbGetConnections(Synset synset) {
		return dao.getEM().createNamedQuery("SenseToSynset.findAllBySynset", SenseToSynset.class)
				.setParameter("idSynset", synset.getId()).getResultList();
	}

	/**
	 * odczytanie wszystkich powiazan
	 * @return lista powiazan
	 */
	public List<SenseToSynset> dbFullGetConnections() {
		return dao.getEM().createNamedQuery("SenseToSynset.findAll", SenseToSynset.class).getResultList();
	}
	
	/**
	 * odczytanie liczby wykorzystanych jednostek
	 * @return liczba wykorzystanych jednostek
	 */
	@Override
	public int dbGetUsedUnitsCount() {
		String queryString = "select count(distinct sts.idSense) FROM SenseToSynset sts";
		TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);


		List<Long> list = q.getResultList();
		if(list.isEmpty() || list.get(0)==null)
			return 0;
		return list.get(0).intValue();
	}
	
	/**
	 * odczytanie liczby wykorzystanych synset
	 * @return liczba wykorzystanych synsetow
	 */
	@Override
	public int dbGetUsedSynsetsCount() {
		String queryString = "select count(distinct sts.idSynset) FROM SenseToSynset sts";
		TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);


		List<Long> list = q.getResultList();
		if(list.isEmpty() || list.get(0)==null)
			return 0;
		return list.get(0).intValue();
	}
	
	/**
	 * odczytanie liczby wpisow w tabeli
	 * @return liczba wpisow
	 */
	@Override
	public int dbGetConnectionsCount() {
		String queryString = "select count(sts.idSense) FROM SenseToSynset sts";
		TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);


		List<Long> list = q.getResultList();
		if(list.isEmpty() || list.get(0)==null)
			return 0;
		return list.get(0).intValue();
	}
	
}
