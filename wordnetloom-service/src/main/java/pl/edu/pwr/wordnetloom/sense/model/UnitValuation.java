package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.dictionary.model.Valuation;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "unit_valuations")
@IdClass(UnitValuationPK.class)
public class UnitValuation implements Serializable, Cloneable{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotation_id")
    private EmotionalAnnotation annotation;

    @Id
    @ManyToOne
    @JoinColumn(name = "valuation")
    private Valuation valuation;

    public UnitValuation(){}

    public UnitValuation(EmotionalAnnotation annotation, Valuation valuation) {
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

class UnitValuationPK implements Serializable {
    protected EmotionalAnnotation annotation;
    protected Valuation valuation;

    public UnitValuationPK(){}

    public UnitValuationPK(EmotionalAnnotation annotation, Valuation valuation) {
        this.annotation = annotation;
        this.valuation = valuation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitValuationPK that = (UnitValuationPK) o;

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
