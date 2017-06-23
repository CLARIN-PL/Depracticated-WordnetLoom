package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.AttributeType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttribute;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

@Remote
public interface DynamicAttributesServiceRemote {

    List<AttributeType> getAllAttributeTypes();

    AttributeType getAttributeType(Long index);

    AttributeType getAttributeType(Text typeName);

    List<AttributeType> getAttributeTypesForName(String tableName);

    List<SenseAttributes> getSenseAttributesForName(Text typeName);

    List<SynsetAttribute> getSynsetAttributesForName(Text typeName);

    SenseAttributes getSenseAttribute(Long index);

    List<SenseAttributes> getSenseAttributes(Sense sense);

    SynsetAttribute getSynsetAttribute(Long index);

    List<SynsetAttribute> getSynsetAttributes(Synset sense);

    void synchronizeAttributeList(Sense sense, List<SenseAttributes> list);

    void synchronizeAttributeList(Synset synset, List<SynsetAttribute> list);

    String getSenseAttribute(Sense sense, String key);

    String getSynsetAttribute(Synset synset, String key);

    void saveOrUpdateSenseAttribute(Sense sense, String key, String value);

    void saveOrUpdateSynsetAttribute(Synset synset, String key, String value);

}
