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

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import pl.edu.pwr.wordnetloom.client.systems.misc.ActionWrapper;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import se.datadosen.component.RiverLayout;

/**
 * okienko z paskiem postepu
 *
 * @author Max
 */
public class ProgressFrame extends IconDialog {

    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;
    private JProgressBar globalProgress;
    private JLabel infoLabel;
    private JLabel globalLabel;
    private JButton cancelButton;
    private boolean canceled = false;

    /**
     * konstruktor
     *
     * @param baseFrame - okno bazowe na ktorym ma byc pasek postepu
     * @param title - tytul okna
     * @param showCancelButton - TRUE pokazuje dodatkowy przycisk do
     * zatrzymywania
     */
    public ProgressFrame(JFrame baseFrame, String title, boolean showCancelButton) {
        super(baseFrame, title, 300, showCancelButton ? 155 : 125);
        this.setLayout(new RiverLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //this.setAlwaysOnTop(true);

        globalLabel = new JLabel(Labels.GLOBAL_PROGRESS);
        infoLabel = new JLabel(" ");
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setPreferredSize(new Dimension(0, 20));
        globalProgress = new JProgressBar();
        globalProgress.setPreferredSize(new Dimension(0, 20));
        globalProgress.setMinimum(0);
        cancelButton = new ButtonExt(Labels.CANCEL, new ActionWrapper(this, "cancelButton_OnClick"), 'a');

        this.add("", infoLabel);
        this.add("br hfill", progressBar);
        this.add("br", globalLabel);
        this.add("br hfill vfill", globalProgress);
        if (showCancelButton) {
            this.add("br center", cancelButton);
        }
    }

    public ProgressFrame(JFrame baseFrame, String title, boolean showCancelButton,
            boolean noModal) {
        this(baseFrame, title, showCancelButton);
        super.setModal(!noModal);
    }

    /**
     * ustawenie dostepnosci przycisku anuluj
     *
     * @param enable - TRUE przycisk dostepny, FALSE przycisk nie dostepny
     */
    public void setCancelEnable(boolean enable) {
        cancelButton.setEnabled(enable);
    }

    /**
     * ustawienie parametrow postepu dla akcji bez podziału na kroki
     *
     * @param info - opis akcji
     */
    public void setProgressParams(String info) {
        progressBar.setIndeterminate(true);
        infoLabel.setText(info + ":");
    }

    /**
     * ustawienie parametrow postepu
     *
     * @param value - wartosc aktualna
     * @param max - maksymalna wartosc
     * @param info - opis akcji
     */
    public void setProgressParams(final int value, final int max, final String info) {
        try {
            GUIUtils.delegateToEDT(new Runnable() {
                public void run() {
                    progressBar.setIndeterminate(value == 0 && max == 0);
                    progressBar.setMaximum(max);
                    progressBar.setValue(value);
                    infoLabel.setText(info + ":");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * ustawienie parametrow globalnego postepu
     *
     * @param value - wartosc aktualna
     * @param max - maksymalna wartosc
     */
    public void setGlobalProgressParams(final int value, final int max) {
        try {
            GUIUtils.delegateToEDT(new Runnable() {
                public void run() {
                    globalProgress.setMaximum(max);
                    globalProgress.setValue(value);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * utawienie wartosci paska postepu
     *
     * @param value - nowa wartosc
     */
    public void setProgressValue(final int value) {
        try {
            GUIUtils.delegateToEDT(new Runnable() {
                public void run() {
                    progressBar.setValue(value);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * utawienie wartosci globlanego paska postepu
     *
     * @param value - nowa wartosc
     */
    public void setGlobalProgressValue(final int value) {
        try {
            GUIUtils.delegateToEDT(new Runnable() {
                public void run() {
                    globalProgress.setValue(value);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * zamkniecie okna
     *
     */
    public void close() {
        Runnable run = new Runnable() {

            public void run() {
                setDefaultCloseOperation(DISPOSE_ON_CLOSE); // zmienienie akcji na dispose
                setVisible(false);		// schowanie okna
                dispose();		        // usuniecie okna z pamieci
            }
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * wcisnieto przycisk anuluj
     */
    public void cancelButton_OnClick() {
        canceled = true;
        cancelButton.setEnabled(false);
    }

    /**
     * odczytuje czy proces zostal przerwany
     *
     * @return TRUE jesli proces zostal przerwany
     */
    public boolean isCanceled() {
        return canceled;
    }

    public void setCancel(boolean canceled) {
        this.canceled = canceled;
    }

    public void setButtonLabel(String text) {
        cancelButton.setText(text);
    }

    public void setIndeterminate(boolean newValue) {
        progressBar.setIndeterminate(newValue);
        globalProgress.setIndeterminate(newValue);
    }

    public void setText(String text) {
        this.infoLabel.setText(text);
    }
}
