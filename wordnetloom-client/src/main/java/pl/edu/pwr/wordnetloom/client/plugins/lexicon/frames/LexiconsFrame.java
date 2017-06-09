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
package pl.edu.pwr.wordnetloom.client.plugins.lexicon.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.commons.collections15.map.HashedMap;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.model.wordnet.Lexicon;

/**
 * Window to choose lexicons
 *
 * @author tnaskret
 */
public class LexiconsFrame extends IconDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final Map<String, Long> lexiconMap = new HashedMap<>();
    private JLabel infoLabel;
    private ButtonExt buttonOk;
    private JCheckBox[] arrayOfLexiconCheckBoxes;

    public LexiconsFrame(JFrame owner, List<Long> lexiconsFromConfig) {
        super(owner, Labels.LEXICON);
        initInfoLabel(Labels.CHOOSE_LEXICON_TO_WORK);
        initAndLoadCheckBoxes(lexiconsFromConfig);
        initAndCalculateWindowSize();
        initButton();
        pack();

        setLocationRelativeTo(owner);
    }

    private void initInfoLabel(String infoMessage) {
        infoLabel = new JLabel(infoMessage);
        this.add("br center", infoLabel);
    }

    private void initAndLoadCheckBoxes(List<Long> lexiconsFromConfig) {

        Collection<Lexicon> lexicons = RemoteUtils.lexicalUnitRemote.getAllLexicons();
        arrayOfLexiconCheckBoxes = new JCheckBox[lexicons.size()];

        int i = 0;
        for (Lexicon lexicon : lexicons) {
            if (i > 0) {
                this.lexiconMap.put(lexicon.getName().getText(),
                        lexicon.getId());
                arrayOfLexiconCheckBoxes[i] = new JCheckBox(lexicon.getName()
                        .getText());
                if (lexiconsFromConfig.contains(lexicon.getId())) {
                    arrayOfLexiconCheckBoxes[i].setSelected(true);
                } else {
                    arrayOfLexiconCheckBoxes[i].setSelected(false);
                }
                this.add("br center", arrayOfLexiconCheckBoxes[i]);
            }
            i++;
        }
    }

    private void initButton() {
        buttonOk = new ButtonExt(Labels.OK, this, KeyEvent.VK_O);
        buttonOk.setEnabled(true);
        this.add("br center", buttonOk);
    }

    private void initAndCalculateWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 300;
        int height = 60 + 30 * (lexiconMap.size() + 1);
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        this.setBounds(x, y, width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private String selectedLexiconsAsIdString() {
        StringBuilder sb = new StringBuilder();

        // Everyone has access to undefined lexicons
        sb.append("0");

        for (int i = 1; i < arrayOfLexiconCheckBoxes.length; i++) {
            if (arrayOfLexiconCheckBoxes[i].isSelected()) {
                sb.append(",");
                sb.append(lexiconMap.get(arrayOfLexiconCheckBoxes[i].getText()));
            }
        }
        return sb.toString();
    }

    /**
     * Show lexicon window
     *
     * @return Lexicons as string list
     */
    public String showModal() {
        this.setVisible(true);
        this.dispose();
        return selectedLexiconsAsIdString();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonOk) {
            setVisible(false);
        }
    }

}
