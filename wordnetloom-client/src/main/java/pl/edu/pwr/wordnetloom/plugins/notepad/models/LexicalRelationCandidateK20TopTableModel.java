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
///**
// * 
// */
package pl.edu.pwr.wordnetloom.plugins.notepad.models;
//
//import java.util.ArrayList;
//
//import javax.swing.table.AbstractTableModel;
//
//import pl.wroc.pwr.ci.plwordnet.database.dto.LexicalRelationCandidateDTO;
//import SpanMapperInterface;
//import RemoteUtils;
//
///**
// * @author czuk
// *
// */
public class LexicalRelationCandidateK20TopTableModel{
	
}
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -1389508982807125615L;
//	private static String FIRST_COLUMN = "Słowo";
//	private static String SECOND_COLUMN = "Słowo powiązane";
//	private static String THIRD_COLUMN = "Powiązanie";
//	
//	private static ArrayList<LexicalRelationCandidateDTO> collection = new ArrayList<LexicalRelationCandidateDTO>();
//	
//	public LexicalRelationCandidateK20TopTableModel(){
//	}
//	
//	/* (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getColumnCount()
//	 */
//	public int getColumnCount() {
//		return 3;
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
//		}
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getRowCount()
//	 */
//	public int getRowCount() {
//		if (LexicalRelationCandidateK20TopTableModel.collection == null)
//			return 0;
//		else
//			return LexicalRelationCandidateK20TopTableModel.collection.size();
//	}
//
//	/* (non-Javadoc)
//	 * @see javax.swing.table.TableModel#getValueAt(int, int)
//	 */
//	public Object getValueAt(int arg0, int arg1) {
//		if (arg0<0 || arg0 >= this.getRowCount())
//			return "row: "+arg0;
//		else
//			switch (arg1) {
//			case 0:
//				return LexicalRelationCandidateK20TopTableModel.collection.get(arg0).getWord1();
//
//			case 1:
//				return LexicalRelationCandidateK20TopTableModel.collection.get(arg0).getWord2();
//
//			case 2:
//				return LexicalRelationCandidateK20TopTableModel.collection.get(arg0).getValue();
//
//			default:
//				return "col: "+arg1;
//			}
//	}
//	
//	public void loadData(String word){
//		LexicalRelationCandidateK20TopTableModel.collection.clear();
//		LexicalRelationCandidateK20TopTableModel.collection.addAll(RemoteUtils.lexicalRelationCandidateRemote.dbFullGetCandidatesWord1(word));
//		this.fireTableDataChanged();
//	}
//
//
//	public int rowSpan(int row, int column) {
//		if (row == 0 && column == 0)
//			return LexicalRelationCandidateK20TopTableModel.collection.size();
//		else
//			return 1;
//	}
//
//}
