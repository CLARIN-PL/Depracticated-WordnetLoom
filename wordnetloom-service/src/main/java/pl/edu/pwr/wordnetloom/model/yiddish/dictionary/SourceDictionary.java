package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class SourceDictionary extends Dictionary{

	public SourceDictionary() {
	}
	
	public SourceDictionary(String value, String desc){
		super(value, desc);
	}
	@Override
    public String toString() {
        return name;
    }
}
