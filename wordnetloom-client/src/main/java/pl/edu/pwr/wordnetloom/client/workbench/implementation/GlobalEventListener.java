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

import java.util.ArrayList;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;

public class GlobalEventListener {

    private static GlobalEventListener listener = new GlobalEventListener();

    public static GlobalEventListener getInstance() {
        return listener;
    }

    private ArrayList<SimpleListenerInterface> unitChangeListeners = new ArrayList<SimpleListenerInterface>();
    private ArrayList<SimpleListenerInterface> newWordChangeListeners = new ArrayList<SimpleListenerInterface>();

    /**
     *
     * @param listener
     */
    public void addUnitSelectionListener(SimpleListenerInterface listener) {
        GlobalEventListener.getInstance().unitChangeListeners.add(listener);
    }

    public void notifyUnitSelectionListener(Object object, Integer tag) {
        Sense unit = (Sense) object;
        for (SimpleListenerInterface listener : GlobalEventListener.getInstance().unitChangeListeners) {
            listener.doAction(unit, tag);
        }
    }

    public void removeUnitSelectionListener(SimpleListenerInterface listener) {
        GlobalEventListener.getInstance().unitChangeListeners.remove(listener);
    }

    /**
     *
     * @param listener
     */
    public void addNewWordsSelectionListener(SimpleListenerInterface listener) {
        GlobalEventListener.getInstance().newWordChangeListeners.add(listener);
    }

    public void notifyNewWordSelectionListeners(Object object, Integer tag) {
        String word = (String) object;
        for (SimpleListenerInterface listener : GlobalEventListener.getInstance().newWordChangeListeners) {
            listener.doAction(word, tag);
        }
    }

    public void removeNewWordSelectionListener(SimpleListenerInterface listener) {
        GlobalEventListener.getInstance().newWordChangeListeners.remove(listener);
    }
}
