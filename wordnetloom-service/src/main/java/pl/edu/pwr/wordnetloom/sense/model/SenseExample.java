package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.*;

@Entity
@Table(name = "sense_examples")
public class SenseExample extends GenericEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sense_attribute_id", referencedColumnName = "id")
    private SenseAttributes senseAttribute;

    @Column(name = "example")
    private String example;

    @Column(name = "type")
    private String type;

    public SenseAttributes getSenseAttribute() {
        return senseAttribute;
    }

    public void setSenseAttribute(SenseAttributes senseAttribute) {
        this.senseAttribute = senseAttribute;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
