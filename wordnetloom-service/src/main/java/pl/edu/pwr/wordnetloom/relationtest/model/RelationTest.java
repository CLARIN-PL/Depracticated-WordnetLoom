package pl.edu.pwr.wordnetloom.relationtest.model;

import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "relation_tests")
public class RelationTest implements Serializable {

    private static final long serialVersionUID = 2710746394598283537L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
