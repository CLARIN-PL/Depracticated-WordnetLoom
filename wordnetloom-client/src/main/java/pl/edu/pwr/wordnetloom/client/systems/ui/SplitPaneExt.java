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

import java.awt.Component;
import javax.swing.JSplitPane;

/**
 * rozbudowany splitter
 *
 * @author Max
 *
 */
public class SplitPaneExt extends JSplitPane {

    private static final long serialVersionUID = 1L;
    private int startDividerLocation = 0;

    /**
     * konstruktor
     *
     * @param splitType - typ spliitera
     * @param first - pierwszy komponent
     * @param second - drugi komponent
     */
    public SplitPaneExt(int splitType, Component first, Component second) {
        super(splitType, true, first, second);
        this.setOneTouchExpandable(true);
        this.setDividerSize(10);
    }

    /**
     * ustawienie podczatkowego polazenia suwaka
     *
     * @param location - polozenie
     */
    public void setStartDividerLocation(int location) {
        this.setDividerLocation(location);
        this.startDividerLocation = location;
    }

    /**
     * ustawienie poczatkowego polozenia suwaka
     *
     */
    public void resetDividerLocation() {
        this.setDividerLocation(startDividerLocation);
    }

    /**
     * schowanie ktorego z paneli
     *
     * @param number - nr panelu do schowania
     */
    public void collapse(int number) {
        if (number == 0) {
            setDividerLocation(0);
        } else {
            setDividerLocation(99999);
        }
    }
}
