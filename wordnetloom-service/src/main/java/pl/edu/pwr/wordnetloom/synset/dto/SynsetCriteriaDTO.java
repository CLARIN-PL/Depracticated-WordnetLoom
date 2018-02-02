package pl.edu.pwr.wordnetloom.synset.dto;

import java.io.Serializable;

public class SynsetCriteriaDTO implements Serializable {

    private String lemma;
    private Long lexiconId;
    private Long partOfSpeechId;
    private Long domainId;
    private Long relationTypeId;
    private String definition;
    private String comment;
    private Boolean abstractSynset;
    private int limit;
    private int offset;

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Long getLexiconId() {
        return lexiconId;
    }

    public void setLexiconId(Long lexiconId) {
        this.lexiconId = lexiconId;
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

    public Long getRelationTypeId() {
        return relationTypeId;
    }

    public void setRelationTypeId(Long relationTypeId) {
        this.relationTypeId = relationTypeId;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        if(definition != null && definition.isEmpty()){
            this.definition = null;
        } else {
            this.definition = definition;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if(comment != null && comment.isEmpty()){
            this.comment = null;
        } else {
            this.comment = comment;
        }
    }

    public Boolean isAbstract(){
        return abstractSynset;
    }

    public void setAbstract(Boolean isAbstract){
        abstractSynset = isAbstract;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit){
        this.limit = limit;
    }

    public int getOffset(){
        return offset;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }
}
