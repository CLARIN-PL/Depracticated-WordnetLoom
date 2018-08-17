package pl.edu.pwr.wordnetloom.synset.dto;

public class SynsetCriteriaDTO extends CriteriaDTO{

    private String definition;
    private Boolean abstractSynset;

    public SynsetCriteriaDTO(){

    }

    public SynsetCriteriaDTO(CriteriaDTO criteriaDTO){
        super(criteriaDTO);
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Boolean isAbstract() {
        return abstractSynset;
    }

    public void setAbstract(Boolean abstractSynset) {
        this.abstractSynset = abstractSynset;
    }
}
