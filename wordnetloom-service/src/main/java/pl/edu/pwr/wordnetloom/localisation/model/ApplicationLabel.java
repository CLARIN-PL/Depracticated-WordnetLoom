package pl.edu.pwr.wordnetloom.localisation.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "application_labels")
public class ApplicationLabel extends GenericEntity{

    @NotNull
    @Column(name = "label_key")
    private String key;

    private String value;

    private String language;

    public String getKey() {
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
