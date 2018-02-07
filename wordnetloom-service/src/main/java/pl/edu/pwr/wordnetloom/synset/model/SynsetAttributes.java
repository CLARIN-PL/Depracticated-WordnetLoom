package pl.edu.pwr.wordnetloom.synset.model;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "synset_attributes")
public class SynsetAttributes {

    private static final long serialVersionUID = -3305787239727633359L;

    @Id
    protected Long id;

    private String definition;

    private String comment;

    @Column(name = "error_comment")
    private String errorComment;

    @ElementCollection
    @CollectionTable(name = "synset_examples", joinColumns = @JoinColumn(name = "synset_attributes_id"))
    @Column(name = "example")
    private List<String> examples;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "princeton_id")
    private String princetonId;

    @Column(name = "ili_id")
    private String iliId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "synset_id")
    @MapsId
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Synset synset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SynsetAttributes() {
        super();
    }

    public SynsetAttributes(String definition, String comment, User owner, String princetonId) {
        this.definition = definition;
        this.comment = comment;
        this.owner = owner;
        this.princetonId = princetonId;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public String getPrincetonId() {
        return princetonId;
    }

    public void setPrincetonId(String princetonId) {
        this.princetonId = princetonId;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public String getErrorComment() {
        return errorComment;
    }

    public void setErrorComment(String errorComment) {
        this.errorComment = errorComment;
    }

    public String getIliId() {
        return iliId;
    }

    public void setIliId(String iliId) {
        this.iliId = iliId;
    }
}
