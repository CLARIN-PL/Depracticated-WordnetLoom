package pl.edu.pwr.wordnetloom.relationtype.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Stateless
public class RelationTypeRepository extends GenericRepository<RelationType> {

    @Inject
    EntityManager em;

    public boolean isReverse(Collection<RelationType> relations, RelationType test) {
        return relations.stream().anyMatch((relation) -> (relation.getAutoReverse() && Objects.equals(relation.getReverse().getId(), test.getId())));
    }

    public void deleteRelationWithChilds(RelationType relation) {
        deleteAllChilds(relation);
        delete(relation);
    }

    public List<RelationType> findChildren(Long relationTypeId) {
        return getEntityManager().createQuery("SELECT rt FROM RelationType rt WHERE rt.parent.id = :parent", RelationType.class)
                .setParameter("parent", relationTypeId)
                .getResultList();
    }

    public void deleteAllChilds(RelationType relationType) {
        getEntityManager().createQuery("DELETE FROM RelationType rt WHERE rt.parent = :parent")
                .setParameter("parent", relationType)
                .executeUpdate();
    }

    public List<RelationType> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RelationType> q = cb.createQuery(RelationType.class);

        Root<RelationType> root = q.from(RelationType.class);
//        root.fetch("parent", JoinType.LEFT);
//        root.fetch("reverse", JoinType.LEFT);

        return em.createQuery(q).getResultList();
    }

    public List<RelationType> findHighestLeafs(RelationArgument arg) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RelationType> q = cb.createQuery(RelationType.class);

        Root<RelationType> root = q.from(RelationType.class);
        Expression<RelationType> parent = root.get("parent");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNull(parent));
        predicates.add(cb.equal(root.get("relationArgument"), arg));

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<RelationType> resultList = em.createQuery(q).getResultList();
        return resultList;
    }

    public List<RelationType> findLeafs(RelationArgument arg) {
        List<RelationType> highs = findHighestLeafs(arg);
        List<RelationType> toReturn = new ArrayList<>();

        highs.forEach((relationType) -> {
            List<RelationType> children = findChildren(relationType.getId());

            if (children == null || children.isEmpty()) {
                toReturn.add(relationType);
            } else {
                toReturn.addAll(children);
            }
        });
        return toReturn;
    }

    public RelationType findReverseByRelationType(Long relationTypeId) {
        return getEntityManager().createQuery("SELECT rt.reverse FROM RelationType rt where rt.id = :id", RelationType.class)
                .setParameter("id", relationTypeId)
                .getSingleResult();
    }

    public Long findReverseId(Long relationTypeId) {
        RelationType r = findById(relationTypeId).getReverse();
        if (r == null) {
            return null;
        }
        return r.getId();
    }

    public RelationType findFullByRelationType(Long relationTypeId) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery(RelationType.class);
        Root o = q.from(RelationType.class);
        o.fetch("parent", JoinType.LEFT);
        o.fetch("reverse", JoinType.LEFT);
        q.select(o);
        q.where(cb.equal(o.get("id"), relationTypeId));

        return (RelationType) getEntityManager()
                .createQuery(q)
                .getSingleResult();
    }

    @Override
    protected Class<RelationType> getPersistentClass() {
        return RelationType.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
