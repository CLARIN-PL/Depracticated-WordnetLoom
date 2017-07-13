package pl.edu.pwr.wordnetloom.relationtest.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Entity
public class SenseRelationTest extends RelationTest {

    @ManyToOne
    @JoinColumn(name = "sense_relation_type_id", referencedColumnName = "id", nullable = false)
    private SenseRelationType relationType;

    public SenseRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SenseRelationType relationType) {
        this.relationType = relationType;
    }

    public SenseRelationTest() {
        super();
    }
}
