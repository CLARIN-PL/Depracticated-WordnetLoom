package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class StatusDictionary extends Dictionary{

	public StatusDictionary() {
	}
	
	public StatusDictionary(String value, String desc) {
		super(value, desc);
	}
	@Override
    public String toString() {
        return name;
    }
}
