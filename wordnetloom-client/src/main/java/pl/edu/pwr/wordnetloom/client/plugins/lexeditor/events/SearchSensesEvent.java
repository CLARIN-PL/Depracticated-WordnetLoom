package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.events;

import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;

public class SearchSensesEvent {

    private SenseCriteriaDTO dto;

    public SearchSensesEvent(){
    }

    public SearchSensesEvent(SenseCriteriaDTO dto){
        this.dto = dto;
    }

    public SenseCriteriaDTO getDto() {
        return dto;
    }
}
