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
package pl.edu.pwr.wordnetloom.plugins.relations.views;
//
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Collection;
//
//import javax.swing.JCheckBox;
//import javax.swing.JComponent;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//
//import RelationType;
//import Synset;
//import RelationsIM;
//import RelationsDA;
//import Tools;
//import ButtonExt;
//import ComboBoxPlain;
//import FamilyManager;
//import LabelExt;
//import TreePanel;
//import AbstractViewUI;
//import se.datadosen.component.RiverLayout;
//
///**
// * klasa opisujacy wyglada okienka z powiązaniami synsetu w postaci drzewa
// * @author Max
// */
public class RelationsTreeViewUI{
	
}
//
//	private static final String	WINDOW_TITLE	= "Drzewo relacji synsetów";
//	private static String				SYNCHRONIZE_WITH_SELECTED_SYNSET_LABEL	= "Sychronizuj z zaznaczonym synstem";
//	private static String				BUTTON_NEW_WINDOW_HINT					= "Otworzenie widoku w nowym oknie";
//	private static String				LABEL_RELATION_TYPE						= "Typ relacji:";
//	private ComboBoxPlain				relationType;
//	private TreePanel					treePanel;
//	private Synset					lastSynset;
//
//	private ButtonExt					buttonOpenNew							= null;
//	private boolean						showOpenNew;
//	private JCheckBox					sychronizeContent						= null;
//	FamilyManager<RelationsTreeViewUI>	fm										= new FamilyManager<RelationsTreeViewUI>();
//
//	/**
//	 * Stworzenenie nowej instancji UI
//	 *
//	 */
//	public RelationsTreeViewUI() {
//		showOpenNew = true;
//	}
//
//	/**
//	 * Odczytanie stanu przelacznika wykonaj synchornizacje z zaznaczonym synstem
//	 * @return jeśli TRUE to wykonaj
//	 */
//	public boolean getPerformSynchronize() {
//		return sychronizeContent == null ? true : sychronizeContent.isSelected();
//	}
//
//	/**
//	 * Stworzenenie nowej instancji UI
//	 * @param showOpenNew - true oznacza, ze nalezy pokazac przycisk otwarcia w nowym oknie
//	 *
//	 */
//	public RelationsTreeViewUI(boolean showOpenNew) {
//		this.showOpenNew = showOpenNew;
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see pl.wroc.pwr.ci.plwordnet.workbench.implementation.AbstractViewUI#initialize(javax.swing.JPanel)
//	 */
//	@Override
//	protected void initialize(JPanel content) {
//		// ustawienie layoutu
//		content.setLayout(new RiverLayout());
//		treePanel=new TreePanel(true);
//
//		relationType=new ComboBoxPlain();
//		relationType.setPreferredSize(new Dimension(300,25));
//		relationType.setEnabled(false);
//		relationType.addActionListener(this);
//
//		// dodanie przycisku w nowym oknie
//		if (showOpenNew) {
//			buttonOpenNew = new ButtonExt(RelationsIM.getNewWindow(),this);
//			buttonOpenNew.setToolTipText(BUTTON_NEW_WINDOW_HINT);
//		} else {
//			sychronizeContent = new JCheckBox(SYNCHRONIZE_WITH_SELECTED_SYNSET_LABEL);
//			sychronizeContent.setSelected(false);
//		}
//
//		if (!showOpenNew) {
//			content.add("",sychronizeContent);
//		}
//		content.add("br", new LabelExt(LABEL_RELATION_TYPE,'t',relationType));
//		content.add("tab",relationType);
//		if (showOpenNew) {
//			content.add("tab",buttonOpenNew);
//		}
//		JScrollPane pane=new JScrollPane(treePanel);
//		content.add("br hfill vfill",pane);
//	}
//
//	/**
//	 * odswiezenie informacji o synsecie
//	 * @param synset - synset
//	 */
//	public void refreshData(Synset synset) {
//		if (relationType.getItemCount()>0)
//			relationType.removeAllItems();
//		treePanel.setNode(null);
//		lastSynset=synset;
//
//		// odczytanie danych o relacjach
//		Collection<RelationType> relations=RelationsDA.getRelationsTypes(synset);
//		for (RelationType typeDTO : relations) {
//			relationType.addItem(typeDTO);
//		}
//		relationType.setEnabled(synset!=null && relations.size()>0);
//
//		// Refresh children views.
//		synchronized (fm.getChildren()) {
//			for (RelationsTreeViewUI child : fm.getChildren()) {
//				if (child.getPerformSynchronize()) {
//					child.refreshData(synset);
//				} else {
//					child.refreshData(child.lastSynset);
//				}
//			}
//		}
//	}
//
//	/**
//	 * odswiezenie ostatnio zaznaczonego obiektu
//	 * @param focus - ostatnio zaznaczony
//	 */
//	public void refreshFocusObject(Synset focus) {
//		if (focus!=null) {
//			treePanel.setFocusTag(focus.getId());
//			treePanel.repaint();
//		} else {
//			treePanel.setFocusTag(null);
//		}
//
//		// Refresh children views.
//		synchronized (fm.getChildren()) {
//			for (RelationsTreeViewUI child : fm.getChildren()) {
//				child.refreshFocusObject(focus);
//			}
//		}
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see AbstractViewUI#getRootComponent()
//	 */
//	@Override
//	public JComponent getRootComponent() {
//		return relationType;
//	}
//
//	/**
//	 * kliknieto w jakis element aktywny
//	 */
//	public void actionPerformed(ActionEvent arg0) {
//		// zmienil sie typ relacji i jest co zmieniać
//		if (arg0.getSource()==relationType && relationType.getItemCount()>0) {
//			RelationType relType=(RelationType) relationType.getItemAt(relationType.getSelectedIndex());
//
//			// zbudowanie drzewa
//			treePanel.setNode(RelationsDA.buildRelationsTree(Tools.findFrame(getContent()),lastSynset,relType));
//
//		//	wcisnieto nowe okno
//		} else if (buttonOpenNew != null &&  arg0.getSource()==buttonOpenNew) {
//
//			RelationsTreeViewUI ui = new RelationsTreeViewUI(false);
//			ui.listeners = listeners;
//			fm.addChild(workbench, WINDOW_TITLE, ui, ui.fm);
//			ui.refreshData(lastSynset);
//		}
//	}
//}
