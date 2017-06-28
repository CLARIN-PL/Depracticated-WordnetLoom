package pl.edu.pwr.wordnetloom.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.relation.model.SynsetRelation;

public class DataEntry implements Serializable {

    private static final long serialVersionUID = 5708346930324155121L;

    private Synset synset;
    private List<SynsetRelation> relsFrom;
    private List<SynsetRelation> relsTo;
    private String label;
    private Long posID;
    private String lexicon;

    public DataEntry() {
        relsFrom = new ArrayList<>();
        relsTo = new ArrayList<>();
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public List<SynsetRelation> getRelsFrom() {
        return relsFrom;
    }

    public void setRelsFrom(List<SynsetRelation> relsFrom) {
        this.relsFrom = relsFrom;
    }

    public List<SynsetRelation> getRelsTo() {
        return relsTo;
    }

    public void setRelsTo(List<SynsetRelation> relsTo) {
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
