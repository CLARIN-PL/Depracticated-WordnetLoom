package pl.edu.pwr.wordnetloom.model.yiddish.dictionary;

import javax.persistence.Entity;

@Entity
public class InterfixDictionary extends Dictionary {

	public InterfixDictionary() {
	}

	public InterfixDictionary(String value, String desc) {
		super(value, desc);
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
