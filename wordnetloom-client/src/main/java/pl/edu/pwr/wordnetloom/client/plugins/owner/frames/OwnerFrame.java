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
package pl.edu.pwr.wordnetloom.client.plugins.owner.frames;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import pl.edu.pwr.wordnetloom.client.plugins.owner.data.SessionData;
import pl.edu.pwr.wordnetloom.client.systems.misc.ActionWrapper;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * okienko z nazwa uzytkownika
 *
 * @author Max
 */
public class OwnerFrame extends IconDialog implements CaretListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private static final String USER_NAME_FORMAT = "%s.%s";

    private final JTextArea infoLabel;
    private final JTextField nameEdit;
    private final JTextField surnameEdit;
    private final ButtonExt buttonOk;

    private String newOwner = null;

    /**
     * konstruktor
     *
     * @param workbench - srodowisko
     */
    private OwnerFrame(Workbench workbench) {
        super(workbench.getFrame(), Labels.USER_DATA, 270, 190);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        infoLabel = new JTextArea(Messages.INFO_ADD_USER_DATA_FIRST);
        infoLabel.setEditable(false);
        infoLabel.setFocusable(false);
        infoLabel.setWrapStyleWord(true);
        infoLabel.setLineWrap(true);
        infoLabel.setBackground(new JLabel().getBackground());

        nameEdit = new JTextField();
        nameEdit.addCaretListener(this);
        nameEdit.addKeyListener(this);

        surnameEdit = new JTextField();
        surnameEdit.setFocusAccelerator('n');
        surnameEdit.addCaretListener(this);
        surnameEdit.addKeyListener(this);

        buttonOk = new ButtonExt(Labels.OK, new ActionWrapper(this, "buttonOk_Clicked"), this, KeyEvent.VK_O);
        buttonOk.setEnabled(false);

        this.add("hfill vfill", infoLabel);
        this.add("br", new LabelExt(Labels.FIRSTNAME, 'i', nameEdit));
        this.add("tab hfill", nameEdit);
        this.add("br", new LabelExt(Labels.LASTNAME, 'n', surnameEdit));
        this.add("tab hfill", surnameEdit);
        this.add("br", new JLabel(" "));
        this.add("br center", buttonOk);
    }

    /**
     * wyświetlenie okienka
     *
     * @param workbench - srodowisko
     * @return nazwa użytkownika
     */
    static public SessionData showModal(Workbench workbench) {
        OwnerFrame frame = new OwnerFrame(workbench);
        SessionData sessionData = new SessionData();
        frame.setVisible(true);
        sessionData.owner = frame.newOwner;
        frame.dispose();
        return sessionData;
    }

    /**
     * przycisk ok zostal nacisniety
     */
    public void buttonOk_Clicked() {
        newOwner = String.format(USER_NAME_FORMAT, nameEdit.getText(), surnameEdit.getText());
        setVisible(false);
    }

    /**
     * zmienił się tekst w polach edycyjnych
     *
     * @param event
     */
    @Override
    public void caretUpdate(CaretEvent event) {
        String name = nameEdit.getText();
        String surname = surnameEdit.getText();
        buttonOk.setEnabled(name != null && name.length() > 0 && surname != null && surname.length() > 0);
    }

    /**
     * wcisnieto klawisz w polach edycyjnych
     *
     * @param event
     */
    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyChar() == KeyEvent.VK_ENTER) {            // kliknieto enter
            if (event.getSource() == nameEdit) { // w polu z imieniem
                event.consume();
                surnameEdit.grabFocus();
            } else if (event.getSource() == surnameEdit) { // w polu z nazwiskiem
                event.consume();
                if (buttonOk.isEnabled()) {
                    buttonOk.doClick();
                } else {
                    nameEdit.grabFocus();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }
}
