package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Lemma")
@XmlAccessorType(XmlAccessType.FIELD)
public class Lemma {

    @XmlAttribute
    private String writtenForm;

    @XmlAttribute
    private String partOfSpeech;

    public String getWrittenForm() {
        return writtenForm;
    }

    public void setWrittenForm(String writtenForm) {
        this.writtenForm = writtenForm;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }
}
