package pl.edu.pwr.wordnetloom.sense.model;

import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "unit_emotions")
public class UnitEmotion implements Serializable, Cloneable {

    @Id
    @ManyToOne
    @JoinColumn(name = "annotation_id")
    private EmotionalAnnotation annotation;

    @Column(name = "emotion")
    private Long emotion;

    public EmotionalAnnotation getAnnotation() {
        return annotation;
    }

    public Long getEmotion() {
        return emotion;
    }

    public void setAnnotation(EmotionalAnnotation annotation) {
        this.annotation = annotation;
    }

    public void setEmotion(Long emotion) {
        this.emotion = emotion;
    }
}
