package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "Definition")
@XmlAccessorType(XmlAccessType.FIELD)
public class Definition {

    @XmlAttribute(name = "gloss")
    private String gloss;

    @XmlElement(name = "Statement")
    private Set<Statement> statement = new HashSet<>();

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    public Set<Statement> getStatement() {
        return statement;
    }

    public void setStatement(Set<Statement> statement) {
        this.statement = statement;
    }
}
