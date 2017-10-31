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
package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * okno wyswietlajace liste elementow
 *
 * @param <T> typ przechowywanego obiektu
 * @param <G> typ obiektu sluzacego jako filtr
 * @author Max
 */
abstract public class AbstractListFrame<T, G> extends
        DialogWindow implements ActionListener, ListSelectionListener,
        KeyListener, MouseListener {

    private static final String STANDARD_VALUE_FILTER = "";
    public static final int WIDTH = 300, HEIGHT = 400;

    private static final long serialVersionUID = 1L;

    protected JTextField filterEdit;
    private final JList itemsList;
    private final MButton buttonChoose;
    private final MButton buttonSearch;
    private final MButton buttonCancel;
    private final MButton buttonNew;

    // model z danymi
    protected GenericListModel<T> listModel = null;
    protected G filterObject = null;
    protected Workbench workbench;

    // wybrane elementy
    protected Collection<T> selectedElements = null;

    /**
     * konstruktor
     *
     * @param workbench      - środwisko
     * @param title          - tytuł okienka
     * @param itemsLabelText - tytuł listy z elementami
     * @param x              - polozenie okna X
     * @param y              - polozenie okna Y
     * @param useButtonNew   - czy wyswietlic przycisk nowy
     */
    protected AbstractListFrame(Workbench workbench, String title,
                                String itemsLabelText, int x, int y, boolean useButtonNew) {
        super(workbench.getFrame(), title, x - WIDTH / 2, y, WIDTH, HEIGHT);
        this.workbench = workbench;
        listModel = new GenericListModel<>();
        setResizable(true);
        //this.setAlwaysOnTop(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addKeyListener(this);

        // utworzenie elementow UI
        filterEdit = new JTextField(STANDARD_VALUE_FILTER);
        filterEdit.addKeyListener(this);

        buttonSearch = MButton.buildSearchButton(this)
                .withKeyListener(this);

        itemsList = new ToolTipList(workbench, listModel,
                ToolTipGenerator.getGenerator());

        // this.itemsList=new JList(this.listModel);
        itemsList.addListSelectionListener(this);
        itemsList
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        itemsList.addKeyListener(this);
        itemsList.addMouseListener(this);

        buttonChoose = MButton.buildSelectButton(this)
                .withEnabled(false)
                .withKeyListener(this);

        buttonCancel = MButton.buildCancelButton(this)
                .withEnabled(true)
                .withKeyListener(this);

        buttonNew = MButton.buildAddButton()
                .withCaption(Labels.NEW)
                .withToolTip(Hints.CREATE_NEW_UNIT)
                .withKeyListener(this)
                .withActionListener(this)
                .withMnemonic(KeyEvent.VK_N)
                .withEnabled(true);

        // panel dolny z przyciskami
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(buttonChoose);
        buttonsPanel.add(buttonCancel);
        if (useButtonNew) {
            buttonsPanel.add(buttonNew);
        }

        add("", new LabelExt(Labels.SEARCH_COLON, 'w', filterEdit));
        add("br hfill", filterEdit);
        add("", buttonSearch);
        addExtraControlsInSearchBox();
        add("br left", new LabelExt(itemsLabelText, 'y', itemsList));
        add("br hfill vfill", new JScrollPane(itemsList));
        add("br center", buttonsPanel);
    }

    protected void addExtraControlsInSearchBox() {

    }

    /**
     * wypelnienie kolekcji odpowiednimi danymi
     *
     * @param filter       - filtr nazw
     * @param filterObject - obiekt wzgledem ktorego nastepuje filtrowanie
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
     * @param newFilterObject - nowy obiekt filtrujący
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
        Collection<T> collection = fillCollection(filterEdit.getText(),
                filterObject); // wypelnienie kolekcji danymi
        listModel.setCollection(collection); // ustawienie kolekji jako
        // danych dla modelu
        if (itemsList != null) {
            itemsList.clearSelection();
            if (listModel.getSize() > 0) {
                itemsList.setSelectedIndex(0);
                itemsList.ensureIndexIsVisible(0);
                itemsList.grabFocus();
            }
        }
    }

    /**
     * kliknięto w przycisk wybierz/zmieniono filtr
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonSearch) {
            refreshListModel(); // uruchomienie szukania
        } else if (event.getSource() == buttonCancel) {
            setVisible(false); // uruchomienie szukania
            listModel.setCollection(null);
        } else if (event.getSource() == buttonNew) {
            invokeNew(); // uruchomienie szukania
        } else if (event.getSource() == buttonChoose) {
            // odczytanie listy zaznaczonych pozycji
            int[] indexs = itemsList.getSelectedIndices();
            if (indexs == null || indexs.length == 0) {
                return;
            }

            selectedElements = new ArrayList<>();

            for (int selectedIndex : indexs) {
                T elem = listModel.getObjectAt(selectedIndex);
                selectedElements.add(elem);
            }

            if (verifySelectedElements()) {
                listModel.setCollection(null);
                setVisible(false);
            } else {
                selectedElements = null;
            }
        }
    }

    /**
     * Sprawdza poprawność wybranych elementów
     *
     * @return TRUE jeżeli wybrane elementy są poprawne
     */
    protected boolean verifySelectedElements() {
        return true;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event != null && event.getValueIsAdjusting()) {
            return;
        }
        int index = itemsList.getSelectedIndex();
        buttonChoose.setEnabled(index != -1);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyChar()) {
            case KeyEvent.VK_ENTER:
                if (event.getSource() == filterEdit) {
                    refreshListModel();
                }
                if (event.getSource() == itemsList && buttonChoose.isEnabled()) {
                    buttonChoose.doClick();
                }
                event.consume();
                break;
            case KeyEvent.VK_ESCAPE:
                event.consume();
                setVisible(false);
                break;
        }
    }

    /**
     * ustawienie trybu zaznaczania
     *
     * @param multiSelect - TRUE mozna zazaczyc wiele elementow, FALSE tylko
     *                    jeden
     */
    protected void setMultSelect(boolean multiSelect) {
        if (multiSelect) {
            itemsList
                    .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            itemsList
                    .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            buttonChoose.doClick();
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
