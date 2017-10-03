package pl.edu.pwr.wordnetloom.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;

@Stateless
public class UnitsRelationsDAOBean implements UnitsRelationsDAOLocal {
	
	@EJB DAOLocal dao;

//	@Override
//	public List<String[]> dbGetRelations(LexicalUnitDTO unit, RelationTypeDTO templateType, int unitType, boolean hideAutoReverse) {
//		
//		final String query_tpl = "SELECT lr.PARENT_ID as lexicalrelation_PARENT_ID, lr.CHILD_ID as lexicalrelation_CHILD_ID, lr.REL_ID as lexicalrelation_REL_ID, lr.valid as lexicalrelation_valid, lr.owner as lexicalrelation_owner, rt.ID as relationtype_ID, rt.objecttype as relationtype_objecttype, rt.PARENT_ID as relationtype_PARENT_ID, rt.REVERSE_ID as relationtype_REVERSE_ID, rt.name as relationtype_name, rt.description as relationtype_description, rt.posstr as relationtype_posstr, rt.autoreverse as relationtype_autoreverse, rt.display as relationtype_display, rt.shortcut as relationtype_shortcut, rt.pwn as relationtype_pwn, rt.order as relationtype_order, lu.ID as lexicalunit_ID, lu.lemma as lexicalunit_lemma, lu.domain as lexicalunit_domain, lu.pos as lexicalunit_pos, lu.tagcount as lexicalunit_tagcount, lu.source as lexicalunit_source, lu.status as lexicalunit_status, lu.comment as lexicalunit_comment, lu.variant as lexicalunit_variant, lu.project as lexicalunit_project, lu.owner as lexicalunit_owner, lu2.ID as lexicalunit2_ID, lu2.lemma as lexicalunit2_lemma, lu2.domain as lexicalunit2_domain, lu2.pos as lexicalunit2_pos, lu2.tagcount as lexicalunit2_tagcount, lu2.source as lexicalunit2_source, lu2.status as lexicalunit2_status, lu2.comment as lexicalunit2_comment, lu2.variant as lexicalunit2_variant, lu2.project as lexicalunit2_project, lu2.owner as lexicalunit2_owner "
//				+ "FROM `lexicalrelation` lr "
//				+ "INNER JOIN `relationtype` rt ON rt.id = lr.rel_id "
//				+ "INNER JOIN `lexicalunit` lu ON lu.id = lr.parent_id "
//				+ "INNER JOIN `lexicalunit` lu2 ON lu2.id = lr.child_id "
//				+ "WHERE lr.%s = %d %s ORDER BY rt.order, rt.id";
//
//		String rel_direct = "CHILD_ID";
//		if (unitType == UnitsRelationsDC.IS_PARENT) {
//			rel_direct = "PARENT_ID";
//		}
//		String add_where = "";
//		if (templateType != null)
//			add_where += String.format("AND rt.id = %d", templateType.getId());
//		if (hideAutoReverse) {
//			add_where += String.format("AND rt.autoreverse = 1");
//		}
//
//		String query = String.format(query_tpl, rel_direct, unit.getId(),
//				add_where);
//
//		QueryResult res = dbq.rawQuery(query);
//		
//		return res.getItems();
//	}
	
	@Override // TODO: refactor
	public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse) {
		
		if(null == unit)
			return new ArrayList<SenseRelation>();
		
		Map<String, Object> params = new HashMap<String, Object>(); 
		String query_tpl = "SELECT distinct sr FROM SenseRelation sr WHERE ";

		if (unitType == SenseRelation.IS_PARENT) {
			query_tpl += " sr.senseFrom.id = :senseID ";
		}else{
			query_tpl += " sr.senseTo.id = :senseID ";
		}
		params.put("senseID", unit.getId());
		
		if (null != templateType){
			query_tpl += " AND sr.relation.id = :relationID ";
			params.put("relationID", templateType.getId());
		}
		
		if (hideAutoReverse) {
			query_tpl += " AND sr.relation.autoReverse = 1 ";
		}

		query_tpl += "ORDER BY sr.relation.id"; 
		
		TypedQuery<SenseRelation> q = dao.getEM().createQuery(query_tpl, SenseRelation.class);
		
		for(Map.Entry<String, Object> entry : params.entrySet()){
			q.setParameter(entry.getKey(), entry.getValue());
		}
		
		return q.getResultList();
	}

}
