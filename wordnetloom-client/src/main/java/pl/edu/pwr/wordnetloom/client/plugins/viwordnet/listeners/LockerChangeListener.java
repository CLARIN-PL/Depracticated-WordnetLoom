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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;

/**
 * Defines an object listening for changes in locker
 *
 * @author amusial
 */
public interface LockerChangeListener {

    /**
     * action made when changes in locker occurs
     *
     * @param vn locker content <code>ViwnNode</code>
     * @return true if changed to already existing <code>ViwnGraphViewUI</code>,
     * false if new <code>ViwnGraphViewUI</code> was created
     *
     */
    ViwnNode lockerSelectionChanged(ViwnNode vn);
}
