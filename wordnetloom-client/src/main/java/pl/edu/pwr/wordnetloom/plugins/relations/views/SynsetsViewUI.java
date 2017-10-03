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
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.Collection;
//
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComponent;
//import javax.swing.JLabel;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.ListSelectionModel;
//import javax.swing.ScrollPaneConstants;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//
//import pl.wroc.pwr.ci.plwordnet.database.cache.DomainManager;
//import pl.wroc.pwr.ci.plwordnet.database.cache.PosManager;
//import pl.wroc.pwr.ci.plwordnet.database.enums.WorkState;
//import pl.wroc.pwr.ci.plwordnet.database.maxdb.IntegerContainer;
//import Domain;
//import PartOfSpeech;
//import RelationArgument;
//import RelationType;
//import Synset;
//import LexicalDA;
//import ColorSynsetListRenderer;
//import RelationsDA;
//import CustomDescription;
//import DialogBox;
//import Tools;
//import GenericListModel;
//import AbstractProgressThread;
//import ToolTipGenerator;
//import ToolTipList;
//import ButtonExt;
//import ComboBoxPlain;
//import LabelExt;
//import TextFieldPlain;
//import RemoteUtils;
//import AbstractViewUI;
//import se.datadosen.component.RiverLayout;
//
///**
// * klasa opisujacy wyglada okienka z synsetami
// * @author Max
// */
public class SynsetsViewUI{
	
}

