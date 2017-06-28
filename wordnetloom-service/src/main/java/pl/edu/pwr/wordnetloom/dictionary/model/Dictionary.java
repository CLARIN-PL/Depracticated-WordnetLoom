package pl.edu.pwr.wordnetloom.dictionary.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.common.model.Localised;

@Entity
@Table(name = "dictionaries")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "dtype",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Dictionary implements Serializable {

    private static final long serialVersionUID = -7858918337069154092L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    protected Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    protected Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    protected Localised descriptionStrings = new Localised();

    public Dictionary() {
    }

    public Dictionary(String locale, String name, String description) {
        this.nameStrings.addString(locale, name);
        this.descriptionStrings.addString(locale, description);
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

    public String getDescription(String locale) {
        return this.descriptionStrings.getString(locale);
    }

    public void setDescription(String locale, String description) {
        this.descriptionStrings.addString(locale, description);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
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
        Dictionary other = (Dictionary) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
