package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Statement")
@XmlAccessorType(XmlAccessType.FIELD)
public class Statement {

    @XmlAttribute
    private String example;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
