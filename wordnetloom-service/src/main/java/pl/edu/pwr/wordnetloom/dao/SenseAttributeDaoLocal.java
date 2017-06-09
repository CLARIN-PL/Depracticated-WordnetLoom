package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

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
