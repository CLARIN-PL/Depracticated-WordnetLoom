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

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;

/**
 * klasa zawierajace okienko do wyboru plikow
 * 
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 *         Jastrzebski</a>
 * @author Max - modyfikacja
 */
public class FileChooser {

	private static String SAVE_TITLE = "Zapis pliku";
	private static String MERGE_TITLE = "Łączenie (wielu) plików";
	private static String IMPORT_TITLE = "Import (wielu) plików";
	private static String IMPORT_ONE_TITLE = "Import pliku";
	private static String IMPORT_BUTTON_ONE_HINT = "Importuj zaznaczony plik";
	private static String CONVERT_TITLE = "Konwersja (wielu) plików";
	private static String QUESTION_FILE_EXISTS = "Plik już istnieje, nadpisać?";
	private static String IMPORT_BUTTON_TEXT = "Importuj";
	private static String IMPORT_BUTTON_HINT = "Importuj zaznaczone pliki";
	private static String CONVERT_BUTTON_TEXT = "Konwertuj";
	private static String CONVERT_BUTTON_HINT = "Konwertuj zaznaczone pliki";
	private static String SAVE_BUTTON_TEXT = "Zapisz";
	private static String SAVE_BUTTON_HINT = "Zapisz plik";
	private static String MERGE_BUTTON_TEXT = "Połącz";
	private static String MERGE_BUTTON_HINT = "Połącz zaznaczone pliki";

	private static FileChooser instance = new FileChooser();
	JFileChooser fileChooser;
	private Thread runner;
	private final static HtmlFileFilter htmlFilter = new HtmlFileFilter();
	private final static TxtFileFilter txtFilter = new TxtFileFilter();
	private final static XmlFileFilter xmlFilter = new XmlFileFilter();
	private final static CSVFileFilter csvFilter = new CSVFileFilter();

	/**
	 * konstrutkor
	 */
	private FileChooser() {
		super();
		runner = new Thread(new Runnable() {

			public void run() {
				fileChooser = new JFileChooser();
				// ustawienie sie w katalogu bieżącym
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.rescanCurrentDirectory();
				fileChooser.setAcceptAllFileFilterUsed(false);
			}

		});
		runner.start();
	}

	/**
	 * odczytanie instancji obiektu
	 * 
	 * @return instancja obiektu
	 */
	public static FileChooser getInstance() {
		return instance;
	}

	/**
	 * wyświetlenie okienka wyboru plikow do importu
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrane pliki
	 */
	public File[] importMultiFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.addChoosableFileFilter(txtFilter);
		fileChooser.setFileFilter(xmlFilter);

		fileChooser.setDialogTitle(IMPORT_TITLE);
		fileChooser.setApproveButtonText(IMPORT_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(IMPORT_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFiles();
		}
		return null;
	}

	/**
	 * wyświetlenie okienka wyboru pliku do importu
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrane pliki
	 */
	public File importXmlFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(xmlFilter);

		fileChooser.setDialogTitle(IMPORT_ONE_TITLE);
		fileChooser.setApproveButtonText(IMPORT_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(IMPORT_BUTTON_ONE_HINT);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * wyświetlenie okienka wyboru plikow tekstowego do importu
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrany plik
	 */
	public File importTextFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(txtFilter);

		fileChooser.setDialogTitle(IMPORT_TITLE);
		fileChooser.setApproveButtonText(IMPORT_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(IMPORT_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * wyświetlenie okienka wyboru plikow do konwersji
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrane pliki
	 */
	public File[] convertMultiFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.addChoosableFileFilter(txtFilter);
		fileChooser.setFileFilter(xmlFilter);

		fileChooser.setDialogTitle(CONVERT_TITLE);
		fileChooser.setApproveButtonText(CONVERT_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(CONVERT_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFiles();
		}
		return null;
	}

	/**
	 * wyświetlenie okienka wyboru plikow do importu
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrane pliki
	 */
	public File[] mergeMultiFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(xmlFilter);

		fileChooser.setDialogTitle(MERGE_TITLE);
		fileChooser.setApproveButtonText(MERGE_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(MERGE_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFiles();
		}
		return null;
	}

	/**
	 * wyświetlenie okienka wyboru pliku do zapisu
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrany plik
	 */
	public File saveFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.addChoosableFileFilter(txtFilter);
		fileChooser.setFileFilter(xmlFilter);

		fileChooser.setDialogTitle(SAVE_TITLE);
		fileChooser.setApproveButtonText(SAVE_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(SAVE_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			// weryfikacja nazwy pliku, czy ma rozszerzenie
			if (fileChooser.getFileFilter() instanceof AbstractFileFilter) {
				// pobranie filtru
				AbstractFileFilter filter = (AbstractFileFilter) fileChooser.getFileFilter();
				// formatowanie nazwy
				selectedFile = filter.formatFilename(selectedFile);
			}
			// co jesli plik istnieje
			if (selectedFile.exists()) {
				if (DialogBox.showYesNo(QUESTION_FILE_EXISTS) == DialogBox.NO) {
					return null;
				}
			}

			return selectedFile;
		}
		return null;
	}

	public File saveCSVFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.addChoosableFileFilter(csvFilter);
		fileChooser.setFileFilter(csvFilter);

		fileChooser.setDialogTitle(SAVE_TITLE);
		fileChooser.setApproveButtonText(SAVE_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(SAVE_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			// weryfikacja nazwy pliku, czy ma rozszerzenie
			if (fileChooser.getFileFilter() instanceof AbstractFileFilter) {
				// pobranie filtru
				AbstractFileFilter filter = (AbstractFileFilter) fileChooser.getFileFilter();
				// formatowanie nazwy
				selectedFile = filter.formatFilename(selectedFile);
			}
			// co jesli plik istnieje
			if (selectedFile.exists()) {
				if (DialogBox.showYesNo(QUESTION_FILE_EXISTS) == DialogBox.NO) {
					return null;
				}
			}

			return selectedFile;
		}
		return null;
	}

	/**
	 * wyświetlenie okienka wyboru pliku html do zapisu
	 * 
	 * @param parent
	 *            - komponent w obrebie ktorego ma dzialac
	 * @return wybrany plik
	 */
	public File saveHTMLFile(Component parent) {
		try {
			runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ustawienie filtrow
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(htmlFilter);

		fileChooser.setDialogTitle(SAVE_TITLE);
		fileChooser.setApproveButtonText(SAVE_BUTTON_TEXT);
		fileChooser.setApproveButtonToolTipText(SAVE_BUTTON_HINT);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			// weryfikacja nazwy pliku, czy ma rozszerzenie
			if (fileChooser.getFileFilter() instanceof AbstractFileFilter) {
				// pobranie filtru
				AbstractFileFilter filter = (AbstractFileFilter) fileChooser.getFileFilter();
				// formatowanie nazwy
				selectedFile = filter.formatFilename(selectedFile);
			}
			// co jesli plik istnieje
			if (selectedFile.exists()) {
				if (DialogBox.showYesNo(QUESTION_FILE_EXISTS) == DialogBox.NO) {
					return null;
				}
			}

			return selectedFile;
		}
		return null;
	}

}
