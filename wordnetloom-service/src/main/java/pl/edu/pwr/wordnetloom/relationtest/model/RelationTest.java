package pl.edu.pwr.wordnetloom.relationtest.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "relation_tests")
public class RelationTest extends GenericEntity {

    private static final long serialVersionUID = 2710746394598283537L;

    @Lob
    private String test;

    @ManyToOne
    @JoinColumn(name = "element_A_part_of_speech_id", referencedColumnName = "id")
    private PartOfSpeech senseApartOfSpeech;

    @ManyToOne
    @JoinColumn(name = "element_B_part_of_speech_id", referencedColumnName = "id")
    private PartOfSpeech senseBpartOfSpeech;

    @Column(name = "position", nullable = false, columnDefinition = "int default 0")
    private Integer position;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "relation_type_id", nullable = false)
    private RelationType relationType;

    public RelationTest() {
    }

    public RelationTest(RelationType relationType) {
        this.relationType = relationType;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public PartOfSpeech getSenseApartOfSpeech() {
        return senseApartOfSpeech;
    }

    public void setSenseApartOfSpeech(PartOfSpeech senseApartOfSpeech) {
        this.senseApartOfSpeech = senseApartOfSpeech;
    }

    public PartOfSpeech getSenseBpartOfSpeech() {
        return senseBpartOfSpeech;
    }

    public void setSenseBpartOfSpeech(PartOfSpeech senseBpartOfSpeech) {
        this.senseBpartOfSpeech = senseBpartOfSpeech;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    @Override
    public String toString() {
        return  test;
    }
}
