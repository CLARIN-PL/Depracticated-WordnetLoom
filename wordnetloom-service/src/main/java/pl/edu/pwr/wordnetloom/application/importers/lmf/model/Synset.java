package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Synset")
@XmlAccessorType(XmlAccessType.FIELD)
public class Synset {

    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute
    private String baseConcept;

    @XmlAttribute
    private String ili;

    @XmlElement(name = "Definition")
    private Definition definition;

    @XmlElement(name = "SynsetRelation")
    private List<SynsetRelation> synsetRelation = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseConcept() {
        return baseConcept;
    }

    public void setBaseConcept(String baseConcept) {
        this.baseConcept = baseConcept;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public List<SynsetRelation> getSynsetRelation() {
        return synsetRelation;
    }

    public void setSynsetRelation(List<SynsetRelation> synsetRelation) {
        this.synsetRelation = synsetRelation;
    }

    public String getIli() {
        return ili;
    }

    public void setIli(String ili) {
        this.ili = ili;
    }
}
