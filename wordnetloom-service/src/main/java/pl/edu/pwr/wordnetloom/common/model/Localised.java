package pl.edu.pwr.wordnetloom.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "localised")
public class Localised implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @NotNull
    @CollectionTable(name = "localised_strings", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
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
