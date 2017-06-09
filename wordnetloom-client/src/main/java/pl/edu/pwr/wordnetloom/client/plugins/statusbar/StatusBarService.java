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
package pl.edu.pwr.wordnetloom.client.plugins.statusbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * usluga zarzadzajca wyswietlaniem tekstu na pasku statusu
 *
 * Nie wyświetla się na Mac OS ;(
 *
 * @author Max
 *
 */
public class StatusBarService extends AbstractService implements Runnable, ActionListener {

    private static final int SLEEP_TIME = 5000;

    private Thread thread = null;   // obiekt z watkiem
    private boolean stop = false;   // zmienna do zatrzymania dzialania watku
    private JMenuItem clearCache; // menu czyczscenie cache'u

    /**
     * konstruktor
     *
     * @param workbench - środowisko pracy
     *
     */
    public StatusBarService(Workbench workbench) {
        super(workbench);
    }

    /**
     * intalacja akcji w menu
     */
    @Override
    public void installMenuItems() {
        JMenu other = workbench.getMenu(Labels.OTHER);
        if (other == null) {
            return;
        }
        clearCache = new MenuItemExt(Labels.CLEAR_CACHE, KeyEvent.VK_W, this);
        other.addSeparator();
        other.add(clearCache);
    }

    @Override
    public boolean onClose() {
        stop = true;
        thread = null;
        return true;
    }

    @Override
    public void onStart() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * uruchomienie watku
     */
    volatile int memoryUsage;

    @Override
    final public void run() {

        while (!stop) {
            // czy cache jest wlaczony
            memoryUsage = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

            // gdy cache jest wylaczony
            SwingUtilities.invokeLater(() -> {
                workbench.setStatusText(String.format("<html>" + Labels.MEMORY_USAGE + "</html>", memoryUsage));
            });

            // uspienie pluginu
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                Logger.getLogger(StatusBarService.class).log(Level.ERROR, "Trying to sleep plugin" + e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == clearCache) {
            Runtime.getRuntime().gc();
            DialogBox.showInformation(Messages.SUCCESS_CACHE_CLEANED);
        }

    }

    @Override
    public void installViews() {
    }
}
