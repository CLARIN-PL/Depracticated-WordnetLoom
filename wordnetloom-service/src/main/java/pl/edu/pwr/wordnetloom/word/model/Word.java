package pl.edu.pwr.wordnetloom.word.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "word")
public class Word extends GenericEntity {

    private static final long serialVersionUID = -1256292370070216845L;

    @NotNull
    private String word;

    public Word() {
    }

    public Word(String lemma) {
        word = lemma;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(id);
        hash = 37 * hash + Objects.hashCode(word);
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
        Word other = (Word) obj;
        return Objects.equals(word, other.word) && Objects.equals(id, other.id);
    }
}
