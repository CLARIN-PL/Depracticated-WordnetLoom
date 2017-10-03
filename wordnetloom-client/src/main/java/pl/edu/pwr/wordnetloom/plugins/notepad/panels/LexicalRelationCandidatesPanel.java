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
package pl.edu.pwr.wordnetloom.plugins.notepad.panels;
//
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//
//import Sense;
//import LexicalRelationCandidateK20BottomTableModel;
//import LexicalRelationCandidateK20TopTableModel;
//import SimpleListenerInterface;
//import SpanTable;
//import SplitPaneExt;
//import GlobalEventListener;
//import se.datadosen.component.RiverLayout;
//
///**
// * Panel do wyświetlania listy słów podobnych do wskazanego słowa z listy k20 słów podobnych.
// * 
// * @author czuk
// *
// */
public class LexicalRelationCandidatesPanel{
	
}
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -8839006177775037660L;
//
//	// Model do osbługi górnej tabelki (słowa podobne do wyświetlanego słowa)
//	private LexicalRelationCandidateK20TopTableModel modelTop = null;
//	
//	// Model do obsługi dolnej tabelki (słowa, do których jest podobne wyświetlane słowo)
//	private LexicalRelationCandidateK20BottomTableModel modelBottom = null;
//	
//	/**
//	 * Konstruktor
//	 */
//	public LexicalRelationCandidatesPanel(){
//		super();
//		
//		this.modelTop = new LexicalRelationCandidateK20TopTableModel();
//		SpanTable tableTop = new SpanTable(this.modelTop, this.modelTop, null);
//		tableTop.setFillsViewportHeight(true);
//		JScrollPane scrollPane = new JScrollPane(tableTop);
//		
//		this.modelBottom = new LexicalRelationCandidateK20BottomTableModel();
//		SpanTable tableBottom = new SpanTable(this.modelBottom, this.modelBottom, null);
//		tableBottom.setFillsViewportHeight(true);
//		JScrollPane scrollPaneBottom = new JScrollPane(tableBottom);
//
//		SplitPaneExt splitPane = new SplitPaneExt(0, scrollPane, scrollPaneBottom);
//		splitPane.setStartDividerLocation(250);
//		
//		this.setLayout(new RiverLayout());		
//		this.add("hfill vfill", splitPane);
//		
//		// dodanie słuchacza do zmiany zaznaczonej jednostki
//		GlobalEventListener.getInstance().addUnitSelectionListener(this);
//	}
//
//	/**
//	 * Obsługa zdarzenia zmiany zaznaczenia jednostki
//	 */
//	public void doAction(Object object, int tag) {
//		if (object instanceof Sense){
////			String lemma = "";
//			if (object != null) 
////				lemma = ((LexicalUnitDTO)object).getLemma();
//				((Sense)object).getLemma();
//
//	
////			this.modelTop.loadData(lemma);
////			this.modelBottom.loadData(lemma);
//		}
//	}
//}
