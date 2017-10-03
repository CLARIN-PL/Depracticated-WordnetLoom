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

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * klasa będąca nadkładką na przycisk, zawiera bardziej rozbudowane konstruktory
 * 
 * @author Max
 *
 */
public class ButtonExt extends JButton implements KeyListener {
	private static final long serialVersionUID = 1L;

	/**
	 * konstruktor
	 * 
	 * @param caption
	 *            - edytkieta
	 * @param actionListener
	 *            - nasłuchiwacz
	 */
	public ButtonExt(String caption, ActionListener actionListener) {
		super(caption);
		this.addActionListener(actionListener);
		this.addKeyListener(this);
	}

	public ButtonExt(ActionListener actionListener) {
		this.addActionListener(actionListener);
		this.addKeyListener(this);
	}

	/**
	 * konstruktor
	 * 
	 * @param icon
	 *            - ikonka dla przycisku
	 * @param actionListener
	 *            - nasłuchiwacz
	 */
	public ButtonExt(ImageIcon icon, ActionListener actionListener) {
		super(icon);
		this.addActionListener(actionListener);
		this.addKeyListener(this);
	}

	/**
	 * konstruktor
	 * 
	 * @param caption
	 *            - edytkieta
	 * @param actionListener
	 *            - nasłuchiwacz akcji
	 * @param mnemonic
	 *            - mnemonik
	 */
	public ButtonExt(String caption, ActionListener actionListener, int mnemonic) {
		super(caption);
		this.addActionListener(actionListener);
		this.setMnemonic(mnemonic);
		this.addKeyListener(this);
	}

	/**
	 * konstruktor
	 * 
	 * @param icon
	 *            - ikonka dla przycisku
	 * @param actionListener
	 *            - nasłuchiwacz akcji
	 * @param mnemonic
	 *            - mnemonik
	 */
	public ButtonExt(ImageIcon icon, ActionListener actionListener, int mnemonic) {
		super(icon);
		this.addActionListener(actionListener);
		this.setMnemonic(mnemonic);
		this.addKeyListener(this);
	}

	/**
	 * konstruktor
	 * 
	 * @param caption
	 *            - edytkieta
	 * @param actionListener
	 *            - nasłuchiwacz akcji
	 * @param keyListener
	 *            - nasluchiwacz klawiszy
	 * @param mnemonic
	 *            - mnemonik
	 */
	public ButtonExt(String caption, ActionListener actionListener, KeyListener keyListener, int mnemonic) {
		super(caption);
		this.addActionListener(actionListener);
		this.setMnemonic(mnemonic);
		this.addKeyListener(keyListener);
	}

	public void keyTyped(KeyEvent arg0) {
		/***/
	}

	public void keyReleased(KeyEvent arg0) {
		/***/
	}

	/**
	 * zdarzenie naciśnięcia enter na przycisku
	 */
	public void keyPressed(KeyEvent arg0) {
		// aby enter działał na przyciskach
		if (arg0.getSource() == this && arg0.getModifiers() == 0 && arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			arg0.consume();
			this.doClick();
		}
	}

}