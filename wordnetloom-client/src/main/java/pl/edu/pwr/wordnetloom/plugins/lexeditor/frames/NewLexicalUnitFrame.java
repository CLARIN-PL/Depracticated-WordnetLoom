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

package pl.edu.pwr.wordnetloom.plugins.lexeditor.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import pl.edu.pwr.wordnetloom.plugins.lexeditor.panel.LexicalUnitPropertiesPanel;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.systems.ui.DomainComboBox;
import pl.edu.pwr.wordnetloom.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.dto.RegisterTypes;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Word;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.utils.Common;
import pl.edu.pwr.wordnetloom.utils.Messages;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

/**
 * okienko do wprowadzania parametrow dla nowych jednostek
 * 
 * @author Max
 */
public class NewLexicalUnitFrame extends IconDialog implements ActionListener {

	private final LexicalUnitPropertiesPanel editPanel;

	private static Domain lastPickDomain = null;
	private static PartOfSpeech lastPickPos = null;

	private static final long serialVersionUID = 1L;
	private boolean wasAddClicked = false;

	private NewLexicalUnitFrame(Workbench workbench, JFrame frame) {
		super(frame, Labels.UNIT_PARAMS, 625, 500);
		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(frame);
		editPanel = new LexicalUnitPropertiesPanel(frame);
		editPanel.getBtnSave().addActionListener(this);
		editPanel.getBtnCancel().addActionListener(this);
		add(editPanel, "hfill");
		pack();
	}

