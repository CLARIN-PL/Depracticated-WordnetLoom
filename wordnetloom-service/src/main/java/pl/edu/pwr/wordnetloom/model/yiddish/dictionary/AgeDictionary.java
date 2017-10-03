package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class AgeDictionary extends Dictionary{
	
	public AgeDictionary() {
    }

    public  AgeDictionary(String value,String desc){
        super(value, desc);
    }
	@Override
    public String toString() {
        return name;
    }
}
