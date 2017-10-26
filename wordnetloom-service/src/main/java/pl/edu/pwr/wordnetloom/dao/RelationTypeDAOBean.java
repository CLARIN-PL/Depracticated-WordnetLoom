package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.RelationTest;
import pl.edu.pwr.wordnetloom.model.RelationType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
public class RelationTypeDAOBean implements RelationTypeDAOLocal {

    @EJB
    private LexicalRelationDAOLocal lexicalRelationDAO;
    @EJB
    private RelationTestDaoLocal testDAO;
    @EJB
    private SynsetRelationDAOLocal synsetRelationDAO;
    @EJB
    private DAOLocal local;

    @Override
    public RelationType dbGet(Long id) {
        return local.getObject(RelationType.class, id);

    }

    @Override
    public boolean isReverseRelation(RelationType[] relations, RelationType test) {
        for (RelationType relation : relations) {
            if (relation.isAutoReverse() && relation.getReverse().getId() == test.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isReverseRelation(Collection<RelationType> relations, RelationType test) {
        for (RelationType relation : relations) {
            if (relation.isAutoReverse() && relation.getReverse().getId() == test.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dbDelete(RelationType relation, List<Long> lexicons) {
        //Removes relation with subrelations
        Collection<RelationType> children = dbGetChildren(relation, lexicons);
        for (RelationType item : children) {
            item = dbGet(item.getId());
            lexicalRelationDAO.dbDelete(item);
            synsetRelationDAO.dbDelete(item);
            local.deleteObject(item);
        }
        RelationType relationType = dbGet(relation.getId());

        lexicalRelationDAO.dbDelete(relationType);
        synsetRelationDAO.dbDelete(relationType);
        local.deleteObject(relationType);
    }

    @Override
    public List<RelationType> dbGetChildren(RelationType relation, List<Long> lexicons) {
        return local.getEM().createNamedQuery("RelationType.dbGetChildren", RelationType.class)
                .setParameter("parent", relation)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Override
    public List<RelationType> dbGetChildrenFull(RelationType relation, List<Long> lexicons) {
        return local.getEM().createNamedQuery("RelationType.dbGetChildrenFull", RelationType.class)
                .setParameter("parent", relation)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Deprecated // TODO: nich na razie będzie, do usunięcia skoro mamy obiekty
    @Override
    public List<RelationTest> dbGetTests(RelationType rel) {
        return testDAO.getRelationTestsFor(rel);
    }

    @Override
    public void dbDeleteAll() {
        local.getEM().createNamedQuery("RelationType.dbDeleteAll", RelationType.class)
                .executeUpdate();
    }

    @Override
    public List<RelationType> dbGetHighest(RelationArgument argument, PartOfSpeech pos, List<Long> lexicons) {
        //zgodnie z odpowiedzia klienta w tym etapie nie wyszukujemy po pos (pomijamy ten argument)
        if (argument == null) {
            return local.getEM().createNamedQuery("RelationType.dbGetHighest", RelationType.class)
                    .setParameter("lexicons", lexicons)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("RelationType.dbGetHighestArgument", RelationType.class)
                .setParameter("argument", getIdRelationArgumentByName(argument.getName().getText()))
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Override
    public List<RelationType> dbGetLeafs(RelationArgument argument, List<Long> lexicons) {
        List<RelationType> highs = dbGetHighest(argument, null, lexicons);
        List<RelationType> toReturn = new ArrayList<>();

        for (RelationType relationType : highs) {
            List<RelationType> children = dbGetChildrenFull(relationType, lexicons);

            if (children == null || children.isEmpty()) {
                relationType = getEagerRelationTypeByID(relationType);
                toReturn.add(relationType);
            } else {
                toReturn.addAll(children);
            }
        }
        return toReturn;
    }

    @Override
    public List<RelationType> dbFullGetRelationTypes(List<Long> lexicons) {
        return local.getEM().createNamedQuery("RelationType.dbFullGetRelationTypes", RelationType.class)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    // TODO: check me
    @Override
    public RelationType dbGetReverseByRelationType(RelationType relationType) {
        TypedQuery<RelationType> tq = local.getEM().
                createNamedQuery("RelationType.dbGetReverseByRelationTypeID", RelationType.class)
                .setParameter("ID", relationType.getId());
        try {
            return tq.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();

            return relationType;
        }
    }

    @Override
    public Long getReverseID(RelationType relationType) {
        RelationType r = dbGet(relationType.getId()).getReverse();
        if (r == null)
            return null;
        return r.getId();
    }

    private Long getIdRelationArgumentByName(String nazwa) {
        if (nazwa.equals(RelationArgument.LEXICAL.getName().getText())) {
            return RelationArgument.LEXICAL.getId();
        } else if (nazwa.equals(RelationArgument.SYNSET.getName().getText())) {
            return RelationArgument.SYNSET.getId();
        } else if (nazwa.equals(RelationArgument.LEXICAL_SPECIAL.getName().getText())) {
            return RelationArgument.LEXICAL_SPECIAL.getId();
        }
        return -1L;
    }

    @Override
    public RelationType getEagerRelationTypeByID(RelationType rt) {
        StringBuilder query = new StringBuilder();
        query.append("select rt from RelationType rt ")
                .append("left join fetch rt.description ")
                .append("left join fetch rt.displayText ")
                .append("left join fetch rt.shortDisplayText ")
                .append("left join fetch rt.argumentType ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("where rt.id = :ID");
        TypedQuery<RelationType> q = local.getEM().createQuery(query.toString(), RelationType.class);
        try {
            RelationType result = q.setParameter("ID", rt.getId()).getSingleResult();
            return result;
        } catch (Exception e) {
            return rt;
        }
    }

    @Override
    public RelationType save(RelationType rel) {
        local.getEM().persist(rel);
        return rel;
    }
}
