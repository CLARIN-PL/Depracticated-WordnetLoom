package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.dictionary.model.AspectDictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.RegisterDictionary;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
    private List<SenseExample> examples;

    @ManyToOne
    @JoinColumn(name = "aspect_id", referencedColumnName = "id")
    private AspectDictionary aspectDictionary;

    @Lob
    private String definition;

    @Lob
    private String comment;

    @ManyToOne
    @JoinColumn(name = "register_id", referencedColumnName = "id")
    private RegisterDictionary register;

    private String link;

    @Column(name = "error_comment")
    private String errorComment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public SenseAttributes() {
        super();
    }

    public SenseAttributes(String definition, String comment, RegisterDictionary register, String link) {
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

    public RegisterDictionary getRegister() {
        return register;
    }

    public void setRegister(RegisterDictionary register) {
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

    public List<SenseExample> getExamples() {
        return examples;
    }

    public void setExamples(List<SenseExample> examples) {
        this.examples = examples;
    }

    public AspectDictionary getAspectDictionary() {
        return aspectDictionary;
    }

    public void setAspectDictionary(AspectDictionary aspectDictionary) {
        this.aspectDictionary = aspectDictionary;
    }
}
