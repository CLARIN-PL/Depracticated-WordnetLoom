package pl.edu.pwr.wordnetloom.domain.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "domain")
public class Domain extends GenericEntity {

    private static final long serialVersionUID = -9015379547562677206L;

    @Column(name = "name_id")
    private Long name;

    @Column(name = "description_id")
    private Long description;

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public Long getDescription() {
        return description;
    }

    public void setDescription(Long description) {
        this.description = description;
    }
}
