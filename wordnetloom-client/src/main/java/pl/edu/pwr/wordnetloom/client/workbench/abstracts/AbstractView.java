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
package pl.edu.pwr.wordnetloom.client.workbench.abstracts;

import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * abstraktycjny widok
 *
 * @author Max
 */
public abstract class AbstractView implements View {

    private final AbstractViewUI viewUI;
    private boolean wasViewInitialized;
    private final String title;
    protected Workbench workbench;

    /**
     * Konstruktor powodujący utworzenie widoku
     *
     * @param workbench - workbench
     * @param title - tytul
     * @param viewUI - interfejs
     *
     */
    protected AbstractView(Workbench workbench, String title, AbstractViewUI viewUI) {
        super();
        this.wasViewInitialized = false;
        this.viewUI = viewUI;
        this.workbench = workbench;
        this.title = title;
    }

    /**
     * Odczytanie wygladu, czyli zawartości widoku
     *
     * @return wyglad
     */
    synchronized protected AbstractViewUI getUI() {
        if (!wasViewInitialized) {
            wasViewInitialized = true;
            viewUI.init(workbench);
        }
        return viewUI;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Collection<ShortCut> getShortCuts() {
        return getUI().perspectiveScopeShortCuts;
    }

    @Override
    public JPanel getPanel() {
        return getUI().getContent();
    }

    @Override
    public JComponent getRootComponent() {
        return getUI().getRootComponent();
    }
}
