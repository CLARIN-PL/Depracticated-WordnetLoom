package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseRelation;

@Local
public interface UnitsRelationsDAOLocal {

    List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse);

}
