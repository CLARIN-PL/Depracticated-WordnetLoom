package pl.edu.pwr.wordnetloom.dto;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SenseDataEntry implements Serializable {

    private static final long serialVersionUID = 5708346930324155121L;

    private Sense sense;
    private List<SenseRelation> relsFrom;
    private List<SenseRelation> relsTo;

    public SenseDataEntry() {
        relsFrom = new ArrayList<>();
        relsTo = new ArrayList<>();
    }

    public Sense getSense() {
        return sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    public List<SenseRelation> getRelsFrom() {
        return relsFrom;
    }

    public void setRelsFrom(List<SenseRelation> relsFrom) {
        this.relsFrom = relsFrom;
    }

    public List<SenseRelation> getRelsTo() {
        return relsTo;
    }

    public void setRelsTo(List<SenseRelation> relsTo) {
        this.relsTo = relsTo;
    }

    public String getLabel() {
        return sense.toString();
    }

    public Long getPosID() {
        return sense.getPartOfSpeech().getId();
    }

    public String getLexicon() {
        return sense.getLexicon().toString();
    }

}
