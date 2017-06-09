package pl.edu.pwr.wordnetloom.service.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.UnitsRelationsDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseRelation;
import pl.edu.pwr.wordnetloom.service.UnitsRelationsServiceRemote;

@Stateless
public class UnitsRelationsServiceBean implements UnitsRelationsServiceRemote {

    @EJB
    private UnitsRelationsDAOLocal local;

    @Override
    public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse) {
        return local.dbGetRelations(unit, templateType, unitType, hideAutoReverse);
    }

}
