package pl.edu.pwr.wordnetloom.localisation.model;


import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.*;

@Entity
@Table(name = "register_types")
public class RegisterType extends GenericEntity{

    @JoinColumn(name = "name_id", referencedColumnName = "id", insertable = false, updatable = false)
    private LocalisedString name;

    public LocalisedString getName() {
        return name;
    }

    public void setName(LocalisedString name) {
        this.name = name;
    }
}
