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

import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconFrame;

/**
 * Klasa dostarczajaca text viewera słuszącego do wyświetlania dowolnego typu
 * tekstu
 *
 * @author Max
 */
public class TextViewer extends IconFrame {

    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;

    /**
     * Convert list of object to string representation
     *
     * @param <T>
     * @param items
     * @return string representation
     */
    static public <T> String convert(Collection<T> items) {
        StringBuilder sb = new StringBuilder();
        items.stream().map((t) -> {
            sb.append(t);
            return t;
        }).forEach((_item) -> {
            sb.append("\n");
        });
        return sb.toString();
    }

    /**
     * Konstruktor okna tekstowego
     *
     * @param title - nazwa okienka
     * @param width - szerokosc okna
     * @param height - wysokosc okna
     */
    private TextViewer(String title, int width, int height) {
        super(width, height);
        setTitle(title);
        setDefaultCloseOperation(IconFrame.DISPOSE_ON_CLOSE);

        textArea = new JTextArea("");
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    /**
     * Wyświetlenie okienka tekstowego
     *
     * @param title - nazwa okienka
     * @param text - tekst do wyświetlenia
     * @param width - szerokość okienka
     * @param height - wysokość okienka
     */
    public static void show(String title, String text, int width, int height) {
        TextViewer textView = new TextViewer(title, width, height);
        textView.textArea.setText(text);
        textView.setVisible(true);
    }
}
