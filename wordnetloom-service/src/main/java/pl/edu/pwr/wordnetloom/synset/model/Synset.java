package pl.edu.pwr.wordnetloom.synset.model;

import org.hibernate.envers.Audited;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Audited
@Entity
@Table(name = "synset")
public class Synset extends GenericEntity {

    private static final long serialVersionUID = 800201223603990725L;

    @Column(name = "split")
    private Integer split = 1;

    @OneToMany(mappedBy = "synset", fetch = FetchType.LAZY)
    @OrderBy("synsetPosition")
    private List<Sense> senses = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexicon_id", referencedColumnName = "id", nullable = false)
    private Lexicon lexicon;

    @Basic
    @Column(name = "abstract")
    private Boolean isAbstract = false;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)
    private List<SynsetRelation> incomingRelations = new ArrayList<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<SynsetRelation> outgoingRelations = new ArrayList<>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    public List<Sense> getSenses() {
        return senses;
    }

    public void setSenses(List<Sense> senses) {
        this.senses = senses;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    public List<SynsetRelation> getIncomingRelations(){return incomingRelations;}

    public void setIncomingRelations(List<SynsetRelation> incomingRelations){
        this.incomingRelations = incomingRelations;
    }

    public List<SynsetRelation> getOutgoingRelations() {return outgoingRelations;}

    public void setOutgoingRelations(List<SynsetRelation> outgoingRelations){
        this.outgoingRelations = outgoingRelations;
    }

    public Boolean getAbstract() {
        return isAbstract;
    }

    public void setAbstract(Boolean anAbstract) {
        isAbstract = anAbstract;
    }
}
