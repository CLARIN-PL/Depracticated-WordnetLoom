package pl.edu.pwr.wordnetloom.synset.model;

import org.hibernate.envers.Audited;
import pl.edu.pwr.wordnetloom.common.model.Example;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "synset_examples")
public class SynsetExample extends GenericEntity implements Example {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "synset_attributes_id")
    private SynsetAttributes synsetAttributes;

    @Column(name = "example")
    private String example;

    @Column(name = "type")
    private String type;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public SynsetAttributes getSynsetAttributes() {
        return synsetAttributes;
    }

    public void setSynsetAttributes(SynsetAttributes ssynsetAttributes) {
        this.synsetAttributes = ssynsetAttributes;
    }
}
