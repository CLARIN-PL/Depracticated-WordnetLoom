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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

import se.datadosen.component.RiverLayout;

/**
 * klasa dialog zawierająca ikonę aplikacji
 * @author Max
 */
public class IconDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	static private int BOTTOM_MARGIN=40;

	/**
	 * konstruktor
	 * @param baseFrame
	 * @param title - tytul okna
	 * @param x - polozenie x
	 * @param y - polozenie y
	 * @param width - szerokosc okna
	 * @param height - wysokosc okna
	 *
	 */
	public IconDialog(JFrame baseFrame,String title,int x, int y,int width,int height) {
		super(baseFrame,title,true);
		// odczytanie rozmiarow ekranu
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		// koreka polożenia
		if (x + width>screenSize.width)
			x = screenSize.width-width;
		if (y+height+BOTTOM_MARGIN>screenSize.height)
			y=screenSize.height-height-BOTTOM_MARGIN;

		// ustawienie parametrów okna
		this.setBounds(x,y,width,height);
		this.setLayout(new RiverLayout());
	}

	/**
	 * konstruktor
	 * @param baseFrame
	 * @param title - tytul okna
	 * @param width - szerokosc okna
	 * @param height - wysokosc okna
	 *
	 */
	public IconDialog(JFrame baseFrame,String title,int width,int height) {
		super(baseFrame,title,true);
		// odczytanie rozmiarow ekranu
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		// koreka polo�enia
		int x=(screenSize.width-width)/2;
		int y=(screenSize.height-height)/2;

		// ustawienie parametrów okna
		this.setBounds(x,y,width,height);
		this.setLayout(new RiverLayout());
	}

	/**
	 * konstruktor - pelny ekran z marginesem 25,25,25,25
	 * @param baseFrame - okno bazowe
	 * @param title - tytul okna
	 *
	 */
	public IconDialog(JFrame baseFrame,String title) {
		super(baseFrame);
		// odczytanie rozmiarow ekranu
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		// koreka polo�enia
		int width=screenSize.width-50;
		int height=screenSize.height-80;
		int x=(screenSize.width-width)/2;
		int y=(screenSize.height-height)/2;

		// ustawienie parametrów okna
		this.setBounds(x,y,width,height);
		this.setLayout(new RiverLayout());
		this.setTitle(title);
		this.setModal(true);
	}
}