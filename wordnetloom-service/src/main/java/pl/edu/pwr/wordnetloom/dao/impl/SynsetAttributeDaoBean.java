package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.dao.AttributeTypeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetAttributeDaoLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.AttributeType;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import pl.edu.pwr.wordnetloom.model.wordnet.SynsetAttribute;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;

@Stateless
public class SynsetAttributeDaoBean implements SynsetAttributeDaoLocal {

    @EJB
    private DAOLocal local;

    @EJB
    private AttributeTypeDaoLocal attributeTypeDao;

    @Override
    public SynsetAttribute getSynsetAttributeForName(Synset synset, String typeName) {
        List<SynsetAttribute> list = local.getEM()
                .createNamedQuery("SynsetAttribute.getSynsetAttributeForName", SynsetAttribute.class)
                .setParameter("typeName", typeName)
                .setParameter("synset", synset.getId())
                .getResultList();

        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public List<SynsetAttribute> getSynsetAttributesForName(Text typeName) {
        return local.getEM()
                .createNamedQuery("SynsetAttribute.getForName", SynsetAttribute.class)
                .setParameter("typeName", typeName)
                .getResultList();
    }

    @Override
    public SynsetAttribute getSynsetAttribute(Long index) {
        return local.getObject(SynsetAttribute.class, index);
    }

    @Override
    public List<SynsetAttribute> getSynsetAttributes(Synset synset) {
        local.mergeObject(synset);
        return local.getEM()
                .createNamedQuery("SynsetAttribute.getForSynset", SynsetAttribute.class)
                .setParameter("synset", synset)
                .getResultList();
    }

    @Override
    public void removeSynsetAttribute(SynsetAttribute s) {
        local.deleteObject(s);
    }

    @Override
    public void persistSynsetAttribute(SynsetAttribute s) {
        local.persistObject(s);
    }

    @Override
    public void saveOrUpdateSynsetAttribute(Synset synset, String key, String value) {
        EntityManager em = local.getEM();
        SynsetAttribute synsetAttribute = null;

        if (null != synset.getId()) {
            synsetAttribute = getSynsetAttributeForName(synset, key);
        }

        if (synsetAttribute == null) {//	zapisujemy, bo nie ma takiego atrybutu
            AttributeType attributeType = attributeTypeDao.getAttributeTypeByNameAndType(AttributeType.COLUMN_DEFINITION_SYNSET, key); //sprawdzamy czy jest taki typ

            if (null == attributeType) {
                Text textKey = attributeTypeDao.getTextByName(key);//sprawdzamy czy jest taki text tabeli Text
                if (null == textKey) {
                    textKey = new Text(key);
                    em.persist(textKey);
                }

                attributeType = new AttributeType();
                attributeType.setTableName(AttributeType.COLUMN_DEFINITION_SYNSET);
                attributeType.setTypeName(textKey);
                em.persist(attributeType);
            }
            synsetAttribute = new SynsetAttribute();

            Text textValue = new Text(value);
            em.persist(textValue);

            synsetAttribute.setSynset(synset);
            synsetAttribute.setType(attributeType);
            synsetAttribute.setValue(textValue);
            em.persist(synsetAttribute);

        } else {// update, bo juz jest taki atrybut
            Text textValue = new Text(value);
            em.persist(textValue);

            synsetAttribute.setValue(textValue);
            em.merge(synsetAttribute);
        }
    }

    @Override
    public void deleteAttributesFor(Synset synset) {
        local.getEM().createNamedQuery("SynsetAttribute.deleteForSynset").setParameter("synset", synset.getId()).executeUpdate();
    }

}
