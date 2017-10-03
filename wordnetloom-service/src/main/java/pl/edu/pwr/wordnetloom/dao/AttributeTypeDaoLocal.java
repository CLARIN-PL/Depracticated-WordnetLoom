package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.model.AttributeType;

@Local
public interface AttributeTypeDaoLocal {

	List<AttributeType> getAllAttributeTypes();
	AttributeType getAttributeType(Long index);
	List<AttributeType> getAttributeTypesForName(String tableName);
	AttributeType getAttributeType(Text typeName);
	Text getTextByName(String name);
	AttributeType getAttributeTypeByName(String name);
	AttributeType getAttributeTypeByNameAndType(String name, String type);
}
