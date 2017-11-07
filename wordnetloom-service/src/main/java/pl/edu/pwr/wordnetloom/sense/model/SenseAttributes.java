package pl.edu.pwr.wordnetloom.sense.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sense_attributes")
public class SenseAttributes extends GenericEntity {

    private static final long serialVersionUID = -6738496417082820449L;

    @Lob
    private String definition;

    @Lob
    private String comment;

    private String register;

    private String link;

    @ElementCollection
    @CollectionTable(name = "sense_examples", joinColumns = @JoinColumn(name = "sense_attributes_id"))
    @Column(name = "example")
    private List<String> examples;

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

    public SenseAttributes(String definition, String comment, String register, String link, List<String> examples) {
        this.definition = definition;
        this.comment = comment;
        this.register = register;
        this.link = link;
        this.examples = examples;
    }

    public SenseAttributes(SenseAttributes sa) {
        definition = sa.definition;
        comment = sa.comment;
        register = sa.register;
        link = sa.link;
        owner = sa.owner;
        examples = new ArrayList<>();
        sa.getExamples().forEach(examples::add);
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

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
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

}
