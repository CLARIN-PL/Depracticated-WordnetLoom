package pl.edu.pwr.wordnetloom.dictionary.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.common.model.Localised;

import javax.persistence.*;

@Entity
@Table(name = "dictionaries")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "dtype",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Dictionary extends GenericEntity {

    private static final long serialVersionUID = -7858918337069154092L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    protected Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    protected Localised descriptionStrings = new Localised();

    public Dictionary() {
    }

    public Dictionary(String locale, String name, String description) {
        nameStrings.addString(locale, name);
        descriptionStrings.addString(locale, description);
    }


    public String getName(String locale) {
        return nameStrings.getString(locale);
    }

    public void setName(String locale, String name) {
        nameStrings.addString(locale, name);
    }

    public String getDescription(String locale) {
        return descriptionStrings.getString(locale);
    }

    public void setDescription(String locale, String description) {
        descriptionStrings.addString(locale, description);
    }

    @Override
    public int hashCode() {
        int prime = 31;
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
