package pl.edu.pwr.wordnetloom.partofspeech.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "part_of_speech")
public class PartOfSpeech extends GenericEntity {

    private static final long serialVersionUID = 6240656223603990725L;

    @NotNull
    @Column(name = "name_id")
    private Long name;

    private String color;

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
