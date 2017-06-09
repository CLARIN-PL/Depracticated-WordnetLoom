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
package pl.edu.pwr.wordnetloom.client.systems.misc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import javax.swing.UIManager;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;

/**
 * Klasa zajmuja sie lokalizacja intefejsu na wybrany jezyk. Wykorzystuje
 * mechanizm refleksji aby podmienic statyczne dane w klasach. Aby tekts mogl
 * zostac podmienionty musi byc statyczny ale nie moze byc oznaczony jako final.
 * Dopuszczalne sa modifikatory dostepu, tj. licalizer poradzi sobie z
 * prywatnymi polami.
 *
 * @author Max
 *
 */
@Deprecated
public class Localizer {

    private static final String LOCALE_PARAM = "Locale";
    private static final String UIMANAGER_PARAM = "UIManager";

    /**
     * Wczytanie definicji komunikatowych z pliku tekstowego i zaiplikowanie ich
     * do systemu
     *
     * @param fileName - nazwa pliku z definicja komunikatow i nazw
     * @param debugMode - wlacza tryb debug
     */
    static public void apply(String fileName, boolean debugMode) {
        Collection<Pair<String, String>> params = loadDefines(fileName);

        for (Pair<String, String> item : params) {
            int classSeparator = item.getA().indexOf(':');
            if (classSeparator != -1) {
                String className = item.getA().substring(0, classSeparator);
                String fieldName = item.getA().substring(classSeparator + 1);

                if (className.equals(UIMANAGER_PARAM)) {
                    // specjalny przypadek, ustawienia dla Swinga
                    UIManager.put(fieldName, item.getB());
                } else if (className.equals(LOCALE_PARAM)) {
                    // specjalny przypadek, ustawienia dla systemu
                    Locale.setDefault(new Locale(fieldName, item.getB()));
                } else {
                    try {
                        Class<?> c = Class.forName(className);
                        if (!c.isEnum()) {
                            Field field = c.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            field.set(null, item.getB());
                            field.setAccessible(false);
                        } else {
                            int fieldSeparator = fieldName.indexOf("*");
                            if (fieldSeparator != -1) {
                                String enumName = fieldName.substring(0, fieldSeparator);
                                String varName = fieldName.substring(fieldSeparator + 1);

                                // odczytanie pola i wartosci pola
                                Field enumField = c.getDeclaredField(enumName);
                                Object entry = enumField.get(null);

                                // ustawienie pola w obiekcie
                                Field toSet = entry.getClass().getDeclaredField(varName);
                                toSet.setAccessible(true);
                                toSet.set(entry, item.getB());
                                toSet.setAccessible(false);
                            } else {
                                System.err.println("Invalid class definition: " + item.getA());
                            }
                        }

                    } catch (Exception e) {
                        if (debugMode) {
                            System.err.println("Class: " + className);
                            System.err.println("Line: " + item.getA() + "=" + item.getB());
                        }
                    }
                }
            } else {
                System.err.println("Invalid class definition: " + item.getA());
            }
        }

        // zastosowanie polski usatwien jesli nie zdefiniowano innych
        if (params.size() == 0) {
            setPolishStrings();
        }
    }

    /**
     * Wczytanie danych z pliku tekstowego
     *
     * @param fileName - nazwa pliku z definicja komunikatow
     * @return kolekcja tlumaczen
     */
    static Collection<Pair<String, String>> loadDefines(String fileName) {
        Collection<Pair<String, String>> params = new ArrayList<>();
        try {
            String line;
            FileInputStream fi = new FileInputStream(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(fi));
            do {
                line = in.readLine(); // wczytanie linii
                if (line != null && line.length() > 0 && !line.startsWith("!")) {
                    int pos = line.indexOf('='); // znalezienie podzialu =
                    if (pos != -1) {
                        String translation = line.substring(pos + 1);
                        translation = translation.replace("\\n", "\n");
                        params.add(new Pair<>(line.substring(0, pos), translation));
                    } else {
                        System.err.println("No '=' in line:\n   " + line);
                    }
                }
            } while (line != null);
            fi.close();
        } catch (FileNotFoundException e) { // brak plik, moze sie zdarzyc
        } catch (IOException e) {           // tutaj zdecydowanie cos poszlo nie tak ;)
        }
        return params;
    }

    /**
     * Ustawienie polskich tekstów
     */
    private static void setPolishStrings() {
        Locale.setDefault(new Locale("pl", "PL"));

        UIManager.put("FileChooser.lookInLabelText", "Szukaj w");
        UIManager.put("FileChooser.saveButtonText", "Zapisz");
        UIManager.put("FileChooser.openButtonText", "Otworz");
        UIManager.put("FileChooser.cancelButtonText", "Anuluj");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Anuluj i wyjdź z okna");
        UIManager.put("FileChooser.updateButtonText", "Modyfikuj");
        UIManager.put("FileChooser.helpButtonText", "Pomoc");
        UIManager.put("FileChooser.fileNameLabelText", "Nazwa pliku:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Pliki typu:");
        UIManager.put("FileChooser.upFolderToolTipText", "Do góry o jeden poziom");
        UIManager.put("FileChooser.homeFolderToolTipText", "Folder domowy");
        UIManager.put("FileChooser.newFolderToolTipText", "Utwórz nowy folder");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Szczegóły");

        UIManager.put("OptionPane.yesButtonText", "Tak");
        UIManager.put("OptionPane.noButtonText", "Nie");
        UIManager.put("OptionPane.cancelButtonText", "Anuluj");
        UIManager.put("OptionPane.okButtonText", "OK");
    }
}
