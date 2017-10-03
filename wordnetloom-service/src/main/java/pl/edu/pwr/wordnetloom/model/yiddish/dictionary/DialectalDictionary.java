package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class DialectalDictionary extends Dictionary{

	public DialectalDictionary() {
	}
	
	public DialectalDictionary(String value, String desc) {
		super( value ,desc);
	}
	
	@Override
    public String toString() {
        return name;
    }
}
