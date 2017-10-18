package pl.edu.pwr.wordnetloom.dto;

import pl.edu.pwr.wordnetloom.common.model.filter.GenericFilter;

public class SenseFilter extends GenericFilter {

    private String lemma;
    private Long lexiconId;
    private Long partOfSpeechId;
    private Long domainId;
    private Long relationTypeId;
    private Long grammaticalGenderId;
    private Long sourceId;
    private Long ageId;
    private Long statusId;
    private Long styleId;
    private Long yiddishDomainId;
    private Long lexicalCharacteristicId;
    private Long domainModifierDictionaryId;
    private String ethymology;
    private String definition;
    private String etymologicalRoot;
    private String example;

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

    public Long getGrammaticalGenderId() {
        return grammaticalGenderId;
    }

    public void setGrammaticalGenderId(Long grammaticalGenderId) {
        this.grammaticalGenderId = grammaticalGenderId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getAgeId() {
        return ageId;
    }

    public void setAgeId(Long ageId) {
        this.ageId = ageId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    public Long getLexicalCharacteristicId() {
        return lexicalCharacteristicId;
    }

    public void setLexicalCharacteristicId(Long lexicalCharacteristicId) {
        this.lexicalCharacteristicId = lexicalCharacteristicId;
    }

    public Long getYiddishDomainId() {
        return yiddishDomainId;
    }

    public void setYiddishDomainId(Long yiddishDomainId) {
        this.yiddishDomainId = yiddishDomainId;
    }

    public Long getDomainModifierDictionaryId() {
        return domainModifierDictionaryId;
    }

    public void setDomainModifierDictionaryId(Long domainModifierDictionaryId) {
        this.domainModifierDictionaryId = domainModifierDictionaryId;
    }

    public String getEthymology() {
        return ethymology;
    }

    public void setEthymology(String ethymology) {
        this.ethymology = ethymology;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getEtymologicalRoot() {
        return etymologicalRoot;
    }

    public void setEtymologicalRoot(String etymologicalRoot) {
        this.etymologicalRoot = etymologicalRoot;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
