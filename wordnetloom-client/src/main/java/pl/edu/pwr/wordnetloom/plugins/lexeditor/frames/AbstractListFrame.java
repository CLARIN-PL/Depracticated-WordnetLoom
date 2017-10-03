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

package pl.edu.pwr.wordnetloom.plugins.lexeditor.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.utils.Hints;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.systems.ui.ButtonExt;

/**
 * okno wyswietlajace liste elementow
 * 
 * @author Max
 * @param <T>
 *            typ przechowywanego obiektu
 * @param <G>
 *            typ obiektu sluzacego jako filtr
 */
abstract public class AbstractListFrame<T, G> extends
        IconDialog implements ActionListener, ListSelectionListener,
		KeyListener, MouseListener {

	private static final String STANDARD_VALUE_FILTER = "";
	public static final int WIDTH = 300, HEIGHT = 400;

	// id
	private static final long serialVersionUID = 1L;

	// elementu interfejsu uzytkownika
	protected JTextField filterEdit;
	private JList itemsList; // rozłączny model i lista? że co?
	private ButtonExt buttonChoose;
	private ButtonExt buttonSearch;
	private ButtonExt buttonCancel;
	private ButtonExt buttonNew;

	// model z danymi
	protected GenericListModel<T> listModel = null;
	protected G filterObject = null;
	protected Workbench workbench;

	// wybrane elementy
	protected Collection<T> selectedElements = null;

	/**
	 * konstruktor
	 * 
	 * @param workbench -
	 *            środwisko
	 * @param title -
	 *            tytuł okienka
	 * @param itemsLabelText -
	 *            tytuł listy z elementami
	 * @param x -
	 *            polozenie okna X
	 * @param y -
	 *            polozenie okna Y
	 * @param useButtonNew -
	 *            czy wyswietlic przycisk nowy
	 */
	protected AbstractListFrame(Workbench workbench, String title,
			String itemsLabelText, int x, int y, boolean useButtonNew) {
		super(workbench.getFrame(), title, x-WIDTH/2, y, WIDTH, HEIGHT);
		this.workbench = workbench;
		this.listModel = new GenericListModel<T>();
		this.setResizable(true);
		//this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.addKeyListener(this);

		// utworzenie elementow UI
		this.filterEdit = new JTextField(STANDARD_VALUE_FILTER);
		this.filterEdit.addKeyListener(this);

		this.buttonSearch = new ButtonExt(Labels.SEARCH_NO_COLON, this,
				KeyEvent.VK_S);
		this.buttonSearch.addKeyListener(this);

		this.itemsList = new ToolTipList(workbench, this.listModel,
				ToolTipGenerator.getGenerator());

		// this.itemsList=new JList(this.listModel);
		this.itemsList.addListSelectionListener(this);
		this.itemsList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.itemsList.addKeyListener(this);
		this.itemsList.addMouseListener(this);

		this.buttonChoose = new ButtonExt(Labels.SELECT, this,
				KeyEvent.VK_W);
		this.buttonChoose.setEnabled(false);
		this.buttonChoose.addKeyListener(this);

		this.buttonCancel = new ButtonExt(Labels.CANCEL, this,
				KeyEvent.VK_A);
		this.buttonCancel.setEnabled(true);
		this.buttonCancel.addKeyListener(this);

		this.buttonNew = new ButtonExt(Labels.NEW, this, KeyEvent.VK_N);
		this.buttonNew.setEnabled(true);
		this.buttonNew.setToolTipText(Hints.CREATE_NEW_UNIT);
		this.buttonNew.addKeyListener(this);

		// panel dolny z przyciskami
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(buttonChoose);
		buttonsPanel.add(buttonCancel);
		if (useButtonNew)
			buttonsPanel.add(buttonNew);

		this.add("", new LabelExt(Labels.SEARCH_COLON, 'w', filterEdit));
		this.add("br hfill", filterEdit);
		this.add("", buttonSearch);
		this.addExtraControlsInSearchBox();
		this.add("br left", new LabelExt(itemsLabelText, 'y', itemsList));
		this.add("br hfill vfill", new JScrollPane(itemsList));
		this.add("br center", buttonsPanel);
	}
	
	protected void addExtraControlsInSearchBox(){
		
	}

	/**
	 * wypelnienie kolekcji odpowiednimi danymi
	 * 
	 * @param filter -
	 *            filtr nazw
	 * @param filterObject -
	 *            obiekt wzgledem ktorego nastepuje filtrowanie
	 * @return wypełniona kolekcja danych
	 */
	abstract protected Collection<T> fillCollection(String filter,
			G filterObject);

	/**
	 * uruchomienie dodanie nowego elemtu
	 */
	abstract protected void invokeNew();

	/**
	 * wyświetlenie okna
	 * 
	 * @param newFilterObject -
	 *            nowy obiekt filtrujący
	 */
	protected void execute(G newFilterObject) {
		filterObject = newFilterObject;
		itemsList.clearSelection();
		filterEdit.setText("");
		filterEdit.grabFocus();
		setVisible(true); // wyswietlenie okna
	}

	/**
	 * odświeżenie danych w modelu
	 */
	protected void refreshListModel() {
		Collection<T> collection = fillCollection(this.filterEdit.getText(),
				this.filterObject); // wypelnienie kolekcji danymi
		this.listModel.setCollection(collection); // ustawienie kolekji jako
													// danych dla modelu
		if (this.itemsList != null) {
			this.itemsList.clearSelection();
			if (listModel.getSize() > 0) {
				this.itemsList.setSelectedIndex(0);
				this.itemsList.ensureIndexIsVisible(0);
				this.itemsList.grabFocus();
			}
		}
	}

	/**
	 * kliknięto w przycisk wybierz/zmieniono filtr
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == buttonSearch) {
			refreshListModel(); // uruchomienie szukania
		} else if (arg0.getSource() == buttonCancel) {
			setVisible(false); // uruchomienie szukania
			this.listModel.setCollection(null);
		} else if (arg0.getSource() == buttonNew) {
			invokeNew(); // uruchomienie szukania
		} else if (arg0.getSource() == buttonChoose) {
			// odczytanie listy zaznaczonych pozycji
			int indexs[] = itemsList.getSelectedIndices();
			if (indexs == null || indexs.length == 0)
				return; // nic nie zaznaczono

			// utworzenie list dla referencji
			selectedElements = new ArrayList<T>();

			for (int selectedIndex : indexs) {
				T elem = this.listModel.getObjectAt(selectedIndex); // odczyt
																	// zaznaczone
																	// elementu
				selectedElements.add(elem); // dodanie do listy zaznaczonych
			}

			if (this.verifySelectedElements()) {
				this.listModel.setCollection(null);
				setVisible(false);
			} else {
				selectedElements = null;
			}
		}
	}
	
	/**
	 * Sprawdza poprawność wybranych elementów
	 * @return TRUE jeżeli wybrane elementy są poprawne
	 */
	protected boolean verifySelectedElements(){
		return true;
	}

	/**
	 * zmienione zazanaczenie na liście
	 */
	public void valueChanged(ListSelectionEvent arg0) {
		if (arg0 != null && arg0.getValueIsAdjusting())
			return;
		int index = this.itemsList.getSelectedIndex();
		this.buttonChoose.setEnabled(index != -1);
	}

	/**
	 * zdarzenie wciśnięcia klawisza
	 */
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		case KeyEvent.VK_ENTER: // gdy wcisnieto enter
			if (arg0.getSource() == filterEdit)
				refreshListModel(); // wywołanie szukania
			if (arg0.getSource() == itemsList && buttonChoose.isEnabled())
				buttonChoose.doClick(); // wybranie jednostki
			arg0.consume();
			break;
		case KeyEvent.VK_ESCAPE: // gdy wcisnieto esc
			arg0.consume();
			setVisible(false);
			break;
		}
	}

	/**
	 * ustawienie trybu zaznaczania
	 * 
	 * @param multiSelect -
	 *            TRUE mozna zazaczyc wiele elementow, FALSE tylko jeden
	 */
	protected void setMultSelect(boolean multiSelect) {
		if (multiSelect)
			this.itemsList
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		else
			this.itemsList
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * gdy kliknięto myszką na liście
	 */
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2) { // czy był to dwuclick
			buttonChoose.doClick();
		}
	}

	public void mousePressed(MouseEvent arg0) {
		/***/
	}

	public void mouseReleased(MouseEvent arg0) {
		/***/
	}

	public void mouseEntered(MouseEvent arg0) {
		/***/
	}

	public void mouseExited(MouseEvent arg0) {
		/***/
	}

	public void keyReleased(KeyEvent arg0) {
		/***/
	}

	public void keyTyped(KeyEvent arg0) {
		/***/
	}
}
