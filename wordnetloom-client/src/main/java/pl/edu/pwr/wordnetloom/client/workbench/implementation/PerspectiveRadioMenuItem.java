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

import com.alee.laf.menu.WebRadioButtonMenuItem;
import pl.edu.pwr.wordnetloom.client.systems.misc.ActionWrapper;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Perspective;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * Klasa dostarcza element menu pochodny do RadioButton, który związany jest z
 * konktretną perspektywą a jego kliknięcie powoduje przełączenie się na wybraną
 * perspektywę (jeśli nie jest aktualnie aktywan)
 *
 * @author Max
 */
public class PerspectiveRadioMenuItem extends WebRadioButtonMenuItem {

    private static final long serialVersionUID = 1L;

    private final String perspectiveName;
    private final Workbench workbench;

    /**
     * Konstruktor, który tworzy obiekt menu a jego kliknięcie (obiektu)
     * powoduje przełączenie perspektywy
     *
     * @param perspective - perspektywa z którą związany jest element menu
     * @param workbench   - środowisko w którym działa
     */
    PerspectiveRadioMenuItem(final Perspective perspective, final Workbench workbench) {
        perspectiveName = perspective.getName();
        this.workbench = workbench;
        addActionListener(new ActionWrapper(this, "menu_onClick"));
        setText(perspectiveName);
    }

    /**
     * Akcja wywoływana w momencie kliknięcia w element menu
     */
    public void menu_onClick() {
        // sprawdzenie czy perspektywa nie jest już aktywna
        if (!workbench.getActivePerspective().getName().equals(perspectiveName)) {
            // zmiena aktywnej perspektywy
            workbench.choosePerspective(perspectiveName);
        }
    }
}
