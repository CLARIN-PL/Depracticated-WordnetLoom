package pl.edu.pwr.wordnetloom.relationtype.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationRepository;

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

    @Inject
    SenseRelationRepository senseRelationRepository;

    public boolean isReverse(Collection<RelationType> relations, RelationType test) {
        if (relations.stream().anyMatch((relation) -> (relation.getAutoReverse() && Objects.equals(relation.getReverse().getId(), test.getId())))) {
            return true;
        }
        return false;
    }

    public void deleteRelationWithChilds(RelationType relation) {
        deleteAllChilds(relation);
        delete(relation);
    }

    public List<RelationType> findChildren(RelationType relation) {
        return getEntityManager().createQuery("SELECT rt FROM RelationType rt WHERE rt.parent.id = :parent", RelationType.class)
                .setParameter("parent", relation.getId())
                .getResultList();
    }

    public void deleteAllChilds(RelationType relationType) {
        getEntityManager().createQuery("DELETE FROM RelationType rt WHERE rt.parent = :parent")
                .setParameter("parent", relationType)
                .executeUpdate();
    }

    public List<RelationType> findHighestLeafs(List<Long> lexicons) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RelationType> q = cb.createQuery(RelationType.class);

        Root<RelationType> root = q.from(RelationType.class);
        Expression<RelationType> parent = root.get("parent");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNull(parent));

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    public List<RelationType> findLeafs(List<Long> lexicons) {
        List<RelationType> highs = findHighestLeafs(lexicons);
        List<RelationType> toReturn = new ArrayList<>();

        highs.stream().forEach((relationType) -> {
            List<RelationType> children = findChildren(relationType);

            if (children == null || children.isEmpty()) {
                toReturn.add(relationType);
            } else {
                toReturn.addAll(children);
            }
        });
        return toReturn;
    }

    public List<RelationType> findFullRelationTypes(List<Long> lexicons) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT rt FROM RelationType rt ")
                .append("left join fetch rt.nameStrings ")
                .append("left join fetch rt.description ")
                .append("left join fetch rt.displayText ")
                .append("left join fetch rt.shortDisplayText ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("rt.lexicons.id IN (:lexicons)");

        return getEntityManager().createQuery(query.toString(), RelationType.class)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public RelationType findReverseByRelationType(RelationType relationType) {
        return getEntityManager().createQuery("SELECT rt.reverse FROM RelationType rt where rt.id = :id", RelationType.class)
                .setParameter("id", relationType.getId())
                .getSingleResult();
    }

    public Long findReverseId(RelationType relationType) {
        RelationType r = findById(relationType.getId()).getReverse();
        if (r == null) {
            return null;
        }
        return r.getId();
    }

    //TODO refaktor
    public RelationType findFullByRelationType(RelationType rt) {

        StringBuilder query = new StringBuilder();

        query.append("select rt from RelationType rt ")
                .append("left join fetch rt.nameStrings ")
                .append("left join fetch rt.descriptionStrings ")
                .append("left join fetch rt.displayStrings ")
                .append("left join fetch rt.shortDisplayStrings ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("where rt.id = :id");

        return getEntityManager().createQuery(query.toString(), RelationType.class)
                .setParameter("id", rt.getId()).getSingleResult();
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
