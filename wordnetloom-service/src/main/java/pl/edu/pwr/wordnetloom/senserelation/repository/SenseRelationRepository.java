package pl.edu.pwr.wordnetloom.senserelation.repository;

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
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Stateless
public class SenseRelationRepository extends GenericRepository<SenseRelation> {

    @PersistenceContext
    EntityManager em;

    public void delete(SenseRelationType relation) {
        getEntityManager().createQuery("DELETE FROM SenseRelation s WHERE s.relationType = :relation", SenseRelation.class)
                .setParameter("relation", relation)
                .executeUpdate();
    }

    public void deleteAll() {
        getEntityManager().createQuery("DELETE FROM SenseRelation s", SenseRelation.class)
                .executeUpdate();
    }

    public List<SenseRelation> findSubRelations(Sense sense, SenseRelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.parent = :parent", SenseRelation.class)
                    .setParameter("parent", sense)
                    .getResultList();
        }
        return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.parent = :parent AND s.relationType = :relation", SenseRelation.class)
                .setParameter("parent", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public List<SenseRelation> findRelationByRelationType(SenseRelationType relationType) {
        if (relationType == null) {
            return findAll("id");
        }
        return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.relationType = :relation", SenseRelation.class)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public Set<Long> findSelfRelations() {
        return new HashSet<>(getEntityManager().createQuery("SELECT s.parent.id FROM SenseRelation s WHERE s.parent = s.child", Long.class)
                .getResultList());
    }

    public List<SenseRelation> findUpperRelations(Sense sense, SenseRelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createNamedQuery("SELECT s FROM SenseRelation s WHERE s.child = :child", SenseRelation.class)
                    .setParameter("child", sense)
                    .getResultList();
        }
        return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.relationType = :relation", SenseRelation.class)
                .setParameter("child", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public List<SenseRelation> findRelations(Sense parent, Sense child, SenseRelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getResultList();
        }
        return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent AND s.relationType = :relation", SenseRelation.class)
                .setParameter("parent", parent)
                .setParameter("child", child)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public SenseRelation findRelation(Sense parent, Sense child, SenseRelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getSingleResult();
        } else {
            return getEntityManager().createNamedQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent AND s.relationType = :relation", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .setParameter("relation", relationType)
                    .getSingleResult();
        }
    }

    public boolean makeRelation(Sense parent, Sense child, SenseRelationType relation) {
        SenseRelation rel = new SenseRelation(relation, parent, child);
        persist(rel);
        return true;
    }

    public void deleteConnection(Sense sense) {
        getEntityManager().createQuery("DELETE FROM SenseRelation s WHERE s.parent.id = :senseID OR s.child.id = :senseID")
                .setParameter("senseID", sense.getId())
                .executeUpdate();
    }

    public Long findAllRelationsCount() {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM SenseRelation s", Long.class)
                .getSingleResult();
    }

    public Long findRelationUseCount(SenseRelationType relation) {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM SenseRelation s WHERE s.relationType = :relation", Long.class)
                .setParameter("relation", relation)
                .getSingleResult();
    }

    public void move(SenseRelationType oldRelation, SenseRelationType newRelation) {
        Query query = getEntityManager().createQuery("UPDATE SenseRelation s SET s.relationType = :newRelation WHERE s.relationType = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    public boolean relationExists(Sense parent, Sense child, SenseRelationType relationType) {
        return findRelations(parent, child, relationType).size() > 0;
    }

    public List<SenseRelationType> findRelationTypesBySense(Sense sense) {
        return getEntityManager().createQuery("SELECT r FROM SenseRelationType r, SenseRelation s WHERE s.parent = :sense AND r.id = s.relationType.id", SenseRelationType.class)
                .setParameter("sense", sense)
                .getResultList();
    }

    public Long findRelationCountBySense(Sense sense) {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM SenseRelation s WHERE s.parent = :sense OR s.child = :sense", Long.class)
                .setParameter("sense", sense)
                .getSingleResult();
    }

    public int deleteImproper() {
        List<Sense> senses = getEntityManager().createNamedQuery("SELECT s FROM Sense s", Sense.class)
                .getResultList();
        return getEntityManager().createQuery("DELETE FROM SenseRelation s WHERE s.parent NOT IN ( :senses ) OR s.child NOT IN ( :senses )", SenseRelation.class)
                .setParameter("senses", senses)
                .executeUpdate();
    }

    public List<SenseRelation> findRelations(Sense unit, SenseRelationType relationType, boolean asParent, boolean hideAutoReverse) {

        if (null == unit) {
            return new ArrayList<>();
        }

        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT distinct sr FROM SenseRelation sr WHERE ");

        if (asParent) {
            query.append(" sr.parent.id = :senseID ");
        } else {
            query.append(" sr.child.id = :senseID ");
        }
        params.put("senseID", unit.getId());

        if (null != relationType) {
            query.append(" AND sr.relation.id = :relationID ");
            params.put("relationID", relationType.getId());
        }

        if (hideAutoReverse) {
            query.append(" AND sr.relation.autoReverse = 1 ");
        }

        query.append("ORDER BY sr.relation.id");

        TypedQuery<SenseRelation> q = getEntityManager().createQuery(query.toString(), SenseRelation.class);

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
