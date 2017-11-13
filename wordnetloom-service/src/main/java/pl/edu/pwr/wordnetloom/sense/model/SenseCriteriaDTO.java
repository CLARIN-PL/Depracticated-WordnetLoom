package pl.edu.pwr.wordnetloom.sense.model;

import java.io.Serializable;
import java.util.List;

public class SenseCriteriaDTO implements Serializable {

    private Long senseId;
    private Long partOfSpeechId;
    private Long domainId;
    private String lemma;
    private Long relationTypeId;
    private List<Long> lexicons;
    private Integer variant;
    private String comment;
    private String example;
//    private String register;
    private Long register;
    private Long synsetId;

    public SenseCriteriaDTO(Long partOfSpeechId, Long domainId, String lemma, List<Long> lexicons) {
        this.partOfSpeechId = partOfSpeechId;
        this.domainId = domainId;
        this.lemma = lemma;
        this.lexicons = lexicons;
    }

    public Long getSenseId() {
        return senseId;
    }

    public void setSenseId(Long senseId) {
        this.senseId = senseId;
    }

    public Long getPartOfSpeechId() {
        return partOfSpeechId;
    }

    public void setPartOfSpeechId(Long partOfSpeechId) {
        this.partOfSpeechId = partOfSpeechId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Long getRelationTypeId() {
        return relationTypeId;
    }

    public void setRelationTypeId(Long relationTypeId) {
        this.relationTypeId = relationTypeId;
    }

    public List<Long> getLexicons() {
        return lexicons;
    }

    public void setLexicons(List<Long> lexicons) {
        this.lexicons = lexicons;
    }

    public Integer getVariant() {
        return variant;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
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

//    public String getRegister() {
//        return register;
//    }
//
//    public void setRegister(String register) {
//        this.register = register;
//    }

    public Long getRegisterId(){
        return register;
    }

    public void setRegisterId(Long registerId){
        this.register = registerId;
    }

    public Long getSynsetId() {
        return synsetId;
    }

    public void setSynsetId(Long synsetId) {
        this.synsetId = synsetId;
    }
}
