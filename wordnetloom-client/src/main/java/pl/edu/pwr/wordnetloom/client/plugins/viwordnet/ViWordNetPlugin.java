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

import java.io.IOException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * The plugin adds a top menu option 'Widok'. The 'Widok' contains an option to
 * automatic resize of the application and notes windows.
 *
 * @author Michał Marcińczuk
 */
public class ViWordNetPlugin implements Plugin {

    /**
     * Plugin installation.
     *
     * @param workbench - an enviroment where the plugin will be installed.
     */
    public void install(Workbench workbench) {
        FileAppender fapp = null;

        PatternLayout lay = new PatternLayout();
        lay.setConversionPattern("%p %d{DATE} [%t] %c{1}: %m%n");

        try {
            fapp = new FileAppender(lay, "logs/viwn.log");
        } catch (IOException e) {
        }

        Logger.getRootLogger().removeAllAppenders();

        Logger.getLogger(ViWordNetPlugin.class).removeAllAppenders();
        Logger.getLogger(ViWordNetPlugin.class).addAppender(fapp);
        Logger.getLogger(ViWordNetPlugin.class).addAppender(new ConsoleAppender(lay));
        Logger.getLogger(ViWordNetPlugin.class).setLevel(Level.ALL);

        //Service vimgr = new ViWordNetConfService(workbench);
        //workbench.installService(vimgr);
        ViWordNetPerspective vwnp = new ViWordNetPerspective(Labels.WORDNET_VISUALIZATION, workbench);
        workbench.installPerspective(vwnp);

        ViWordNetService vwns = new ViWordNetService(workbench, Labels.WORDNET_VISUALIZATION, vwnp);
        workbench.installService(vwns);
        vwnp.setService(vwns);

        Logger.getLogger(ViwnGraphViewUI.class).log(Level.DEBUG, "installed");
    }
}
