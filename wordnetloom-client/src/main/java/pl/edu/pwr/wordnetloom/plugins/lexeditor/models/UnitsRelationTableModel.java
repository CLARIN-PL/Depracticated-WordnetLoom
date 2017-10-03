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
//package pl.wroc.pwr.ci.plwordnet.plugins.lexeditor.models;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import pl.wroc.pwr.ci.plwordnet.database.dc.UnitsRelationsDC;
//import Sense;
//import SenseRelation;
//import AbstractSimpleTableModel;
//import SpanMapperInterface;
//
///**
// * model dla tabeli z parami jednostek
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.lexeditor.models;
public class UnitsRelationTableModel {
	
}
//public class UnitsRelationTableModel extends AbstractSimpleTableModel implements SpanMapperInterface  {
//
//	private static String FIRST_COLUMN = "Źródłowa";
//	private static String SECOND_COLUMN = "Relacja";
//	private static String THIRD_COLUMN = "Docelowa";
//	private static String FOUR_COLUMN = "Popr.";
//
//	private int lastCount=0;
//	private Sense[] rowToUnitMap=null;
//	private UnitsRelationsDC[] rowToRelationsMap=null;
//	private long lastFocusUnit=0;
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
//	 * odczytanie obiektu
//	 * @param row - wiersz
//	 * @return relacja
//	 */
//	public SenseRelation getObjectAt(int row) {
//		return rowToRelationsMap[row].getRelation(row);
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getValueAt(int, int)
//	 */
//	public Object getValueAt(int row, int col) {
//		String desc;
//		switch (col) {
//			case 0: desc=rowToUnitMap[row].toString();
//					return rowToUnitMap[row].getId().longValue()==lastFocusUnit?"`"+desc:desc;
//			case 1: return rowToRelationsMap[row].getRelationType().getFullName();
//			case 2: Sense unit=rowToRelationsMap[row].getRelation(row).getSenseTo();
//				    desc=unit.toString();
//				    return unit.getId().longValue()==lastFocusUnit?"`"+desc:desc;
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
//		switch (col) {
//			case 0: return rowToUnitMap[row];
//			case 1: return rowToRelationsMap[row].getRelationType();
//			case 2: return rowToRelationsMap[row].getRelation(row).getSenseTo();
//			case 3: return true; //rowToRelationsMap[row].getRelation(row).isValid();
//		}
//		return null;
//	}
//
//	/**
//	 * ustawienie obiektu zaznaczonego
//	 * @param focusUnit - aktywna jednostka
//	 */
//	public void setFocusObject(Sense focusUnit) {
//		lastFocusUnit=focusUnit.getId().longValue();
//		notifyAllListeners(); // powiadomienie słuchaczy o zmianie
//	}
//
//	/**
//	 * ustawienie kolekcji danych
//	 * @param newRelations - relacja i jednostki nia powiazane
//	 */
//	public void setCollection(Collection<UnitsRelationsDC> newRelations) {
//		// polczenie wielkosci
//		lastCount=0;
//		if (newRelations==null)
//			newRelations=new ArrayList<UnitsRelationsDC>();
//
//		for (UnitsRelationsDC item : newRelations) {
//			lastCount+=item.getSize();
//		}
//
//		rowToUnitMap=new Sense[lastCount];
//		int rowIndex=0;
//		for (UnitsRelationsDC relationsDC : newRelations) {
//			int count=relationsDC.getSize();
//			for (int i=0;i<count;i++) {
//				rowToUnitMap[rowIndex++]=relationsDC.getRelation(i).getSenseFrom();
//			}
//		}
//
//		// utworzenie tablicy mapujacej
//		rowToRelationsMap=new UnitsRelationsDC[getRowCount()];
//		int index=0;
//		for (UnitsRelationsDC relation : newRelations) {
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
//	private int getUnitsSpanCount(int row) {
//		long baseID=rowToUnitMap[row].getId().longValue();
//		int spanCount=1;
//		for (int i=row-1;i>=0;i--) {
//			if (rowToUnitMap[i].getId().longValue()!=baseID)
//				break;
//			spanCount++;
//		}
//		for (int i=row+1;i<rowToUnitMap.length;i++) {
//			if (rowToUnitMap[i].getId().longValue()!=baseID)
//				break;
//			spanCount++;
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
//			case 0:return getUnitsSpanCount(row);
//			case 1:return rowToRelationsMap[row].getSize();
//		}
//		return 1;
//	}
//}