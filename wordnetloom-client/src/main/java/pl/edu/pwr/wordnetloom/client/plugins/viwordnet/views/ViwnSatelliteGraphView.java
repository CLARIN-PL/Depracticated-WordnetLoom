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

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.GraphChangeListener;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * @author amusial
 *
 */
public class ViwnSatelliteGraphView extends AbstractView implements GraphChangeListener {

    protected ViwnSatelliteGraphView(Workbench workbench, String title,
            AbstractViewUI viewUI) {
        super(workbench, title, viewUI);
    }

    /**
     * @param workbench an enviroment where the plugin will be installed
     * @param title title of this plugin
     */
    public ViwnSatelliteGraphView(Workbench workbench, String title) {
        super(workbench, title, new ViwnSatelliteGraphViewUI());
    }

    /**
     * @param workbench
     * @param title
     * @param graphUI
     */
    public ViwnSatelliteGraphView(Workbench workbench, String title, ViwnGraphViewUI graphUI) {
        super(workbench, title, new ViwnSatelliteGraphViewUI(graphUI));
    }

    /**
     * @param graphUI
     */
    public void setGraphViewUI(ViwnGraphViewUI graphUI) {
        ((ViwnSatelliteGraphViewUI) this.getUI()).set(graphUI);
    }

    /**
     * refresh this panel
     */
    public void refreshView() {
        ((ViwnSatelliteGraphViewUI) this.getUI()).refreshViewUI();
    }

    public void graphChanged() {
        refreshView();
    }

}
