package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "SenseAxis")
@XmlAccessorType(XmlAccessType.FIELD)
public class SenseAxis {

    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute
    private String relType;

    @XmlElement(name = "Target")
    private Set<Target> targets = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String retType) {
        relType = retType;
    }

    public Set<Target> getTargets() {
        return targets;
    }

    public void setTargets(Set<Target> targets) {
        this.targets = targets;
    }
}
