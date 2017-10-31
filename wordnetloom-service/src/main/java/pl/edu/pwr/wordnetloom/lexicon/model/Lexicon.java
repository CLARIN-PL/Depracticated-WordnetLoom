package pl.edu.pwr.wordnetloom.lexicon.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "lexicon")
public class Lexicon extends GenericEntity {

    private static final long serialVersionUID = -1256292370070216845L;

    @NotNull
    private String name;

    @NotNull
    private String identifier;

    @NotNull
    @Column(name = "language_name")
    private String languageName;

    public Lexicon() {
    }

    public Lexicon(String name, String identifier, String languageName) {
        this.name = name;
        this.identifier = identifier;
        this.languageName = languageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Lexicon other = (Lexicon) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return name;
    }
}
