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
package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * klasa będąca rozbudowanym menu o pole tag
 *
 * @author Max
 */
public class MenuItemExt extends JMenuItem {

    private static final long serialVersionUID = 1L;
    private Object tag;

    /**
     * konstruktor
     *
     * @param text - tekst do wyświetlenia
     */
    public MenuItemExt(String text) {
        super(text);
        tag = null;
    }

    /**
     * konstruktor
     *
     * @param text - tekst do wyświetlenia
     * @param mnemonic - mnemonic
     */
    public MenuItemExt(String text, int mnemonic) {
        super(text);
        setMnemonic(mnemonic);
        tag = null;
    }

    /**
     * konstruktor
     *
     * @param text - tekst do wyświetlenia
     * @param mnemonic - mnemonic
     * @param actionListener - obsluga akcji menu
     */
    public MenuItemExt(String text, int mnemonic, ActionListener actionListener) {
        super(text);
        setMnemonic(mnemonic);
        addActionListener(actionListener);
        tag = null;
    }

    /**
     * konstruktor
     *
     * @param text - tekst do wyświetlenia
     * @param keyStroke - skrot klawiaturowy
     */
    public MenuItemExt(String text, KeyStroke keyStroke) {
        super(text);
        this.setAccelerator(keyStroke);
        tag = null;
    }

    /**
     * konstruktor
     *
     * @param text - tekst do wyświetlenia
     * @param keyStroke - skrot klawiaturowy
     * @param mnemonic - mnemonic
     */
    public MenuItemExt(String text, KeyStroke keyStroke, int mnemonic) {
        super(text);
        setMnemonic(mnemonic);
        this.setAccelerator(keyStroke);
        tag = null;
    }

    /**
     * odczytanie taga
     *
     * @return tag
     */
    public Object getTag() {
        return tag;
    }

    /**
     * ustawienie taga
     *
     * @param tag - tag
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }
}
