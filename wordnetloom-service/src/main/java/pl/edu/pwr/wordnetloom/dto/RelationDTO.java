package pl.edu.pwr.wordnetloom.dto;

public class RelationDTO {

    private Long relationTypeId;
    private Long target;
    private String lemma;
    private Long pos;

    public RelationDTO(Long target, Long relationTypeId, String lemma) {
        this.target = target;
        this.relationTypeId = relationTypeId;
        this.lemma = lemma;

    }

    public RelationDTO(Long target, Long relationTypeId, String lemma, Long pos) {
        this.target = target;
        this.relationTypeId = relationTypeId;
        this.lemma = lemma;
        this.pos = pos;
    }

    public Long getTarget() {
        return target;
    }

    public Long getRelationTypeId() {
        return relationTypeId;
    }

    public String getLemma() {
        return lemma;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }
}
