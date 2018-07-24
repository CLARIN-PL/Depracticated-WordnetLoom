package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.dictionary.model.Emotion;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "unit_emotions")
@IdClass(UnitEmotionPK.class)
public class UnitEmotion implements Serializable, Cloneable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotation_id")
    private EmotionalAnnotation annotation;

//    @Column(name = "emotion")
//    private Long emotion;
    @Id
    @ManyToOne
    @JoinColumn(name = "emotion")
    private Emotion emotion;

    public UnitEmotion(){}

    public UnitEmotion(EmotionalAnnotation annotation, Emotion emotion) {
        setAnnotation(annotation);
        setEmotion(emotion);
    }

    public EmotionalAnnotation getAnnotation() {
        return annotation;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setAnnotation(EmotionalAnnotation annotation) {
        this.annotation = annotation;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}

class UnitEmotionPK implements Serializable {
    protected EmotionalAnnotation annotation;
    protected Emotion emotion;

    public UnitEmotionPK(){}
    public UnitEmotionPK(EmotionalAnnotation annotation, Emotion emotion) {
        this.annotation = annotation;
        this.emotion = emotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitEmotionPK that = (UnitEmotionPK) o;

        if (!annotation.equals(that.annotation)) return false;
        return emotion.equals(that.emotion);
    }

    @Override
    public int hashCode() {
        int result = annotation.hashCode();
        result = 31 * result + emotion.hashCode();
        return result;
    }
}
