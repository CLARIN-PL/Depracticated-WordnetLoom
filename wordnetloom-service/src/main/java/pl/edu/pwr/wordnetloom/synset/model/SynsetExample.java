package pl.edu.pwr.wordnetloom.synset.model;

import org.hibernate.envers.Audited;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "synset_examples")
public class SynsetExample extends GenericEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "synset_attributes_id")
    private SynsetAttributes synsetAttributes;

    @Column(name = "example")
    private String example;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public SynsetAttributes getSynsetAttributes() {
        return synsetAttributes;
    }

    public void setSynsetAttributes(SynsetAttributes ssynsetAttributes) {
        this.synsetAttributes = ssynsetAttributes;
    }
}
