package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class LexicalCharacteristicDictionary extends Dictionary {

	public LexicalCharacteristicDictionary() {
	}

	public LexicalCharacteristicDictionary(String value, String desc) {
		super(value, desc);
	}

	@Override
    public String toString() {
        return name;
    }
}
