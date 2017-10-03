package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class DomainDictionary extends Dictionary {

	public DomainDictionary() {
	}
	
	public DomainDictionary(String value,String desc){
		super(value, desc);
	}

	@Override
    public String toString() {
        return name;
    }
}
