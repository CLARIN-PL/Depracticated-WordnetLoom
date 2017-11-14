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

import pl.edu.pwr.wordnetloom.client.systems.misc.NodeDrawer;

import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private NodeDrawer parent = null;
    private boolean sizeSet = false;
    private boolean down = true;
    private Object focusTag = null;

    /**
     * konstruktor
     *
     * @param down - jest true to pionowe drzewo, jesli false to poziome
     */
    public TreePanel(boolean down) {
        setBackground(Color.WHITE);
        this.down = down;
    }

    /**
     * obiekt podeswietlany
     *
     * @param object - obiekt
     */
    public void setFocusTag(Object object) {
        focusTag = object;
    }

    /**
     * ustawienie kierunku drzewa
     *
     * @param down - jest true to pionowe drzewo, jesli false to poziome
     */
    public void setDirection(boolean down) {
        this.down = down;
        sizeSet = false;
        repaint();
    }

    /**
     * ustawienie danych do narysowania
     *
     * @param parent - ojciec
     */
    public void setNode(NodeDrawer parent) {
        this.parent = parent;
        sizeSet = false;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (parent != null) {
            g.setFont(g.getFont().deriveFont(9f));
            if (down) {
                parent.trialDrawY(g);
                parent.drawY(g, 10, 10, focusTag);
            } else {
                parent.trialDrawX(g);
                parent.drawX(g, 10, 10, focusTag);
            }

            if (!sizeSet) {
                sizeSet = true;
                Dimension size = new Dimension(parent.getBound().width + 20, parent.getBound().height + 20);
                setPreferredSize(size);
                setSize(size);
            }
        }
    }
}
