package pl.edu.pwr.wordnetloom.model.dto;

import java.io.Serializable;

public class SynsetInfo implements Serializable {

    private static final long serialVersionUID = -3464178034594997731L;

    private Long synsetID;
    private Long posID;
    private String domain;
    private String word;
    private String lexicon;
    private String isAbstract;
    private Integer senseNumber;

    public SynsetInfo() {
    }

    public SynsetInfo(Long synsetID, Long posID, String domain, String word, String isAbstract, Integer senseNumber, String lexicon) {
        this.synsetID = synsetID;
        this.posID = posID;
        this.domain = domain;
        this.word = word;
        this.isAbstract = isAbstract;
        this.senseNumber = senseNumber;
        this.lexicon = lexicon;
    }

    public Long getSynsetID() {
        return synsetID;
    }

    public void setSynsetID(Long synsetID) {
        this.synsetID = synsetID;
    }

    public Long getPosID() {
        return posID;
    }

    public void setPosID(Long posID) {
        this.posID = posID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(String isAbstract) {
        this.isAbstract = isAbstract;
    }

    public Integer getSenseNumber() {
        return senseNumber;
    }

    public void setSenseNumber(Integer senseNumber) {
        this.senseNumber = senseNumber;
    }

    public String getLexicon() {
        return lexicon;
    }

    public void setLexicon(String lexicon) {
        this.lexicon = lexicon;
    }
}
