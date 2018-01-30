package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "LexicalResource")
@XmlAccessorType(XmlAccessType.FIELD)
public class LexicalResource {

    @XmlElement(name = "GlobalInformation")
    private GlobalInformation globalInformation;

    @XmlElement(name = "Lexicon")
    private List<Lexicon> lexicon = new ArrayList<>();

    @XmlElement(name = "SenseAxes")
    private SenseAxes senseAxes;

    public List<Lexicon> getLexicon() {
        return lexicon;
    }

    public void setLexicon(List<Lexicon> lexicon) {
        this.lexicon = lexicon;
    }

    public GlobalInformation getGlobalInformation() {
        return globalInformation;
    }

    public void setGlobalInformation(GlobalInformation globalInformation) {
        this.globalInformation = globalInformation;
    }

    public SenseAxes getSenseAxes() {
        return senseAxes;
    }

    public void setSenseAxes(SenseAxes senseAxes) {
        this.senseAxes = senseAxes;
    }
}
