package pl.edu.pwr.wordnetloom.systems.ui;

import java.util.List;

import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;

//Lexcion combo
public class LexiconComboBox extends ComboBoxPlain<Lexicon>{

	private static final long serialVersionUID = -3667933167567163L;
	private List<Lexicon> all;
	private String nullRepresentation;

	public LexiconComboBox(String nullItemRepresentation){
		this.nullRepresentation = nullItemRepresentation;
		loadLexicons();
		loadItems();
	}

	private void loadLexicons(){
		all = LexiconManager.getInstance().getFullLexicons();
	}

	private void loadItems(){
		removeAllItems();
		addItem(new CustomDescription<Lexicon>(nullRepresentation, null));
		for(Lexicon lexicon : all){
			if(lexicon.getId()>0){
				addItem(new CustomDescription<Lexicon>(lexicon.toString(), lexicon));
			}
		}
	}

	public void refreshLexicons(){
		loadLexicons();
		loadItems();
	}
}
