package pl.edu.pwr.wordnetloom.sense.model;

import org.hibernate.annotations.*;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.dictionary.model.Markedness;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "emotional_annotations")
public class EmotionalAnnotation extends GenericEntity {

    public EmotionalAnnotation() {
        emotions = new LinkedHashSet<>();
        valuations = new LinkedHashSet<>();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sense_id")
    private Sense sense;

    @Column(name = "super_anotation")
    private boolean superAnnotation;

    @Column(name = "has_emotional_characteristic")
    private boolean emotionalCharacteristic;


    @OneToMany(mappedBy = "annotation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<SenseEmotion> emotions;

    @OneToMany(mappedBy = "annotation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<SenseValuation> valuations;

    @ManyToOne()
    @JoinColumn(name = "markedness_id")
    private Markedness markedness;

    private String example1;

    private String example2;

    @ManyToOne()
    @JoinColumn(name = "owner")
    private User owner;

    public Sense getSense() {
        return sense;
    }

    public boolean isSuperAnnotation() {
        return superAnnotation;
    }

    public boolean hasEmotionalCharacteristic() {
        return emotionalCharacteristic;
    }

    public Set<SenseEmotion> getEmotions() {
        return emotions;
    }

    public Set<SenseValuation> getValuations() {
        return valuations;
    }

    public Markedness getMarkedness() {
        return markedness;
    }

    public String getExample1() {
        return example1;
    }

    public String getExample2() {
        return example2;
    }

    public User getOwner() {
        return owner;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    public void setSuperAnnotation(boolean superAnnotation){
        this.superAnnotation = superAnnotation;
    }

    public void setEmotionalCharacteristic(boolean emotionalCharacteristic) {
        this.emotionalCharacteristic = emotionalCharacteristic;
    }

    public void setEmotions(Set<SenseEmotion> emotions){
        this.emotions = emotions;
    }

    public void setValuations(Set<SenseValuation> valuations){
        this.valuations = valuations;
    }

    public void setMarkedness(Markedness markedness){
        this.markedness = markedness;
    }

    public void setExample1(String example){
        this.example1 = example;
    }

    public void setExample2(String example) {
        this.example2 = example;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }
}
