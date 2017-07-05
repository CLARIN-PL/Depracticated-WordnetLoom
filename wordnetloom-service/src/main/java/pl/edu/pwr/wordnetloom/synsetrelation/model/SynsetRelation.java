package pl.edu.pwr.wordnetloom.synsetrelation.model;

import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Entity
@Table(name = "synset_relation")
public class SynsetRelation implements Serializable {

    private static final long serialVersionUID = 1624355230288462084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "synset_relation_type_id", referencedColumnName = "id", nullable = false)
    private SynsetRelationType relationType;

    @OneToOne
    @JoinColumn(name = "parent_synset_id", referencedColumnName = "id", nullable = false)
    private Synset parent;

    @OneToOne
    @JoinColumn(name = "child_synset_id", referencedColumnName = "id", nullable = false)
    private Synset child;

    public SynsetRelation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Synset getParent() {
        return parent;
    }

    public void setParent(Synset parent) {
        this.parent = parent;
    }

    public Synset getChild() {
        return child;
    }

    public void setChild(Synset child) {
        this.child = child;
    }

    public SynsetRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SynsetRelationType relationType) {
        this.relationType = relationType;
    }

}
