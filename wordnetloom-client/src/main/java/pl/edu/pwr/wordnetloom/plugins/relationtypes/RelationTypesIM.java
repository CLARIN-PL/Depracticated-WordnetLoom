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

package pl.edu.pwr.wordnetloom.plugins.relationtypes;

import javax.swing.ImageIcon;

/**
 * klasa zarządzające ikonami
 * @author Max
 *
 */
public class RelationTypesIM {
	private static ImageIcon newImage = new ImageIcon("icons/add.gif");
	private static ImageIcon newSubImage = new ImageIcon("icons/new.gif");
	private static ImageIcon deleteImage = new ImageIcon("icons/delete.gif");
	private static ImageIcon editImage = new ImageIcon("icons/edit.gif");
	private static ImageIcon openImage = new ImageIcon("./icons/openIcon.gif");
	private static ImageIcon closedImage = new ImageIcon("./icons/closedIcon.gif");
	private static ImageIcon leafImage = new ImageIcon("./icons/leafIcon.gif");
	private static ImageIcon upImage = new ImageIcon("./icons/upLU.gif");
	private static ImageIcon downImage = new ImageIcon("./icons/downLU.gif");

	private RelationTypesIM() {/***/}

	/**
	 * ikona zamknietej relacji
	 * @return ikona
	 */
	public static ImageIcon getClosedImage() {
		return closedImage;
	}

	/**
	 * ikona usuniecia 
	 * @return ikona
	 */
	public static ImageIcon getDeleteImage() {
		return deleteImage;
	}

	/**
	 * ikona edycji
	 * @return ikona
	 */ 
	public static ImageIcon getEditImage() {
		return editImage;
	}

	/**
	 * ikona liscia
	 * @return ikona
	 */
	public static ImageIcon getLeafImage() {
		return leafImage;
	}

	/**
	 * ikona nowy
	 * @return ikona
	 */
	public static ImageIcon getNewImage() {
		return newImage;
	}

	/**
	 * ikona nowy podtyp
	 * @return ikona
	 */
	public static ImageIcon getNewSubImage() {
		return newSubImage;
	}

	/**
	 * ikona otwarta
	 * @return ikona
	 */
	public static ImageIcon getOpenImage() {
		return openImage;
	}
	
	public static ImageIcon getMoveUpImage() {
		return upImage;
	}

	public static ImageIcon getMoveDownImage() {
		return downImage;
	}
}