	public Sense saveAndReturnNewSense() {
		Sense newUnit = new Sense();

		newUnit.setLemma(new Word(editPanel.getLemma().getText()));
		newUnit.setLexicon(editPanel.getLexicon().retriveComboBoxItem());

		PartOfSpeech pos = editPanel.getPartOfSpeech().retriveComboBoxItem();
		newUnit.setPartOfSpeech(pos);

		Domain domain = editPanel.getDomain().retriveComboBoxItem();
		newUnit.setDomain(domain);

		int variant = RemoteUtils.lexicalUnitRemote.dbGetNextVariant(editPanel.getLemma().getText(), pos);
		newUnit.setSenseNumber(variant);

		newUnit = LexicalDA.saveUnitRefreshID(newUnit);

		if (editPanel.getDefinition().getText() != null && !editPanel.getDefinition().getText().isEmpty())
			RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.DEFINITION,
					editPanel.getDefinition().getText());
		if (editPanel.getComment().getText() != null && !editPanel.getComment().getText().isEmpty())
			RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.COMMENT,
					editPanel.getComment().getText());
		if (editPanel.getRegister().getSelectedItem().toString() != null)
			RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.REGISTER,
					editPanel.getRegister().getSelectedItem().toString());
		String examples = trasfotmExamplesToString(editPanel.getExamplesModel().toArray());
		if (examples != null && !examples.isEmpty())
			RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.USE_CASES, examples);
		if (editPanel.getLink().getText() != null && !editPanel.getLink().getText().isEmpty())
			RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.LINK,
					editPanel.getLink().getText());
		if (editPanel.getComment().getText() != null && !editPanel.getComment().getText().isEmpty())
			RemoteUtils.trackerRemote.insertedLexicalUnit(newUnit, editPanel.getComment().getText(),
					PanelWorkbench.getOwnerFromConfigManager());

		return newUnit;
	}

	private String trasfotmExamplesToString(final Object[] examples) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < examples.length; i++) {
			sb.append(examples[i]);
			sb.append("|");
		}
		return sb.toString();
	}

	static public Sense showModal(Workbench workbench, PartOfSpeech newPos) {
		return showModal(workbench, workbench.getFrame(), null, newPos, DomainManager.getInstance().getByID(0));
	}

	static public Sense showModal(Workbench workbench, String word, PartOfSpeech newPos) {
		return showModal(workbench, workbench.getFrame(), word, newPos, DomainManager.getInstance().getByID(0));
	}

	/**
	 * wyswietlenie okienka dialogowego
	 * 
	 * @param workbench
	 *            - srodowisko
	 * @param frame
	 * @param word
	 * @param newPos
	 *            - pos dla jednostki, null gdy mozna go wybrac
	 * @param domain
	 * @return nowa jednostka lub null gdy anulowano
	 */
	static public Sense showModal(Workbench workbench, JFrame frame, String word, PartOfSpeech newPos, Domain domain) {

		NewLexicalUnitFrame modalFrame = new NewLexicalUnitFrame(workbench, frame);
		modalFrame.editPanel.getLexicon().setSelectedIndex(1);
		if (word != null) {
			modalFrame.editPanel.getLemma().setText(word);
			modalFrame.editPanel.getLemma().setEditable(false);
		} else {
			modalFrame.editPanel.getLemma().setText("");
		}
		if (newPos != null) {
			modalFrame.editPanel.getPartOfSpeech().setSelectedItem(newPos);
		} else if (lastPickPos != null) {
			modalFrame.editPanel.getPartOfSpeech()
					.setSelectedItem(new CustomDescription<PartOfSpeech>(lastPickPos.toString(), lastPickPos));
		}

		if (lastPickDomain != null) {
			modalFrame.editPanel.getDomain().setSelectedItem(new CustomDescription<Domain>(
					DomainComboBox.nameWithoutPrefix(lastPickDomain.toString()), lastPickDomain));
		}

		modalFrame.editPanel.getRegister().setSelectedItem(RegisterTypes.OG);

		modalFrame.setVisible(true);
		Sense newUnit = null;
		if (modalFrame.wasAddClicked) {
			newUnit = modalFrame.saveAndReturnNewSense();
		}

		modalFrame.dispose();
		modalFrame = null;
		return newUnit;
	}

	/**
	 * nacisnieto ktorys z przyciskow
	 */
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == editPanel.getBtnSave()) {

			String testLemma = editPanel.getLemma().getText();
			// spradzenie czy nie ma juz takich
			List<Sense> units = new ArrayList<Sense>();
			units = LexicalDA.getFullLexicalUnits(testLemma, LexiconManager.getInstance().getLexicons());
			if (validateSelections()) {
				if (checkUnitExists(testLemma, units)) {
					this.wasAddClicked = true;
					this.setVisible(false);
				}
			}

			lastPickDomain = editPanel.getDomain().retriveComboBoxItem();
			lastPickPos = editPanel.getPartOfSpeech().retriveComboBoxItem();

		} else if (event.getSource() == editPanel.getBtnCancel()) {
			this.setVisible(false);
		}
	}

	private boolean checkUnitExists(String testLemma, List<Sense> units) {
		if (units != null && units.size() > 0) {

			Domain testDomain = editPanel.getDomain().retriveComboBoxItem();
			PartOfSpeech testPos = editPanel.getPartOfSpeech().retriveComboBoxItem();
			String testComments = editPanel.getComment().getText();

			for (Sense unit : units) {
				String comment = Common.getSenseAttribute(unit, Sense.COMMENT);
				// check domain exists
				if (testLemma.equals(unit.getLemma().getWord()) && unit.getDomain().getId().equals(testDomain.getId())
						&& unit.getPartOfSpeech().equals(testPos) && testComments.equals(comment)) {
					this.setAlwaysOnTop(false);
					DialogBox.showError(Messages.FAILURE_UNIT_EXISTS);
					this.setAlwaysOnTop(true);
					return false;
				}
			}
		}
		return true;
	}

	private boolean validateSelections() {
		if (editPanel.getLemma().getText() == null || "".equals(editPanel.getLemma().getText())) {
			DialogBox.showError(Messages.SELECT_LEMMA);
			return false;
		}
		if (editPanel.getLexicon().retriveComboBoxItem() == null) {
			DialogBox.showError(Messages.SELECT_LEXICON);
			return false;
		}
		if (editPanel.getPartOfSpeech().retriveComboBoxItem() == null) {
			DialogBox.showError(Messages.SELECT_POS);
			return false;
		}
		if (editPanel.getDomain().retriveComboBoxItem() == null) {
			DialogBox.showError(Messages.SELECT_DOMAIN);
			return true;
		}
		return true;
	}
}
