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

import com.alee.laf.text.WebTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.io.Serializable;

public class MTextField extends WebTextField implements Serializable {

    private static Font newFont = new JLabel().getFont().deriveFont(Font.PLAIN);
    private static Color disabledBackground = new JLabel().getBackground();
    private static Color enabledBackground = new JTextField().getBackground();
    private String oldValue = null;

    public MTextField(String text) {
        super(text);
        setFont(newFont);
        setDisabledTextColor(getForeground());
        setFocusHandler();
    }

    public MTextField(String text, KeyListener keyListener, int width,
                      int height) {
        this(text);

        addKeyListener(keyListener);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        setFocusHandler();
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

    public boolean wasTextChanged() {
        String value = getText();
        if (oldValue == null && value == null) {
            return false;
        }
        if (oldValue == null && value != null) {
            return true;
        }
        if (oldValue != null && value == null) {
            return true;
        }
        return !oldValue.equals(value);
    }

    /**
     * ustawia odpowiednią obsługę zdarzenia uzyskania focusu przez co
     * kliknięcie w kontrolke zazancza cały tekst
     */
    private void setFocusHandler() {
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
            }

            @Override
            public void focusGained(FocusEvent e) {
                setSelectionStart(0);
                setSelectionEnd(getText().length());
            }
        });
    }
}
