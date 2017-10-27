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
package pl.edu.pwr.wordnetloom.client.plugins.relationtypes;

import javax.swing.*;

/**
 * klasa zarządzające ikonami
 *
 * @author Max
 */
public class RelationTypesIM {

    private static final ImageIcon openImage = new ImageIcon(RelationTypesIM.class.getClassLoader().getResource("icons/openIcon.gif"));
    private static final ImageIcon closedImage = new ImageIcon(RelationTypesIM.class.getClassLoader().getResource("icons/closedIcon.gif"));
    private static final ImageIcon leafImage = new ImageIcon(RelationTypesIM.class.getClassLoader().getResource("icons/leafIcon.gif"));

    private RelationTypesIM() {
    }

    /**
     * ikona zamknietej relacji
     *
     * @return ikona
     */
    public static ImageIcon getClosedImage() {
        return closedImage;
    }

    /**
     * ikona liscia
     *
     * @return ikona
     */
    public static ImageIcon getLeafImage() {
        return leafImage;
    }

    /**
     * ikona otwarta
     *
     * @return ikona
     */
    public static ImageIcon getOpenImage() {
        return openImage;
    }


}
