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

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import se.datadosen.component.RiverLayout;

/**
 * klasa frame zawierająca ikonę aplikacji
 *
 * @author Max
 */
public class IconFrame extends JFrame {

    private static final String FILE_MAIN_ICON = "icons/wordnet.gif";
    private static final long serialVersionUID = 1L;
    static private int BOTTOM_MARGIN = 40;
    JFrame baseFrame;

    /**
     * konstruktor
     *
     */
    public IconFrame() {
        setIconImage(new ImageIcon(IconFrame.class.getClassLoader().getResource(FILE_MAIN_ICON)).getImage());
    }

    /**
     * konstruktor
     *
     * @param baseFrame
     * @param title - tytul okna
     * @param width - szerokosc okna
     * @param height - wysokosc okna
     *
     */
    public IconFrame(JFrame baseFrame, String title, int width, int height) {
        this(title, width, height);
        this.baseFrame = baseFrame;
    }

    /**
     * konstruktor
     *
     * @param title - tytul okna
     * @param x - polozenie x
     * @param y - polozenie y
     * @param width - szerokosc okna
     * @param height - wysokosc okna
     *
     */
    public IconFrame(String title, int x, int y, int width, int height) {
        this.setIconImage(new ImageIcon(IconFrame.class.getClassLoader().getResource(FILE_MAIN_ICON)).getImage());
        // odczytanie rozmiarow ekranu

        Dimension screenSize = new Dimension(
                (int) this.getGraphicsConfiguration().getBounds().getWidth(),
                (int) this.getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int offsetX = getGraphicsConfiguration().getBounds().x;
        int offsetY = getGraphicsConfiguration().getBounds().y;

        // koreka polożenia
        if (x + width > screenSize.width) {
            x = offsetX + screenSize.width - width;
        }
        if (y + height + BOTTOM_MARGIN > screenSize.height) {
            y = offsetY + screenSize.height - height - BOTTOM_MARGIN;
        }

        // ustawienie parametrów okna
        this.setBounds(x, y, width, height);
        this.setLayout(new RiverLayout());
        this.setTitle(title);
    }

    /**
     * konstruktor
     *
     * @param width - szerokosc okna
     * @param height - wysokosc okna
     *
     */
    public IconFrame(int width, int height) {
        this.setIconImage(new ImageIcon(FILE_MAIN_ICON).getImage());
        // odczytanie rozmiarow ekranu
        Dimension screenSize = new Dimension(
                (int) this.getGraphicsConfiguration().getBounds().getWidth(),
                (int) this.getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int x = getGraphicsConfiguration().getBounds().x + (screenSize.width - width) / 2;
        int y = (screenSize.height - height - BOTTOM_MARGIN) / 2;

        // ustawienie parametrów okna
        this.setBounds(x, y, width, height);
        this.setLayout(new RiverLayout());
    }

    /**
     * konstruktor
     *
     * @param title - tytul okna
     * @param width - szerokosc okna
     * @param height - wysokosc okna
     *
     */
    public IconFrame(String title, int width, int height) {
        this(width, height);
        this.setTitle(title);
    }

    /**
     * konstruktor - pelny ekran z marginesem 25,25,25,25
     *
     * @param baseFrame - okno bazowe
     * @param title - tytul okna
     *
     */
    public IconFrame(JFrame baseFrame, String title) {
        this.baseFrame = baseFrame;

        // odczytanie rozmiarow ekranu
        Dimension screenSize = new Dimension(
                (int) this.getGraphicsConfiguration().getBounds().getWidth(),
                (int) this.getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int width = screenSize.width - 50;
        int height = screenSize.height - 80;
        int x = getGraphicsConfiguration().getBounds().x + (screenSize.width - width) / 2;
        int y = (screenSize.height - height - BOTTOM_MARGIN) / 2;

        // ustawienie parametrów okna
        this.setBounds(x, y, width, height);
        this.setLayout(new RiverLayout());
        this.setTitle(title);
    }

    /*
	 *  (non-Javadoc)
	 * @see java.awt.Component#setLocation(int, int)
     */
    @Override
    public void setLocation(int x, int y) {
        Dimension screenSize = new Dimension(
                (int) this.getGraphicsConfiguration().getBounds().getWidth(),
                (int) this.getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int offsetX = getGraphicsConfiguration().getBounds().x;
        int offsetY = getGraphicsConfiguration().getBounds().y;

        // koreka polożenia
        if (x + this.getWidth() > screenSize.width) {
            x = offsetX + screenSize.width - this.getWidth();
        }
        if (y + this.getHeight() + BOTTOM_MARGIN > screenSize.height) {
            y = offsetY + screenSize.height - this.getHeight() - BOTTOM_MARGIN;
        }

        // ustawienie parametrów okna
        super.setLocation(x, y);
    }

    /**
     * Wyswietlenie okienka jako modalne
     */
    public void showModal() {
        final JFrame frame = this;

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                baseFrame.setEnabled(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                baseFrame.setEnabled(true);
                frame.removeWindowListener(this);
                baseFrame.toFront();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                if (frame.isShowing()) {
                    frame.toFront();
                } else {
                    baseFrame.removeWindowListener(this);
                }
            }
        });

        frame.setVisible(true);
    }

}
