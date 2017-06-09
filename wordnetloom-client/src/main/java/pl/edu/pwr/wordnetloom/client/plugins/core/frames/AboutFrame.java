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
package pl.edu.pwr.wordnetloom.client.plugins.core.frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * klasa zawierajace informacje o pogramie
 *
 * @author Max
 *
 */
public class AboutFrame extends IconDialog implements ActionListener {

    private static final String TEXT_COPYRIGHT = "Copyright 2005-2014, Politechnika Wrocławska";
    private static final String TEXT_AUTHORS_1 = "Bartosz Broda, Łukasz Jastrzębski,";
    private static final String TEXT_AUTHORS_2 = "Paweł Koczan, Michał Marcińczuk,";
    private static final String TEXT_AUTHORS_3 = "Adam Musiał, Maciej Piasecki,";
    private static final String TEXT_AUTHORS_4 = "Radosław Ramocki, Michał Stanek";
    private static final String PARAM_OWNER = "Owner";
    private static final long serialVersionUID = 1L;

    /**
     * konstruktor
     *
     * @param workbench - srodowisko
     */
    private AboutFrame(Workbench workbench) {
        super(workbench.getFrame(), Labels.ABOUT_APP, 360, 470);
        this.setLocationRelativeTo(workbench.getFrame());
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container content = this.getContentPane();
        content.setBackground(new Color(238, 238, 238));
        content.add("center", new JLabel(new ImageIcon("icons/logo.gif"), JLabel.CENTER));
        content.add("br left", new LabelPlain(Labels.ABOUT_DESCRIPTION));
        content.add("br", new JLabel(" "));
        content.add("br", new JLabel(Labels.VERSION_COLON));
        content.add("tab", new LabelPlain(workbench.getVersion()));
        content.add("br", new JLabel(Labels.CREATED_COLON));
        content.add("tab", new LabelPlain(TEXT_AUTHORS_1));
        content.add("br tab", new LabelPlain(TEXT_AUTHORS_2));
        content.add("br tab", new LabelPlain(TEXT_AUTHORS_3));
        content.add("br tab", new LabelPlain(TEXT_AUTHORS_4));
        content.add("br", new JLabel(Labels.USER_COLON));
        content.add("tab", new LabelPlain(workbench.getParam(PARAM_OWNER)));
        content.add("br", new JLabel(" "));
        content.add("br center", new JLabel(TEXT_COPYRIGHT));
        content.add("br", new JLabel(Labels.ALL_RIGHTS_RESERVED));
        content.add("p", new ButtonExt(Labels.OK, this, KeyEvent.VK_O));
        pack();
    }

    /**
     * wyswietlenie okienka dialogowego
     *
     * @param workbench - srodowisko
     *
     */
    static public void showModal(Workbench workbench) {
        AboutFrame frame = new AboutFrame(workbench);
        frame.setVisible(true);
    }

    /**
     * kliknięto w przycisk OK
     */
    public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
    }

}
