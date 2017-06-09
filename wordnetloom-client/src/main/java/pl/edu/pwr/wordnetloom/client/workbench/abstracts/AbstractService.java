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

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Service;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * Abstrakcyjna usługa, będąca nakładką na interfejs Service
 *
 * @author Max
 */
public abstract class AbstractService implements Service {

    protected Workbench workbench; // uchwyt dla workbench

    public AbstractService(Workbench workbench) {
        super();
        this.workbench = workbench;
    }

    @Override
    public String getId() {
        return this.getClass().getName();
    }
}
