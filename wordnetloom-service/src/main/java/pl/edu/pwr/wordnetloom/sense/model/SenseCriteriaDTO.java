package pl.edu.pwr.wordnetloom.sense.model;

import java.io.Serializable;
import java.util.List;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationType;

public class SenseCriteriaDTO implements Serializable {

    private Long senseId;
    private PartOfSpeech partOfSpeech;
    private Domain domain;
    private String lemma;
    private SenseRelationType relationType;
    private List<Long> lexicons;
    private Integer variant;
    private String comment;
    private String example;
    private String register;
    private Long synsetId;

    public SenseCriteriaDTO(PartOfSpeech partOfSpeech, Domain domain, String lemma, List<Long> lexicons) {
        this.partOfSpeech = partOfSpeech;
        this.domain = domain;
        this.lemma = lemma;
        this.lexicons = lexicons;
    }

    public Long getSenseId() {
        return senseId;
    }

    public void setSenseId(Long senseId) {
        this.senseId = senseId;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public SenseRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SenseRelationType relationType) {
        this.relationType = relationType;
    }

    public List<Long> getLexicons() {
        return lexicons;
    }

    public void setLexicons(List<Long> lexicons) {
        this.lexicons = lexicons;
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

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public Long getSynsetId() {
        return synsetId;
    }

    public void setSynsetId(Long synsetId) {
        this.synsetId = synsetId;
    }

    public Integer getVariant() {
        return variant;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

}
