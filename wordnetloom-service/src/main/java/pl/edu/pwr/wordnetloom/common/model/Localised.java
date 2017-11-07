package pl.edu.pwr.wordnetloom.common.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "localised")
public class Localised extends GenericEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "localised_strings",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    private Map<String, String> strings = new HashMap<>();

    public Localised() {
    }

    public Localised(Map<String, String> map) {
        strings = map;
    }

    public void addString(String locale, String text) {
        strings.put(locale, text);
    }

    public String getString(String locale) {
        String returnValue = strings.get(locale);
        return returnValue;
    }

    public Boolean isValid() {
        if (strings.keySet().contains(null) || strings.values().contains(null)) {
            return false;
        }
        return true;
    }
}
