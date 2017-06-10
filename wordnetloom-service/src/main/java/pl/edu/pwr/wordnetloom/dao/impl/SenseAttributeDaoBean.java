package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.dao.AttributeTypeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.SenseAttributeDaoLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.AttributeType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;

@Stateless
public class SenseAttributeDaoBean implements SenseAttributeDaoLocal {

    @EJB
    private DAOLocal local;

    @EJB
    private AttributeTypeDaoLocal attributeTypeDao;

    @Override
    public SenseAttribute getSenseAttributeForName(Sense sense, String typeName) {

        List<SenseAttribute> list = local.getEM()
                .createNamedQuery("SenseAttribute.getSenseAttributeForName", SenseAttribute.class)
                .setParameter("typeName", typeName)
                .setParameter("sense", sense.getId())
                .getResultList();

        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public List<SenseAttribute> getSenseAttributesForName(Text typeName) {
        return local.getEM()
                .createNamedQuery("SenseAttribute.getForName", SenseAttribute.class)
                .setParameter("typeName", typeName)
                .getResultList();
    }

    @Override
    public SenseAttribute getSenseAttribute(Long index) {
        return local.getObject(SenseAttribute.class, index);
    }

    @Override
    public List<SenseAttribute> getSenseAttributes(Sense sense) {
        return local.getEM()
                .createNamedQuery("SenseAttribute.getForSense", SenseAttribute.class)
                .setParameter("sense", sense)
                .getResultList();
    }

    @Override
    public void removeSenseAttribute(SenseAttribute s) {
        local.deleteObject(s);
    }

    @Override
    public void removeSenseAttribute(Sense s) {
        local.getEM()
                .createNamedQuery("SenseAttribute.deleteBySense")
                .setParameter("senseID", s.getId())
                .executeUpdate();
    }

    @Override
    public void persistSenseAttribute(SenseAttribute s) {
        local.persistObject(s);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void saveOrUpdateSenseAttribute(Sense sense, String key, String value) {
        EntityManager em = local.getEM();
        SenseAttribute senseAttribute = null;

        if (null != sense.getId()) {
            senseAttribute = getSenseAttributeForName(sense, key);//sprawdzamy czy jest taki atrybut dla sensu
        } else {
            Word word = sense.getLemma();

            word = em.merge(word);
            sense.setLemma(word);
            sense.setSenseNumber(1);
            em.persist(sense);
        }

        if (senseAttribute == null) {//	zapisujemy, bo nie ma takiego atrybutu
            AttributeType attributeType = attributeTypeDao.getAttributeTypeByNameAndType("sense", key); //sprawdzamy czy jest taki typ

            if (null == attributeType) {
                Text textKey = attributeTypeDao.getTextByName(key);//sprawdzamy czy jest taki text w tabeli Text
                if (null == textKey) {
                    textKey = new Text(key);
                    em.persist(textKey);
                }

                attributeType = new AttributeType();
                attributeType.setTableName(AttributeType.COLUMN_DEFINITION_SENSE);
                attributeType.setTypeName(textKey);
                em.persist(attributeType);
            }
            senseAttribute = new SenseAttribute();

            Text textValue = new Text(value);
            em.persist(textValue);

            senseAttribute.setSense(sense);
            senseAttribute.setType(attributeType);
            senseAttribute.setValue(textValue);
            em.persist(senseAttribute);

        } else {// update, bo juz jest taki atrybut
            senseAttribute.getValue().setText(value);
            em.merge(senseAttribute);
        }
    }
}
