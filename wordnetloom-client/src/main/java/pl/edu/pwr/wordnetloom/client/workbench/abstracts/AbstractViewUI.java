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
package pl.edu.pwr.wordnetloom.client.workbench.abstracts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenersContainer;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * abstrakcyjny wygląd widoku
 *
 * @author Max
 */
public abstract class AbstractViewUI implements KeyListener {

    // lista sluchaczy wykorzystywana
    protected SimpleListenersContainer listeners = new SimpleListenersContainer();

    protected Workbench workbench;  // środowisko
    private JPanel content;       // głowny panel
    // lista skrótów działających w obrębie widoku, są one obsługiwane
    // lokalnie przez tą klasę
    protected Collection<ShortCut> viewScopeShortCuts = new ArrayList<ShortCut>();
    // lista skrótów działających w obrębie całej persepetywy, są zarządzane
    // przez workbench
    protected Collection<ShortCut> perspectiveScopeShortCuts = new ArrayList<ShortCut>();

    /**
     * Inicjalizacja wygladu
     *
     * @param workbench - sodowisko pracy
     */
    public void init(Workbench workbench) {
        this.workbench = workbench;
        content = new JPanel();           // przygotowanie głównego panelu
        initialize(content);            // inicjalizacja elementów należących do panelu
        setKeyListeners(content);       // ustawienie słuchaczy dla wszystkich elementów należących do panel
    }

    /**
     * Istawienie nasluchiwania wcisniecia klawisza dla wszystkich dzieci
     *
     * @param component
     */
    private void setKeyListeners(Component component) {
        if (component != null) {
            component.addKeyListener(this);
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            Component[] children = container.getComponents();
            for (Component child : children) {
                setKeyListeners(child);
            }
        }
    }

    /**
     * Utworzenie zawartosci panelu. Za pomocą tej metody dostawne są przyciski
     * i inne elementy interfjesu użytkownika
     *
     * @param content
     */
    protected abstract void initialize(JPanel content);

    /**
     * Dodanie słuchacza do listy sluchaczy
     *
     * @param newListener - nowy słuchacz
     */
    public void addActionListener(SimpleListenerInterface newListener) {
        listeners.add(newListener);
    }

    /**
     * Ustawienie koloru tła dla panelu oraz elementów do niego należących
     * takich jak inne panele i checkboxy
     *
     * @param color - nowy kolor
     */
    public void setBackgroundColor(Color color) {
        if (content != null) {
            content.setBackground(color);
            Component[] components = content.getComponents();
            if (components != null) {
                for (Component component : components) {
                    if (component instanceof JPanel || component instanceof JCheckBox
                            || component instanceof JTextArea) {
                        component.setBackground(color);
                    }
                }
            }
        }
    }

    /**
     * Instalacja skrotu klawiszowego dla przycisku, który działa w obrębie
     * danego okna
     *
     * @param button - przycisk
     * @param modifiers - modyfikator
     * @param keyCode - klawisz
     */
    public void installViewScopeShortCut(AbstractButton button, int modifiers, int keyCode) {
        viewScopeShortCuts.add(new ShortCut(button, modifiers, keyCode));
    }

    /**
     * Instalacja globalnego skrotu klawiszowego, który działa w obrębie całej
     * perspektywy - np. tak rozwiązywane sa przyciski na toolbarze
     *
     * @param button - przycisk
     * @param modifiers - modyfikacja
     * @param keyCode - kod
     */
    public void installPerspectiveScopeShortCut(AbstractButton button, int modifiers, int keyCode) {
        perspectiveScopeShortCuts.add(new ShortCut(button, modifiers, keyCode));
    }

    /**
     * Odczytanie glownego elementu okna, na którym ma zostać ustawiony focus w
     * sytuacji gdy następuje przejście pomiędzy widokami za pomocą Ctrl+1,
     * Ctrl+2
     *
     * @return glowny element
     */
    abstract public JComponent getRootComponent();

    /**
     * Wcisnieto jakiś przycisk na kontrolkach, w tym przypadku może to być
     * jakiś skrót klawiaturowy
     */
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getSource() instanceof JTextField) // dla pol tekstowych nie dziala
        {
            return;
        }
        // przejscie po skrotach lokalnych (globalne zostaną obsłużone przez swinga, gdyż
        // jest to niewidzialne menu)
        for (ShortCut shortCut : viewScopeShortCuts) {
            // wyszykiwanie skrótu
            if (arg0.getModifiers() == shortCut.getModifiers() && arg0.getKeyCode() == shortCut.getKeyCode()) {
                arg0.consume();      // konsumcja zdarzenia
                shortCut.doAction(); // wywołanie akcji związanej ze skrótem
                break;
            }
        }
    }

    public void keyTyped(KeyEvent arg0) {
        /**
         *
         */
    }

    public void keyReleased(KeyEvent arg0) {
        /**
         *
         */
    }

    /**
     * Get main content of the view.
     *
     * @return main panel
     */
    public JPanel getContent() {
        return content;
    }
}
