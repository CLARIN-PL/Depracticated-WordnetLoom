package pl.edu.pwr.wordnetloom.relationtype.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationRepository;

@Stateless
public class SenseRelationTypeRepository extends GenericRepository<SenseRelationType> {

    @Inject
    EntityManager em;

    @Inject
    SenseRelationRepository senseRelationRepository;

    public boolean isReverse(SenseRelationType[] relations, SenseRelationType test) {
        for (SenseRelationType relation : relations) {
            if (relation.getAutoReverse() && relation.getReverse().getId().equals(test.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isReverse(Collection<SenseRelationType> relations, SenseRelationType test) {
        if (relations.stream().anyMatch((relation) -> (relation.getAutoReverse() && Objects.equals(relation.getReverse().getId(), test.getId())))) {
            return true;
        }
        return false;
    }

    public void delete(SenseRelationType relation) {
        //Removes relation with subrelations
        Collection<SenseRelationType> children = findChildren(relation);
        for (SenseRelationType item : children) {
            senseRelationRepository.delete(relation);
            delete(item);
        }
        super.delete(relation);
    }

    public List<SenseRelationType> findChildren(SenseRelationType relation) {
        return getEntityManager().createQuery("SELECT rt FROM SenseRelationType rt WHERE rt.parent = :parent", SenseRelationType.class)
                .setParameter("parent", relation)
                .getResultList();
    }

    public void deleteAll() {
        getEntityManager().createQuery("DELETE FROM SenseRelationType rt WHERE rt.parent = :parent", SenseRelationType.class)
                .executeUpdate();
    }

    public List<SenseRelationType> findHighestLeafs(List<Long> lexicons) {
        return getEntityManager().createQuery("FROM SenseRelationType rt WHERE rt.parent = NULL AND rt.lexicon.id IN (:lexicons)", SenseRelationType.class)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public List<SenseRelationType> findLeafs(List<Long> lexicons) {
        List<SenseRelationType> highs = findHighestLeafs(lexicons);
        List<SenseRelationType> toReturn = new ArrayList<>();

        highs.stream().forEach((relationType) -> {
            List<SenseRelationType> children = findChildren(relationType);

            if (children == null || children.isEmpty()) {
                toReturn.add(relationType);
            } else {
                toReturn.addAll(children);
            }
        });
        return toReturn;
    }

    public List<SenseRelationType> findFullRelationTypes(List<Long> lexicons) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT rt FROM SenseRelationType rt ")
                .append("left join fetch rt.name ")
                .append("left join fetch rt.description ")
                .append("left join fetch rt.displayText ")
                .append("left join fetch rt.shortDisplayText ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("rt.lexicon.id IN (:lexicons)");

        return getEntityManager().createQuery(query.toString(), SenseRelationType.class)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public SenseRelationType findReverseByRelationType(SenseRelationType relationType) {
        return getEntityManager().createQuery("SELECT rt.reverse FROM SenseRelationType rt where rt.id = :ID", SenseRelationType.class)
                .setParameter("ID", relationType.getId())
                .getSingleResult();
    }

    public Long findReverseId(SenseRelationType relationType) {
        SenseRelationType r = findById(relationType.getId()).getReverse();
        if (r == null) {
            return null;
        }
        return r.getId();
    }

    public SenseRelationType findFullByRelationType(SenseRelationType rt) {
        StringBuilder query = new StringBuilder();
        query.append("select rt from SenseRelationType rt ")
                .append("left join fetch rt.description ")
                .append("left join fetch rt.displayText ")
                .append("left join fetch rt.shortDisplayText ")
                .append("left join fetch rt.argumentType ")
                .append("left join fetch rt.parent ")
                .append("left join fetch rt.reverse ")
                .append("where rt.id = :ID");
        return getEntityManager().createQuery(query.toString(), SenseRelationType.class)
                .setParameter("ID", rt.getId()).getSingleResult();
    }

    @Override
    protected Class<SenseRelationType> getPersistentClass() {
        return SenseRelationType.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
