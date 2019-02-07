package pl.edu.pwr.wordnetloom.sense.dto;

import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;

import java.util.List;

public class SenseCriteriaDTO extends CriteriaDTO {

    private Sense sense;
    private Integer variant;
    private String example;
    private Register register;

    public SenseCriteriaDTO(){

    }

    public SenseCriteriaDTO(CriteriaDTO criteriaDTO){
        super(criteriaDTO);
    }

    public SenseCriteriaDTO(PartOfSpeech partOfSpeech, Domain domainId, String lemma, List<Long> lexicons){
        // TODO sprawdzić, czy da radę to jakoś to przerobić
        setPartOfSpeech(partOfSpeech);
        setDomain(domainId);
        setLemma(lemma);
        setLexicons(lexicons);
    }

    public Sense getSense() {
        return sense;
    }

    public Long getSenseId() {
        return sense != null ? sense.getId() : null;
    }

    public void setSense(Sense sense){
        this.sense = sense;
    }

    public Integer getVariant() {
        return variant;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        if(example != null && !example.isEmpty()) {
            this.example = example;
        }
    }

    public Register getRegister() {
        return register;
    }

    public Long getRegisterId() {
        return register != null ? register.getId() : null;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}
