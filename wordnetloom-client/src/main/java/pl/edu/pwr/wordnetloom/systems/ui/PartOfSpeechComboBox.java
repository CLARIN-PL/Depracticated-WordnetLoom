package pl.edu.pwr.wordnetloom.systems.ui;

import java.util.List;

import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.Main;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;

public class PartOfSpeechComboBox extends ComboBoxPlain<PartOfSpeech> {

	private static final long serialVersionUID = -2688488007035679886L;
	private List<PartOfSpeech> all;
	private String nullRepresentation;

	public PartOfSpeechComboBox(String nullItemRepresentation) {
		this.nullRepresentation = nullItemRepresentation;
	}

	private void loadPartOfSpeech() {
		all = PosManager.getInstance().getAllPOSes();
	}

	private void loadPosByLexicon(long id) {
		all = PosManager.getInstance().getPOSForLexicon(id);
	}

	private void loadItems() {
		removeAllItems();
		addItem(new CustomDescription<PartOfSpeech>(nullRepresentation, null));
		for (PartOfSpeech pos : all) {
			addItem(new CustomDescription<PartOfSpeech>(pos.toString(), pos));
		}
	}

	public void showUbyItems() {
		removeAllItems();
		List<PartOfSpeech> ubyOnly = PosManager.getInstance().getPOSForLexicon(3l);
		addItem(new CustomDescription<PartOfSpeech>(nullRepresentation, null));
		for (PartOfSpeech pos : ubyOnly) {
			String desc = Main.getResouce("label." + pos.getUbyType().toString());
			addItem(new CustomDescription<PartOfSpeech>(desc, pos));
		}
	}

	public void filterByLexicon(Lexicon lexicon) {
		loadPosByLexicon(lexicon.getId());
		loadItems();
	}

	public void withoutFilter() {
		loadPartOfSpeech();
		loadItems();
	}
}
