package pl.edu.pwr.wordnetloom.synset.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "synset_attributes")
public class SynsetAttributes extends GenericEntity {

    private static final long serialVersionUID = -3305787239727633359L;

    private String definition;

    private String comment;

    @ElementCollection
    @CollectionTable(name = "synset_examples", joinColumns = @JoinColumn(name = "synset_attributes_id"))
    @Column(name = "example")
    private List<String> examples;

    @Basic
    @Column(name = "abstract")
    private Boolean isAbstract = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "princeton_id")
    private String princetonId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "synset_id")
    @MapsId
    private Synset synset;

    public SynsetAttributes() {
        super();
    }

    public SynsetAttributes(String definition, String comment, boolean isAbstract, User owner, String princetonId) {
        this.definition = definition;
        this.comment = comment;
        this.isAbstract = isAbstract;
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

    public Boolean getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(Boolean isAbstract) {
        this.isAbstract = isAbstract;
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

}
