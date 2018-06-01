package pl.edu.pwr.wordnetloom.wordform.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "word_form")
@NamedQuery(name = WordForm.FIND_FORM_BY_WORD, query = "SELECT w FROM WordForm w WHERE w.word = :word")
public class WordForm implements Serializable {

    public static final String FIND_FORM_BY_WORD = "WordForm.findFormByWord";
    private static final long serialVersionUID = 1984202638795803311L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String tag;

    private String form;

    public WordForm(){};

    public WordForm(String word, String tag, String form) {
        this.word = word;
        this.tag = tag;
        this.form = form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordForm)) return false;

        WordForm wordForm = (WordForm) o;

        if (id != null ? !id.equals(wordForm.id) : wordForm.id != null) return false;
        if (word != null ? !word.equals(wordForm.word) : wordForm.word != null) return false;
        if (tag != null ? !tag.equals(wordForm.tag) : wordForm.tag != null) return false;
        return form != null ? form.equals(wordForm.form) : wordForm.form == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (form != null ? form.hashCode() : 0);
        return result;
    }
}
