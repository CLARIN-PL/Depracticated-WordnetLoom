package pl.edu.pwr.wordnetloom.synset.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.user.model.User;

@Entity
@Table(name = "synset_attributes")
public class SynsetAttributes implements Serializable {

    private static final long serialVersionUID = -3305787239727633359L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "princenton_id")
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
