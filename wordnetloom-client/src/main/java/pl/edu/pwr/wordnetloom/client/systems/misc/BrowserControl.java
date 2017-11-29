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
package pl.edu.pwr.wordnetloom.client.systems.misc;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;

import java.io.IOException;

/**
 * Klasa uruchamiajace przegladarka internetowego pod Windowsem i Linuxem
 *
 * @author Steven Spencer
 * @author Max - modyfikacja
 */
public class BrowserControl implements Loggable {

    private static volatile BrowserControl instance = null;
    private static final String UNIX_PATH = "netscape";        // przegladarka dla linux
    private static final String UNIX_FLAG = "-service openURL"; // komenda dla uruchomionej przegladarki

    /**
     * Uruchomienie przegladarki
     *
     * @param url - musi zawierac "http://" lub "file://"
     */
    private BrowserControl() {
    }

    public static BrowserControl getInstance() {
        if (instance == null) {
            synchronized (BrowserControl.class) {
                instance = new BrowserControl();
            }
        }
        return instance;
    }

    public void displayURL(String url) {
        try {
            if (isWindowsPlatform()) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                // Under Unix, Netscape has to be running for the "-service"
                // command to work.  So, we try sending the command and
                // check for an exit value.  If the exit command is 0,
                // it worked, otherwise we need to start the browser.
                // cmd = 'netscape -service openURL(http://www.javaworld.com)'
                Process p = Runtime.getRuntime().exec(UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")");
                try {
                    // wait for exit code -- if it's 0, command worked, otherwise we need to start the browser up.
                    int exitCode = p.waitFor();
                    if (exitCode != 0) { // Command failed, start up the browser
                        p = Runtime.getRuntime().exec(UNIX_PATH + " " + url);
                    }
                } catch (InterruptedException e) {
                    logger().error("Trying to call browser", e);
                }
            }
        } catch (IOException e) {
            logger().error("No file" + e);
        }
    }

    /**
     * Sprwadzenie czy systemem operacyjnym jest MS Windows
     *
     * @return true jesli to windows
     */
    public boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        return os != null && os.startsWith("Windows");
    }
}
