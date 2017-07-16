package pl.edu.pwr.wordnetloom.synsetrelation.repository;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

@Stateless
public class SynsetRelationRepository extends GenericRepository<SynsetRelation> {

    @Inject
    EntityManager em;

    public boolean delete(Synset parent, Synset child, SynsetRelationType relationType) {
        Query query = getEntityManager().createQuery("DELETE FROM SynsetRelation s WHERE s.parent.id = :parent AND s.child.id = :child AND s.relationType.id = :relationType", SynsetRelation.class);
        query.setParameter("synsetFrom", parent.getId())
                .setParameter("synsetTo", child.getId())
                .setParameter("relation", relationType.getId());

        return query.executeUpdate() > 0;
    }

    public void delete(SynsetRelationType relationType) {
        getEntityManager().createQuery("DELETE FROM SynsetRelation s WHERE s.relationType.id = :relationType", SynsetRelation.class)
                .setParameter("relationType", relationType.getId())
                .executeUpdate();
    }

    public void deleteAll() {
        getEntityManager().createQuery("DELETE FROM SynsetRelation", SynsetRelation.class)
                .executeUpdate();
    }

    public List<SynsetRelation> findSubRelations(Synset synset, SynsetRelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("SELECT s FROM SynsetRelation s WHERE s.parent = :parent", SynsetRelation.class)
                    .setParameter("parent", synset)
                    .getResultList();
        }
        return getEntityManager().createQuery("SELECT s FROM SynsetRelation s WHERE s.parent = :parent AND s.relationType = :relation", SynsetRelation.class)
                .setParameter("parent", synset)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public List<SynsetRelation> findUpperRelations(Synset synset, SynsetRelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createNamedQuery("SELECT s FROM SynsetRelation s WHERE s.child = :child", SynsetRelation.class)
                    .setParameter("child", synset)
                    .getResultList();
        }
        return getEntityManager().createQuery("SELECT s FROM SynsetRelation s WHERE s.child = :child AND s.relationType = :relation", SynsetRelation.class)
                .setParameter("child", synset)
                .setParameter("relation", relationType)
                .getResultList();
    }

    public void deleteConnection(Synset synset) {
        getEntityManager().createQuery("DELETE FROM SynsetRelation s WHERE s.parent.id = :id OR s.child.id = :id")
                .setParameter("id", synset.getId())
                .executeUpdate();
    }

    public Long findAllRelationsCount() {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM SynsetRelation s", Long.class)
                .getSingleResult();
    }

    public Long findRelationTypeUseCount(SynsetRelationType relation) {
        return getEntityManager().createQuery("SELECT COUNT(sr) FROM SynsetRelation sr WHERE sr.relationType.id = :relation ", Long.class)
                .setParameter("relation", relation.getId()).getSingleResult();
    }

    public void move(SynsetRelationType oldRelation, SynsetRelationType newRelation) {
        Query query = getEntityManager().createQuery("UPDATE SynsetRelation s SET s.relationType= :newRelation WHERE s.relationType = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    public boolean checkRelationExists(Synset parent, Synset child, SynsetRelationType relation) {
        return findRelations(parent, child, relation).size() > 0;
    }

    public List<SynsetRelationType> findtRelationTypesBySynset(Synset synset) {
        return getEntityManager().createQuery("SELECT sr.relationType FROM SynsetRelation sr WHERE sr.parent.id = :id OR sr.child.id = :id", SynsetRelationType.class)
                .setParameter("id", synset.getId())
                .getResultList();
    }

    public int deleteImproper() {
        List<Synset> synsets = getEntityManager().createNamedQuery("SELECT s FROM Synset s", Synset.class)
                .getResultList();
        return getEntityManager().createQuery("DELETE FROM SynsetRelation s WHERE s.parent NOT IN ( :synsets ) OR s.child NOT IN ( :synsets )", SynsetRelation.class)
                .setParameter("synsets", synsets)
                .executeUpdate();
    }

    public List<SynsetRelation> findRelations(Synset parent, Synset child, SynsetRelationType relation) {
        return getEntityManager().createQuery("SELECT sr FROM SynsetRelation sr WHERE sr.parent.id = :parent AND sr.child = : child AND sr.relationType.id = :relation", SynsetRelation.class)
                .setParameter("parent", parent.getId())
                .setParameter("child", child.getId())
                .setParameter("relation", relation.getId())
                .getResultList();
    }

    public SynsetRelation findRelation(Synset parent, Synset child, SynsetRelationType relation) {
        List<SynsetRelation> relations = findRelations(parent, child, relation);
        if (relations.isEmpty() || relations.get(0) == null) {
            return null;
        }
        return relations.get(0);
    }

    public Long findRelationCountBySynset(Synset synset) {
        return getEntityManager().createNamedQuery("SynsetRelation.countSynsetRelations", Long.class)
                .setParameter("synsetID", synset.getId())
                .getSingleResult();
    }

    /**
     * Returns a path to the top synset in relation graph.
     *
     * @param synset start synset
     * @param rtype relation type
     * @return path to the top synset
     */
    // TODO: refactoring is needed
    // FIXME: move to service, logic overload!
    public List<Long> findTopPath(Synset synset, Long rtype) {
        ArrayList<Long> path = new ArrayList<>();

        DirectedGraph<Long, SynsetRelation> g = new DirectedSparseGraph<>();

        g.addVertex(synset.getId());
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(synset.getId());
        while (!stack.isEmpty()) {
            Long item = stack.pop();

            List<SynsetRelation> rels = getEntityManager().createNamedQuery("SynsetRelation.dbGetTopPath", SynsetRelation.class)
                    .setParameter("id_s", item)
                    .setParameter("id_r", rtype)
                    .getResultList();

            for (SynsetRelation rel : rels) {
                stack.push(rel.getChild().getId());
                g.addEdge(rel, item, rel.getChild().getId());
            }
        }

        DijkstraShortestPath<Long, SynsetRelation> dsp
                = new DijkstraShortestPath<>(g);

        Map<Long, Number> map = dsp.getDistanceMap(synset.getId());
        Long last = new Long(-1);
        for (Entry<Long, Number> p : map.entrySet()) {
            last = p.getKey();
        }

        if (last != -1) {
            List<SynsetRelation> p = dsp.getPath(synset.getId(), last);
            for (SynsetRelation r : p) {
                path.add(r.getChild().getId());
            }
        }
        return path;
    }

    public List<Synset> findTopPathInSynsets(Synset synset, Long rtype) {
        ArrayList<Synset> path = new ArrayList<>();

        DirectedGraph<Long, SynsetRelation> g = new DirectedSparseGraph<>();

        g.addVertex(synset.getId());
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(synset.getId());

        while (!stack.isEmpty()) {
            Long item = stack.pop();

            List<SynsetRelation> rels = getEntityManager().createNamedQuery("SynsetRelation.dbGetTopPath", SynsetRelation.class)
                    .setParameter("id_s", item)
                    .setParameter("id_r", rtype)
                    .getResultList();

            for (SynsetRelation rel : rels) {
                stack.push(rel.getChild().getId());
                g.addEdge(rel, item, rel.getChild().getId());
            }
        }

        DijkstraShortestPath<Long, SynsetRelation> dsp
                = new DijkstraShortestPath<>(g);

        Map<Long, Number> map = dsp.getDistanceMap(synset.getId());
        Long last = new Long(-1);
        for (Entry<Long, Number> p : map.entrySet()) {
            last = p.getKey();
        }

        if (last != -1) {
            List<SynsetRelation> p = dsp.getPath(synset.getId(), last);
            for (SynsetRelation r : p) {
                path.add(r.getParent());
            }
        }
        return path;
    }

    public List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset) {
        return getEntityManager().createNamedQuery("FROM SynsetRelation sr WHERE sr.child.id = :id", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .getResultList();
    }

    public List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset) {
        return getEntityManager().createNamedQuery("FROM SynsetRelation sr WHERE sr.parent.id = :id", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .getResultList();
    }

    @Override
    protected Class<SynsetRelation> getPersistentClass() {
        return SynsetRelation.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
