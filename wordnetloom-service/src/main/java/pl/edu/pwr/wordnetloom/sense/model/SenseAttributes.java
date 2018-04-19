package pl.edu.pwr.wordnetloom.sense.model;

import org.hibernate.envers.Audited;
import pl.edu.pwr.wordnetloom.dictionary.model.Aspect;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Audited
@Entity
@Table(name = "sense_attributes")
public class SenseAttributes implements Serializable, Cloneable {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sense_id")
    @MapsId
    private Sense sense;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "senseAttributes", orphanRemoval = true)
    private Set<SenseExample> examples;

    @ManyToOne
    @JoinColumn(name = "aspect_id", referencedColumnName = "id")
    private Aspect aspect;

    @Lob
    private String definition;

    @Lob
    private String comment;

    @ManyToOne
    @JoinColumn(name = "register_id", referencedColumnName = "id")
    private Register register;

    private String link;

    @Column(name = "error_comment")
    private String errorComment;

    @Audited
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public SenseAttributes() {
        super();
    }

    public SenseAttributes(String definition, String comment, Register register, String link) {
        this.definition = definition;
        this.comment = comment;
        this.register = register;
        this.link = link;
    }

    public SenseAttributes(SenseAttributes sa) {
        definition = sa.definition;
        comment = sa.comment;
        register = sa.register;
        link = sa.link;
        owner = sa.owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Sense getSense() {
        return sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getErrorComment() {
        return errorComment;
    }

    public void setErrorComment(String errorComment) {
        this.errorComment = errorComment;
    }

    public Set<SenseExample> getExamples() {
        return examples;
    }

    public void setExamples(Set<SenseExample> examples) {
        this.examples = examples;
    }

    public void addExample(SenseExample e){
        if( examples == null){
            examples = new HashSet<>();
        }
        examples.add(e);
    }

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
    }
}
