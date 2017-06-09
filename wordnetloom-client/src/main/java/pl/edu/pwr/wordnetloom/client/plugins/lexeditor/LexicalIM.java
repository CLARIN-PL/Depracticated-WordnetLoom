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
package pl.edu.pwr.wordnetloom.client.plugins.lexeditor;

import javax.swing.ImageIcon;

/**
 * klasa zarządzające ikonami
 *
 * @author Max
 *
 */
public class LexicalIM {

    private static final ImageIcon newImage = new ImageIcon("icons/new.gif");
    private static final ImageIcon newImage2 = new ImageIcon("icons/new2.gif");
    private static final ImageIcon upLUImage = new ImageIcon("icons/upLU.gif");
    private static final ImageIcon downLUImage = new ImageIcon("icons/downLU.gif");
    private static final ImageIcon relationsImage = new ImageIcon("icons/relations.gif");
    private static final ImageIcon switchImage = new ImageIcon("icons/switchToUnits.gif");
    private static final ImageIcon addImage = new ImageIcon("icons/add.gif");
    private static final ImageIcon deleteImage = new ImageIcon("icons/delete.gif");
    private static final ImageIcon duplicateImage = new ImageIcon("icons/duplicateSynset.gif");
    private static final ImageIcon insertBetweenImage = new ImageIcon("icons/insertBetween.gif");
    private static final ImageIcon toNew = new ImageIcon("icons/toNewSmall.gif");
    private static final ImageIcon edit = new ImageIcon("icons/edit.gif");
    private static final ImageIcon busy = new ImageIcon("icons/busy.gif");
    private static final ImageIcon right = new ImageIcon("icons/right.png");
    private static final ImageIcon user = new ImageIcon("icons/user.png");

    private LexicalIM() {
    }

    /**
     * ikona do gory
     *
     * @return ikona
     */
    public static ImageIcon getUp() {
        return upLUImage;
    }

    /**
     * ikona w dol
     *
     * @return ikona
     */
    public static ImageIcon getDown() {
        return downLUImage;
    }

    /**
     * ikona relacji
     *
     * @return ikona
     */
    public static ImageIcon getRelations() {
        return relationsImage;
    }

    /**
     * ikona dodaj
     *
     * @return ikona
     */
    public static ImageIcon getAdd() {
        return addImage;
    }

    /**
     * ikona usun
     *
     * @return ikona
     */
    public static ImageIcon getDelete() {
        return deleteImage;
    }

    public static ImageIcon getRight() {
        return right;
    }

    /**
     * ikona utworzenie nowego synsetu
     *
     * @return ikona
     */
    public static ImageIcon getToNew() {
        return toNew;
    }

    /**
     * ikona nowy
     *
     * @return ikona
     */
    public static ImageIcon getNew() {
        return newImage;
    }

    /**
     * ikona nowy
     *
     * @return ikona
     */
    public static ImageIcon getNew2() {
        return newImage2;
    }

    /**
     * ikona przelaczenia
     *
     * @return ikona
     */
    public static ImageIcon getSwitch() {
        return switchImage;
    }

    /**
     * ikona powiel
     *
     * @return ikona
     */
    public static ImageIcon getDuplicate() {
        return duplicateImage;
    }

    /**
     * ikona wstawiania relacji w srodek
     *
     * @return ikona
     */
    public static ImageIcon getInsertBetween() {
        return insertBetweenImage;
    }

    /**
     * icon small busy circle
     *
     * @return ikona
     */
    public static ImageIcon getSmallBusy() {
        return busy;
    }

    public static ImageIcon getEdit() {
        return edit;
    }

    public static ImageIcon getUser() {
        return user;
    }
}
