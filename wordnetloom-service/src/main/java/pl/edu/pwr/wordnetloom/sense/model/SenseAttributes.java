package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "sense_attributes")
public class SenseAttributes {

    @Lob
    private String definition;

    @Lob
    private String comment;

    private String register;

    private String link;

    @Column(name = "error_comment")
    private String errorComment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sense_id")
    @MapsId
    private Sense sense;

    public SenseAttributes() {
        super();
    }

    public SenseAttributes(String definition, String comment, String register, String link) {
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

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
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
}
