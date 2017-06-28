package pl.edu.pwr.wordnetloom.partofspeech.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import pl.edu.pwr.wordnetloom.common.model.Localised;

@Entity
@Table(name = "part_of_speech")
public class PartOfSpeech implements Serializable {

    private static final long serialVersionUID = 6240656223603990725L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    @Valid
    private Localised nameStrings = new Localised();

    public PartOfSpeech() {
        super();
    }

    public PartOfSpeech(String locale, String name) {
        this.nameStrings.addString(locale, name);
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

}
