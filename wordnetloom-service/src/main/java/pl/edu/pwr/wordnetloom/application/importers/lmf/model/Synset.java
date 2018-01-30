package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Synset")
@XmlAccessorType(XmlAccessType.FIELD)
public class Synset {

    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute
    private String baseConcept;

    @XmlElement(name = "Definition")
    private Definition definition;

    @XmlElement(name = "SynsetRelations")
    private SynsetRelations synsetRelations;

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

    public SynsetRelations getSynsetRelations() {
        return synsetRelations;
    }

    public void setSynsetRelations(SynsetRelations synsetRelations) {
        this.synsetRelations = synsetRelations;
    }
}
