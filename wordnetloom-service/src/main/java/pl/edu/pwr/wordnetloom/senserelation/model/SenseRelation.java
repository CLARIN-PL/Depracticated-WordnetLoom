package pl.edu.pwr.wordnetloom.senserelation.model;

import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Entity
@Table(name = "sense_relation")
public class SenseRelation implements Serializable {

    private static final long serialVersionUID = -9013001788054096196L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "relation_type_id", referencedColumnName = "id", nullable = false)
    private SenseRelationType relationType;

    @ManyToOne
    @JoinColumn(name = "parent_sense_id", referencedColumnName = "id", nullable = false)
    private Sense parent;

    @ManyToOne
    @JoinColumn(name = "child_sense_id", referencedColumnName = "id", nullable = false)
    private Sense child;

    public SenseRelation() {
        super();
    }

    public SenseRelation(SenseRelationType relationType, Sense parent, Sense child) {
        this.relationType = relationType;
        this.parent = parent;
        this.child = child;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SenseRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SenseRelationType relationType) {
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
