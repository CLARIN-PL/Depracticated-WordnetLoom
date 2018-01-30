package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SynsetRelation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SynsetRelation {

    @XmlAttribute
    private String targets;

    @XmlAttribute
    private String relType;


    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }
}
