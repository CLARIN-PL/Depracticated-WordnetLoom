package pl.edu.pwr.wordnetloom.relationtype.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import pl.edu.pwr.wordnetloom.synsetrelation.repository.SynsetRelationRepository;

@Stateless
public class SynsetRelationTypeRepository extends GenericRepository<SynsetRelationType> {

    @Inject
    EntityManager em;

    @Inject
    SynsetRelationRepository synsetRelationRepository;

    public boolean isReverse(SynsetRelationType[] relations, SynsetRelationType test) {
        for (SynsetRelationType relation : relations) {
            if (relation.getAutoReverse() && relation.getReverse().getId().equals(test.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isReverse(Collection<SynsetRelationType> relations, SynsetRelationType test) {
        if (relations.stream().anyMatch((relation) -> (relation.getAutoReverse() && Objects.equals(relation.getReverse().getId(), test.getId())))) {
            return true;
        }
        return false;
    }

    public List<SynsetRelationType> findChildren(SynsetRelationType relation) {
        return getEntityManager().createQuery("SELECT rt FROM SynsetRelationType rt WHERE rt.parent = :parent", SynsetRelationType.class)
                .setParameter("parent", relation)
                .getResultList();
    }

    public void deleteAll() {
        getEntityManager().createQuery("DELETE FROM SynsetRelationType rt WHERE rt.parent = :parent", SynsetRelationType.class)
                .executeUpdate();
    }

    public List<SynsetRelationType> findHighestLeafs(List<Long> lexicons) {
        return getEntityManager().createQuery("FROM SynsetRelationType rt WHERE rt.parent = NULL AND rt.lexicon.id IN (:lexicons)", SynsetRelationType.class)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public List<SynsetRelationType> findLeafs(List<Long> lexicons) {
        List<SynsetRelationType> highs = findHighestLeafs(lexicons);
        List<SynsetRelationType> toReturn = new ArrayList<>();

        highs.stream().forEach((relationType) -> {
            List<SynsetRelationType> children = findChildren(relationType);

            if (children == null || children.isEmpty()) {
                toReturn.add(relationType);
            } else {
                toReturn.addAll(children);
            }
        });
        return toReturn;
    }

    public List<SynsetRelationType> findFullRelationTypes(List<Long> lexicons) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT rt FROM SynsetRelationType rt ")
                .append("left join fetch rt.name ")
                .append("left join fetch rt.description ")
                .append("left join fetch rt.displayText ")
                .append("left join fetch rt.shortDisplayText ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("rt.lexicon.id IN (:lexicons)");

        return getEntityManager().createQuery(query.toString(), SynsetRelationType.class)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public SynsetRelationType findReverseByRelationType(SynsetRelationType relationType) {
        return getEntityManager().createQuery("SELECT rt.reverse FROM SynsetRelationType rt where rt.id = :ID", SynsetRelationType.class)
                .setParameter("ID", relationType.getId())
                .getSingleResult();
    }

    public Long findReverseId(SynsetRelationType relationType) {
        SynsetRelationType r = findById(relationType.getId()).getReverse();
        if (r == null) {
            return null;
        }
        return r.getId();
    }

    public SynsetRelationType findFullByRelationType(SynsetRelationType rt) {
        StringBuilder query = new StringBuilder();
        query.append("select rt from SynsetRelationType rt ")
                .append("left join fetch rt.description ")
                .append("left join fetch rt.displayText ")
                .append("left join fetch rt.shortDisplayText ")
                .append("left join fetch rt.argumentType ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("where rt.id = :ID");
        return getEntityManager().createQuery(query.toString(), SynsetRelationType.class)
                .setParameter("ID", rt.getId()).getSingleResult();
    }

    @Override
    protected Class<SynsetRelationType> getPersistentClass() {
        return SynsetRelationType.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
