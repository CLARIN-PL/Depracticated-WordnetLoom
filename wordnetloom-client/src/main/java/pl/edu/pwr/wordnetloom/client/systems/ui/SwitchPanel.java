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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * panel zawierajacy dwa obiektu, ktore mozna wymieniac w zalenosci od
 * aktywnosci panelu
 *
 * @author Max
 */
public class SwitchPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Component enabled = null;
    private Component disabled = null;

    /**
     * konstruktor
     *
     * @param enabled - obiekt wyswietlany gdy aktywny
     * @param disabled - obiekt wyswietlany gdy nieaktywny
     * @param preferredSize - preferowana wielkosc obiektu
     */
    public SwitchPanel(Component enabled, Component disabled, Dimension preferredSize) {
        this.enabled = enabled;
        this.disabled = disabled;
        this.disabled.setEnabled(false);
        this.setPreferredSize(preferredSize);
        this.setLayout(new BorderLayout());
        this.add(enabled, BorderLayout.CENTER);
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        if (value) {
            this.remove(0);
            this.add(enabled, BorderLayout.CENTER);
            this.updateUI();
        } else {
            this.remove(0);
            this.add(disabled, BorderLayout.CENTER);
            this.updateUI();
        }
    }
}
