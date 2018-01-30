package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Lexicon")
@XmlAccessorType(XmlAccessType.FIELD)
public class Lexicon {

    @XmlElement(name = "LexicalEntry")
    private List<LexicalEntry> lexicalEntries = new ArrayList<>();

    @XmlElement(name = "Synset")
    private List<Synset> synsets = new ArrayList<>();

    @XmlAttribute
    private String languageCoding;

    @XmlAttribute
    private String label;

    @XmlAttribute
    private String language;

    @XmlAttribute
    private String owner;

    @XmlAttribute
    private String version;

    public List<LexicalEntry> getLexicalEntries() {
        return lexicalEntries;
    }

    public void setLexicalEntries(List<LexicalEntry> lexicalEntries) {
        this.lexicalEntries = lexicalEntries;
    }

    public List<Synset> getSynsets() {
        return synsets;
    }

    public void setSynsets(List<Synset> synsets) {
        this.synsets = synsets;
    }

    public String getLanguageCoding() {
        return languageCoding;
    }

    public void setLanguageCoding(String languageCoding) {
        this.languageCoding = languageCoding;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
