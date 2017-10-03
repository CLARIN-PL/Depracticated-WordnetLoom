package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.AttributeTypeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.model.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.dao.SenseAttributeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetAttributeDaoLocal;
import pl.edu.pwr.wordnetloom.model.AttributeType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetAttribute;

@Stateless
public class DynamicAttributesServiceBean implements DynamicAttributesServiceRemote {
	
	@EJB private DAOLocal local;
	@EJB private AttributeTypeDaoLocal attributeType;
	@EJB private SenseAttributeDaoLocal senseAttribute;
	@EJB private SynsetAttributeDaoLocal synsetAttribute;
	
	@Override
	public List<AttributeType> getAllAttributeTypes(){
		return attributeType.getAllAttributeTypes();
	}

	@Override
	public AttributeType getAttributeType(Long index) {
		return attributeType.getAttributeType(index);
	}

	@Override
	public List<AttributeType> getAttributeTypesForName(String tableName) {
		return attributeType.getAttributeTypesForName(tableName);
	}

	@Override
	public AttributeType getAttributeType(Text typeName) {
		return attributeType.getAttributeType(typeName);
	}

	@Override
	public List<SenseAttribute> getSenseAttributesForName(Text typeName) {
		return senseAttribute.getSenseAttributesForName(typeName);
	}

	@Override
	public SenseAttribute getSenseAttribute(Long index) {
		return senseAttribute.getSenseAttribute(index);
	}

	@Override
	public List<SenseAttribute> getSenseAttributes(Sense sense) {
		return senseAttribute.getSenseAttributes(sense);
	}

	@Override
	public SynsetAttribute getSynsetAttribute(Long index) {
		return synsetAttribute.getSynsetAttribute(index);
	}

	@Override
	public List<SynsetAttribute> getSynsetAttributes(Synset sense) {
		return synsetAttribute.getSynsetAttributes(sense);
	}

	@Override
	public List<SynsetAttribute> getSynsetAttributesForName(Text typeName) {
		return synsetAttribute.getSynsetAttributesForName(typeName);
	}

	@Override
	public String getSenseAttribute(Sense sense, String key){
		SenseAttribute sa = senseAttribute.getSenseAttributeForName(sense, key);
		if(sa == null || sa.getValue() == null || sa.getValue().getText() == null)
			return null;
		return sa.getValue().getText();
	}
	
	@Override
	public String getSynsetAttribute(Synset synset, String key){
		SynsetAttribute sa = synsetAttribute.getSynsetAttributeForName(synset, key);
		if(sa == null || sa.getValue() == null || sa.getValue().getText() == null)
			return null;
		return sa.getValue().getText();
	}
	
	@Override
	public void saveOrUpdateSenseAttribute(Sense sense, String key, String value){
		senseAttribute.saveOrUpdateSenseAttribute(sense, key, value);
	}
	
	@Override
	public void saveOrUpdateSynsetAttribute(Synset synset, String key, String value){
		synsetAttribute.saveOrUpdateSynsetAttribute(synset, key, value);
	}
	
	@Override
	public void synchronizeAttributeList(Sense sense, List<SenseAttribute> list){
		// step one, take all Attributes attached to first param.
		List<SenseAttribute> oldList = getSenseAttributes(sense);
		
		// step two, remove them.
		for(SenseAttribute s : oldList){
			senseAttribute.removeSenseAttribute(s);
		}
		
		// step three, persist new attributes
		for(SenseAttribute s : list){
			senseAttribute.persistSenseAttribute(s);
		}
		
		// TODO:
		// we can do it better, ie. checking for every change in new list and discard
		// attributes that didn't change, then merge other causing updates / inserts into
		// database. it would recquire far more logic in this method
		// and changes in Sense and SenseAttribute classes as well (hashCode and equals
		// that would need a lot of testing due to Hibernate's nature)
	}
	
	@Override
	public void synchronizeAttributeList(Synset synset, List<SynsetAttribute> list){
		// step one, take all Attributes attached to first param.
		List<SynsetAttribute> oldList = getSynsetAttributes(synset);
		
		// step two, remove them.
		for(SynsetAttribute s : oldList){
			synsetAttribute.removeSynsetAttribute(s);
		}
		
		// step three, persist new attributes
		for(SynsetAttribute s : list){
			synsetAttribute.persistSynsetAttribute(s);
		}
		
		// TODO:
		// we can do it better, ie. checking for every change in new list and discard
		// attributes that didn't change, then merge other causing updates / inserts into
		// database. it would recquire far more logic in this method
		// and changes in Synset and SynsetAttribute classes as well (hashCode and equals
		// that would need a lot of testing due to Hibernate's nature)
	}
	
}
