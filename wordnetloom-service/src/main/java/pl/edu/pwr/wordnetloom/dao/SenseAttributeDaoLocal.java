package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

@Local
public interface SenseAttributeDaoLocal {

    SenseAttributes getSenseAttributeForName(Sense sense, String typeName);

    List<SenseAttributes> getSenseAttributesForName(Text typeName);

    SenseAttributes getSenseAttribute(Long index);

    List<SenseAttributes> getSenseAttributes(Sense sense);

    void removeSenseAttribute(SenseAttributes s);

    void removeSenseAttribute(Sense s);

    void persistSenseAttribute(SenseAttributes s);

    void saveOrUpdateSenseAttribute(Sense sense, String key, String value);

}
