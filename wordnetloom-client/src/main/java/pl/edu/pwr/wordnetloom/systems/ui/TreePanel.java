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
import java.awt.Graphics;

import javax.swing.JPanel;

import pl.edu.pwr.wordnetloom.systems.misc.NodeDrawer;

/**
 * klasa rysująca drzewo
 * @author Max
 *
 */
public class TreePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private NodeDrawer parent=null;
	private boolean sizeSet=false;
	private boolean down=true;
	private Object focusTag=null;
	
	/**
	 * konstruktor
	 * @param down - jest true to pionowe drzewo, jesli false to poziome
	 */
	public TreePanel(boolean down) {
		this.setBackground(Color.WHITE);
		this.down=down;
	}

	/**
	 * obiekt podeswietlany
	 * @param object - obiekt
	 */
	public void setFocusTag(Object object) {
		this.focusTag=object;
	}
	
	/**
	 * ustawienie kierunku drzewa
	 * @param down - jest true to pionowe drzewo, jesli false to poziome
	 */
	public void setDirection(boolean down) {
		this.down=down;
		this.sizeSet=false;
		this.repaint();
	}
	
	/**
	 * ustawienie danych do narysowania
	 * @param parent - ojciec
	 */
	public void setNode(NodeDrawer parent) {
		this.parent=parent;
		this.sizeSet=false;
		this.repaint();
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g); // odrysowanie z nadrzednego

		if (parent!=null) {
			g.setFont(g.getFont().deriveFont(9f));
			if (down) {
				parent.trialDrawY(g);
				parent.drawY(g,10,10,focusTag);
			} else {
				parent.trialDrawX(g);
				parent.drawX(g,10,10,focusTag);
			}

			// korekta wymiarow
			if (!sizeSet) {
				sizeSet=true;
				Dimension size=new Dimension(parent.getBound().width+20,parent.getBound().height+20);
				this.setPreferredSize(size);
				this.setSize(size);
			}
		}
	}
}
