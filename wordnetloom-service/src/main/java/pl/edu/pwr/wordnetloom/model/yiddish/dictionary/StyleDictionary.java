package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class StyleDictionary extends Dictionary{

	public StyleDictionary() {
	}
	
	public StyleDictionary(String value, String desc){
		super(value, desc);
	}
	@Override
    public String toString() {
        return name;
    }
}
