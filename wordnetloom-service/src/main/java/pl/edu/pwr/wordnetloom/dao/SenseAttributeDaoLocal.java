package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.model.Sense;

@Local
public interface SenseAttributeDaoLocal {

	public SenseAttribute getSenseAttributeForName(Sense sense, String typeName);

	public List<SenseAttribute> getSenseAttributesForName(Text typeName);

	public SenseAttribute getSenseAttribute(Long index);
	public List<SenseAttribute> getSenseAttributes(Sense sense);

	public void removeSenseAttribute(SenseAttribute s);

	public void removeSenseAttribute(Sense s) ;

	public void persistSenseAttribute(SenseAttribute s);

	public void saveOrUpdateSenseAttribute(Sense sense, String key, String value);

}
