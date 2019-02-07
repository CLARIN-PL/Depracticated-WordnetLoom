package pl.edu.pwr.wordnetloom.dictionary.model;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.awt.*;

@Audited
@Entity
public class Status extends Dictionary {

    @Column(name = "color")
    private String color;

    public Status() {
        super();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
