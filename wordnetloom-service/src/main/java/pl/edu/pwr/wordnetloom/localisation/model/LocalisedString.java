package pl.edu.pwr.wordnetloom.localisation.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "application_localised_string")
public class LocalisedString implements Serializable {

    @Valid
    @NotNull
    @EmbeddedId
    private LocalisedKey key;

    @Column(name = "value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalisedString() {
    }

    public LocalisedString(Long id, String lang, String value) {
        addKey(id, lang);
        this.value = value;
    }

    public void addKey(Long id, String language) {
        if (key != null) {
            key.setId(id);
            key.setLanguage(language);
        } else {
            key = new LocalisedKey(id, language);
        }
    }

    public LocalisedKey getKey() {
        return key;
    }

    public void setKey(LocalisedKey key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalisedString)) return false;

        LocalisedString string = (LocalisedString) o;

        if (key != null ? !key.equals(string.key) : string.key != null) return false;
        return value != null ? value.equals(string.value) : string.value == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
