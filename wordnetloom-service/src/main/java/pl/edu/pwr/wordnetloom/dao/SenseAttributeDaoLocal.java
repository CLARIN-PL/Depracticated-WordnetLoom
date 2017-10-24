package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.Text;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SenseAttributeDaoLocal {

    SenseAttribute getSenseAttributeForName(Sense sense, String typeName);

    List<SenseAttribute> getSenseAttributesForName(Text typeName);

    SenseAttribute getSenseAttribute(Long index);

    List<SenseAttribute> getSenseAttributes(Sense sense);

    void removeSenseAttribute(SenseAttribute s);

    void removeSenseAttribute(Sense s);

    void persistSenseAttribute(SenseAttribute s);

    void saveOrUpdateSenseAttribute(Sense sense, String key, String value);

}
