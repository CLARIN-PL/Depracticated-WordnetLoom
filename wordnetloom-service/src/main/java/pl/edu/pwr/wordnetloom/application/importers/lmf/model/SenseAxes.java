package pl.edu.pwr.wordnetloom.application.importers.lmf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "SenseAxes")
@XmlAccessorType(XmlAccessType.FIELD)
public class SenseAxes {

    @XmlElement(name = "SenseAxis")
    private List<SenseAxis> senseAxes = new ArrayList<>();

    public List<SenseAxis> getSenseAxes() {
        return senseAxes;
    }

    public void setSenseAxes(List<SenseAxis> senseAxes) {
        this.senseAxes = senseAxes;
    }
}
