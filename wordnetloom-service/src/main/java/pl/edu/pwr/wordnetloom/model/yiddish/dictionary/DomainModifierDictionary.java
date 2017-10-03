package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class DomainModifierDictionary extends Dictionary {

	public DomainModifierDictionary() {
	}
	
	public DomainModifierDictionary(String value,String desc){
		super(value, desc);
	}
	
	@Override
    public String toString() {
        return name;
    }
}
