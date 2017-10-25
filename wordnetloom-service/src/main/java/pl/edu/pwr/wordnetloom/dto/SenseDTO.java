package pl.edu.pwr.wordnetloom.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class SenseDTO implements Serializable {

    private Long id;
    private String lemma;
    private Long partOfSpeech;

    private Map<String, Set<RelationDTO>> incomming;
    private Map<String, Set<RelationDTO>> outgoing;

    public SenseDTO(Long id, String lemma, Long partOfSpeech, Map<String, Set<RelationDTO>> incomming, Map<String, Set<RelationDTO>> outgoing) {
        this.id = id;
        this.lemma = lemma;
        this.partOfSpeech = partOfSpeech;
        this.incomming = incomming;
        this.outgoing = outgoing;
    }

    public Long getId() {
        return id;
    }

    public String getLemma() {
        return lemma;
    }

    public Long getPartOfSpeech() {
        return partOfSpeech;
    }

    public Map<String, Set<RelationDTO>> getIncomming() {
        return incomming;
    }

    public Map<String, Set<RelationDTO>> getOutgoing() {
        return outgoing;
    }
}
