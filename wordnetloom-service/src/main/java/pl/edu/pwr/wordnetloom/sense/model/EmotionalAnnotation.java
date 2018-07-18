package pl.edu.pwr.wordnetloom.sense.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JoinColumnOrFormula;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.user.model.User;
import sun.security.acl.PermissionImpl;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "emotional_annotations")
public class EmotionalAnnotation extends GenericEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sense_id")
    private Sense sense;

    @Column(name = "super_anotation")
    private boolean superAnnotation;

    @Column(name = "has_emotional_characteristic")
    private boolean emotionalCharacteristic;


    @OneToMany(mappedBy = "annotation", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UnitEmotion> emotions;

    @OneToMany(mappedBy = "annotation", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UnitValuation> valuations;

    private String markedness;

    private String example1;

    private String example2;

    @ManyToOne
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

    public List<UnitEmotion> getEmotions() {
        return emotions;
    }

    public List<UnitValuation> getValuations() {
        return valuations;
    }

    public String getMarkedness() {
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

    public void setSuperAnnotation(boolean superAnnotation){
        this.superAnnotation = superAnnotation;
    }

    public void setEmotionalCharacteristic(boolean emotionalCharacteristic) {
        this.emotionalCharacteristic = emotionalCharacteristic;
    }

    public void setEmotions(List<UnitEmotion> emotions){
        this.emotions = emotions;
    }

    public void setValuations(List<UnitValuation> valuations){
        this.valuations = valuations;
    }

    public void setMarkedness(String markedness){
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

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "sense_id")
//    private Sense sense;
//
//    @Column(name = "super_anotation")
//    private boolean superAnotation;
//
//    @Column(name = "has_emotional_characteristic")
//    private boolean hasEmotionalCharacteristic;
//
//    private String emotions;
//
//    private String valuations;
//
//    private String markedness;
//
//    private String example1;
//
//    private String example2;
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Sense getSense() {
//        return sense;
//    }
//
//    public void setSense(Sense sense) {
//        this.sense = sense;
//    }
//
//    public boolean isSuperAnotation() {
//        return superAnotation;
//    }
//
//    public void setSuperAnotation(boolean superAnotation) {
//        this.superAnotation = superAnotation;
//    }
//
//    public boolean isHasEmotionalCharacteristic() {
//        return hasEmotionalCharacteristic;
//    }
//
//    public void setHasEmotionalCharacteristic(boolean hasEmotionalCharacteristic) {
//        this.hasEmotionalCharacteristic = hasEmotionalCharacteristic;
//    }
//
//    public String getEmotions() {
//        return emotions;
//    }
//
//    public void setEmotions(String emotions) {
//        this.emotions = emotions;
//    }
//
//    public String getValuations() {
//        return valuations;
//    }
//
//    public void setValuations(String valuations) {
//        this.valuations = valuations;
//    }
//
//    public String getMarkedness() {
//        return markedness;
//    }
//
//    public void setMarkedness(String markedness) {
//        this.markedness = markedness;
//    }
//
//    public String getExample1() {
//        return example1;
//    }
//
//    public void setExample1(String example1) {
//        this.example1 = example1;
//    }
//
//    public String getExample2() {
//        return example2;
//    }
//
//    public void setExample2(String example2) {
//        this.example2 = example2;
//    }
}
