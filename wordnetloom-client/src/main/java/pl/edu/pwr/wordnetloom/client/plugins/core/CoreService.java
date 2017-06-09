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
package pl.edu.pwr.wordnetloom.client.plugins.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultEditorKit;
import pl.edu.pwr.wordnetloom.client.plugins.core.frames.AboutFrame;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * klasa dostarczająca podsawową usługę - menu
 *
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 * Jastrzebski</a>
 * @version CVS $Id$
 * @author Max - modyfikacja i rozbudowa
 */
public class CoreService extends AbstractService implements MenuListener {

    private static final String SHOW_TOOLTIPS_PARAM = "ShowTooltips";
    private JMenu edit, other, window; //perspective
    private JCheckBoxMenuItem showTooltips;

    /**
     * konstruktor
     *
     * @param workbench - środowisko pracy
     *
     */
    public CoreService(Workbench workbench) {
        super(workbench);
        // ustawienie domyslnego okna nadzrzednego dla komunikatow
        DialogBox.setParentWindow(workbench.getFrame());
    }

    /**
     * instalacja akcji
     */
    public void installMenuItems() {
        final Workbench w = super.workbench;

        JMenu file = new JMenu(Labels.FILE);
        file.setMnemonic(KeyEvent.VK_P);

        file.add(new JMenu(Labels.IMPORT));
        file.add(new JMenu(Labels.EXPORT));

        // wyjscie z programu
        file.add(new MenuItemExt(Labels.EXIT, KeyEvent.VK_W, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        }));

        edit = new JMenu(Labels.EDIT);
        edit.addMenuListener(this);
        edit.setMnemonic(KeyEvent.VK_E);
        edit.add(new DefaultEditorKit.CopyAction() {
            private static final long serialVersionUID = 1L;

            {
                putValue(Action.NAME, Labels.CUT);
                putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_W));
            }
        });

        edit.add(new DefaultEditorKit.CopyAction() {
            private static final long serialVersionUID = 1L;

            {
                putValue(Action.NAME, Labels.COPY);
                putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_K));
            }
        });
        edit.add(new DefaultEditorKit.PasteAction() {
            private static final long serialVersionUID = 1L;

            {
                putValue(Action.NAME, Labels.PASTE);
                putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
            }
        });

        JMenu help = new JMenu(Labels.HELP);
        help.setMnemonic(KeyEvent.VK_C);

        // o programie
        help.add(new MenuItemExt(Labels.ABOUT_APP, KeyEvent.VK_O, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AboutFrame.showModal(w);
            }
        }));

        // wyswietlanie tooltipow
        showTooltips = new JCheckBoxMenuItem(Labels.SHOW_TOOLTIPS);
        showTooltips.setMnemonic(KeyEvent.VK_D);
        showTooltips.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {		 	 // wywolanie zdarzenia
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                w.setParam(SHOW_TOOLTIPS_PARAM, item.isSelected() ? "1" : "0"); // ustawienie pokazywnia tooltipow
                ToolTipGenerator.getGenerator().setEnabledTooltips(item.isSelected());
            }
        });

        window = new JMenu(Labels.WINDOW);
        window.setMnemonic(KeyEvent.VK_O);

        // standardowe ustawienia okna
        window.add(new MenuItemExt(Labels.DEFAULT_SETTINGS, KeyEvent.VK_S, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                w.getActivePerspective().resetViews();
            }
        }));

        window.addSeparator();
        window.addMenuListener(this);
        window.add(showTooltips);

        other = new JMenu(Labels.OTHER);
        other.setMnemonic(KeyEvent.VK_I);
        other.addMenuListener(this);

        workbench.installMenu(file);
        workbench.installMenu(edit);
        workbench.installMenu(other);
        workbench.installMenu(window);
        workbench.installMenu(help);
    }

    /**
     * menu perspektywa zostalo rozwiniete
     */
    public void menuSelected(MenuEvent arg0) {
    }

    public boolean onClose() {
        return true;
    }

    public void onStart() {
        /**
         *
         */
    }

    public void installViews() {
        /**
         *
         */
    }

    public void menuDeselected(MenuEvent arg0) {
        /**
         *
         */
    }

    public void menuCanceled(MenuEvent arg0) {
        /**
         *
         */
    }
}
