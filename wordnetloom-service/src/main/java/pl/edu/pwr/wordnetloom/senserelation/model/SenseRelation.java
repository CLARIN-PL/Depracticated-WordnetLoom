package pl.edu.pwr.wordnetloom.senserelation.model;

import org.hibernate.envers.Audited;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "sense_relation")
public class SenseRelation extends GenericEntity {

    private static final long serialVersionUID = -9013001788054096196L;

    @ManyToOne
    @JoinColumn(name = "relation_type_id", referencedColumnName = "id", nullable = false)
    private RelationType relationType;

    @ManyToOne
    @JoinColumn(name = "parent_sense_id", referencedColumnName = "id", nullable = false)
    private Sense parent;

    @ManyToOne
    @JoinColumn(name = "child_sense_id", referencedColumnName = "id", nullable = false)
    private Sense child;

    public SenseRelation() {
        super();
    }

    public SenseRelation(RelationType relationType, Sense parent, Sense child) {
        this.relationType = relationType;
        this.parent = parent;
        this.child = child;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public Sense getParent() {
        return parent;
    }

    public void setParent(Sense parent) {
        this.parent = parent;
    }

    public Sense getChild() {
        return child;
    }

    public void setChild(Sense child) {
        this.child = child;
    }
}
