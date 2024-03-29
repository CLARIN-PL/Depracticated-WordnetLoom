package pl.edu.pwr.wordnetloom.synsetrelation.repository;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Stateless
public class SynsetRelationRepository extends GenericRepository<SynsetRelation> {

    @Inject
    EntityManager em;

    public boolean delete(Synset parent, Synset child, RelationType relationType) {
        Query query = getEntityManager().createQuery("DELETE FROM SynsetRelation s WHERE s.parent.id = :parent AND s.child.id = :child AND s.relationType.id = :relationType", SynsetRelation.class);
        query.setParameter("synsetFrom", parent.getId())
                .setParameter("synsetTo", child.getId())
                .setParameter("relation", relationType.getId());

        return query.executeUpdate() > 0;
    }

    public void delete(RelationType relationType) {
        getEntityManager().createQuery("DELETE FROM SynsetRelation s WHERE s.relationType.id = :relationType", SynsetRelation.class)
                .setParameter("relationType", relationType.getId())
                .executeUpdate();
    }

    public void delete(SynsetRelation relation){
        em.remove(em.contains(relation) ? relation : em.merge(relation));
    }

    public void deleteAll() {
        getEntityManager().createQuery("DELETE FROM SynsetRelation", SynsetRelation.class)
                .executeUpdate();
    }

    public List<SynsetRelation> findSubRelations(Synset synset, RelationType relationType) {
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

    public List<SynsetRelation> findUpperRelations(Synset synset, RelationType relationType) {
        if (relationType == null) {
            return getEntityManager().createQuery("SELECT s FROM SynsetRelation s WHERE s.child = :child", SynsetRelation.class)
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

    public Long findRelationTypeUseCount(RelationType relation) {
        return getEntityManager().createQuery("SELECT COUNT(sr) FROM SynsetRelation sr WHERE sr.relationType.id = :relation ", Long.class)
                .setParameter("relation", relation.getId()).getSingleResult();
    }

    public void move(RelationType oldRelation, RelationType newRelation) {
        Query query = getEntityManager().createQuery("UPDATE SynsetRelation s SET s.relationType= :newRelation WHERE s.relationType = :oldRelation");
        query.setParameter("oldRelation", oldRelation)
                .setParameter("newRelation", newRelation)
                .executeUpdate();
    }

    public boolean checkRelationExists(Synset parent, Synset child, RelationType relation) {
        return findRelations(parent, child, relation).size() > 0;
    }

    public List<RelationType> findtRelationTypesBySynset(Synset synset) {
        return getEntityManager().createQuery("SELECT sr.relationType FROM SynsetRelation sr WHERE sr.parent.id = :id OR sr.child.id = :id", RelationType.class)
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

    public List<SynsetRelation> findRelations(Synset parent, Synset child, RelationType relation) {
        return getEntityManager().createQuery("SELECT sr FROM SynsetRelation sr WHERE sr.parent.id = :parent AND sr.child.id = :child AND sr.relationType.id = :relation", SynsetRelation.class)
                .setParameter("parent", parent.getId())
                .setParameter("child", child.getId())
                .setParameter("relation", relation.getId())
                .getResultList();
    }

    public SynsetRelation findRelation(Synset parent, Synset child, RelationType relation) {
        List<SynsetRelation> relations = findRelations(parent, child, relation);
        if (relations.isEmpty() || relations.get(0) == null) {
            return null;
        }
        return relations.get(0);
    }

    public DirectedGraph<Long, SynsetRelation> findDirectedGraph(Synset synset, RelationType relationType){
        return getRelationDirectedGraph(synset, relationType.getId());
    }

    public List<Synset> findPath(Synset synset, RelationType relationType){
        DirectedGraph<Long, SynsetRelation> graph = getRelationDirectedGraph(synset, relationType.getId());
        List<Synset> result = new ArrayList<>();
        result.add(synset);
        Deque<Synset> synsetDeque = new ArrayDeque<>();
        synsetDeque.push(synset);
        Synset currentSynset;
        Synset childSynset;
        DataEntry dataEntry;
        Set<Long> downloadedSynsets = new HashSet<>();
        while(!synsetDeque.isEmpty()) {
            currentSynset = synsetDeque.pop();
            Collection<SynsetRelation> relations = graph.getIncidentEdges(currentSynset.getId());
            for(SynsetRelation relation : relations){
                if(relation.getChild().getId().equals(currentSynset.getId())){
                    continue;
                }
                childSynset = relation.getChild();
                result.add(childSynset);
                if(!downloadedSynsets.contains(childSynset.getId())){
                    synsetDeque.push(childSynset);
                }
            }
            downloadedSynsets.add(currentSynset.getId());
        }
        return result;
    }

    private Long getLastElement(Map<Long, Number> map) {
        return map.entrySet().stream().skip(map.size()-1).findFirst().get().getKey();
    }

    private List<SynsetRelation> findSynsetRelation(Long parentId, Long relationTypeId) {
        return getEntityManager().createQuery("SELECT s FROM SynsetRelation s  LEFT JOIN FETCH s.parent WHERE (s.parent.id = :id_s) AND s.relationType.id = :id_r")
                .setParameter("id_s", parentId)
                .setParameter("id_r", relationTypeId)
                .getResultList();
    }

    private DirectedGraph<Long, SynsetRelation> getRelationDirectedGraph(Synset synset, Long rtype) {
        DirectedGraph<Long, SynsetRelation> g = new DirectedSparseGraph<>();

        g.addVertex(synset.getId());
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(synset.getId());
        Long item;
        List<SynsetRelation> relations;
        while (!stack.isEmpty()) {
            item = stack.pop();
            relations = findSynsetRelation(item, rtype);
            for (SynsetRelation rel : relations) {
                // TODO przemyśleć to, aby pobierało wszystko
                if (!g.containsVertex(rel.getChild().getId())) {
                    stack.push(rel.getChild().getId());
                    g.addEdge(rel, item, rel.getChild().getId());
                }
            }
        }
        return g;
    }

    private List<SynsetRelation> getRelations(Synset synset, List<Long> lexicons, String joinColumn, String synsetIdColumn, NodeDirection[] directions) {
        Query query = getEntityManager().createQuery("FROM SynsetRelation sr LEFT JOIN FETCH sr." + joinColumn + " AS synset " +
                "LEFT JOIN FETCH synset.senses AS sense " +
                "LEFT JOIN FETCH sense.domain " +
                "LEFT JOIN FETCH sense.lexicon " +
                "LEFT JOIN FETCH sense.partOfSpeech "+
                "LEFT JOIN FETCH sr.relationType AS type " +
                "LEFT JOIN FETCH sense.word AS word " +
                "WHERE sr." + synsetIdColumn + ".id = :id " +
                "AND type.nodePosition IN (:directions)" +
                "AND sense.synsetPosition = 0 " +
                "AND synset.lexicon.id IN (:lexicons)")
                .setParameter("id", synset.getId())
                .setParameter("directions", Arrays.asList(directions))
                .setParameter("lexicons", lexicons);
        return query.getResultList();
    }

    private List<SynsetRelation> findAllRelationBySynset(Synset synset, List<Long> lexicons, boolean synsetIsParent, NodeDirection[] directions) {
        final String PARENT = "parent";
        final String CHILD = "child";
        String synsetIdColumn;
        String fetchColumn;
        if (synsetIsParent) {
            synsetIdColumn = PARENT;
            fetchColumn = CHILD;
        } else {
            synsetIdColumn = CHILD;
            fetchColumn = PARENT;
        }
        return getRelations(synset, lexicons, fetchColumn, synsetIdColumn, directions);
    }

    public List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        NodeDirection[] oppositeDirections = new NodeDirection[directions.length];
        for(int i = 0; i < directions.length; i++)
        {
            oppositeDirections[i] = directions[i].getOpposite();
        }

        return findAllRelationBySynset(synset, lexicons, false, oppositeDirections);
    }

    public List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        return findAllRelationBySynset(synset, lexicons, true, directions);
    }

    public List<SynsetRelation> findSimpleRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons) {
        return findSimpleRelations(synset, lexicons, false);
    }

    public List<SynsetRelation> findSimpleRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons) {
        return findSimpleRelations(synset, lexicons, true);
    }

