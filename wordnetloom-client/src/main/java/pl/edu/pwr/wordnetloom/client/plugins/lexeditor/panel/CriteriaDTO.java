package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import java.util.List;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;

public class CriteriaDTO {

    private String lemma;
    private int lexicon;
    private int partOfSpeech;
    private int domain;
    private int relation;
    private int register;
    private StatusDictionary status;
    private String definition;
    private String comment;
    private String example;
    private String synsetType;
    private List<Sense> sense;

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public int getLexicon() {
        return lexicon;
    }

    public void setLexicon(int lexicon) {
        this.lexicon = lexicon;
    }

    public int getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(int partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public int getDomain() {
        return domain;
    }

    public void setDomain(int domain) {
        this.domain = domain;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getSynsetType() {
        return synsetType;
    }

    public void setSynsetType(String synsetType) {
        this.synsetType = synsetType;
    }

    public List<Sense> getSense() {
        return sense;
    }

    public void setSense(List<Sense> sense) {
        this.sense = sense;
    }

    public StatusDictionary getStatus() {
        return status;
    }

    public void setStatus(StatusDictionary status) {
        this.status = status;
    }

}
