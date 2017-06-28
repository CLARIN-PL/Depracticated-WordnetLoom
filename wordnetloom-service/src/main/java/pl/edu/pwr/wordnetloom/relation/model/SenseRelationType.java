package pl.edu.pwr.wordnetloom.relation.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SenseRelationType extends RelationType {
    
    @ManyToOne
    @JoinColumn(name = "parent_relation_type_id", nullable = true)
    private SenseRelationType parent;

    @ManyToOne
    @JoinColumn(name = "reverse_relation_type_id", nullable = true)
    private SenseRelationType reverse;

    public SenseRelationType getParent() {
        return parent;
    }

    public void setParent(SenseRelationType parent) {
        this.parent = parent;
    }

    public SenseRelationType getReverse() {
        return reverse;
    }

    public void setReverse(SenseRelationType reverse) {
        this.reverse = reverse;
    }

}
