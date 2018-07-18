package pl.edu.pwr.wordnetloom.sense.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "unit_valuations")
public class UnitValuation implements Serializable, Cloneable{

    @Id
    @ManyToOne
    @JoinColumn(name = "annotation_id")
    private EmotionalAnnotation annotation;

    @Column(name = "valuation")
    private Long valuation;

    public EmotionalAnnotation getAnnotation() {
        return annotation;
    }

    public Long getValuation()
    {
        return valuation;
    }

    public void setEmotionalAnnotation(EmotionalAnnotation annotation) {
        this.annotation = annotation;
    }

    public void setValuation(Long valuation){
        this.valuation = valuation;
    }
}
