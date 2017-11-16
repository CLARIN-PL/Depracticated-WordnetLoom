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

import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;


public class ViWordNetPlugin implements Plugin {


    public void install(Workbench workbench) {

        //Service vimgr = new ViWordNetConfService(workbench);
        //workbench.installService(vimgr);
        ViWordNetPerspective vwnp = new ViWordNetPerspective(Labels.WORDNET_VISUALIZATION, workbench);
        workbench.installPerspective(vwnp);

        ViWordNetService vwns = new ViWordNetService(workbench, Labels.WORDNET_VISUALIZATION, vwnp);
        workbench.installService(vwns);
        vwnp.setService(vwns);

        logger().debug("Installed");
    }
}
