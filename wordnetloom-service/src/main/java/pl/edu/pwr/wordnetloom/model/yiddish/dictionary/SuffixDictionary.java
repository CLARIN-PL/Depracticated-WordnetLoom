package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class SuffixDictionary extends Dictionary{

	public SuffixDictionary() {
	}
	
	public SuffixDictionary(String value, String desc) {
		super( value, desc );
	}

    @Override
    public String toString() {
    	if(description.length() > 8){
    		String shortDesc = description.substring(0, 8) + " ...";
    		return name +" ( "+shortDesc+" )";
    	}
        return name +" ( "+description+" )";
    }
}
