package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.AttributeType;
import pl.edu.pwr.wordnetloom.model.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetAttribute;

@Remote
public interface DynamicAttributesServiceRemote {
	
	/* *******************
	 * SELECTS
	 ******************* */
	
	/* basic AttributeType */
	public List<AttributeType> getAllAttributeTypes();
	public AttributeType getAttributeType(Long index);
	public AttributeType getAttributeType(Text typeName);
	
	/* extended search */
	public List<AttributeType> getAttributeTypesForName(String tableName);
	public List<SenseAttribute> getSenseAttributesForName(Text typeName);
	public List<SynsetAttribute> getSynsetAttributesForName(Text typeName);
	
	/* SenseAttribute */
	public SenseAttribute getSenseAttribute(Long index);
	public List<SenseAttribute> getSenseAttributes(Sense sense);
	
	/* SynsetAttribute */
	public SynsetAttribute getSynsetAttribute(Long index);
	public List<SynsetAttribute> getSynsetAttributes(Synset sense);
	
	public void synchronizeAttributeList(Sense sense, List<SenseAttribute> list);
	public void synchronizeAttributeList(Synset synset, List<SynsetAttribute> list);
	public String getSenseAttribute(Sense sense, String key);
	public String getSynsetAttribute(Synset synset, String key);
	public void saveOrUpdateSenseAttribute(Sense sense, String key, String value);
	public void saveOrUpdateSynsetAttribute(Synset synset, String key, String value);

}
