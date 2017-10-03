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

package pl.edu.pwr.wordnetloom.plugins.relations;

import javax.swing.ImageIcon;

/**
 * klasa zarządzające ikonami
 * @author Max
 *
 */
public class RelationsIM {
	private static ImageIcon toNew = new ImageIcon("icons/toNew.gif");
	private static ImageIcon toExisten = new ImageIcon("icons/toExisten.gif");
	private static ImageIcon toMerge = new ImageIcon("icons/toMerge.gif");
	private static ImageIcon onlyRelation = new ImageIcon("icons/onlyRelation.gif");
	private static ImageIcon deleteImage = new ImageIcon("icons/delete.gif");
	private static ImageIcon switchImage = new ImageIcon("icons/switch.gif");
	private static ImageIcon switchSecondImage = new ImageIcon("icons/switchD.gif");
	private static ImageIcon switchBothImage = new ImageIcon("icons/switchBoth.gif");
	private static ImageIcon addImage = new ImageIcon("icons/add.gif");
	private static ImageIcon newWindow = new ImageIcon("icons/newWindow.gif");

	/**
	 * konstruktor prywatny
	 *
	 */
	private RelationsIM() {/***/}

	/**
	 * ikona utworzenie nowego synsetu
	 * @return ikona
	 */
	public static ImageIcon getToNew() {
		return toNew;
	}

	/**
	 * ikona przeniesienia do istniejacego
	 * @return ikona
	 */
	public static ImageIcon getToExisten() {
		return toExisten;
	}

	/**
	 * ikona połączenie synsetów
	 * @return ikona
	 */
	public static ImageIcon getToMerge() {
		return toMerge;
	}

	/**
	 * ikona utworzenia relacji
	 * @return ikona
	 */
	public static ImageIcon getOnlyRelation() {
		return onlyRelation;
	}

	/**
	 * ikona usun
	 * @return ikona
	 */
	public static ImageIcon getDelete() {
		return deleteImage;
	}

	/**
	 * ikona przełącz
	 * @return ikona
	 */
	public static ImageIcon getSwitch() {
		return switchImage;
	}

	/**
	 * ikona przełącz docelowy
	 * @return ikona
	 */
	public static ImageIcon getSwitchSecond() {
		return switchSecondImage;
	}

	/**
	 * ikona przełącz oba
	 * @return ikona
	 */
	public static ImageIcon getSwitchBoth() {
		return switchBothImage;
	}

	/**
	 * ikona dodaj
	 * @return ikona
	 */
	public static ImageIcon getAdd() {
		return addImage;
	}

	/**
	 * ikona nowe okno
	 * @return ikona
	 */
	public static ImageIcon getNewWindow() {
		return newWindow;
	}
}
