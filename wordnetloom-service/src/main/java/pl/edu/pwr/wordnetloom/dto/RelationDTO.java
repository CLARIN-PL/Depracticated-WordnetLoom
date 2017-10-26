package pl.edu.pwr.wordnetloom.dto;

public class RelationDTO {

    private String relationName;
    private Long target;
    private String lemma;
    private Long pos;

    public RelationDTO(Long target, String relationName, String lemma) {
        this.target = target;
        this.relationName = relationName;
        this.lemma = lemma;

    }

    public RelationDTO(Long target, String relationName, String lemma, Long pos) {
        this.target = target;
        this.relationName = relationName;
        this.lemma = lemma;
        this.pos = pos;
    }

    public String getRelationName() {
        return relationName;
    }

    public Long getTarget() {
        return target;
    }

    public String getLemma() {
        return lemma;
    }

    public Long getPos() {
        return pos;
    }
}
