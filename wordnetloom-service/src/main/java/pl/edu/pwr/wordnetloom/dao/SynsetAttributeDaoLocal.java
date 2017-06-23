package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttribute;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

@Local
public interface SynsetAttributeDaoLocal {

    SynsetAttribute getSynsetAttributeForName(Synset synset, String typeName);

    List<SynsetAttribute> getSynsetAttributesForName(Text typeName);

    SynsetAttribute getSynsetAttribute(Long index);

    List<SynsetAttribute> getSynsetAttributes(Synset sense);

    void removeSynsetAttribute(SynsetAttribute s);

    void persistSynsetAttribute(SynsetAttribute s);

    void saveOrUpdateSynsetAttribute(Synset synset, String key, String value);

    void deleteAttributesFor(Synset synset);

}
