package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.dictionary.model.Valuation;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sense_valuations")
@IdClass(SenseValuationPK.class)
public class SenseValuation implements Serializable, Cloneable{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotation_id")
    private EmotionalAnnotation annotation;

    @Id
    @ManyToOne
    @JoinColumn(name = "valuation")
    private Valuation valuation;

    public SenseValuation(){}

    public SenseValuation(EmotionalAnnotation annotation, Valuation valuation) {
        setEmotionalAnnotation(annotation);
        setValuation(valuation);
    }

    public EmotionalAnnotation getAnnotation() {
        return annotation;
    }

    public Valuation getValuation(){
        return valuation;
    }

    public void setEmotionalAnnotation(EmotionalAnnotation annotation) {
        this.annotation = annotation;
    }

    public void setValuation(Valuation valuation) {
        this.valuation = valuation;
    }
}

class SenseValuationPK implements Serializable {
    protected EmotionalAnnotation annotation;
    protected Valuation valuation;

    public SenseValuationPK(){}

    public SenseValuationPK(EmotionalAnnotation annotation, Valuation valuation) {
        this.annotation = annotation;
        this.valuation = valuation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SenseValuationPK that = (SenseValuationPK) o;

        if (!annotation.equals(that.annotation)) return false;
        return valuation.equals(that.valuation);
    }

    @Override
    public int hashCode() {
        int result = annotation.hashCode();
        result = 31 * result + valuation.hashCode();
        return result;
    }
}
