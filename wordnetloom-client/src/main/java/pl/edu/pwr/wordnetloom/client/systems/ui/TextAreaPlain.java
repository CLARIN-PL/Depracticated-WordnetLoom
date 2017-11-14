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

import com.alee.laf.text.WebTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class TextAreaPlain extends WebTextArea {

    private static final long serialVersionUID = -2441734577355869232L;
    private static Font newFont = new JLabel().getFont().deriveFont(Font.PLAIN);
    private static Color disabledBackground = new JLabel().getBackground();
    private static Color enabledBackground = new JTextField().getBackground();
    private String oldValue = null;

    /**
     * konstruktor
     *
     * @param text - tekst
     */
    public TextAreaPlain(String text) {
        super(text);
        setFont(newFont);
        setDisabledTextColor(getForeground());
        setBorder(new JTextField().getBorder());
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(null);
    }

    /**
     * konstruktor
     *
     * @param text        - tekst
     * @param keyListener - sluchacz klawiszy
     * @param width       - szerokosc kontrolki
     * @param height      - wysokosc kontrolki
     */
    public TextAreaPlain(String text, KeyListener keyListener, int width, int height) {
        this(text);

        addKeyListener(keyListener);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        if (enable) {
            setBackground(enabledBackground);
        } else {
            setBackground(disabledBackground);
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        oldValue = text;
    }

    /**
     * czy tekst się zmienił
     *
     * @return true jeśli tekst się zmienił
     */
    public boolean wasTextChanged() {
        String value = getText();
        if (oldValue == null) {
            return value == null;
        }
        return !oldValue.equals(value);
    }

}
