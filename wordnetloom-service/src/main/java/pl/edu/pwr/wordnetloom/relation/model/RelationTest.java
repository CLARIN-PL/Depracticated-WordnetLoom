package pl.edu.pwr.wordnetloom.relation.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Entity
@Table(name = "relation_test")
@NamedQueries({
    @NamedQuery(name = "RelationTest.removeRelationTestsFor",
            query = "DELETE FROM RelationTest r WHERE r.relationType = :relationType"),

    @NamedQuery(name = "RelationTest.removeAllRelationTests",
            query = "DELETE FROM RelationTest r"),

    @NamedQuery(name = "RelationTest.getRelationTestsFor",
            query = "SELECT r FROM RelationTest r WHERE r.relationType = :relationType"),

    @NamedQuery(name = "RelationTest.getAllRelationTests",
            query = "SELECT r FROM RelationTest r"),})
public class RelationTest implements Serializable {

    private static final long serialVersionUID = 2710746394598283537L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String text;

    @ManyToOne
    @JoinColumn(name = "relation_type_id", referencedColumnName = "id", nullable = false)
    private RelationType relationType;

    @ManyToOne
    @JoinColumn(name = "part_of_speech_id", referencedColumnName = "id", nullable = false)
    private PartOfSpeech partOfSpeech;

    @Column(name = "position", nullable = false, columnDefinition = "int default = '0'")
    private Integer position;

    public RelationTest() {
        super();
    }

    public RelationTest(Long id, String text, RelationType relationType, PartOfSpeech pos, Integer position) {
        this.text = text;
        this.relationType = relationType;
        this.partOfSpeech = pos;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
