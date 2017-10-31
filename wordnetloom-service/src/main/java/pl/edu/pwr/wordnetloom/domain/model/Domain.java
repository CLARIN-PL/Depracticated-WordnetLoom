package pl.edu.pwr.wordnetloom.domain.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.common.model.Localised;

import javax.persistence.*;

@Entity
@Table(name = "domain")
public class Domain extends GenericEntity {

    private static final long serialVersionUID = -9015379547562677206L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private final Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private final Localised descriptionStrings = new Localised();

    public Domain() {
        super();
    }

    public Domain(String locale, String name, String description) {
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

}
