package pl.edu.pwr.wordnetloom.dto;

import java.io.Serializable;
import java.util.Set;

public class SenseGraphDTO implements Serializable {

    private Long id;
    private String lemma;
    private Long partOfSpeech;
    private Set<SenseDTO> incomming;
    private Set<SenseDTO> outgoing;

    public SenseGraphDTO(Long id, String lemma, Long partOfSpeech, Set<SenseDTO> incomming, Set<SenseDTO> outgoing) {
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

    public Set<SenseDTO> getIncomming() {
        return incomming;
    }

    public Set<SenseDTO> getOutgoing() {
        return outgoing;
    }
}
