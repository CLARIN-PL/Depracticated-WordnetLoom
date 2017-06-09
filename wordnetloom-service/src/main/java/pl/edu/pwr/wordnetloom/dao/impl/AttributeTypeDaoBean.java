package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.AttributeTypeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.AttributeType;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

@Stateless
public class AttributeTypeDaoBean implements AttributeTypeDaoLocal {

    @EJB
    private DAOLocal local;

    @Override
    public List<AttributeType> getAllAttributeTypes() {
        return local.getAllObjects(AttributeType.class);
    }

    @Override
    public AttributeType getAttributeType(Long index) {
        return local.getObject(AttributeType.class, index);
    }

    @Override
    public List<AttributeType> getAttributeTypesForName(String tableName) {
        return local.getEM()
                .createNamedQuery("AttributeType.findByName", AttributeType.class)
                .setParameter("tableName", tableName)
                .getResultList();
    }

    @Override
    public AttributeType getAttributeType(Text typeName) {
        List<AttributeType> list = local.getEM()
                .createNamedQuery("AttributeType.findByTypeName", AttributeType.class)
                .setParameter("typeName", typeName).getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public AttributeType getAttributeTypeByName(String name) {
        List<AttributeType> list = local.getEM()
                .createNamedQuery("AttributeType.AttributeTypeByName", AttributeType.class)
                .setParameter("name", name).getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public AttributeType getAttributeTypeByNameAndType(String name, String type) {
        List<AttributeType> list = local.getEM()
                .createNamedQuery("AttributeType.AttributeTypeByNameAndType", AttributeType.class)
                .setParameter("name", name)
                .setParameter("type", type)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Text getTextByName(String name) {
        List<Text> list = local.getEM()
                .createNamedQuery("Text.findByTextName", Text.class)
                .setParameter("name", name).getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

}
