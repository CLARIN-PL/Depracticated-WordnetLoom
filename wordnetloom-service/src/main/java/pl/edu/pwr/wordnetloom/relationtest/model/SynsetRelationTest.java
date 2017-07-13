package pl.edu.pwr.wordnetloom.relationtest.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;

@Entity
public class SynsetRelationTest extends RelationTest {

    @ManyToOne
    @JoinColumn(name = "synset_relation_type_id", referencedColumnName = "id", nullable = false)
    private SynsetRelationType relationType;

    public SynsetRelationTest() {
        super();
    }

    public SynsetRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SynsetRelationType relationType) {
        this.relationType = relationType;
    }

}
