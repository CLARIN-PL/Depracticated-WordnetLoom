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
package pl.edu.pwr.wordnetloom.client.workbench.implementation;

import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Klasa będąca kontenerem na menu zawierająca metody do dodawania i
 * przeszukiwania zainstalowanych menu
 *
 * @author Max
 */
public class MenuHolder {

    private static final String SHORTCUTS = "Shortcuts";

    private final WebMenuBar menuBar;
    // private final WebMenu shortCutsMenu;

    public MenuHolder() {
        menuBar = new WebMenuBar();
        //  shortCutsMenu = new WebMenu(SHORTCUTS);
        // shortCutsMenu.setVisible(false);
        //menuBar.add(shortCutsMenu);
    }

    /**
     * Odczytanie głownego kontenera (całości)
     *
     * @return kontener (pasek) z menu
     */
    public WebMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Instalacja nowego menu
     *
     * @param item - element do zainstalowania
     */
    public void install(WebMenu item) {
        menuBar.add(item);
    }

    /**
     * Install menu on on given position. Negative index is counted from the
     * end.
     *
     * @param item
     * @param index
     */
    public void install(WebMenu item, int index) {
        if (index < 0) {
            if (index < -1) {
                index = menuBar.getMenuCount() + index + 1;
            }
        }
        menuBar.add(item, index);
    }

    /**
     * Odczytanie menu o podanej nazwie
     *
     * @param name - nawa menu do odczytania
     * @return menu lub NULL gdy nie istnieje
     */
    public WebMenu getMenu(String name) {
        Component[] components = menuBar.getComponents();
        for (Component component : components) {
            if (component instanceof WebMenu
                    && ((WebMenu) component).getText().equals(name)) {
                return (WebMenu) component;
            }
        }
        return null;
    }

    /**
     * Ustawienie skrótów używanych globalnych w obrębie całej aplikacji (w
     * praktyce w obrębie jednej perspektywy)
     *
     * @param shortCuts - kolekcja skrótów do zainstalowania
     */
    public void setShortCuts(Collection<ShortCut> shortCuts) {
        // shortCutsMenu.removeAll();

        // po wszystkich skrotach
        for (ShortCut cut : shortCuts) {
            MMenuItem newItem = new MMenuItem(".")
                    .withKeyStroke(cut.getKeyStroke());

            newItem.setTag(cut);
            newItem.addActionListener((ActionEvent arg0) -> {
                        MMenuItem item = (MMenuItem) arg0.getSource();
                        ShortCut shortCut = (ShortCut) item.getTag();
                        shortCut.doAction();
                    }
            );
            //    shortCutsMenu.add(newItem);
        }
    }

}
