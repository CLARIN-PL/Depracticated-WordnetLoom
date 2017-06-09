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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.LockerChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnLockerViewUI.LockerElementRenderer;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * @author amusial
 *
 */
public class ViwnLockerView extends AbstractView {

    protected ViwnLockerView(Workbench workbench, String title,
            AbstractViewUI viewUI) {
        super(workbench, title, viewUI);
    }

    /**
     * @param workbench
     * @param title
     */
    public ViwnLockerView(Workbench workbench, String title) {
        super(workbench, title, new ViwnLockerViewUI());
    }

    /**
     * add locker
     *
     * @param vn
     * @param ler
     *
     */
    public void addToLocker(Object vn, LockerElementRenderer ler) {
        ((ViwnLockerViewUI) this.getUI()).addToLocker(vn, ler);
    }

    /**
     * @return ViwnLockerViewUI
     */
    public ViwnLockerViewUI getViewUI() {
        return (ViwnLockerViewUI) this.getUI();
    }

    /**
     * @param listener listener to add
     */
    public void addSynsetSelectionChangeListener(SynsetSelectionChangeListener listener) {
        ((ViwnLockerViewUI) this.getUI()).synsetSelectionChangeListeners.add(listener);
    }

    /**
     * @param listener listener to remove
     *
     */
    public void removeSynsetSelectionChangeListener(SynsetSelectionChangeListener listener) {
        ((ViwnLockerViewUI) this.getUI()).synsetSelectionChangeListeners.remove(listener);
    }

    /**
     * @param lcl LockerChangeListener to add
     *
     */
    public void addLockerChangeListener(LockerChangeListener lcl) {
        ((ViwnLockerViewUI) this.getUI()).lockerSelectionChangeListeners.add(lcl);
    }

    /**
     * @param lcl LockerChangeListener to remove
     */
    public void removeLockerChangeListener(LockerChangeListener lcl) {
        ((ViwnLockerViewUI) this.getUI()).lockerSelectionChangeListeners.remove(lcl);
    }

    public void refreshData() {
        ((ViwnLockerViewUI) this.getUI()).refreshData();
    }
}
