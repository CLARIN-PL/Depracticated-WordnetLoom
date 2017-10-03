package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class TranscriptionDictionary extends Dictionary{

	public TranscriptionDictionary() {
	}
	
	public TranscriptionDictionary(String value, String desc) {
		super( value ,desc);
	}
	
	@Override
    public String toString() {
        return name;
    }
}
