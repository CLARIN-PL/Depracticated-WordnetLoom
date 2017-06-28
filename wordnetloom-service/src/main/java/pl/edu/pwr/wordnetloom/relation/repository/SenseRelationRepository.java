package pl.edu.pwr.wordnetloom.relation.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.relation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Stateless
public class SenseRelationRepository extends GenericRepository<SenseRelation> {

    @PersistenceContext
    EntityManager em;

    @Override
    public void dbDelete(SenseRelation rel) {
        local.deleteObject(SenseRelation.class, rel.getId());
    }

    @Override
    public void dbDelete(RelationType relation) {
        local.getEM().createNamedQuery("LexicalRelation.dbDelete", SenseRelation.class)
                .setParameter("relation", relation)
                .executeUpdate();
    }

    @Override
    public void dbDeleteAll() {
        local.getEM().createNamedQuery("LexicalRelation.dbDeleteAll", SenseRelation.class)
                .executeUpdate();
    }

    @Override
    public SenseRelation dbGet(Long id) {
        List<SenseRelation> list = local.getEM()
                .createNamedQuery("SenseRelation.findSenseRelationByID", SenseRelation.class)
                .setParameter("id", id)
                .getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<SenseRelation> dbGetSubRelations(Sense sense, RelationType relationType) {
        if (relationType == null) {
            return local.getEM().createNamedQuery("LexicalRelation.dbGetSubRelations", SenseRelation.class)
                    .setParameter("parent", sense)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("LexicalRelation.dbGetSubRelationsWithRelation", SenseRelation.class)
                .setParameter("parent", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }

    @Override
    public List<SenseRelation> dbFastGetRelations(RelationType relationType) {
        if (relationType == null) {
            return local.getEM().createNamedQuery("LexicalRelation.dbFastGetRelations", SenseRelation.class)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("LexicalRelation.dbFastGetRelationsWithRelation", SenseRelation.class)
                .setParameter("relation", relationType)
                .getResultList();
    }

    @Override
    public Set<Long> dbSelftRelations() {
        List<SenseRelation> sr = local.getEM().createNamedQuery("LexicalRelation.dbSelftRelations", SenseRelation.class)
                .getResultList();
        Set<Long> ids = new HashSet<>();
        for (SenseRelation s : sr) {
            ids.add(s.getSenseFrom().getId());
        }
        return ids;
    }

    @Override
    public List<SenseRelation> dbGetUpperRelations(Sense sense, RelationType relationType) {
        if (relationType == null) {
            return local.getEM().createNamedQuery("LexicalRelation.dbGetUpperRelations", SenseRelation.class)
                    .setParameter("child", sense)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("LexicalRelation.dbGetUpperRelationsWithRelation", SenseRelation.class)
                .setParameter("child", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }

    @Override
    public List<SenseRelation> dbGetFullRelations(Sense parent) {
        return dbGetSubRelations(parent, null);
    }

    @Override
    public List<SenseRelation> dbGetRelations(Sense parent, Sense child, RelationType relationType) {
        if (relationType == null) {
            return local.getEM().createNamedQuery("LexicalRelation.dbGetRelations", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("LexicalRelation.dbGetRelationsWithRelation", SenseRelation.class)
                .setParameter("parent", parent)
                .setParameter("child", child)
                .setParameter("relation", relationType)
                .getResultList();
    }

    @Override
    public SenseRelation dbGetRelation(Sense parent, Sense child, RelationType relationType) {
        List<SenseRelation> list = new ArrayList<>();
        if (relationType == null) {
            list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelations", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getResultList();
        } else {
            list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationsWithRelation", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .setParameter("relation", relationType)
                    .getResultList();
        }
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean dbMakeRelation(Sense parent, Sense child, RelationType relation) {
        SenseRelation rel = new SenseRelation();

        rel.setRelation(relation);
        rel.setSenseFrom(parent);
        rel.setSenseTo(child);

        local.persistObject(rel);

        return true;
    }

    @Override
    public void dbDeleteConnection(Sense sense) {
        local.getEM().createNamedQuery("LexicalRelation.dbDeleteConnection")
                .setParameter("senseID", sense.getId())
                .executeUpdate();
    }

    @Override
    public List<SenseRelation> dbFullGetRelations() {
        return dbFastGetRelations(null);
    }

    @Override
    public int dbGetRelationsCount() {
        List<Long> list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationsCount", Long.class).getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    @Override
    public int dbGetRelationUseCount(RelationType relation) {
        List<Long> list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationUseCount", Long.class)
                .setParameter("relation", relation)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    @Override
    public void dbMove(RelationType oldRelation, RelationType newRelation) {
        Query query = local.getEM().createQuery("UPDATE SenseRelation s SET s.relation = :newRelation WHERE s.relation = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    @Override
    public boolean dbRelationExists(Sense parent, Sense child, RelationType relationType) {
        return dbGetRelations(parent, child, relationType).size() > 0;
    }

    @Override
    public List<RelationType> dbGetRelationTypesOfUnit(Sense sense) {
        return local.getEM().createNamedQuery("LexicalRelation.dbGetRelationTypesOfUnit", RelationType.class)
                .setParameter("sense", sense)
                .getResultList();
    }

    @Override
    public int dbGetRelationCountOfUnit(Sense sense) {
        List<Long> list = local.getEM().createNamedQuery("LexicalRelation.dbGetRelationCountOfUnit", Long.class)
                .setParameter("sense", sense)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    @Override
    public int dbDeleteImproper() {
        List<Sense> senses = local.getEM().createNamedQuery("Sense.findAll", Sense.class).getResultList();
        return local.getEM().createNamedQuery("LexicalRelation.dbDeleteImproper", SenseRelation.class)
                .setParameter("senses", senses)
                .executeUpdate();
    }

    @Override // TODO: refactor
    public List<SenseRelation> dbGetRelations(Sense unit, RelationType templateType, int unitType, boolean hideAutoReverse) {

        if (null == unit) {
            return new ArrayList<SenseRelation>();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        String query_tpl = "SELECT distinct sr FROM SenseRelation sr WHERE ";

        if (unitType == SenseRelation.IS_PARENT) {
            query_tpl += " sr.senseFrom.id = :senseID ";
        } else {
            query_tpl += " sr.senseTo.id = :senseID ";
        }
        params.put("senseID", unit.getId());

        if (null != templateType) {
            query_tpl += " AND sr.relation.id = :relationID ";
            params.put("relationID", templateType.getId());
        }

        if (hideAutoReverse) {
            query_tpl += " AND sr.relation.autoReverse = 1 ";
        }

        query_tpl += "ORDER BY sr.relation.id";

        TypedQuery<SenseRelation> q = dao.getEM().createQuery(query_tpl, SenseRelation.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return q.getResultList();
    }

    @Override
    protected Class<SenseRelation> getPersistentClass() {
        return SenseRelation.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
