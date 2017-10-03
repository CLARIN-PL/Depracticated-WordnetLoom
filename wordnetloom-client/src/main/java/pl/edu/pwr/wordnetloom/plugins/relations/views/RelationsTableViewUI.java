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
//package pl.wroc.pwr.ci.plwordnet.plugins.relations.views;
//
//import java.awt.Point;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.Collection;
//
//import javax.swing.JCheckBox;
//import javax.swing.JComponent;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.ListSelectionModel;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//
//import pl.wroc.pwr.ci.plwordnet.database.dc.SynsetsRelationsDC;
//import RelationArgument;
//import RelationType;
//import Sense;
//import Synset;
//import SynsetRelation;
//import LexicalIM;
//import LexicalDA;
//import RelationTypeFrame;
//import SynsetsListFrame;
//import HighlightTableRenderer;
//import RelationsIM;
//import RelationsDA;
//import SynsetsRelationTableModel;
//import DialogBox;
//import SpanTable;
//import ToolTipGenerator;
//import ButtonExt;
//import FamilyManager;
//import RemoteUtils;
//import AbstractViewUI;
//import se.datadosen.component.RiverLayout;
//
///**
// * klasa opisujacy wyglada okienka z powiązaniami synsetu w postaci tabeli
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.relations.views;
public class RelationsTableViewUI {
	
}
//public class RelationsTableViewUI extends AbstractViewUI implements ActionListener,ListSelectionListener,MouseListener {
//
//	private static String				WINDOW_TITLE										= "Tabela relacji synsetów";
//	private static String				HIDE_AUTOREVERSE_RELATIONS							= "Ukryj relacje symetryczne";
//	private static String				SYNCHRONIZE_WITH_SELECTED_SYNSET_LABEL				= "Sychronizuj z zaznaczonym synstem";
//	private static String				BUTTON_NEW_WINDOW_HINT								= "Otworzenie widoku w nowym oknie";
//	private static String				QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION		= "Czy utworzyć powiązania dla relacji odwrotnej (%s)?";
//	private static String				ERROR_DESTINATION_AND_SOURCE_SYNSET_ARE_THE_SAME	= "Synsety źródłowy i docelowy są identyczne.";
//	private static String				QUESTION_CREATE_SIMILAR_ENTRY_FOR_REVERSE_RELATION	= "Czy utworzyć analogiczny wpis dla relacji odwrotnej (%s)?";
//	private static String				ERROR_SYNSET_IS_ALREADY_IN_THE_RELATION				= "Wstawiany synset znajduje się już w podanej relacji.";
//	private static String				QUESTION_DO_YOU_WANT_TO_REMOVE_RELATION				= "Czy na pewno chcesz usunąć relację?";
//	private static String				BUTTON_INSERT_HINT									= "Wstawienie synsetu pośredniego do relacji";
//	private static String				BUTTON_SWITCH_TO_SECOND_HINT						= "Użyj wybranego synsetu jak docelowego (Ctrl+D)";
//	private static String				BUTTON_SWITCH_HINT									= "Użyj wybranego synsetu jak głównego (Ctrl+G)";
//	private static String				BUTTON_REMOVE_HINT									= "Usuniecie relacji pomiędzy synsetami (DELETE)";
//	private static String				BUTTON_ADD_HINT										= "Dodanie nowej relacji pomiędzy synsetami (INSERT)";
//	private static String				RELATION_ALREADY_EXISTS								= "<html>Relacja typu <font color=\"blue\">%s</font> pomiędzy<br><font color=\"blue\">%s</font><br>i<br><font color=\"blue\">%s</font><br>już istnieje.</html>";
//
//	private SpanTable					table;
//	private SynsetsRelationTableModel	model;
//	private Synset					lastSynset											= null;
//	private ButtonExt					buttonRemove, buttonAdd;
//	private ButtonExt					buttonInsert;
//	private ButtonExt					buttonSwitchToRoot;
//	private ButtonExt					buttonSwitchToSecond;
//	private ButtonExt					buttonOpenNew										= null;
//
//	private JCheckBox					hideAutoReverse;
//
//	private boolean						showOpenNew;
//	private JCheckBox					sychronizeContent									= null;
//	FamilyManager<RelationsTableViewUI>	fm													= new FamilyManager<RelationsTableViewUI>();
//
//	/** odswiezenie widoku synsetow */
//	public static final int				RELATION_WAS_DELETED								= 0;
//	/** pokazanie synsetu w pierwszym widoku */
//	public static final int				SHOW_SYNSET_IN_FIRST_VIEW							= 1;
//	/** pokazanie synsetu w drugim widoku */
//	public static final int				SHOW_SYNSET_IN_SECOND_VIEW							= 2;
//	/** odswiezenie drzewa relacji */
//	public static final int				RELATION_WAS_CHANGED								= 3;
//	/** Flag to hide switch to second */
//	public static final int				HIDE_SWITCH_TO_SECOND								= 1;
//	/** Flag to hide switch to root */
//	public static final int				HIDE_SWITCH_TO_ROOT									= 2;
//	private final int					flags;
//
//	/**
//	 */
//	public RelationsTableViewUI() {
//		this.showOpenNew = true;
//		this.flags = 0;
//	}
//
//	/**
//	 * @param flags 
//	 */
//	public RelationsTableViewUI(int flags) {
//		this.flags = flags;
//		this.showOpenNew = true;
//	}
//
//	/**
//	 * @param flags 
//	 * @param showOpenNew
//	 *
//	 */
//	public RelationsTableViewUI(int flags, boolean showOpenNew) {
//		this.flags = flags;
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
//		model=new SynsetsRelationTableModel();
//		table=new SpanTable(model,model,ToolTipGenerator.getGenerator());
//
//		// renderer dla komorek
//		table.setDefaultRenderer(String.class,new HighlightTableRenderer());
//		table.setDefaultRenderer(Boolean.class,new HighlightTableRenderer());
//
//		// utawienie kolumn
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		table.getColumnModel().getColumn(0).setPreferredWidth(250);
//		table.getColumnModel().getColumn(1).setPreferredWidth(100);
//		table.getColumnModel().getColumn(1).setMaxWidth(200);
//		table.getColumnModel().getColumn(2).setPreferredWidth(300);
//		table.getColumnModel().getColumn(3).setPreferredWidth(45);
//		table.getColumnModel().getColumn(3).setMaxWidth(45);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		table.setCellSelectionEnabled(true);
//		table.getColumnModel().getSelectionModel().addListSelectionListener(this);
//		table.getSelectionModel().addListSelectionListener(this);
//		table.addMouseListener(this);
//
//		// dodanie przycisku usun
//		buttonRemove = new ButtonExt(RelationsIM.getDelete(),this);
//		buttonRemove.setEnabled(false);
//		buttonRemove.setToolTipText(BUTTON_REMOVE_HINT);
//		installViewScopeShortCut(buttonRemove,0,KeyEvent.VK_DELETE);
//
//		buttonAdd = new ButtonExt(RelationsIM.getAdd(),this);
//		buttonAdd.setEnabled(false);
//		buttonAdd.setToolTipText(BUTTON_ADD_HINT);
//		installViewScopeShortCut(buttonAdd,0,KeyEvent.VK_INSERT);
//
//		// dodanie przycisku wstaw w srodek
//        buttonInsert=new ButtonExt(LexicalIM.getInsertBetween(),this);
//        buttonInsert.setToolTipText(BUTTON_INSERT_HINT);
//        buttonInsert.setEnabled(false);
//
//        // dodanie przycisku przełącz
//        if ((flags & HIDE_SWITCH_TO_ROOT) == 0) {
//			buttonSwitchToRoot = new ButtonExt(RelationsIM.getSwitch(),this);
//			buttonSwitchToRoot.setEnabled(false);
//			buttonSwitchToRoot.setToolTipText(BUTTON_SWITCH_HINT);
//			installViewScopeShortCut(buttonSwitchToRoot,KeyEvent.CTRL_MASK,KeyEvent.VK_G);
//        }
//
//		// przycisk przelacz docelowy
//		if ((flags & HIDE_SWITCH_TO_SECOND) == 0) {
//			buttonSwitchToSecond = new ButtonExt(RelationsIM.getSwitchSecond(),this,KeyEvent.VK_D);
//			buttonSwitchToSecond.setEnabled(false);
//			buttonSwitchToSecond.setToolTipText(BUTTON_SWITCH_TO_SECOND_HINT);
//			installViewScopeShortCut(buttonSwitchToSecond,KeyEvent.CTRL_MASK,KeyEvent.VK_D);
//		}
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
//		
//		// Enabling/disabling of showing autoreverse relations
//		hideAutoReverse = new JCheckBox(HIDE_AUTOREVERSE_RELATIONS);
//		hideAutoReverse.setSelected(false);
//		hideAutoReverse.addActionListener(this);
//		
//		content.add("",hideAutoReverse);
//
//		content.add("br hfill vfill",new JScrollPane(table));
//		content.add("center br",buttonAdd);
//		content.add("",buttonInsert);
//		content.add("",buttonRemove);
//		content.add("",new JLabel(" "));
//		if ((flags & HIDE_SWITCH_TO_ROOT) == 0) {
//			content.add("",buttonSwitchToRoot);
//		}
//		if ((flags & HIDE_SWITCH_TO_SECOND) == 0) {
//			content.add("",buttonSwitchToSecond);
//		}
//
//		if (showOpenNew) {
//			content.add("",new JLabel(" "));
//			content.add("",buttonOpenNew);
//		}
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
//	 * odswiezenie informacji o synsecie
//	 * @param synset - synset
//	 */
//	public void refreshData(Synset synset) {
//		//System.err.println("REFRESH_DATA START");
//		lastSynset=synset;
//		buttonAdd.setEnabled(lastSynset!=null);
//		Collection<SynsetsRelationsDC> relations=RelationsDA.getFastRelations(lastSynset, hideAutoReverse.isSelected());
//
//		// odswiezenie modelu
//		model.setCollection(relations);
//
//		// Refresh children views.
//		synchronized (fm.getChildren()) {
//			for (RelationsTableViewUI child : fm.getChildren()) {
//				if (child.getPerformSynchronize()) {
//					child.refreshData(synset);
//				} else {
//					child.refreshData(child.lastSynset);
//				}
//			}
//		}
//		//System.err.println("REFRESH_DATA END");
//	}
//
//	/**
//	 * odswiezenie ostatnio zaznaczonego obiektu
//	 * @param focus - ostatnio zaznaczony
//	 */
//	public void refreshFocusObject(Synset focus) {
//		model.setFocusObject(focus);
//
//		// Refresh children views.
//		synchronized (fm.getChildren()) {
//			for (RelationsTableViewUI child : fm.getChildren()) {
//				child.refreshFocusObject(focus);
//			}
//		}
//	}
//
//	/**
//	 * wcisnieto pokaz wszystkie relacje
//	 */
//	public void actionPerformed(ActionEvent arg0) {
//		// wcisnieto usun
//		if (arg0.getSource()==buttonRemove) {
//			// niewlasciwa kolumna
//			if (table.getSelectedColumn()!=2) return;
//			if (DialogBox.showYesNoCancel(QUESTION_DO_YOU_WANT_TO_REMOVE_RELATION)==DialogBox.YES) {
//				int row=table.getSelectedRow();
//
//				// odczytanie relacji
//				SynsetRelation relation=model.getObjectAt(row);
//				RelationsDA.delete(relation);
//
//				// odswiezenie widoku
//				refreshData(lastSynset);
//				listeners.notifyAllListeners(null,RELATION_WAS_DELETED);
//			}
//		} else if (arg0.getSource()==buttonInsert) {  // wcisnieto wstaw
//			// niewlasciwa kolumna
//			if (table.getSelectedColumn()!=2) return;
//			Point location=buttonAdd.getLocationOnScreen();
//			Sense template=new Sense();
//			template.setPartOfSpeech(RelationsDA.getPos(lastSynset));
//			Collection<Synset> selectedSynsets = SynsetsListFrame.showModal(
//														workbench,
//														location.x,location.y,
//														false,
//														template);
//			if (selectedSynsets == null) return;
//
//			int row=table.getSelectedRow();
//			SynsetRelation relation=model.getObjectAt(row);
//
//			// odczytanie detali relacji
////			relation = RemoteUtils.synsetRelationRemote.dbGetDetails(relation);
//			Synset selectedSynset = selectedSynsets.iterator().next();
//			Synset parentSynset = relation.getSynsetFrom();
//			Synset childSynset = relation.getSynsetTo();
//			RelationType relationType = relation.getRelation();
//
//			// sprawdzenie czy juz przypadkiem takiej nie ma
//			if (childSynset.equals(selectedSynsets) || parentSynset.equals(selectedSynsets) ||
//				RelationsDA.checkIfRelationExists(parentSynset,selectedSynset,relationType) ||
//				RelationsDA.checkIfRelationExists(selectedSynset,childSynset,relationType)) {
//
//				DialogBox.showError(ERROR_SYNSET_IS_ALREADY_IN_THE_RELATION);
//				return;
//			}
//
//			// wyswietlenie okna z relacjami
//			RelationType rel=RelationTypeFrame.showModal(workbench,
//													RelationArgument.SYNSET,
//													RelationsDA.getPos(parentSynset),
//												    relationType,
//													RelationsDA.getUnits(parentSynset),
//													RelationsDA.getUnits(selectedSynset),
//													RelationsDA.getUnits(childSynset));
//
//			if (rel==null) return;  // nie ma co kontynować
//
//			// utworzenie nowych relacji
//			RelationsDA.makeRelation(parentSynset, selectedSynset, relationType);
//			RelationsDA.makeRelation(selectedSynset,childSynset, relationType);
//
//			// usuniecie starej relacji
//			RelationsDA.delete(relation);
//
//			// czy istieje relacja odwrotna
//			if (rel.getReverse()!=null) {
//				RelationType reverse=RelationsDA.getReverseRelation(rel);
//				// odczytanie wpisu dla relacji odwrotnej
//				SynsetRelation reverseRelation = RemoteUtils.synsetRelationRemote.dbGetRelation(
//															childSynset,
//															parentSynset,
//															reverse);
//				if (!RelationsDA.checkIfRelationExists(childSynset, selectedSynset, reverse) &&
//					!RelationsDA.checkIfRelationExists(selectedSynset,parentSynset,reverse)) {
//					String reverseName=LexicalDA.getRelationName(reverse);
//					if (rel.isAutoReverse() || DialogBox.showYesNo(String.format(QUESTION_CREATE_SIMILAR_ENTRY_FOR_REVERSE_RELATION,reverseName))==DialogBox.YES) {
//						// utworzenie nowych relacji
//						RelationsDA.makeRelation(childSynset, selectedSynset, reverse);
//						RelationsDA.makeRelation(selectedSynset,parentSynset, reverse);
//
//						// usuniecie starej relacji
//						RelationsDA.delete(reverseRelation);
//					}
//				}
//			}
//
//			// odswiezenie danych
//			refreshData(lastSynset);
//			listeners.notifyAllListeners(lastSynset,RELATION_WAS_CHANGED);
//
//		// wcisnieto dodaj
//		} else if (arg0.getSource()==buttonAdd) {
//			Point location=buttonAdd.getLocationOnScreen();
//			//Sense template=new Sense();
//			//template.setPos(RelationsDA.getPos(lastSynset));
//			Collection<Synset> selectedSynsets = SynsetsListFrame.showModal(workbench,location.x,location.y,false,null);
//			if (selectedSynsets != null) {
//				Synset descSynset=null;
//				for (Synset Synset : selectedSynsets) {
//					if (Synset.getId().longValue()==lastSynset.getId().longValue()) {
//						DialogBox.showError(ERROR_DESTINATION_AND_SOURCE_SYNSET_ARE_THE_SAME);
//						return;
//					}
//					descSynset=Synset; // odczytanie synsetu
//				}
//				if (descSynset==null) return; // nie ma co dodac
//
//				// wyswietlenie okna z relacjami
//				RelationType rel=RelationTypeFrame.showModal(workbench,RelationArgument.SYNSET,RelationsDA.getPos(lastSynset),lastSynset.getUnits(),RelationsDA.getUnits(descSynset));
//				if (rel==null) return;  // nie ma co kontynować
//
//				// sprawdzenie czy przypadkiem nie ma juz relacji
//				if (RelationsDA.checkIfRelationExists(lastSynset,descSynset,rel)) {
//					DialogBox.showError(String.format(RELATION_ALREADY_EXISTS,rel.getFullName(),lastSynset.toString(),descSynset.toString()));
//					return;
//				}
//
//				// utworzenie relacji
//				RelationsDA.makeRelation(lastSynset,descSynset,rel);
//
//				// czy istieje relacja odwrotna
//				if (rel.getReverse()!=null) {
//					// czy utworzyc dla relacji odwrotnej
//					RelationType reverse=LexicalDA.getReverseRelation(rel);
//					String reverseName=LexicalDA.getRelationName(reverse);
//					//Pobierz testy dla relacji odwrotnej
//                    Collection<String> tests=LexicalDA.getTests(reverse,lastSynset.toString(),descSynset.toString(),RemoteUtils.synsetRemote.dbGetPos(lastSynset));
//                    String test="\n\n";
//                    for(String i : tests){
//                        test+=i + "\n";
//                    }
//					if (rel.isAutoReverse() || DialogBox.showYesNo(String.format(QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION + test,reverseName))==DialogBox.YES) {
//						if (RelationsDA.checkIfRelationExists(descSynset,lastSynset,rel)) {
//							DialogBox.showError(String.format(RELATION_ALREADY_EXISTS,reverseName,descSynset.toString(),lastSynset.toString()));
//						} else {
//							RelationsDA.makeRelation(descSynset,lastSynset,reverse);
//						}
//					}
//				}
//				// odswiezenie danych
//				refreshData(lastSynset);
//				listeners.notifyAllListeners(lastSynset,RELATION_WAS_CHANGED);
//			}
//
//		// przelaczenie sie na synset
//		} else if (arg0.getSource()==buttonSwitchToRoot) {
//			// niewlasciwa kolumna
//			if (table.getSelectedColumn()!=0 && table.getSelectedColumn()!=2) return;
//			int row=table.getSelectedRow();
//			// odczytanie sysnetu i relacji
//			Synset secondSynset=table.getSelectedColumn()==0?model.getObjectAt(row).getSynsetFrom():model.getObjectAt(row).getSynsetTo();
//			// powiadomienie zainteresowanych
//			listeners.notifyAllListeners(secondSynset,SHOW_SYNSET_IN_FIRST_VIEW);
//
//		// przelaczenie sie na synset docelowy
//		} else if (arg0.getSource()==buttonSwitchToSecond) {
//			// niewlasciwa kolumna
//			if (table.getSelectedColumn()!=0 && table.getSelectedColumn()!=2) return;
//			int row=table.getSelectedRow();
//			// odczytanie sysnetu i relacji
//			Synset secondSynset=table.getSelectedColumn()==0?model.getObjectAt(row).getSynsetFrom():model.getObjectAt(row).getSynsetTo();
//			// powiadomienie zainteresowanych
//			listeners.notifyAllListeners(secondSynset,SHOW_SYNSET_IN_SECOND_VIEW);
//
//		//	wcisnieto nowe okno
//		} else if (buttonOpenNew != null &&  arg0.getSource()==buttonOpenNew) {
//			RelationsTableViewUI ui = new RelationsTableViewUI(flags, false);
//			ui.listeners = listeners;
//			fm.addChild(workbench, WINDOW_TITLE, ui, ui.fm);
//			ui.hideAutoReverse.setEnabled(this.hideAutoReverse.isEnabled());
//			ui.refreshData(lastSynset);
//			
//			// Showing/hiding auto reverse relations was clicked
//		} else if (arg0.getSource() == hideAutoReverse) {
//			refreshData(lastSynset);
//		}
//	}
//
//
//	/**
//	 * zmienilo sie zaznacznenie w tabeli
//	 */
//	public void valueChanged(ListSelectionEvent arg0) {
//		if (arg0 != null && arg0.getValueIsAdjusting()) return;
//		int col=table.getSelectedColumn();
//		int row=table.getSelectedRow();
//		buttonRemove.setEnabled(col==2 && row!=-1);
//		buttonInsert.setEnabled(col==2 && row!=-1);
//		buttonSwitchToRoot.setEnabled((col==2 || col==0) && row!=-1);
//		if ((flags & HIDE_SWITCH_TO_SECOND) == 0)
//			buttonSwitchToSecond.setEnabled((col==2 || col==0) && row!=-1);
//	}
//
//	/*
//	 *  (non-Javadoc)
//	 * @see AbstractViewUI#getRootComponent()
//	 */
//	@Override
//	public JComponent getRootComponent() {
//		return table;
//	}
//
//	/**
//	 * wykonano dwuklik na tabeli
//	 */
//	public void mouseClicked(MouseEvent arg0) {
//		if (arg0.getSource()==table && arg0.getClickCount()==1) {
//			if (table.getSelectedColumn()!=3) return;
////			int row=table.getSelectedRow();
//			// odczytanie relacji
////			SynsetRelation relation=model.getObjectAt(row);
////			RelationsDA.setValid(relation,!relation.isValid().booleanValue());
//
//			// odswiezenie widoku
//			refreshData(lastSynset);
//		}
//	}
//
//	public void mousePressed(MouseEvent arg0) {/***/}
//	public void mouseReleased(MouseEvent arg0) {/***/}
//	public void mouseEntered(MouseEvent arg0) {/***/}
//	public void mouseExited(MouseEvent arg0) {/***/}
//}
