package pl.edu.pwr.wordnetloom.relationtest.model;

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
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

@Entity
@Table(name = "relation_test")
public class SynsetRelationTest implements Serializable {

    private static final long serialVersionUID = 2710746394598283537L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String test;

    @ManyToOne
    @JoinColumn(name = "synset_relation_type_id", referencedColumnName = "id", nullable = false)
    private SynsetRelationType relationType;

    @ManyToOne
    @JoinColumn(name = "synset_A_part_of_speech_id", referencedColumnName = "id")
    private PartOfSpeech synsetApartOfSpeech;

    @ManyToOne
    @JoinColumn(name = "synset_B_part_of_speech_id", referencedColumnName = "id")
    private PartOfSpeech synsetBpartOfSpeech;

    @Column(name = "position", nullable = false, columnDefinition = "int default = '0'")
    private Integer position;

    public SynsetRelationTest() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public PartOfSpeech getSynsetApartOfSpeech() {
        return synsetApartOfSpeech;
    }

    public void setSynsetApartOfSpeech(PartOfSpeech synsetApartOfSpeech) {
        this.synsetApartOfSpeech = synsetApartOfSpeech;
    }

    public PartOfSpeech getSynsetBpartOfSpeech() {
        return synsetBpartOfSpeech;
    }

    public void setSynsetBpartOfSpeech(PartOfSpeech synsetBpartOfSpeech) {
        this.synsetBpartOfSpeech = synsetBpartOfSpeech;
    }

    public SynsetRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SynsetRelationType relationType) {
        this.relationType = relationType;
    }

}
