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

package pl.edu.pwr.wordnetloom.systems.misc;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pl.edu.pwr.wordnetloom.utils.GUIUtils;

/**
 * klasa wyświetlająca okienka dialogowe (z przyciskami w jezyku polskim)
 * @author Max
 *
 */
public class DialogBox {

	/** wybrano tak */
	public static final int YES = 0;
	/** wybrano nie */
	public static final int NO = 1;
	/** wybrano anuluj */
	public static final int CANCEL = 2;

	private static String INPUT_TITLE = "Wprowadzanie danych";
	private static String QUESTION_TITLE = "Pytanie";
	private static String INFORMATION_TITLE = "Informacja";
	private static String ERROR_TITLE = "Błąd";
	private static String BUTTON_YES_CAPTION = "Tak";
	private static String BUTTON_NO_CAPTION = "Nie";
	private static String BUTTON_OK_CAPTION = "OK";
	private static String BUTTON_CANCEL_CAPTION = "Anuluj";
	private static String BUTTON_YES_MNEMONIC = "T";
	private static String BUTTON_NO_MNEMONIC = "N";
	private static String BUTTON_OK_MNEMONIC = "O";
	private static String BUTTON_CANCEL_MNEMONIC = "A";

	private static JFrame parentWindow=null;
	private DialogBox() {/***/}


	/**
	 * wyświetlenie błedu
	 * @param title - tytuł okienka
	 * @param message - komunikat
	 */
	static public void showError(String title,String message) {
		JOptionPane.showMessageDialog(parentWindow,message,title,JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * wyświetlenie błedu
	 * @param message - komunikat
	 */
	static public void showError(final String message) {
		try {
			GUIUtils.delegateToEDT(new Runnable() {
				public void run() {
					showError(ERROR_TITLE, message);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * wyświetlenie informacji
	 * @param title - tytuł okienka
	 * @param message - komunikat
	 */
	static public void showInformation(String title,String message) {
		JOptionPane.showMessageDialog(parentWindow,message,title,JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * wyświetlenie informacji
	 * @param message - komunikat
	 */
	static public void showInformation(String message) {
		showInformation(INFORMATION_TITLE,message);
	}

	/**
	 * wyświetlenie zapytanie tak, nie
	 * @param title - tytuł okienka
	 * @param message - komunikat
	 * @return wybrana odpowiedź
	 */
	static public int showYesNo(String title,String message) {
		int result = showConfirmDialog(message, title, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		return result==JOptionPane.CLOSED_OPTION?NO:result;
	}

	/**
	 * wyświetlenie zapytanie tak, nie
	 * @param message - komunikat
	 * @return wybrana odpowiedź
	 */
	static public int showYesNo(String message) {
		return showYesNo(QUESTION_TITLE,message);
	}

	/**
	 * wyświetlenie zapytanie tak, nie, anuluj
	 * @param title - tytuł okienka
	 * @param message - komunikat
	 * @return wybrana odpowiedź
	 */
	static public int showYesNoCancel(String title,String message) {
		int result = showConfirmDialog(message, title, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
		return result==JOptionPane.CLOSED_OPTION?CANCEL:result;
	}

	/**
	 * wyświetlenie zapytanie tak, nie, anuluj
	 * @param message - komunikat
	 * @return wybrana odpowiedź
	 */
	static public int showYesNoCancel(String message) {
		return showYesNoCancel(QUESTION_TITLE,message);
	}

	/**
	 * wyswietlenie okienka do wproadzania danych
	 * @param title - tytul okienka
	 * @param message - komunikat
	 * @param defaultValue - domysla wartosc
	 * @return wpisana odpowiedz albo NULL
	 */
	static public String inputDialog(String title,String message,String defaultValue) {
		return (String)JOptionPane.showInputDialog(parentWindow,message,title,JOptionPane.PLAIN_MESSAGE,null,null,defaultValue);
	}

	/**
	 * wyswietlenie okienka do wproadzania danych
	 * @param message - komunikat
	 * @param defaultValue - domysla wartosc
	 * @return wpisana odpowiedz albo NULL
	 */
	static public String inputDialog(String message,String defaultValue) {
		return inputDialog(INPUT_TITLE,message,defaultValue);
	}

	/**
	 * Wystietla okienko JOptionPane w języku polskim oraz wiąże odpowiednio
	 * mnemoniki z przyciskami.
	 *
	 * @param msg Trećć komunikatu
	 * @param title Tytył okna
	 * @param type Typ okna np. JOptionPane.QUESTION_MESSAGE
	 * @param option Wyświetlane opcje np. JOptionPane.YES_NO_OPTION
	 * @return wybrana opcja
	 */
	public static int showConfirmDialog(final String msg, final String title,
			final int type, final int option)
	{
		
		class Tmp implements Runnable {
			public int result;

			public void run() {
				// Create option pane and dialog.
				JOptionPane pane = new JOptionPane(msg, type, option);
				JDialog dialog = pane.createDialog(parentWindow, title);

				// Add mnemonics to buttons.
				String[] text = { BUTTON_YES_CAPTION, BUTTON_NO_CAPTION, BUTTON_OK_CAPTION, BUTTON_CANCEL_CAPTION };
				int[] mnemonic = { BUTTON_YES_MNEMONIC.charAt(0), BUTTON_NO_MNEMONIC.charAt(0), BUTTON_OK_MNEMONIC.charAt(0), BUTTON_CANCEL_MNEMONIC.charAt(0) };
				addMnemonicsToButtons(dialog, text, mnemonic);

				dialog.setVisible(true);
				
				result = JOptionPane.CLOSED_OPTION;

				Object selectedValue = pane.getValue();
				if (selectedValue != null) {
					if (selectedValue instanceof Integer) {
						result = ((Integer) selectedValue).intValue();
					}
				}
			}
		}
		
		Tmp run = new Tmp();
		
		try {
			GUIUtils.delegateToEDT(run);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return run.result;
	}

	/**
	 * dodanie mneomonikow do przyciskow
	 * @param parentComponent - komponent
	 * @param text - nazwy przyciskow
	 * @param mnemonic - mnemoniki dla przyciskow
	 */
	private static void addMnemonicsToButtons(Component parentComponent, String[] text, int[] mnemonic) {
		Collection<Component> components = getAllSubComponents(parentComponent);
		for (Component child : components) {
			if (child instanceof AbstractButton) {
				AbstractButton b = (AbstractButton) child;
				String btnText = b.getText();
				if (btnText == null) {
					btnText = "";
				}
				int textCount = text.length;
				for (int j = 0; j < textCount; j++) {
					if (btnText.equals(text[j])) {
						b.setMnemonic(mnemonic[j]);
					}
				}
			}
		}
	}

	/**
	 * odczytanie wszystkich podobiektow
	 * @param c - komponent
	 * @return tablica komponentow
	 */
	private static Collection<Component> getAllSubComponents(Component c) {
		Collection<Component> table=new ArrayList<Component>();
		if (c instanceof Container) {
			Component[] children = ((Container) c).getComponents();
			for (Component child : children) {
				table.add(child);
				if (child instanceof Container) {
					table.addAll(getAllSubComponents(child));
				}
			}
		}
		return table;
	}


	/**
	 * ustawienie domyslnego okna nadrzednego komunikatow
	 * @param parentWindow - domysle okno nadrzedne
	 */
	public static void setParentWindow(JFrame parentWindow) {
		DialogBox.parentWindow = parentWindow;
	}
}
