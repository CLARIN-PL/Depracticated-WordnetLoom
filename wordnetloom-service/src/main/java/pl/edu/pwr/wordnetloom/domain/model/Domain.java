package pl.edu.pwr.wordnetloom.domain.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.common.model.Localised;

@Entity
@Table(name = "domain")
@NamedQueries({
    @NamedQuery(name = "Domain.getFromSenses",
            query = "SELECT DISTINCT s.domain FROM Sense s ORDER BY s.domain ASC"),
    @NamedQuery(name = "Domain.getAllDomains",
            query = "SELECT DISTINCT d FROM Domain d LEFT JOIN FETCH d.name LEFT JOIN FETCH d.description WHERE d.lexicon.id IN (:lexicons)")

})
public class Domain implements Serializable {

    private static final long serialVersionUID = -9015379547562677206L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private Localised descriptionStrings = new Localised();

    public Domain() {
        super();
    }

    public Domain(String locale, String name, String description) {
        this.nameStrings.addString(locale, name);
        this.descriptionStrings.addString(locale, description);
    }

    public Long getId() {
        return this.id;
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

}
