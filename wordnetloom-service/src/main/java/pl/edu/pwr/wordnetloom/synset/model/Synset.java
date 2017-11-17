package pl.edu.pwr.wordnetloom.synset.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "synset")
public class Synset extends GenericEntity {

    private static final long serialVersionUID = 800201223603990725L;

    @Column(name = "split")
    private Integer split = 0;

    @OneToMany(mappedBy = "synset", fetch = FetchType.LAZY)
    @OrderBy("synsetPosition")
    private List<Sense> senses = new ArrayList<>();

    @NotNull
    @Valid
    @OneToOne(mappedBy = "synset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SynsetAttributes synsetAttributes;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexicon_id", referencedColumnName = "id", nullable = false)
    private Lexicon lexicon;

    private Integer status = 0;

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)
//    private final List<SynsetRelation> incomingRelations = new ArrayList<>();
    private List<SynsetRelation> incomingRelations = new ArrayList<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
//    private final List<SynsetRelation> outgoingRelations = new ArrayList<>();
    private List<SynsetRelation> outgoingRelations = new ArrayList<>();

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

    public SynsetAttributes getSynsetAttributes() {
        return synsetAttributes;
    }

    public void setSynsetAttributes(SynsetAttributes synsetAttributes) {
        this.synsetAttributes = synsetAttributes;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    public List<SynsetRelation> getIncomingRelations() {
        return incomingRelations;
    }

    public List<SynsetRelation> getOutgoingRelations() {
        return outgoingRelations;
    }

    public void setIncomingRelations(List<SynsetRelation> relations){
        incomingRelations = relations;
    }

    public void setOutgoingRelations(List<SynsetRelation> relations){
        outgoingRelations = relations;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
