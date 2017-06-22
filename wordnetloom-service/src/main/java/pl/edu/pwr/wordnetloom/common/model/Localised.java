package pl.edu.pwr.wordnetloom.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Localised implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    private Map<String, String> strings = new HashMap<>();

    public Localised() {
    }

    public Localised(Map<String, String> map) {
        this.strings = map;
    }

    public void addString(String locale, String text) {
        strings.put(locale, text);
    }

    public String getString(String locale) {
        String returnValue = strings.get(locale);
        return (returnValue != null ? returnValue : null);
    }

}
