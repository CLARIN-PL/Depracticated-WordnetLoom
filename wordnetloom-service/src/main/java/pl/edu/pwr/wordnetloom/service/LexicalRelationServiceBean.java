package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.dao.DAOBean;
import pl.edu.pwr.wordnetloom.dao.LexicalRelationDAOLocal;
import pl.edu.pwr.wordnetloom.model.Sense;

@Stateless
public class LexicalRelationServiceBean extends DAOBean implements LexicalRelationServiceRemote {

	@EJB private LexicalRelationDAOLocal local;

	@Override
	public void dbDelete(SenseRelation rel) {
		local.dbDelete(rel);
	}

	@Override
	public void dbDelete(RelationType relation) {
		local.dbDelete(relation);
	}
	

	@Override
	public void dbDeleteAll() {
		local.dbDeleteAll();
	}
	
	@Override
	public List<SenseRelation> dbGetSubRelations(Sense sense,
			RelationType relationType) {
		return local.dbGetSubRelations(sense, relationType);
	}

	@Override
	public List<SenseRelation> dbFastGetRelations(RelationType relationType) {
		return local.dbFastGetRelations(relationType);
	}

	@Override
	public Set<Long> dbSelftRelations() {
		return local.dbSelftRelations();
	}

	@Override
	public List<SenseRelation> dbGetUpperRelations(Sense sense,
			RelationType relationType) {
		return local.dbGetUpperRelations(sense, relationType);
	}

	@Override
	public List<SenseRelation> dbGetFullRelations(Sense parent) {
		return local.dbGetFullRelations(parent);
	}

	@Override
	public List<SenseRelation> dbGetRelations(Sense parent, Sense child,
			RelationType relationType) {
		return local.dbGetRelations(parent, child, relationType);
	}

	@Override
	public SenseRelation dbGetRelation(Sense parent, Sense child,
			RelationType relationType) {
		return local.dbGetRelation(parent, child, relationType);
	}

	@Override
	public boolean dbMakeRelation(Sense parent, Sense child, RelationType relation) {
		return local.dbMakeRelation(parent, child, relation);
	}

	@Override
	public void dbDeleteConnection(Sense sense) {
		local.dbDeleteConnection(sense);
	}

	@Override
	public List<SenseRelation> dbFullGetRelations() {
		return local.dbFullGetRelations();
	}

	@Override
	public int dbGetRelationsCount() {
		return local.dbGetRelationsCount();
	}

	@Override
	public int dbGetRelationUseCount(RelationType relation) {
		return local.dbGetRelationUseCount(relation);
	}

	@Override
	public void dbMove(RelationType oldRelation, RelationType newRelation) {
		local.dbMove(oldRelation, newRelation);
	}

	@Override
	public boolean dbRelationExists(Sense parent, Sense child,
			RelationType relationType) {
		return local.dbRelationExists(parent, child, relationType);
	}

	@Override
	public List<RelationType> dbGetRelationTypesOfUnit(Sense sense) {
		return local.dbGetRelationTypesOfUnit(sense);
	}

	@Override
	public int dbGetRelationCountOfUnit(Sense sense) {
		return local.dbGetRelationCountOfUnit(sense);
	}

	@Override
	public int dbDeleteImproper() {
		return local.dbDeleteImproper();
	}

	@Override
	public SenseRelation dbGet(Long id) {
		return local.dbGet(id);
	}

}
