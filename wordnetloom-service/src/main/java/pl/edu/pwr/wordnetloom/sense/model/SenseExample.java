package pl.edu.pwr.wordnetloom.sense.model;

import org.hibernate.envers.Audited;
import pl.edu.pwr.wordnetloom.common.model.Example;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.synset.model.SynsetExample;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "sense_examples")
public class SenseExample extends GenericEntity implements Example{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sense_attribute_id")
    private SenseAttributes senseAttributes;

    @Column(name = "example")
    private String example;

    @Column(name = "type")
    private String type;

    public SenseAttributes getSenseAttributes() {
        return senseAttributes;
    }

    public void setSenseAttributes(SenseAttributes senseAttributes) {
        this.senseAttributes = senseAttributes;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Example copy() {
        Example newExample = new SenseExample();
        newExample.setExample(this.getExample());
        newExample.setType(this.getType());
        return newExample;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof SenseExample)) return false;
//        if (!super.equals(o)) return false;
//
//        SenseExample example1 = (SenseExample) o;
//
//        if (senseAttributes != null ? !senseAttributes.equals(example1.senseAttributes) : example1.senseAttributes != null)
//            return false;
//        if (example != null ? !example.equals(example1.example) : example1.example != null) return false;
//        return type != null ? type.equals(example1.type) : example1.type == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (senseAttributes != null ? senseAttributes.hashCode() : 0);
//        result = 31 * result + (example != null ? example.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        return result;
//    }
}
