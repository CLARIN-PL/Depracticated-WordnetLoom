///*
//    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
//                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
//                       Radosław Ramocki, Michał Stanek
//    Part of the WordnetLoom
//
//    This program is free software; you can redistribute it and/or modify it
//under the terms of the GNU General Public License as published by the Free
//Software Foundation; either version 3 of the License, or (at your option)
//any later version.
//
//    This program is distributed in the hope that it will be useful, but
//WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//or FITNESS FOR A PARTICULAR PURPOSE. 
//
//    See the LICENSE and COPYING files for more details.
//*/
//
package pl.edu.pwr.wordnetloom.plugins.relations.models;
//
//import java.util.Collection;
//
//import pl.wroc.pwr.ci.plwordnet.database.dc.SynsetsRelationsDC;
//import Synset;
//import SynsetRelation;
//import AbstractSimpleTableModel;
//import SpanMapperInterface;
//
///**
// * model dla tabeli z parami synsetów
// * @author Max
// */
public class SynsetsRelationTableModel{
	
}
//
//	private static String FIRST_COLUMN = "Źródłowy synset";
//	private static String THIRD_COLUMN = "Docelowy synset";
//	private static String SECOND_COLUMN = "Relacja";
//	private static String FOUR_COLUMN = "Poprawność";
//
//	private int lastCount=0;
//	private Synset[] rowToSynsetMap=null;
//	private SynsetsRelationsDC[] rowToRelationsMap=null;
//	private long lastFocusSynset=0;
//
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getColumnClass(int)
//	 */
//	@Override
//	public Class<?> getColumnClass(int arg0) {
//		return arg0<3?String.class:Boolean.class;
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getRowCount()
//	 */
//	public int getRowCount() {
//		return lastCount;
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getColumnCount()
//	 */
//	public int getColumnCount() {
//		return 4;
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getColumnName(int)
//	 */
//	public String getColumnName(int col) {
//		switch (col) {
//			case 0:return FIRST_COLUMN;
//			case 1:return SECOND_COLUMN;
//			case 2:return THIRD_COLUMN;
//			case 3:return FOUR_COLUMN;
//		}
//		return null;
//	}
//
//	/**
//	 * formatowanie opisu dla synsetu
//	 * @param desc - opis oryginalny
//	 * @return opis przetworzony
//	 */
//	private String formatSynsetDescription(String desc) {
//		int length=desc.length();
//		int pos=desc.indexOf('(');
//		desc="<html>(<font color=\"0000FF\"><u>"+desc.substring(pos+1,length);
//		desc=desc.replace(" |","</u></font> |");
//		if (desc.endsWith("S")) { // czy nie konczy sie jako S - sztuczny
//			desc=desc.substring(0,desc.length()-1)+"<font color=\"red\">A</font>";
//		}
//		return desc+"</html>";
//	}
//
//	/**
//	 * odczytanie obiektu
//	 * @param row - wiersz
//	 * @return relacja
//	 */
//	public SynsetRelation getObjectAt(int row) {
//		return rowToRelationsMap[row].getRelation(row);
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getValueAt(int, int)
//	 */
//	public Object getValueAt(int row, int col) {
//		String desc;
//		try {
//			switch (col) {
//				case 0: desc=formatSynsetDescription(rowToSynsetMap[row].toString());
//						return rowToSynsetMap[row].getId().longValue()==lastFocusSynset?"`"+desc:desc;
//				case 1: return rowToRelationsMap[row].getRelationType().getFullName();
//				case 2: Synset synset=rowToRelationsMap[row].getRelation(row).getSynsetTo();
//					    desc=formatSynsetDescription(synset.toString());
//					    return synset.getId().longValue()==lastFocusSynset?"`"+desc:desc;
//			}
//		}
//		catch (Exception e) {
//			Main.logException(e);
//		}
//		return getRealValueAt(row,col);
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see AbstractSimpleTableModel#getRealValueAt(int, int)
//	 */
//	@Override
//	public Object getRealValueAt(int row, int col) {
//		try {
//			switch (col) {
//				case 0: return rowToSynsetMap[row];
//				case 1: return rowToRelationsMap[row].getRelationType();
//				case 2: return rowToRelationsMap[row].getRelation(row).getSynsetTo();
//				case 3: return true; //rowToRelationsMap[row].getRelation(row).isValid();
//			}
//		}
//		catch (Exception e) {
//			Main.logException(e);
//		}
//		return null;
//	}
//
//	/**
//	 * ustawienie obiektu zaznaczonego
//	 * @param focusSynset
//	 */
//	public void setFocusObject(Synset focusSynset) {
//		if (focusSynset!=null && focusSynset.getId()!=null) {
//			lastFocusSynset=focusSynset.getId().longValue();
//			notifyAllListeners(); // powiadomienie słuchaczy o zmianie
//		}
//	}
//
//	/**
//	 * ustawienie kolekcji danych
//	 * @param newRelations - relacja i synsety nia powiazane
//	 */
//	public void setCollection(Collection<SynsetsRelationsDC> newRelations) {
//		// polczenie wielkosci
//		lastCount=0;
//		if (newRelations!=null) {
//			for (SynsetsRelationsDC item : newRelations) {
//				lastCount+=item.getSize();
//			}
//		}
//
//		rowToSynsetMap=new Synset[lastCount];
//		int rowIndex=0;
//		for (SynsetsRelationsDC relationsDC : newRelations) {
//			int count=relationsDC.getSize();
//			for (int i=0;i<count;i++) {
//				rowToSynsetMap[rowIndex++]=relationsDC.getRelation(i).getSynsetFrom();
//			}
//		}
//
//		// utworzenie tablicy mapujacej
//		rowToRelationsMap=new SynsetsRelationsDC[getRowCount()];
//		int index=0;
//		for (SynsetsRelationsDC relation : newRelations) {
//			int size=relation.getSize();
//			relation.setIndexOffset(index); // ustawienie offsetu na liscie
//			for (int i=0;i<size;i++) {
//				rowToRelationsMap[index++]=relation;
//			}
//		}
//
//		notifyAllListeners(); // powiadomienie słuchaczy o zmianie
//	}
//
//	/**
//	 * wyliczenie liczby powiazanych wierszy
//	 * @param row - wiersz bazowy
//	 * @return liczba powiazanych wierszy
//	 */
//	private int getSynsetSpanCount(int row) {
//		int spanCount=1;
//		try {
//			Synset mainSynset = rowToSynsetMap[row];
//			if (mainSynset != null) {
//				long baseID = mainSynset.getId().longValue();
//				for (int i=row-1;i>=0;i--) {
//					Synset synset = rowToSynsetMap[i];
//					if (synset == null || synset.getId().longValue()!=baseID)
//						break;
//					spanCount++;
//				}
//				for (int i=row+1;i<rowToSynsetMap.length;i++) {
//					Synset synset = rowToSynsetMap[i];
//					if (synset==null || synset.getId().longValue()!=baseID)
//						break;
//					spanCount++;
//				}
//			}
//		} catch (Exception e) {
//			Main.logException(e);
//		}
//		return spanCount;
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see pl.wroc.pwr.ci.plwordnet.plugins.relations.models.SpanMapperInterface#rowSpan(int, int)
//	 */
//	public int rowSpan(int row, int column) {
//		switch (column) {
//			case 0:return getSynsetSpanCount(row);
//			case 1:return rowToRelationsMap[row].getSize();
//		}
//		return 1;
//	}
//}