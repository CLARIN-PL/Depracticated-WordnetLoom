package pl.edu.pwr.wordnetloom.synsetrelation.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.persistence.*;

@Entity
@Table(name = "synset_relation")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SynsetRelation extends GenericEntity {

    private static final long serialVersionUID = 1624355230288462084L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "synset_relation_type_id", referencedColumnName = "id", nullable = false)
    private RelationType relationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_synset_id", referencedColumnName = "id", nullable = false)
    private Synset parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_synset_id", referencedColumnName = "id", nullable = false)
    private Synset child;

    public SynsetRelation() {
    }

    public SynsetRelation(Long id, Long relationTypeId, Long parentId, Long childId, NodeDirection direction){
        this.id = id;
        relationType = new RelationType();
        relationType.setId(relationTypeId);
        relationType.setNodePosition(direction);
        parent = new Synset();
        parent.setId(parentId);
        child = new Synset();
        child.setId(childId);

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