    private List<SynsetRelation> findSimpleRelations(Synset synset, List<Long> lexicons, boolean synsetIsChild) {
        final String CHILD = "child";
        final String PARENT = "parent";
        String synsetFetchColumn = PARENT;
        if (synsetIsChild) {
            synsetFetchColumn = CHILD;
        }

        Query query = getEntityManager().createQuery("SELECT new SynsetRelation(sr.id,sr.relationType.id, sr.parent.id, sr.child.id, sr.relationType.nodePosition) FROM SynsetRelation sr " +
                "WHERE sr." + synsetFetchColumn + ".id = :id ")
//                "AND sr." + synsetFetchColumn + ".lexicon.id IN  (:lexicons)")
                .setParameter("id", synset.getId());
//                .setParameter("lexicons", lexicons);
        return (List<SynsetRelation>) query.getResultList();
    }

    /**
     * Metoda pobierająca relację synsetów, gdzie podany synset jest albo rodzicem, albo dzieckiem
     *
     * @param synset synset dla którego zostaną znalezione relacje
     * @return wszystkie relację podanego synsetu
     */
    public List<SynsetRelation> findRelations(Synset synset) {
        return getEntityManager().createQuery("SELECT sr FROM SynsetRelation sr WHERE sr.parent.id =:id OR sr.child.id =:id", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .getResultList();
    }

    public void create(Synset parent, Synset child, RelationType relationType){
        SynsetRelation relation = new SynsetRelation();
        relation.setParent(parent);
        relation.setChild(child);
        relation.setRelationType(relationType);
        persist(relation);
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
