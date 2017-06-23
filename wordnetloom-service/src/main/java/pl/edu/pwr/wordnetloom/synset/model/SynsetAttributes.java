package pl.edu.pwr.wordnetloom.synset.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "synset_attributes")
@NamedQueries({
    @NamedQuery(name = "SynsetAttribute.getForName",
            query = "SELECT sa FROM SynsetAttribute sa, AttributeType at WHERE at.typeName = :typeName AND at.tableName = 'synset' AND sa.type = at"),
    @NamedQuery(name = "SynsetAttribute.getForSynset",
            query = "SELECT sa FROM SynsetAttribute sa WHERE sa.synset = :synset"),
    @NamedQuery(name = "SynsetAttribute.getSynsetAttributeForName",
            query = "SELECT sa FROM SynsetAttribute sa WHERE sa.type.tableName='synset' AND sa.type.typeName.text = :typeName AND sa.synset.id = :synset "),
    @NamedQuery(name = "SynsetAttribute.deleteForSynset",
            query = "DELETE FROM SynsetAttribute sa WHERE sa.synset.id = :synset"),})
public class SynsetAttributes implements Serializable {

    private static final long serialVersionUID = -3305787239727633359L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String definition;

    private String comment;

    @Basic
    private Boolean isAbstract;

    private String owner;

    private String princetonId;

    public SynsetAttributes() {
        super();
    }

    public SynsetAttributes(String definition, String comment, boolean isAbstract, String owner, String princetonId) {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPrincetonId() {
        return princetonId;
    }

    public void setPrincetonId(String princetonId) {
        this.princetonId = princetonId;
    }

}
