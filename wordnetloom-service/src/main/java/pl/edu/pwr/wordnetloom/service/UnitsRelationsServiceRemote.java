package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseRelation;

@Remote
public interface UnitsRelationsServiceRemote {

    List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse);

}
