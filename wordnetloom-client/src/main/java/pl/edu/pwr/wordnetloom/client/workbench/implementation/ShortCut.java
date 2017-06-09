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
package pl.edu.pwr.wordnetloom.client.workbench.implementation;

import java.awt.Toolkit;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import pl.edu.pwr.wordnetloom.client.systems.misc.ActionWrapper;

/**
 * Klasa przechowujaca skroty klawiszowe dla przyciskow i komponentów oraz
 * związane z nimi akcje
 *
 * @author Max
 */
public class ShortCut {

    private KeyStroke keyStroke;   // kod klawisza
    private int modifiers;         // modifikator dla klawisza postaci Ctrl, Shift, Alt
    private AbstractButton button; // przycisk z którym związany jest skrót
    // jeśli jest to skrót do przycisku
    private JComponent component;  // komponent, który ma zostać podświetlony
    // jeśli jest to skrót do komponentu (widoku) typu Ctrl+1
    private JTabbedPane pane;      // kontener z zakładami
    private int tabIndex;          // numer zakładki
    private ActionWrapper action;  // akcja do wykonania

    /**
     * Konstruktor dla skrótu, który jest instalowany dla przycisku i powoduje
     * wywołanie kliknięcia w przycisk
     *
     * @param button - przycisk
     * @param modifiers - modifkacja
     * @param keyCode - skrot
     */
    public ShortCut(AbstractButton button, int modifiers, int keyCode) {
        this.button = button;
        this.keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        this.modifiers = modifiers;
        this.component = null;
        this.pane = null;
    }

    /**
     * Konstruktor dla skrótu, który jest instalowany dla okna i powoduje
     * przełączenie zakładaki oraz ustawienie focusu dla konkretnego komponentu
     *
     * @param pane - panel z zakladkami
     * @param component - komponent do podswietlenia
     * @param modifiers - modifkacja
     * @param keyCode - skrot
     */
    public ShortCut(JTabbedPane pane, JComponent component, int modifiers, int keyCode) {
        this.button = null;
        this.keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        this.component = component;
        this.pane = pane;
        this.tabIndex = pane.getTabCount() - 1;
    }

    public ShortCut(ActionWrapper action, int modifiers, int keyCode) {
        this.button = null;
        this.keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        this.component = null;
        this.pane = null;
    }

    /**
     * Ukonanie akcji zwiazanej ze skrotem
     */
    public void doAction() {
        // czy jest to skrót do przycisku
        // jeśli tak to wywoływany jest click
        if (button != null && button.isEnabled()) {
            button.doClick();

            // jest to skrót do komponentu
            // a więc ustawienie focuu
        } else if (component != null) {
            if (pane != null) {          // zmienie aktywnej zakladki
                pane.setSelectedIndex(tabIndex);
                Toolkit.getDefaultToolkit().sync();
            }
            component.grabFocus(); // ustawienie focusu
        } else if (action != null) {
            action.doAction(null, -1);
        }
    }

    /**
     * Odczyt skrotu klawiszowego
     *
     * @return skrot
     */
    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    /**
     * Odczyt modyfikacji związanej ze skrótem
     *
     * @return modyfikacja
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * Odczyt kodu klawisza
     *
     * @return kod
     */
    public int getKeyCode() {
        return keyStroke.getKeyCode();
    }
}
