package pl.edu.pwr.wordnetloom.relationtest.model;

import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Entity
@Table(name = "sense_relation_test")
public class SenseRelationTest implements Serializable {

    private static final long serialVersionUID = 2710746394598283537L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String test;

    @ManyToOne
    @JoinColumn(name = "sense_relation_type_id", referencedColumnName = "id", nullable = false)
    private SenseRelationType relationType;

    @ManyToOne
    @JoinColumn(name = "sense_A_part_of_speech_id", referencedColumnName = "id", nullable = false)
    private PartOfSpeech senseApartOfSpeech;

    @ManyToOne
    @JoinColumn(name = "sense_B_part_of_speech_id", referencedColumnName = "id", nullable = false)
    private PartOfSpeech senseBpartOfSpeech;

    @Column(name = "position", nullable = false, columnDefinition = "int default = '0'")
    private Integer position;

    public SenseRelationTest() {
        super();
    }

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

    public SenseRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SenseRelationType relationType) {
        this.relationType = relationType;
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

}
