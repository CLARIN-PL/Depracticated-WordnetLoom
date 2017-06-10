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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.frames.RelationDisplayConfFrame;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/*
 * this is just a stub for gui relations configuration
 */
public class ViWordNetConfService extends AbstractService
        implements ActionListener {

    public ViWordNetConfService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installMenuItems() {
        JMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        other.add(new MenuItemExt(Labels.RELATIONS_CONFIGURATION, KeyEvent.VK_K, this));
    }

    @Override
    public void installViews() {
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RelationDisplayConfFrame.showModal(workbench);
    }
}
