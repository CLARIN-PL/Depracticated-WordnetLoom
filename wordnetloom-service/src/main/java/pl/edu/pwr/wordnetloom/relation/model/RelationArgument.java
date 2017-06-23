package pl.edu.pwr.wordnetloom.relation.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.common.model.Localised;

@Entity
@Table(name = "relation_argument")
public class RelationArgument implements Serializable {

    public static final RelationArgument LEXICAL = new RelationArgument(0L, "relacja leksykalna", "PL");
    public static final RelationArgument SYNSET = new RelationArgument(1L, "relacja pomiędzy synsetami", "PL");
    public static final RelationArgument LEXICAL_SPECIAL = new RelationArgument(2L, "relacja synonimii", "PL");

    private static final long serialVersionUID = 9122691733246478907L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private Localised nameStrings = new Localised();

    public RelationArgument() {
    }

    public RelationArgument(Long i, String name, String locale) {
        this.id = i;
        this.nameStrings.addString(locale, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName(String locale) {
        return this.nameStrings.getString(locale);
    }

    public void setName(String locale, String name) {
        this.nameStrings.addString(locale, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof RelationArgument)) {
            return false;
        }
        RelationArgument e = (RelationArgument) o;

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

    public static RelationArgument[] values() {
        return new RelationArgument[]{LEXICAL, LEXICAL_SPECIAL, SYNSET};
    }
}
