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
package pl.edu.pwr.wordnetloom.client.systems.progress;

import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;

/**
 * abstrakcyjan klasa do obslugi watku z paskiem postepu
 *
 * @author Max
 *
 */
abstract public class AbstractProgressThread implements Runnable {

    private static final boolean RUN_THREAD = true;
    protected ProgressFrame progress; // okno z paskiem postepu
    protected Object tag;
    protected Thread thread;

    /**
     * konstruktor
     *
     * @param baseFrame - okno bazowe na którym ma być wyświetlony pasek postępu
     * @param title - nazwa okienka
     * @param tag - obiekt z dodatkowymi parametrami
     */
    public AbstractProgressThread(JFrame baseFrame, String title, Object tag) {
        this(baseFrame, title, tag, false);
    }

    public AbstractProgressThread(JFrame baseFrame, String title, Object tag,
            boolean showCancelButton) {
        this(baseFrame, title, tag, showCancelButton, false, true);
    }

    /**
     * konstruktor
     *
     * @param baseFrame - okno bazowe na którym ma być wyświetlony pasek postępu
     * @param title - nazwa okienka
     * @param tag - obiekt z dodatkowymi parametrami
     * @param showCancelButton - true jesli przycisk anluj ma zostac wyswietlony
     * @param noModal - true jeśli okno ma nie być modalne
     * @param start - true jeśli wątek ma zostac uruchimiony automatycznie
     */
    public AbstractProgressThread(final JFrame baseFrame, final String title, final Object tag,
            final boolean showCancelButton, final boolean noModal, boolean start) {

        Runnable run = () -> {
            progress = new ProgressFrame(baseFrame, title, showCancelButton, noModal);
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException | InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(AbstractProgressThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        this.tag = tag;
        if (start) {
            start();
        }
    }

    public final void start() {
        start(true);
    }

    public void start(boolean wait) {
        if (RUN_THREAD) {
            final AbstractProgressThread apt = this;

            Runnable run = () -> {
                thread = new Thread(apt);
                thread.start();
                progress.setVisible(true);
            };

            try {
                GUIUtils.delegateToEDT(run);
            } catch (InterruptedException | InvocationTargetException ex) {
                java.util.logging.Logger.getLogger(AbstractProgressThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            if (wait) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Logger.getLogger(AbstractProgressThread.class).log(Level.ERROR, "Whiel joing threds " + e);
                }
            }
        } else {
            run();
        }
    }

    abstract protected void mainProcess();

    @Override
    final public void run() {
        mainProcess();

        progress.close();
    }

    final public Object getTag() {
        return tag;
    }

    final public void stop() {
        thread = null;
    }
}
