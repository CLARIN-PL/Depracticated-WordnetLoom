package pl.edu.pwr.wordnetloom.synsetrelation.model;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "synset_relation")
public class SynsetRelation implements Serializable {

    private static final long serialVersionUID = 1624355230288462084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "synset_relation_type_id", referencedColumnName = "id", nullable = false)
    private RelationType relationType;

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

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

}
