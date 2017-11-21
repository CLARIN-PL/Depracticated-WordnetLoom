package pl.edu.pwr.wordnetloom.common.dto;

import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DataEntry implements Serializable {

    private static final long serialVersionUID = 5708346930324155121L;

    private Synset synset;
    private Set<SynsetRelation> relsFrom;
    private Set<SynsetRelation> relsTo;
    private String label;
    private Long posID;
    private String lexicon;

    public DataEntry() {
        relsFrom = new HashSet<>();
        relsTo = new HashSet<>();
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public Set<SynsetRelation> getRelsFrom() {
        return relsFrom;
    }

    public void setRelsFrom(Set<SynsetRelation> relsFrom) {
        this.relsFrom = relsFrom;
    }

    public Set<SynsetRelation> getRelsTo() {
        return relsTo;
    }

    public void setRelsTo(Set<SynsetRelation> relsTo) {
        this.relsTo = relsTo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getPosID() {
        return posID;
    }

    public void setPosID(Long posID) {
        this.posID = posID;
    }

    public String getLexicon() {
        return lexicon;
    }

    public void setLexicon(String lexicon) {
        this.lexicon = lexicon;
    }
}
