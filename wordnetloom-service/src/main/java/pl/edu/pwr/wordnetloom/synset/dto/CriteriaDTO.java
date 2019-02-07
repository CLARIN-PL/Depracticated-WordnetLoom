package pl.edu.pwr.wordnetloom.synset.dto;

import EDU.oswego.cs.dl.util.concurrent.FJTask;
import pl.edu.pwr.wordnetloom.dictionary.model.Markedness;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CriteriaDTO implements Serializable{

    private String lemma;
    private Long synsetId;

    private List<Long> lexiconsIds;
    private PartOfSpeech partOfSpeech;
    private Domain domain;
    private RelationType relationType;
    private String comment;
    private List<Long> emotions;
    private List<Long> valuations;
    private Markedness markedness;
    private Status status;
    private int limit;
    private int offset;

    public CriteriaDTO(){

    }

    public CriteriaDTO(CriteriaDTO criteria) {
        setLemma(criteria.getLemma());
        setLexicons(criteria.getLexicons());
        setPartOfSpeech(criteria.getPartOfSpeech());
        setDomain(criteria.getDomain());
        setRelationType(criteria.getRelationType());
        setComment(criteria.getComment());
        setEmotions(criteria.getEmotions());
        setValuations(criteria.getValuations());
        setMarkedness(criteria.getMarkedness());
        setLimit(criteria.getLimit());
        setOffset(criteria.getOffset());
        setStatus(criteria.getStatus());

    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Long getSynsetId() {
        return synsetId;
    }

    public void setSynsetId(Long synsetId) {
        this.synsetId = synsetId;
    }

    public List<Long> getLexicons() {
        return lexiconsIds;
    }

    public void setLexicons(List<Long> lexiconsIds) {
        this.lexiconsIds = lexiconsIds;
    }

    public Long getLexiconId() {
        if(lexiconsIds != null && !lexiconsIds.isEmpty()){
            return lexiconsIds.get(0);
        }
        return null;
    }

    public void setLexiconId(Long lexiconId) {
        this.lexiconsIds = new ArrayList<>();
        lexiconsIds.add(lexiconId);
    }

    public Long getPartOfSpeechId() {
        return partOfSpeech != null ? partOfSpeech.getId() : null;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech){
        this.partOfSpeech = partOfSpeech;
    }

    public Long getDomainId() {
        return domain != null ? domain.getId() : null;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain){
        this.domain = domain;
    }

    public Long getRelationTypeId() {
        return relationType != null ? relationType.getId() : null;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType){
        this.relationType = relationType;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        if(comment == null || comment.isEmpty()){
            this.comment = null;
        } else {
            this.comment = comment;
        }
    }

    public List<Long> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Long> emotions) {
        this.emotions = emotions;
    }

    public List<Long> getValuations() {
        return valuations;
    }

    public void setValuations(List<Long> valuations) {
        this.valuations = valuations;
    }

    public Long getMarkednessId() {
        return markedness != null ? markedness.getId() : null;
    }

    public Markedness getMarkedness(){
        return markedness;
    }

    public void setMarkedness(Markedness markedness) {
        this.markedness = markedness;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Status getStatus() {
        return status;
    }

    public Long getStatusId(){
        if (status != null){
            return status.getId();
        }
        return null;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
