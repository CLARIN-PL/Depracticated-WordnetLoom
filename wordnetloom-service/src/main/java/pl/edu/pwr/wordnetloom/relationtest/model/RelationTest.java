package pl.edu.pwr.wordnetloom.relationtest.model;

import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "relation_tests")
public class RelationTest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationTest)) return false;

        RelationTest that = (RelationTest) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (test != null ? !test.equals(that.test) : that.test != null) return false;
        if (senseApartOfSpeech != null ? !senseApartOfSpeech.equals(that.senseApartOfSpeech) : that.senseApartOfSpeech != null)
            return false;
        if (senseBpartOfSpeech != null ? !senseBpartOfSpeech.equals(that.senseBpartOfSpeech) : that.senseBpartOfSpeech != null)
            return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        return relationType != null ? relationType.equals(that.relationType) : that.relationType == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (test != null ? test.hashCode() : 0);
        result = 31 * result + (senseApartOfSpeech != null ? senseApartOfSpeech.hashCode() : 0);
        result = 31 * result + (senseBpartOfSpeech != null ? senseBpartOfSpeech.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (relationType != null ? relationType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return test;
    }
}
