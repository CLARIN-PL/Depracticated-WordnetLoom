package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "LexicalEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class LexicalEntry {

    @XmlAttribute(required = true)
    private String id;

    @XmlElement(name = "Lemma")
    private Lemma lemma;

    @XmlElement(name = "Sense")
    private Set<Sense> sense = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lemma getLemma() {
        return lemma;
    }

    public void setLemma(Lemma lemma) {
        this.lemma = lemma;
    }

    public Set<Sense> getSense() {
        return sense;
    }

    public void setSense(Set<Sense> sense) {
        this.sense = sense;
    }
}
