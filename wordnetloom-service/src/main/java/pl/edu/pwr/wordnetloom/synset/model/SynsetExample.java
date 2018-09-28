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

    @Override
    public String getExample() {
        return example;
    }

    @Override
    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type){
        this.type = type;
    }

    public SynsetAttributes getSynsetAttributes() {
        return synsetAttributes;
    }

    public void setSynsetAttributes(SynsetAttributes ssynsetAttributes) {
        this.synsetAttributes = ssynsetAttributes;
    }

    @Override
    public Example copy() {
        Example example = new SynsetExample();
        example.setType(this.getType());
        example.setExample(this.getExample());
        return example;
    }
}
