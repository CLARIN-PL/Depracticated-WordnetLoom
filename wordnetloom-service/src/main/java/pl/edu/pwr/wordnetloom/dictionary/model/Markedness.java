package pl.edu.pwr.wordnetloom.dictionary.model;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

@Audited
@Entity
public class Markedness extends Dictionary{

    @Column(name = "value")
    private Long value;

    public Long getValue(){
        return value;
    }

    public void setValue(Long value){
        this.value = value;
    }
}
