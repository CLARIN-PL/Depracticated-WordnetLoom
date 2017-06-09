package pl.edu.pwr.wordnetloom.model.wordnet;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "text")
@NamedQueries({
    @NamedQuery(name = "Text.findByTextName", query = "select t from Text t where t.text =:name")})
public class Text implements Serializable {

    private static final long serialVersionUID = 6526507616412209035L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    public Text() {
    }

    public Text(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Text)) {
            return false;
        }
        Text e = (Text) o;

        if (id == null) {
            return false;
        }
        return id.equals(e.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = (id.hashCode());
        if (hashCode == 0) {
            return super.hashCode();
        }
        return hashCode;
    }
}
