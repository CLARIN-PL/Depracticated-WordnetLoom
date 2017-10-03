package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class InflectionDictionary extends Dictionary{

	public InflectionDictionary() {
	}
	
	public InflectionDictionary(String value, String desc) {
		super( value ,desc);
	}
	
	@Override
    public String toString() {
        return name;
    }
}
