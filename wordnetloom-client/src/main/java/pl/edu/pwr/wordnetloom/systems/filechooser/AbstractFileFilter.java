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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * uniwersalny filtr plikow
 * 
 * @author Max
 *
 */
abstract public class AbstractFileFilter extends FileFilter {

	protected String fileExtension = null; // rozszerzenie
	protected String filterDescription = null;// opis

	/**
	 * konstrutkor
	 * 
	 * @param newExtension
	 *            - nowe rozszerzenie
	 * @param newDescription
	 *            - nowy opis
	 */
	public AbstractFileFilter(String newExtension, String newDescription) {
		fileExtension = newExtension;
		filterDescription = newDescription;
	}

	/**
	 * sprawdzenie czy plik spelnia wymagania
	 */
	@Override
	public boolean accept(File arg0) {
		return arg0.isDirectory() || arg0.getName().toLowerCase().endsWith(fileExtension);
	}

	/**
	 * odczyt opisu filtra
	 */
	@Override
	public String getDescription() {
		return filterDescription;
	}

	/**
	 * sformatowanie pliku
	 * 
	 * @param file
	 * @return plik z rozszerzeniem
	 */
	public File formatFilename(File file) {
		if (!file.isDirectory() && accept(file))
			return file;
		return new File(file.getAbsolutePath() + fileExtension);
	}
}
