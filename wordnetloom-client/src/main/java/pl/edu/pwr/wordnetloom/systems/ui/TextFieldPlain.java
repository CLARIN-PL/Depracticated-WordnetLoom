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

package pl.edu.pwr.wordnetloom.systems.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * nakladka na jtextfield wylaczajaca pogrubiona czcionke
 * 
 * @author Max
 * 
 */
public class TextFieldPlain extends JTextField {
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
	public TextFieldPlain(String text) {
		super(text);
		setFont(newFont);
		setDisabledTextColor(getForeground());
		setFocusHandler();
	}

	/**
	 * konstruktor
	 * 
	 * @param text - tekst
	 * @param keyListener - sluchacz klawiszy
	 * @param width - szerokosc kontrolki
	 * @param height - wysokosc kontrolki
	 */
	public TextFieldPlain(String text, KeyListener keyListener, int width,
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
		if (enable)
			setBackground(enabledBackground);
		else
			setBackground(disabledBackground);
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
		if(oldValue == null && value == null){
			return false;
		}
		if(oldValue == null && value !=null){
			return true;
		}
		if(oldValue != null && value ==null){
			return true;
		}
		return !oldValue.equals(value);
	}

	/**
	 * ustawia odpowiednią obsługę zdarzenia uzyskania focusu przez co
	 * kliknięcie w kontrolke zazancza cały tekst
	 * 
	 */
	private void setFocusHandler() {
		this.addFocusListener(new FocusListener() {
			/***
			 * Kontrolka utraciła focus
			 */
			public void focusLost(FocusEvent e) {
				/***/
			}

			/**
			 * Kontrolka uzyskała focus
			 */
			public void focusGained(FocusEvent e) {
				setSelectionStart(0);
				setSelectionEnd(getText().length());
			}
		});
	}
}
