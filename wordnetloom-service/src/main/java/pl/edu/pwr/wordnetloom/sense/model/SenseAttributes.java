package pl.edu.pwr.wordnetloom.sense.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "sense_attributes")
@NamedQueries({
    @NamedQuery(name = "SenseAttribute.getForName", query = "SELECT sa FROM SenseAttribute sa, AttributeType at WHERE at.typeName = :typeName AND at.tableName = 'sense' AND sa.type = at"),
    @NamedQuery(name = "SenseAttribute.getForSense", query = "SELECT sa FROM SenseAttribute sa WHERE sa.sense = :sense"),
    @NamedQuery(name = "SenseAttribute.getSenseAttributeForName", query = "SELECT sa FROM SenseAttribute sa, AttributeType at WHERE "
            + "at.typeName.text = :typeName AND at.tableName = 'sense' AND sa.type = at AND sa.sense.id = :sense"),
    @NamedQuery(name = "SenseAttribute.deleteBySense", query = "DELETE FROM SenseAttribute sa WHERE sa.sense.id = :senseID"),})
public class SenseAttributes implements Serializable {

    private static final long serialVersionUID = -6738496417082820449L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String definition;

    @Lob
    private String comment;

    private String register;

    private String link;

    private String examples;

    public SenseAttributes() {
        super();
    }

    public SenseAttributes(String definition, String comment, String register, String link, String examples, Sense sense) {
        this.definition = definition;
        this.comment = comment;
        this.register = register;
        this.link = link;
        this.examples = examples;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

}
