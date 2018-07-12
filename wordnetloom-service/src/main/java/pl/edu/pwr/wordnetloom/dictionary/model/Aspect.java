package pl.edu.pwr.wordnetloom.dictionary.model;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

@Audited
@Entity
public class Aspect extends Dictionary{

    @Column(name = "tag")
    private String tag;

    public String getTag(){
        return tag;
    }

    public void setTag(String tag){
        this.tag = tag;
    }
}
