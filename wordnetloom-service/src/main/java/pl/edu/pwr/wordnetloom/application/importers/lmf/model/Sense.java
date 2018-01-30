package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Sense")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sense {

    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute
    private String synset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSynset() {
        return synset;
    }

    public void setSynset(String synset) {
        this.synset = synset;
    }
}
