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

package pl.edu.pwr.wordnetloom.workbench.implementation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import pl.edu.pwr.wordnetloom.systems.ui.MenuItemExt;

/**
 * Klasa będąca kontenerem na menu zawierająca metody 
 * do dodawania i przeszukiwania zainstalowanych menu
 * @author Max
 *
 */
public class MenuHolder
{
    private static final String SHORTCUTS = "Shortcuts";
    
	// obiekt przechowujący menu
    private JMenuBar menuBar;
    // menu dla skrótów klawiszowych, niewidzlane
    private JMenu shortCutsMenu;

    /**
     * Konstructor
     *
     */
    public MenuHolder() {
    	menuBar=new JMenuBar();
    	shortCutsMenu=new JMenu(SHORTCUTS);
    	shortCutsMenu.setVisible(false);
    	menuBar.add(shortCutsMenu);
    }
    
    /**
     * Odczytanie głownego kontenera (całości)
     * @return kontener (pasek) z menu
     */
    public JMenuBar getMenuBar() {
    	return menuBar;
    }

    /**
     * Instalacja nowego menu
     * @param item - element do zainstalowania
     */
    public void install(JMenu item) {
    	this.menuBar.add(item);
    }

    /**
     * Install menu on on given position. Negative index is counted from the end.
     * @param item
     * @param index 
     */
    public void install(JMenu item, int index) {
    	if (index < 0){
    		if (index < -1)
    			index = this.menuBar.getMenuCount() + index + 1;
    	}
    	this.menuBar.add(item, index);
    }

    /**
     * Odczytanie menu o podanej nazwie
     * @param name - nawa menu do odczytania
     * @return menu lub NULL gdy nie istnieje
     */
    public JMenu getMenu(String name) {
        Component[] components = menuBar.getComponents();
        for (Component component : components) {
            if(component instanceof JMenu &&
            		((JMenu)component).getText().equals(name))
            {
                return (JMenu)component;
            }
        }
        return null;
    }
    
    /**
     * Ustawienie skrótów używanych globalnych w obrębie całej aplikacji
     * (w praktyce w obrębie jednej perspektywy)
     * @param shortCuts - kolekcja skrótów do zainstalowania
     */
    public void setShortCuts(Collection<ShortCut> shortCuts) {
    	shortCutsMenu.removeAll();
    	
    	// po wszystkich skrotach
		for (ShortCut cut : shortCuts) { 
			MenuItemExt newItem=new MenuItemExt(".",cut.getKeyStroke());
			newItem.setTag(cut); // dodanie taga
			newItem.addActionListener(new ActionListener() { // instalacja akcji
				public void actionPerformed(ActionEvent arg0) {
					MenuItemExt item=(MenuItemExt)arg0.getSource();
					ShortCut shortCut=(ShortCut)item.getTag(); // odczytane obiektu
					shortCut.doAction(); // wykonanie akcji
				}
			});
			shortCutsMenu.add(newItem);
		}
    }
    
}

