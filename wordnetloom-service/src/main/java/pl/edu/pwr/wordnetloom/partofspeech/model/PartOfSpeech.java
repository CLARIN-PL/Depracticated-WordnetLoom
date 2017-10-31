package pl.edu.pwr.wordnetloom.partofspeech.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.common.model.Localised;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Table(name = "part_of_speech")
public class PartOfSpeech extends GenericEntity {

    private static final long serialVersionUID = 6240656223603990725L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    @Valid
    private final Localised nameStrings = new Localised();

    private String color;

    public PartOfSpeech() {
        super();
    }

    public PartOfSpeech(String locale, String name) {
        nameStrings.addString(locale, name);
    }

    public String getName(String locale) {
        return nameStrings.getString(locale);
    }

    public void setName(String locale, String name) {
        nameStrings.addString(locale, name);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
