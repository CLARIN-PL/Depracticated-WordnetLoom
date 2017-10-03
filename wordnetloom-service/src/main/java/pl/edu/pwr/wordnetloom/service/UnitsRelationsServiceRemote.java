package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;

@Remote
public interface UnitsRelationsServiceRemote {
	
//	public List<String[]> dbGetRelations(LexicalUnitDTO unit, RelationTypeDTO templateType, int unitType, boolean hideAutoReverse);
	public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse);
	
}
