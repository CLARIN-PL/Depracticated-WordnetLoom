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
package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.frames;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

/**
 * okienko do edycji testu
 *
 * @author Max
 */
public class TestEditorFrame extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final ButtonExt buttonOk, buttonCancel;

    private final TextFieldPlain testText;
    private final ComboBoxPlain testPos;

    private PartOfSpeech lastPos = PartOfSpeechManager.getInstance().getById(1l);
    private String lastText = "";

    /**
     * konstruktor
     *
     * @param owner - srodowisko
     * @param text - tresc testu
     * @param pos - czesc mowy
     */
    private TestEditorFrame(JFrame owner, PartOfSpeech pos, String text) {
        super(owner, Labels.EDIT_TEST, 650, 130);

        lastPos = pos;
        lastText = text;

        this.setResizable(false);
        //this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension normal = new Dimension(100, 25);

        testText = new TextFieldPlain(lastText);
        testText.setPreferredSize(normal);
        testPos = new ComboBoxPlain(PartOfSpeechManager.getInstance().getAll().toArray(new PartOfSpeech[]{}));
        testPos.setPreferredSize(normal);

        buttonOk = new ButtonExt(Labels.OK, this, KeyEvent.VK_O);
        buttonCancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);

        if (pos != null) {
            testPos.setSelectedItem(pos);
        }

        // dodanie do zawartości okna
        this.add("", new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 't', testPos));
        this.add("tab", new LabelExt(Labels.TEST_CONTENT_COLON, 't', testText));

        this.add("br", testPos);
        this.add("tab hfill", testText);

        this.add("p center", buttonOk);
        this.add("", buttonCancel);
    }

    /**
     * wyświetlenie okienka
     *
     * @param owner - środowisko
     * @param text - nowy tekst
     * @param pos - czesc mowy
     * @return tablica zawierajaca [nowy tekst,nowy pos] - jesli nowy tekst jest
     * null to wcisnieto anuluj
     */
    static public Pair<String, PartOfSpeech> showModal(JFrame owner, String text, PartOfSpeech pos) {
        TestEditorFrame frame = new TestEditorFrame(owner, pos, text);
        frame.setVisible(true);
        return new Pair<>(frame.lastText, frame.lastPos);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == buttonCancel) {
            lastText = null;
            lastPos = null;
            setVisible(false);

        } else if (arg0.getSource() == buttonOk) {
            lastText = testText.getText();
            lastPos = (PartOfSpeech) testPos.getItemAt(testPos.getSelectedIndex());
            setVisible(false);
        }
    }

}
