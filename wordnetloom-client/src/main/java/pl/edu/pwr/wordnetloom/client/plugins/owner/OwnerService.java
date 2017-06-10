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
package pl.edu.pwr.wordnetloom.client.plugins.owner;

import pl.edu.pwr.wordnetloom.client.plugins.owner.data.SessionData;
import pl.edu.pwr.wordnetloom.client.plugins.owner.frames.OwnerFrame;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * serwis zajmujący sie obsluga danych uzytkownika
 *
 * @author Max
 */
public class OwnerService extends AbstractService {

    private static final String PARAM_OWNER = "Owner";
    private static final String PARAM_PROJECT = "Project";

    /**
     * konstruktor serwisu
     *
     * @param workbench - srodowisko uruchomieniowe
     */
    public OwnerService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void onStart() {

        if (workbench.getParam(PARAM_OWNER) == null || workbench.getParam(PARAM_PROJECT) == null) {
            // brak danych o właścicielu, wyświetlenie okienka do wprowadzenia danych
            SessionData data = OwnerFrame.showModal(workbench);
            if (data.owner == null) {
                System.exit(0); // nie będziemy działać dalej
            } else {
                workbench.setParam(PARAM_OWNER, data.owner);
                workbench.updateOwner();
            }
        }
    }

    @Override
    public void installMenuItems() {
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void installViews() {
    }

}
