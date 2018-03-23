package pl.edu.pwr.wordnetloom.senserelation.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

@Stateless
public class SenseRelationRepository extends GenericRepository<SenseRelation> {

    @Inject
    EntityManager em;

    public void delete(RelationType relation) {
        getEntityManager().createQuery("DELETE FROM SenseRelation s WHERE s.relationType = :relation", SenseRelation.class)
                .setParameter("relation", relation)
                .executeUpdate();
    }

    public void deleteAll() {
        getEntityManager().createQuery("DELETE FROM SenseRelation s", SenseRelation.class)
                .executeUpdate();
    }

    public List<SenseRelation> findSubRelations(Sense sense, RelationType relationType) {
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

    public List<SenseRelation> findRelationByRelationType(RelationType relationType) {
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

    public List<SenseRelation> findUpperRelations(Sense sense, RelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child", SenseRelation.class)
                    .setParameter("child", sense)
                    .getResultList();
        }
        return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.relationType = :relation", SenseRelation.class)
                .setParameter("child", sense)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public List<SenseRelation> findRelations(Sense parent, Sense child, RelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent", SenseRelation.class)
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

    public SenseRelation findRelation(Sense parent, Sense child, RelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .getSingleResult();
        } else {
            return getEntityManager().createQuery("SELECT s FROM SenseRelation s WHERE s.child = :child AND s.parent = :parent AND s.relationType = :relation", SenseRelation.class)
                    .setParameter("parent", parent)
                    .setParameter("child", child)
                    .setParameter("relation", relationType)
                    .getSingleResult();
        }
    }

    public boolean makeRelation(Sense parent, Sense child, RelationType relation) {
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

    public Long findRelationUseCount(RelationType relation) {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM SenseRelation s WHERE s.relationType = :relation", Long.class)
                .setParameter("relation", relation)
                .getSingleResult();
    }

    public void move(RelationType oldRelation, RelationType newRelation) {
        Query query = getEntityManager().createQuery("UPDATE SenseRelation s SET s.relationType = :newRelation WHERE s.relationType = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    public boolean relationExists(Sense parent, Sense child, RelationType relationType) {
        return findRelations(parent, child, relationType).size() > 0;
    }

    public List<RelationType> findRelationTypesBySense(Sense sense) {
        return getEntityManager().createQuery("SELECT r FROM RelationType r, SenseRelation s WHERE s.parent = :sense AND r.id = s.relationType.id", RelationType.class)
                .setParameter("sense", sense)
                .getResultList();
    }

    public Long findRelationCountBySense(Sense sense) {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM SenseRelation s WHERE s.parent = :sense OR s.child = :sense", Long.class)
                .setParameter("sense", sense)
                .getSingleResult();
    }

    public int deleteImproper() {
        List<Sense> senses = getEntityManager().createQuery("SELECT s FROM Sense s", Sense.class)
                .getResultList();
        return getEntityManager().createQuery("DELETE FROM SenseRelation s WHERE s.parent NOT IN ( :senses ) OR s.child NOT IN ( :senses )", SenseRelation.class)
                .setParameter("senses", senses)
                .executeUpdate();
    }

    public List<SenseRelation> findRelations(Sense unit, RelationType relationType, boolean asParent, boolean hideAutoReverse) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(SenseRelation.class);

        Root<SenseRelation> relationRoot = query.from(SenseRelation.class);
        Join<SenseRelation, RelationType> relationTypeJoin = relationRoot.join("relationType");
        String fetchColumn = asParent ? "child" : "parent";

        Fetch<SenseRelation, Sense> senseFetch = relationRoot.fetch(fetchColumn);
        senseFetch.fetch("domain");

        List<Predicate> predicateList = new ArrayList<>();
        Predicate sensePredicate;
        if(asParent) {
            sensePredicate = criteriaBuilder.equal(relationRoot.get("parent"), unit.getId());
        } else {
            sensePredicate = criteriaBuilder.equal(relationRoot.get("child"), unit.getId());
        }
        predicateList.add(sensePredicate);

        if(relationType != null){
            Predicate relationPredicate = criteriaBuilder.equal(relationRoot.get("relationType"), relationType);
            predicateList.add(relationPredicate);
        }
        if(hideAutoReverse){
            Predicate autoReversePredicate = criteriaBuilder.equal(relationTypeJoin.get("autoReverse"), 1);
            predicateList.add(autoReversePredicate);
        }
        query.where(sensePredicate);

        return getEntityManager().createQuery(query).getResultList();
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
