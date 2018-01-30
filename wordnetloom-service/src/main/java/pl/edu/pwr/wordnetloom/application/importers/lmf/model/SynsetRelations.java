package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "SynsetRelations")
@XmlAccessorType(XmlAccessType.FIELD)
public class SynsetRelations {

    @XmlElement(name = "SynsetRelation")
    private List<SynsetRelation> synsetRelation = new ArrayList<>();

    public List<SynsetRelation> getSynsetRelation() {
        return synsetRelation;
    }

    public void setSynsetRelation(List<SynsetRelation> synsetRelation) {
        this.synsetRelation = synsetRelation;
    }
}
