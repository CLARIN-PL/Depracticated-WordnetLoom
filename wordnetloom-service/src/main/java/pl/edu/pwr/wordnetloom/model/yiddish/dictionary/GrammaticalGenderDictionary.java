package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class GrammaticalGenderDictionary extends Dictionary{

	public GrammaticalGenderDictionary() {
	}
	
	public GrammaticalGenderDictionary(String value, String desc) {
		super(value, desc);
	}
}
