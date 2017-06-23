package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.UnitsRelationsDAOLocal;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.relation.model.SenseRelation;

@Stateless
public class UnitsRelationsDAOBean implements UnitsRelationsDAOLocal {

    @EJB
    DAOLocal dao;

    @Override // TODO: refactor
    public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse) {

        if (null == unit) {
            return new ArrayList<SenseRelation>();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        String query_tpl = "SELECT distinct sr FROM SenseRelation sr WHERE ";

        if (unitType == SenseRelation.IS_PARENT) {
            query_tpl += " sr.senseFrom.id = :senseID ";
        } else {
            query_tpl += " sr.senseTo.id = :senseID ";
        }
        params.put("senseID", unit.getId());

        if (null != templateType) {
            query_tpl += " AND sr.relation.id = :relationID ";
            params.put("relationID", templateType.getId());
        }

        if (hideAutoReverse) {
            query_tpl += " AND sr.relation.autoReverse = 1 ";
        }

        query_tpl += "ORDER BY sr.relation.id";

        TypedQuery<SenseRelation> q = dao.getEM().createQuery(query_tpl, SenseRelation.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return q.getResultList();
    }

}
