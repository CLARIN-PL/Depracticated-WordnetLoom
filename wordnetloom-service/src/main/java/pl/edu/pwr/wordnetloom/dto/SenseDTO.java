package pl.edu.pwr.wordnetloom.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class SenseDTO implements Serializable {

    private Long id;
    private String lemma;
    private Long partOfSpeech;

    private Map<Long, Set<RelationDTO>> incomming;
    private Map<Long, Set<RelationDTO>> outgoing;

    public SenseDTO(Long id, String lemma, Long partOfSpeech, Map<Long, Set<RelationDTO>> incomming, Map<Long, Set<RelationDTO>> outgoing) {
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

    public Map<Long, Set<RelationDTO>> getIncomming() {
        return incomming;
    }

    public Map<Long, Set<RelationDTO>> getOutgoing() {
        return outgoing;
    }
}
