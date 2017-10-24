package pl.edu.pwr.wordnetloom.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "synset_attribute")
@NamedQueries({
        @NamedQuery(name = "SynsetAttribute.getForName",
                query = "SELECT sa FROM SynsetAttribute sa, AttributeType at WHERE at.typeName = :typeName AND at.tableName = 'synset' AND sa.type = at"),
        @NamedQuery(name = "SynsetAttribute.getForSynset",
                query = "SELECT sa FROM SynsetAttribute sa WHERE sa.synset = :synset"),
        @NamedQuery(name = "SynsetAttribute.getSynsetAttributeForName",
                query = "SELECT sa FROM SynsetAttribute sa WHERE sa.type.tableName='synset' AND sa.type.typeName.text = :typeName AND sa.synset.id = :synset "),
        @NamedQuery(name = "SynsetAttribute.deleteForSynset",
                query = "DELETE FROM SynsetAttribute sa WHERE sa.synset.id = :synset"),})
public class SynsetAttribute implements Serializable {

    private static final long serialVersionUID = -3305787239727633359L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type", referencedColumnName = "id", nullable = false)
    private AttributeType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "value", referencedColumnName = "id", nullable = false)
    private Text value;

    @ManyToOne
    @JoinColumn(name = "synset", referencedColumnName = "id", nullable = false)
    private Synset synset;

    public SynsetAttribute() {
    }

    public SynsetAttribute(SynsetAttribute sa, Synset synset) {
        type = sa.type;
        value = new Text(sa.value.getText());
        this.synset = synset;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Text getValue() {
        return value;
    }

    public void setValue(Text value) {
        this.value = value;
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof SynsetAttribute))
            return false;
        SynsetAttribute e = (SynsetAttribute) o;

        if (id == null)
            return false;
        return id.equals(e.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = (id.hashCode());
        if (hashCode == 0)
            return super.hashCode();
        return hashCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
