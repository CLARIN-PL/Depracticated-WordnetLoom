package pl.edu.pwr.wordnetloom.application.model;

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
@Table(name = "application_localized_label")
public class ApplicationLocalizedLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String key;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "value_id")
    private Localised valueStrings = new Localised();
    
     public ApplicationLocalizedLabel() {
        super();
    }

    public ApplicationLocalizedLabel(String locale, String key, String value) {
        this.key = key;
        this.valueStrings.addString(locale, value);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue(String locale) {
        return this.valueStrings.getString(locale);
    }

    public void setValue(String locale, String value) {
        this.valueStrings.addString(locale, value);
    }
}
