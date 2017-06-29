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
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class SynsetRelationRepository extends GenericRepository<SynsetRelation> {

    @PersistenceContext
    EntityManager em;

    @Override
    public void dbDelete(SynsetRelation rel) {
        RelationType relation = rel.getRelation();

        if (relation.isAutoReverse()) {
            RelationType reverse = relationType.dbGetReverseByRelationType(relation);
            dbDelete(rel.getSynsetTo(), rel.getSynsetFrom(), reverse);
        }

        try {
            local.deleteObject(SynsetRelation.class, rel.getId());
        } catch (Exception e) {
            System.err.println(this.getClass() + ": WARRNING: " + e.getLocalizedMessage());
        }
    }

    @Override
    public boolean dbDelete(Synset parent, Synset child, RelationType relation) {
        Query q = local.getEM().createNamedQuery("SynsetRelation.dbDelete");

        Query qp = q
                .setParameter("synsetFrom", parent.getId())
                .setParameter("synsetTo", child.getId())
                .setParameter("relation", relation.getId());

        return qp.executeUpdate() > 0;
    }

    @Override
    public void dbDelete(RelationType relation) {
        local.getEM().createNamedQuery("SynsetRelation.dbDeleteByRelation", SynsetRelation.class)
                .setParameter("relation", relation.getId())
                .executeUpdate();
    }

    public SynsetRelation dbGet(Long id) {
        List<SynsetRelation> list = local.getEM()
                .createNamedQuery("SynsetRelation.dbGetSynsetRelationByID", SynsetRelation.class)
                .setParameter("id", id)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean dbMakeRelation(Synset parent, Synset child, RelationType rel) {
        SynsetRelation s = new SynsetRelation();
        s.setRelation(rel);
        s.setSynsetFrom(parent);
        s.setSynsetTo(child);
        local.persistObject(s);
        return true;
    }

    @Override
    public void dbDeleteAll() {
        local.getEM().createNamedQuery("SynsetRelation.dbDeleteAll", SynsetRelation.class)
                .executeUpdate();
    }

    @Override
    public List<SynsetRelation> dbGetSubRelations(Synset synsetFrom, RelationType relation, List<Long> lexicons) {
        if (relation == null) {
            return local.getEM().createNamedQuery("SynsetRelation.dbGetSubRelations", SynsetRelation.class)
                    .setParameter("synsetFrom", synsetFrom.getId())
                    .setParameter("lexicons", lexicons)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("SynsetRelation.dbGetSubRelationsWithRelation", SynsetRelation.class)
                .setParameter("synsetFrom", synsetFrom.getId())
                .setParameter("relation", relation.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Override
    public List<SynsetRelation> dbGetUpperRelations(Synset synsetTo, RelationType relation, List<Long> lexicons) {
        if (relation == null) {
            return local.getEM().createNamedQuery("SynsetRelation.dbGetUpperRelations", SynsetRelation.class)
                    .setParameter("synsetTo", synsetTo.getId())
                    .setParameter("lexicons", lexicons)
                    .getResultList();
        }
        return local.getEM().createNamedQuery("SynsetRelation.dbGetUpperRelationsWithRelation", SynsetRelation.class)
                .setParameter("synsetTo", synsetTo.getId())
                .setParameter("relation", relation.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Override
    public void dbDeleteConnection(Synset synset) {
        local.getEM().createNamedQuery("SynsetRelation.dbDeleteConnection")
                .setParameter("synset", synset.getId())
                .executeUpdate();
    }

    @Override
    public List<SynsetRelation> dbFullGetRelations() {
        return local.getEM().createNamedQuery("SynsetRelation.dbFullGetRelations", SynsetRelation.class)
                .getResultList();
    }

    // FIXME: używamy dbFullGetRelations, nie mamy własnego `orm`
    @Override
    public List<SynsetRelation> dbFastGetRelations(RelationType templateType) {
        return dbFullGetRelations();
    }

    @Override
    public int dbGetRelationsCount() {
        List<Long> list = local.getEM().createNamedQuery("SynsetRelation.dbGetRelationsCount", Long.class)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    @Override
    public int dbGetRelationUseCount(RelationType relation) {
        List<Long> list = local.getEM().createNamedQuery("SynsetRelation.dbGetRelationUseCount", Long.class)
                .setParameter("relation", relation)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    @Override
    public void dbMove(RelationType oldRelation, RelationType newRelation) {
        Query query = local.getEM().createQuery("UPDATE SynsetRelation s SET s.relation = :newRelation WHERE s.relation = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    @Override // TODO: check me
    public boolean dbRelationExists(Synset synsetFrom, Synset synsetTo, RelationType relation) {
        return local.getEM().createNamedQuery("SynsetRelation.dbRelationExists", SynsetRelation.class)
                .setParameter("synsetFrom", synsetFrom.getId())
                .setParameter("synsetTo", synsetTo.getId())
                .setParameter("relation", relation.getId())
                .getResultList().size() > 0;
    }

    @Override
    public List<RelationType> dbGetRelationTypesOfSynset(Synset synset) {
        return local.getEM().createNamedQuery("SynsetRelation.dbGetRelationTypesOfSynset", RelationType.class)
                .setParameter("synset", synset)
                .getResultList();
    }

    @Override
    public int dbDeleteImproper() {
        List<Synset> synsets = local.getEM().createNamedQuery("Synset.getAllIDs", Synset.class).getResultList();
        return local.getEM().createNamedQuery("SynsetRelation.dbDeleteImproper", SynsetRelation.class)
                .setParameter("synsets", synsets)
                .executeUpdate();
    }

    @Override
    public List<SynsetRelation> dbGetRelations(Synset parent, Synset child, RelationType relation) {
        return local.getEM().createNamedQuery("SynsetRelation.dbGetRelations", SynsetRelation.class)
                .setParameter("parent", parent)
                .setParameter("child", child)
                .setParameter("relation", relation)
                .getResultList();
    }

    @Override
    public List<SynsetRelation> dbGetRelations(Long id) {
        return local.getEM().createNamedQuery("SynsetRelation.dbGetRelationsByID", SynsetRelation.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public SynsetRelation dbGetRelation(Synset parent, Synset child, RelationType relation) {
        List<SynsetRelation> relations = dbGetRelations(parent, child, relation);
        if (relations.isEmpty() || relations.get(0) == null) {
            return null;
        }
        return relations.get(0);
    }

    @Override
    public int dbGetRelationCountOfSynset(Synset synset) {
        Number count = local.getEM().createNamedQuery("SynsetRelation.countSynsetRelations", Number.class)
                .setParameter("synsetID", synset.getId())
                .getSingleResult();

        int c = count.intValue();
        return c;
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
    @Override
    public List<Long> dbGetTopPath(Synset synset, Long rtype) {
        ArrayList<Long> path = new ArrayList<Long>();

        DirectedGraph<Long, SynsetRelation> g
                = new DirectedSparseGraph<Long, SynsetRelation>();

        g.addVertex(synset.getId());
        Deque<Long> stack = new ArrayDeque<Long>();
        stack.push(synset.getId());
        while (!stack.isEmpty()) {
            Long item = stack.pop();

            List<SynsetRelation> rels = local.getEM().createNamedQuery("SynsetRelation.dbGetTopPath", SynsetRelation.class)
                    .setParameter("id_s", item)
                    .setParameter("id_r", rtype)
                    .getResultList();

            for (SynsetRelation rel : rels) {
                stack.push(rel.getSynsetTo().getId());
                g.addEdge(rel, item, rel.getSynsetTo().getId());
            }
        }

        DijkstraShortestPath<Long, SynsetRelation> dsp
                = new DijkstraShortestPath<Long, SynsetRelation>(g);

        Map<Long, Number> map = dsp.getDistanceMap(synset.getId());
        Long last = new Long(-1);
        for (Entry<Long, Number> p : map.entrySet()) {
            last = p.getKey();
        }

        if (last != -1) {
            List<SynsetRelation> p = dsp.getPath(synset.getId(), last);
            for (SynsetRelation r : p) {
                path.add(r.getSynsetTo().getId());
            }
        }
        return path;
    }

    @Override
    public List<Synset> dbGetTopPathInSynsets(Synset synset, Long rtype) {
        ArrayList<Synset> path = new ArrayList<Synset>();

        DirectedGraph<Long, SynsetRelation> g
                = new DirectedSparseGraph<Long, SynsetRelation>();

        g.addVertex(synset.getId());
        Deque<Long> stack = new ArrayDeque<Long>();
        stack.push(synset.getId());
        while (!stack.isEmpty()) {
            Long item = stack.pop();

            List<SynsetRelation> rels = local.getEM().createNamedQuery("SynsetRelation.dbGetTopPath", SynsetRelation.class)
                    .setParameter("id_s", item)
                    .setParameter("id_r", rtype)
                    .getResultList();

            for (SynsetRelation rel : rels) {
                stack.push(rel.getSynsetTo().getId());
                g.addEdge(rel, item, rel.getSynsetTo().getId());
            }
        }

        DijkstraShortestPath<Long, SynsetRelation> dsp
                = new DijkstraShortestPath<Long, SynsetRelation>(g);

        Map<Long, Number> map = dsp.getDistanceMap(synset.getId());
        Long last = new Long(-1);
        for (Entry<Long, Number> p : map.entrySet()) {
            last = p.getKey();
        }

        if (last != -1) {
            List<SynsetRelation> p = dsp.getPath(synset.getId(), last);
            for (SynsetRelation r : p) {
                path.add(r.getSynsetTo());
            }
        }
        return path;
    }

    @Override
    public List<SynsetRelation> getRelatedRelations(Synset synset, List<Long> lexicons) {
        List<SynsetRelation> related = local.getEM().createNamedQuery("SynsetRelation.AllSynsetBySynsetFromORSynsetTo", SynsetRelation.class)
                .setParameter("synset", synset.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
        if (related == null) {
            return new ArrayList<SynsetRelation>();
        }
        return related;
    }

    @Override
    public List<SynsetRelation> getRelationsSynsetTo(Synset synset) {
        List<SynsetRelation> relations = local.getEM().createNamedQuery("SynsetRelation.AllSynsetBySynsetTo", SynsetRelation.class)
                .setParameter("synset", synset.getId())
                .getResultList();
        return relations;
    }

    @Override
    public List<SynsetRelation> getRelatedRelations(Set<Long> synsetIDs) {
        List<SynsetRelation> related = local.getEM().createNamedQuery("SynsetRelation.SynsetsBySynsetsIDFromORSynsetsIDTo", SynsetRelation.class)
                .setParameter("synsets", synsetIDs)
                .getResultList();
        if (related == null) {
            return new ArrayList<SynsetRelation>();
        }
        return related;
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
