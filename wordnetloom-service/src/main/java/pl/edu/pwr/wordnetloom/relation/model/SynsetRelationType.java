package pl.edu.pwr.wordnetloom.relation.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SynsetRelationType extends RelationType {
    
    @Basic
    @Column(name = "multilingual", nullable = false, columnDefinition = "bit")
    private Boolean multilingual = false;
    
    @ManyToOne
    @JoinColumn(name = "parent_relation_type_id", nullable = true)
    private SynsetRelationType parent;

    @ManyToOne
    @JoinColumn(name = "reverse_relation_type_id", nullable = true)
    private SynsetRelationType reverse;

    public Boolean getMultilingual() {
        return multilingual;
    }

    public void setMultilingual(Boolean multilingual) {
        this.multilingual = multilingual;
    }

    public SynsetRelationType getParent() {
        return parent;
    }

    public void setParent(SynsetRelationType parent) {
        this.parent = parent;
    }

    public SynsetRelationType getReverse() {
        return reverse;
    }

    public void setReverse(SynsetRelationType reverse) {
        this.reverse = reverse;
    }
    
    
}
