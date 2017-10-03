package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.UnitsRelationsDAOLocal;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;

@Stateless
public class UnitsRelationsServiceBean implements UnitsRelationsServiceRemote {
	
	@EJB private UnitsRelationsDAOLocal local;

//	public ArrayList<String[]> dbGetRelations(LexicalUnitDTO unit,
//			RelationTypeDTO templateType, int unitType, boolean hideAutoReverse) {
//		return local.dbGetRelations(unit, templateType, unitType, hideAutoReverse);
//	}
	
	@Override
	public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse){
		return local.dbGetRelations(unit, templateType, unitType, hideAutoReverse);
	}

}
