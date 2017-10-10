package pl.edu.pwr.wordnetloom.plugins.lexeditor.views;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.panel.LexicalUnitPropertiesPanel;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.systems.ui.ToastMessage;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.dto.RegisterTypes;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.systems.enums.WorkState;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.utils.Messages;
import se.datadosen.component.RiverLayout;

public class LexicalUnitPropertiesViewUI extends AbstractViewUI {

	private LexicalUnitPropertiesPanel editPanel;

	private JPanel content;
	private final ViwnGraphViewUI graphUI;

	public LexicalUnitPropertiesViewUI(ViwnGraphViewUI graphUI) {
		this.graphUI = graphUI;
	}

	@Override
	public void initialize(final JPanel content) {
		this.content = content;
		this.content.setLayout(new RiverLayout());
		editPanel = new LexicalUnitPropertiesPanel(graphUI.getWorkbench().getFrame());
		content.add("hfill", editPanel);

		editPanel.getBtnSave().addActionListener(e -> {
//				editPanel.getBtnSave().setEnabled(false);
            saveChangesInUnit();
//				editPanel.getBtnSave().setEnabled(false);
			// okieno jednostek posiada kilka paneli. Do każdego panelu podpięty jest odzielny słuchacz, realizujący
			// zapis danych znajdujących się w konkretnym panelu.
			// ten słuchacz powinien byc wykonywany jako ostatni, dlatego w tym miejscu zostaje wyświetlony komunikat
			// informujący o statusie zapisu
			showSaveMessage(true); //TODO znaleźć sposób, aby rozpoznać czy zapis się udał czy nie
        });
	}

	private void showSaveMessage(boolean success)
	{
		String messageText;
		Color textColor;
		if(success) {
			messageText = Labels.SAVE_SUCCESS;
			textColor = Color.GREEN;
		} else {
			messageText = Labels.SAVE_FAILURE;
			textColor = Color.RED;
		}
		ToastMessage message = new ToastMessage(messageText, ToastMessage.SHORT_MESSAGE, editPanel.getBtnSave(), new Point(0, -30));
		message.setTextColor(textColor);
		message.showMessage();
	}

	public void fillKPWrExample(Object[] examples) {
		if (examples == null)
			return;
		for (int i = 0; i < examples.length; i++) {
			editPanel.getExamplesModel().addElement(examples[i]);
		}
		editPanel.getBtnSave().setEnabled(true);
	}

	public void refreshData(Sense unit) {
		editPanel.setSense(unit);
	}

	public void closeWindow(ActionListener close) {
		editPanel.getBtnCancel().addActionListener(close);
	}

	public void saveChangesInUnit() {
		if (validateSelections()) {
			try {
				String lemma = editPanel.getLemma().getText();
				PartOfSpeech pos = editPanel.getPartOfSpeech().retriveComboBoxItem();
				Domain domain = editPanel.getDomain().retriveComboBoxItem();
				int variant = Integer.parseInt(editPanel.getVariant().getText());
				String register = editPanel.getRegister().getSelectedItem() != null
						? editPanel.getRegister().getSelectedItem().toString() : RegisterTypes.BRAK_REJESTRU.toString();
				String definition = editPanel.getDefinition().getText();
				String example = transformExamplesToString();
				String link = editPanel.getLink().getText();
				String comment = editPanel.getComment().getText();

				// Zmienił się lemat, więc należy uaktualnić numer lematu
				// (wariant)
				if (!editPanel.getSense().getLemma().getWord().equals(lemma)) {
					variant = LexicalDA.getAvaibleVariantNumber(lemma, pos, LexiconManager.getInstance().getLexicons());
				}

				if (!LexicalDA.updateUnit(editPanel.getSense(), lemma, editPanel.getLexicon().retriveComboBoxItem(),
						variant, domain, pos, 0, WorkState.values()[0], comment, register, example, link, definition)) {
					refreshData(editPanel.getSense());
					DialogBox.showError(Messages.ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_UNITS);
				}

				// Uaktualnij numer wariantu, jeżeli został w związku ze zmianą
				// lematu
				editPanel.getVariant().setText("" + variant);

				ViwnNode node = graphUI.getSelectedNode();
				if (node != null && node instanceof ViwnNodeSynset) {
					ViwnNodeSynset s = (ViwnNodeSynset) node;
					s.setLabel(null);
					graphUI.graphChanged();
				} else {
					// dodano nowy synset, nie istnieje on nigdzie w grafie
					graphUI.graphChanged();
				}

				listeners.notifyAllListeners(editPanel.getSense());
			} catch (Exception e) {
				Logger.getLogger(LexicalUnitPropertiesViewUI.class).log(Level.ERROR, "Number format" + e);
				e.printStackTrace();
				DialogBox.showError(Messages.ERROR_WRONG_NUMBER_FORMAT);
			}
		}
	}

	protected String transformExamplesToString() {
		Object[] examples = editPanel.getExamplesModel().toArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < examples.length; i++) {
			sb.append(examples[i]);
			sb.append("|");
		}
		return sb.toString();
	}

	@Override
	public JComponent getRootComponent() {
		if (editPanel == null) {
			return editPanel.getLemma();
		}
		return editPanel.getLemma();
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
			return false;
		}
		return true;
	}
}