//public class SynsetsViewUI extends AbstractViewUI implements ActionListener,
//		ListSelectionListener, KeyListener, MouseListener {
//	
//}
//
//	private static String		RELATION_LABEL						= "Relacje:";
//	private static String		MESSAGE_ERROR_CANNOT_CHANGE_STATUS	= "Nie można było zmienić statusu dla synsetu %s,\ngdyż istnieją błędy w relacjach.";
//	private static String		QUESTION_SET_STATUS_MESSAGE			= "Czy na pewno chcesz zmienić status zaznaczonych synsetów?";
//	private static String		MENU_SET_VALIDATED_STATUS_LABEL		= "Ustaw status: sprawdzone";
//	private static String		MENU_SET_ERROR_STATUS_LABEL			= "Ustaw status: błędne";
//	private static String		MENU_SET_DONE_STATUS_LABEL			= "Ustaw status: przetworzone";
//	private static String		MENU_SET_TODO_STATUS_LABEL			= "Ustaw status: nieprzetworzone";
//	private static String		VALUE_COUNT_SIMPLE					= "Ilość: %s";
//	private static String		VALUE_COUNT_FULL					= "Ilość: %s z %s";
//	private static String		LABEL_LIMIT_TO						= "Ogranicz do %s elementów";
//	private static String		DOMAIN_LABEL						= "Dziedzina:";
//	static String				PROGRESS_REFRESH_TITLE				= "Odświeżanie listy";
//	static String				PROGRESS_SEARCH_TITLE				= "Szukanie synsetów";
//	private static String		WINDOW_SEARCH_TITLE					= "Wyszukiwanie";
//	private static String		LABEL_SYNSETS						= "Synsety:";
//	private static String		WORKSTATE_SPECIAL_VALUE				= "Nie lub częściowo przetworzone";
//	private static String		VALUE_DOMAIN_ALL					= "Wszystkie";
//	private static String		VALUE_STATUS_ALL					= "Wszystkie";
//	private static String		VALUE_RELATIONS_ALL					= "Wszystkie";
//	private static String		LABEL_BUTTON_SEARCH					= "Szukaj";
//	private static String		LABEL_STATUS						= "Status:";
//	private static String		LABEL_FILTER						= "Wyszukaj:";
//	private static String		STANDARD_VALUE_FILTER				= "";
//	private static String		SUPER_MODE							= "SuperMode";
//	private static String		SUPER_MODE_VALUE					= "1";
//	private static String 		LABEL_PART_OF_SPEECH				= "Część mowy:";
//	private static final int	MAX_ITEMS_COUNT						= 500;
//
//	ToolTipList					synsetsList;
//	private JLabel				infoLabel;
//	private JScrollPane			synsetsScroll;
//	private JTextField			filterEdit;
//	private ComboBoxPlain		statusCombo;
//	private ComboBoxPlain		domainCombo;
//	private ComboBoxPlain		relationsCombo;
//	private ComboBoxPlain		partofspeechCombo;
//	private JButton				buttonSearch;
//	private JCheckBox			limitResult;
//
//	private boolean				quiteMode							= false;
//
//	// elementy z danymi
//	String						oldFilter							= null;
//	int							oldStatus							= 0;
//	String						oldDomain							= null;
//	RelationType				oldRelation							= null;
//	int							oldPos								= -1;
//	Synset					lastSynset							= null;
//	GenericListModel<Synset>	listModel							= new GenericListModel<Synset>();
//
//	// menu
//	private JMenuItem			setValidatedStatus;
//	private JMenuItem			setErrorStatus;
//	private JMenuItem			setDoneStatus;
//	private JMenuItem			setTodoStatus;
//
//	/*
//	 * (non-Javadoc)
//	 * @see pl.wroc.pwr.ci.plwordnet.workbench.implementation.AbstractViewUI#initialize(javax.swing.JPanel)
//	 */
//	@Override
//	protected void initialize(JPanel content) {
//		// ustawienie layoutu
//		content.setLayout(new RiverLayout());
//
//		// utworzenie pol
//		filterEdit = new TextFieldPlain(STANDARD_VALUE_FILTER);
//		filterEdit.setFocusAccelerator('f');
//		filterEdit.addKeyListener(this);
//
//		statusCombo = new ComboBoxPlain();
//		statusCombo.addItem(VALUE_STATUS_ALL);
//		statusCombo.addItem(WORKSTATE_SPECIAL_VALUE);
//
//		domainCombo = new ComboBoxPlain();
//		domainCombo.setPreferredSize(new Dimension(150, 20));
//
//		// odswiezenie domen
//		refreshDomains();
//
//		// dodanie elementow z enum
//		for (WorkState workState : WorkState.values())
//			statusCombo.addItem(workState.toString());
//		statusCombo.setPreferredSize(new Dimension(150, 20));
//
//		limitResult = new JCheckBox(String.format(LABEL_LIMIT_TO, "" + MAX_ITEMS_COUNT));
//		limitResult.setSelected(true);
//
//		infoLabel = new JLabel();
//		infoLabel.setText(String.format(VALUE_COUNT_SIMPLE, "0"));
//
//		buttonSearch = new ButtonExt(LABEL_BUTTON_SEARCH, this, KeyEvent.VK_K);
//
//		synsetsList = new ToolTipList(workbench, listModel, ToolTipGenerator.getGenerator());
//		// ustawienie pojedynczego zaznaczania
//		synsetsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		synsetsList.getSelectionModel().addListSelectionListener(this);
//		synsetsList.setCellRenderer(new ColorSynsetListRenderer());
//		synsetsList.addMouseListener(this);
//
//		synsetsScroll = new JScrollPane(synsetsList);
//		synsetsScroll.addComponentListener(new ComponentAdapter() {
//			@Override
//			public void componentResized(ComponentEvent e) {
//				Dimension currentDim = e.getComponent().getSize();
//				listModel.setTag(new Integer(currentDim.width - 20));
//				//synsetsList.updateUI();
//			}
//		});
//
//		relationsCombo = new ComboBoxPlain();
//		relationsCombo.addItem(new CustomDescription<RelationType>(VALUE_RELATIONS_ALL, null));
//
//		/* Utworzenie comboboxu do wyboru klasy fleksyjnej */
//		partofspeechCombo = new ComboBoxPlain();
//		partofspeechCombo.addItem(VALUE_STATUS_ALL);
//		for (PartOfSpeech p : PosManager.getInstance().getAllPOSes())
//			partofspeechCombo.addItem(p.toString());
//		
//		// odswiezenie relacji
//		refreshRelations();
//
//		JPanel criterias = new JPanel();
//		criterias.setLayout(new RiverLayout());
//		criterias.add("", new LabelExt(LABEL_FILTER, 'w', filterEdit));
//		criterias.add("br hfill", filterEdit);
//		criterias.add("br", new LabelExt(LABEL_STATUS, 's', statusCombo));
//		criterias.add("br hfill", statusCombo);
//		criterias.add("br", new LabelExt(DOMAIN_LABEL, 'd', domainCombo));
//		criterias.add("br hfill", domainCombo);
//		criterias.add("br", new LabelExt(RELATION_LABEL, 'r', relationsCombo));
//		criterias.add("br hfill", relationsCombo);
//		criterias.add("br", new LabelExt(LABEL_PART_OF_SPEECH, 'f', partofspeechCombo));
//		criterias.add("br hfill", partofspeechCombo);
//		criterias.add("br left", limitResult);
//
//		int height = criterias.getPreferredSize().height;
//		criterias.setMaximumSize(new Dimension(0, height));
//		criterias.setMinimumSize(new Dimension(0, height));
//		criterias.setPreferredSize(new Dimension(0, height));
//
//		final int scrollHeight = 150;
//		JScrollPane scroll = new JScrollPane(criterias);
//		scroll.setMaximumSize(new Dimension(0, scrollHeight));
//		scroll.setMinimumSize(new Dimension(0, scrollHeight));
//		scroll.setPreferredSize(new Dimension(0, scrollHeight));
//		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//
//		content.setLayout(new RiverLayout());
//		content.add("hfill", scroll);
//		content.add("br center", buttonSearch);
//		content.add("br left", new LabelExt(LABEL_SYNSETS, 'n', synsetsList));
//		content.add("br hfill vfill", synsetsScroll);
//		content.add("br left", infoLabel);
//
//		// elementy menu
//		setDoneStatus = new JMenuItem(MENU_SET_DONE_STATUS_LABEL);
//		setDoneStatus.addActionListener(this);
//		setTodoStatus = new JMenuItem(MENU_SET_TODO_STATUS_LABEL);
//		setTodoStatus.addActionListener(this);
//		setValidatedStatus = new JMenuItem(MENU_SET_VALIDATED_STATUS_LABEL);
//		setValidatedStatus.addActionListener(this);
//		setErrorStatus = new JMenuItem(MENU_SET_ERROR_STATUS_LABEL);
//		setErrorStatus.addActionListener(this);
//
//		// instalacja popup menu
//		synsetsList.addPopupItem(setTodoStatus);
//		synsetsList.addPopupItem(setDoneStatus);
//
//		// tylko dla koordynatorów
//		if (workbench.getParam(SUPER_MODE) != null && workbench.getParam(SUPER_MODE).equals(SUPER_MODE_VALUE)) {
//			synsetsList.addPopupItem(setValidatedStatus);
//			synsetsList.addPopupItem(setErrorStatus);
//		}
//
//	}
//
//	/**
//	 * Odswiezenie listy relacji
//	 * 
//	 */
//	private void refreshRelations() {
//		if (relationsCombo == null)
//			return;
//		
//		Collection<RelationType> types = RemoteUtils.relationTypeRemote.dbGetLeafs(RelationArgument.SYNSET);
//
//		quiteMode = true;// nie wywolywac akcji
//		int selected = relationsCombo.getSelectedIndex();
//
//		relationsCombo.removeAllItems();
//		relationsCombo.addItem(new CustomDescription<RelationType>(VALUE_RELATIONS_ALL, null));
//		// dodanie elementow z enum
//		for (RelationType t : types) {
//			relationsCombo.addItem(new CustomDescription<RelationType>(RemoteUtils.relationTypeRemote.dbGetFullName(t).getFullName(), t));
//		}
//
//		if (selected != -1)
//			relationsCombo.setSelectedIndex(selected);
//		quiteMode = false;
//	}
//
//	/**
//	 * zaznaczenie w tabeli zostalo zmienione
//	 */
//	public void valueChanged(ListSelectionEvent arg0) {
//		if (synsetsList == null)
//			return;
//		if (arg0 != null && arg0.getValueIsAdjusting())
//			return;
//		int returnValue = synsetsList.getSelectedIndex();
//		if (listModel != null) {
//			Synset synset = listModel.getObjectAt(returnValue);
//
//			// powiadomienie zainteresowanych
//			listeners.notifyAllListeners(synsetsList.getSelectedIndices().length == 1 ? synset : null);
//		}
//	}
//
//	/**
//	 * wywołanie szukania elementu
//	 */
//	private void invokeSearch() {
//		// odczyt filtrów
//		oldFilter = filterEdit.getText();
//		oldStatus = statusCombo.getSelectedIndex();
//		oldDomain = (String) (domainCombo.getSelectedIndex() == 0 ? null : domainCombo.getItemAt(domainCombo.getSelectedIndex()));
//		oldPos = partofspeechCombo.getSelectedIndex() - 1; 
//		if (relationsCombo.getSelectedIndex() == 0) {
//			oldRelation = null;
//		} else {
//			@SuppressWarnings("unchecked")
//			CustomDescription<RelationType> item = (CustomDescription<RelationType>) relationsCombo.getItemAt(relationsCombo.getSelectedIndex());
//			oldRelation = item.getObject();
//		}
//		refreshData();
//	}
//
//	/**
//	 * odświeżenie listy synsetow
//	 */
//	public void refreshData() {
//		final IntegerContainer realSize = new IntegerContainer();
//		final int limitSize = limitResult.isSelected() ? MAX_ITEMS_COUNT : 0;
//
//		// uruchomienie glownego watku
//		new AbstractProgressThread(Tools.findFrame(getContent()), WINDOW_SEARCH_TITLE, null) {
//			/**
//			 * glowna procedura watku
//			 */
//			@Override
//			protected void mainProcess() {
//				try{
//					// ustawienie parametrow dla paska postepu
//					progress.setGlobalProgressParams(0, 2);
//					progress.setProgressParams(0, 1, PROGRESS_SEARCH_TITLE);
//	
//					// odczyt danych
//					Collection<Synset> synsets = RelationsDA.getSynsets(oldFilter, oldStatus, oldDomain, oldRelation, limitSize, realSize, oldPos);
//					
//					progress.setProgressParams(0, 1, PROGRESS_REFRESH_TITLE);
//					progress.setGlobalProgressValue(1);
//	
//					// odczytanie zaznaczonego synsetu
//					if (lastSynset == null && synsetsList != null && !synsetsList.isSelectionEmpty()) {
//						lastSynset = listModel.getObjectAt(synsetsList.getSelectedIndex());
//					}
//	
//					// ustawienie danych w kolekcji
//					listModel.setCollection(synsets);
//				}
//				catch (Exception ex){
//					Main.logException(ex);
//				}
//			}
//		};
//
//		synchronized (synsetsList) {			
//			if (synsetsList != null) {
//				int index = lastSynset != null ? listModel.getIndex(lastSynset) : -1;
//				if (index == -1 && listModel.getSize() > 0)
//					index = 0;
//				if (synsetsList != null) {
//					if (index != -1 && index < listModel.getSize()) {
//						try {
//							// Check if it's not alredy selected
//							if (index != synsetsList.getSelectedIndex()) {
//								Toolkit.getDefaultToolkit().sync();
//								synsetsList.setSelectedIndex(index);
//								Toolkit.getDefaultToolkit().sync();
//								synsetsList.ensureIndexIsVisible(index);
//							} else {
//								valueChanged(null);
//							}
//						} catch (Exception e) {
//							Main.logException(e);
//						}
//					} else {
//						synsetsList.clearSelection();
//					}
//				}
//				infoLabel.setText(String.format(listModel.getSize() < realSize.getValue() ? VALUE_COUNT_FULL : VALUE_COUNT_SIMPLE, "" + listModel.getSize(), "" + realSize.getValue()));
//			}
//		}
//		lastSynset = null;
//
//		// odswiezenie domen
//		refreshDomains();
//
//		// odswiezenie relacji
//		refreshRelations();
//	}
//
//	/**
//	 * odswiezenei listy domen
//	 * 
//	 */
//	private void refreshDomains() {
//		if (domainCombo == null)
//			return;
//
//		// dodac domeny
//		Domain[] domains = DomainManager.getInstance().sortedDomainsAsArray();
//
//		quiteMode = true;// nie wywolywac akcji
//		int selected = domainCombo.getSelectedIndex();
//
//		domainCombo.removeAllItems();
//		domainCombo.addItem(VALUE_DOMAIN_ALL);
//		// dodanie elementow z enum
//		for (Domain domain : domains)
//			domainCombo.addItem(domain.toString());
//		if (selected != -1)
//			domainCombo.setSelectedIndex(selected);
//		quiteMode = false;
//	}
//
//	/**
//	 * wciśnięto przycisk szukaj
//	 */
//	public void actionPerformed(ActionEvent arg0) {
//		if (quiteMode)
//			return;
//		if (arg0.getSource() == buttonSearch) {
//			invokeSearch();
//			if (synsetsList != null)
//				synsetsList.grabFocus();
//
//			// wywolanie ustaw status sprawdzone
//		} else if (arg0.getSource() == setValidatedStatus || arg0.getSource() == setErrorStatus || arg0.getSource() == setTodoStatus || arg0.getSource() == setDoneStatus) {
//			int returnValues[] = synsetsList.getSelectedIndices();
//			if (returnValues == null || returnValues.length == 0)
//				return;
//			// warto sie zapytac
//			if (returnValues.length != 1 && DialogBox.showYesNo(QUESTION_SET_STATUS_MESSAGE) != DialogBox.YES)
//				return;
//
//			// nowa wartosc statusu
//			WorkState ws = WorkState.WORKING;
//			if (arg0.getSource() == setValidatedStatus)
//				ws = WorkState.VALIDATED;
//			else if (arg0.getSource() == setErrorStatus)
//				ws = WorkState.MISTAKE;
//			else if (arg0.getSource() == setDoneStatus)
//				ws = WorkState.DONE;
//			else if (arg0.getSource() == setTodoStatus)
//				ws = WorkState.TODO;
//
//			// zmiana statusu jednostki
//			for (int i : returnValues) {
//				Synset synset = listModel.getObjectAt(i);
//				synset = LexicalDA.refresh(synset);
//				if (!LexicalDA.updateSynset(synset, ws)) {
//					DialogBox.showError(String.format(MESSAGE_ERROR_CANNOT_CHANGE_STATUS, synset.toString()));
//				}
//			}
//			redrawList();
//		}
//	}
//
//	/**
//	 * zdarzenie wciśnięcia klawisza w oknie z filtrem
//	 */
//	@Override
//	public void keyPressed(KeyEvent arg0) {
//		super.keyPressed(arg0);
//		if (arg0.getKeyChar() == KeyEvent.VK_ENTER && !arg0.isConsumed()) {
//			arg0.consume();
//			invokeSearch(); // wywołanie szukania
//		}
//	}
//
//	@Override
//	public void keyTyped(KeyEvent arg0) {
//		/***/
//	}
//
//	@Override
//	public void keyReleased(KeyEvent arg0) {
//		/***/
//	}
//
//	/**
//	 * ustawienie ostatnio używanego synsetu
//	 * @param synset - synset
//	 * @param textFilter - filtr do wyszukiwania
//	 */
//	public void setLastSynset(Synset synset, String textFilter) {
//		this.lastSynset = synset;
//		quiteMode = true; // tryb cichy
//		this.filterEdit.setText(textFilter);
//		this.statusCombo.setSelectedIndex(0);
//		oldFilter = textFilter;
//		oldStatus = 0;
//		quiteMode = false;// wylaczenie trybu cichego
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see AbstractViewUI#getRootComponent()
//	 */
//	@Override
//	public JComponent getRootComponent() {
//		return synsetsList;
//	}
//
//	/**
//	 * odrysowanie zawartości listy bez ponownego odczytu danych
//	 */
//	public void redrawList() {
//		synsetsList.repaint();
//	}
//
//	/**
//	 * Odczytanie aktualnie ustawionego filtra
//	 * @return filter
//	 */
//	public String getFilter() {
//		return filterEdit != null ? filterEdit.getText() : "";
//	}
//
//	/**
//	 * Ustawienie nowej wartosci dla filtra
//	 * @param filter - nowa wartosc dla filtra
//	 */
//	public void setFilter(String filter) {
//		if (filterEdit != null)
//			filterEdit.setText(filter);
//		oldFilter = filterEdit.getText();
//	}
//
//	/**
//	 * Odczytanie aktualnie ustawionej dziedziny
//	 * @return dziedzina
//	 */
//	public Domain getDomain() {
//		if (domainCombo == null)
//			return null;
//		return domainCombo.getSelectedIndex() <= 0 ? null : DomainManager.getInstance().decode((String) domainCombo.getItemAt(domainCombo.getSelectedIndex()));
//	}
//
//	/**
//	 * Ustawienie nowej wartosci dla dziedziny
//	 * @param domain - wartosc dziedziny
//	 */
//	public void setDomain(Domain domain) {
//		if (domainCombo == null)
//			return;
//		if (domain == null) {
//			domainCombo.setSelectedIndex(0);
//		} else {
//			domainCombo.setSelectedItem(domain.toString());
//		}
//		oldDomain = (String) (domainCombo.getSelectedIndex() == 0 ? null : domainCombo.getItemAt(domainCombo.getSelectedIndex()));
//	}
//
//	/**
//	 * Odczytanie wybranego statusu
//	 * @return wybrany status
//	 */
//	public int getStatus() {
//		return statusCombo != null ? statusCombo.getSelectedIndex() : 0;
//	}
//
//	/**
//	 * Ustawienie nowego statusu
//	 * @param status - status
//	 */
//	public void setStatus(int status) {
//		if (statusCombo == null)
//			return;
//		statusCombo.setSelectedIndex(status);
//		oldStatus = statusCombo.getSelectedIndex();
//	}
//
//	/**
//	 * użytko myszi na liście
//	 */
//	public void mouseReleased(MouseEvent arg0) {
//		if (arg0.isPopupTrigger()) {
//			// czy jest coś zaznaczone
//			int returnValue = synsetsList.getSelectedIndex();
//			setDoneStatus.setEnabled(returnValue != -1);
//			setValidatedStatus.setEnabled(returnValue != -1);
//			setErrorStatus.setEnabled(returnValue != -1);
//		}
//	}
//
//	public void mouseClicked(MouseEvent e) {
//		/***/
//	}
//
//	public void mouseEntered(MouseEvent e) {
//		/***/
//	}
//
//	public void mouseExited(MouseEvent e) {
//		/***/
//	}
//
//	public void mousePressed(MouseEvent e) {
//		/***/
//	}
//}
