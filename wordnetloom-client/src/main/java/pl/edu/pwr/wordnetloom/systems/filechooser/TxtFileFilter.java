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

package pl.edu.pwr.wordnetloom.systems.filechooser;

/**
 * klasa filtrujaca pliki tekstowe
 * 
 * @author Max
 *
 */
public class TxtFileFilter extends AbstractFileFilter {

	private static String TXT_DESCRIPTION = "Pliki tekstowe (*.txt)";
	private static final String TXT_EXTENSION = ".txt";

	/**
	 * konstruktor
	 *
	 */
	public TxtFileFilter() {
		super(TXT_EXTENSION, TXT_DESCRIPTION);
	}
}