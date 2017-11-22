package pl.edu.pwr.wordnetloom.synsetrelation.repository;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.word.model.Word;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;

import java.util.*;
import java.util.Map.Entry;

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
        return getEntityManager().createQuery("SELECT sr FROM SynsetRelation sr WHERE sr.parent.id = :parent AND sr.child = : child AND sr.relationType.id = :relation", SynsetRelation.class)
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

    public Long findRelationCountBySynset(Synset synset) {
        return getEntityManager().createNamedQuery("SynsetRelation.countSynsetRelations", Long.class)
                .setParameter("synsetID", synset.getId())
                .getSingleResult();
    }

    /**
     * Returns a path to the top synset in relation graph.
     *
     * @param synset start synset
     * @param rtype  relation type
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

    public List<SynsetRelation> findRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons) {

        /*Query query = getEntityManager().createQuery("FROM SynsetRelation sr LEFT JOIN FETCH sr.parent AS synset " +
                "LEFT JOIN FETCH synset.senses AS sense " +
                "LEFT JOIN FETCH sense.domain " +
                "LEFT JOIN FETCH sense.lexicon lexicon " +
                "WHERE sr.child.id = :id AND sense.synsetPosition = 0 AND lexicon.id IN (:lexicons)", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .setParameter("lexicons", lexicons);
        return query.getResultList();*/
        return findRelations(synset, lexicons, true);
//        return getEntityManager().createQuery("FROM SynsetRelation sr WHERE  sr.child.id = :id AND sr.parent.lexicon.id IN (:lexicons)", SynsetRelation.class)
//                .setParameter("id", synset.getId())
//                .setParameter("lexicons", lexicons)
//                .getResultList();
    }

    private List<SynsetRelation> getRelationsFrom(Synset synset, List<Long> lexicons){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SynsetRelation> query = criteriaBuilder.createQuery(SynsetRelation.class);
        Root<SynsetRelation> relationRoot = query.from(SynsetRelation.class);
        Join<SynsetRelation, RelationType> relationTypeJoin = relationRoot.join("relationType");
        Join<SynsetRelation, Synset> synsetJoin = relationRoot.join("child");
        Join<SynsetRelation, Sense> senseJoin = synsetJoin.join("senses");
        Join<Synset, Word> wordJoin = senseJoin.join("word",JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        Predicate parentPredicate = criteriaBuilder.equal(relationRoot.get("parent"), synset.getId());
//        Predicate lexiconsPredicate = relationTypeJoin.get("lexicons").in(lexicons);
        Predicate synsetPositionPredicate = criteriaBuilder.equal(senseJoin.get("synsetPosition"), 0);
        predicates.add(parentPredicate);
//        predicates.add(lexiconsPredicate);
        predicates.add(synsetPositionPredicate);
        query.where(predicates.toArray(new Predicate[0]));

        List<Order> orders = new ArrayList<>();
        Order nodePositionOrder = criteriaBuilder.asc(relationTypeJoin.get("nodePosition"));
        Order wordOrder = criteriaBuilder.asc(wordJoin.get("word"));
        orders.add(nodePositionOrder);
        orders.add(wordOrder);
        query.orderBy(orders);

        return getEntityManager().createQuery(query).getResultList();
    }

    private List<SynsetRelation> fetchRelatedSynset(List<SynsetRelation> relations){
        List<Long> synsetRelationsIds = new ArrayList<>();
        relations.forEach(e->synsetRelationsIds.add(e.getId()));

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SynsetRelation> query= criteriaBuilder.createQuery(SynsetRelation.class);

        Root<SynsetRelation> root = query.from(SynsetRelation.class);
        Fetch<SynsetRelation, Synset> synset = root.fetch("child");
        Fetch<Synset, Sense> sense = synset.fetch("senses");
        Fetch<Sense, Domain> domainFetch = sense.fetch("domain");
        Fetch<Sense, Lexicon> lexiconFetch = sense.fetch("lexicon");
        Fetch<Sense, PartOfSpeech> partOfSpeechFetch = sense.fetch("partOfSpeech");


/*
        Join<SynsetRelation, Synset> synsetJoin = relationRoot.join("child");
        Join<SynsetRelation, Sense> senseJoin = synsetJoin.join("senses");
        Fetch<SynsetRelation, Sense> senseFetch = synsetFetch.fetch("senses");
        */

        List<Predicate> predicates = new ArrayList<>();
        Predicate idPredicate = root.get("id").in(synsetRelationsIds);
        predicates.add(idPredicate);

        query.where(predicates.toArray(new Predicate[0]));
/*

        Query query = getEntityManager().createQuery("FROM SynsetRelation sr " +
                "JOIN FETCH sr.child AS synset " +
                "JOIN FETCH synset.senses AS sense " +
                "JOIN FETCH sense.domain " +
                "JOIN FETCH sense.lexicon " +
                "WHERE sr.id IN (:ids) AND sense.synsetPosition = 0")
                .setParameter("ids", synsetRelationsIds);
*/

//        List<SynsetRelation> resultList = query.getResultList();
        List<SynsetRelation> resultList = getEntityManager().createQuery(query).getResultList();

        return resultList;
    }

    public List<SynsetRelation> findRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons) {
        final List<SynsetRelation> allFromSynsetRelations = getRelationsFrom(synset, lexicons);
        List<SynsetRelation> resultList = new ArrayList<>();

        int[] directionCounter = new int[4];
        Arrays.fill(directionCounter, 0);

        int filledDirectionsCounter = 0;

        for(SynsetRelation relation : allFromSynsetRelations){
            int direction = relation.getRelationType().getNodePosition().ordinal();
            if(directionCounter[direction] != 4){
                resultList.add(relation);
                directionCounter[direction]++;
                if(directionCounter[direction]==4){
                    filledDirectionsCounter++;
                }
                if(filledDirectionsCounter==4){
                    return resultList;
                }
            }
        }
        resultList = fetchRelatedSynset(resultList);
        return resultList;

//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<SynsetRelation> query = criteriaBuilder.createQuery(SynsetRelation.class);
//        Root<SynsetRelation> relationRoot = query.from(SynsetRelation.class);
//        Fetch<SynsetRelation, Synset> synsetFetch = relationRoot.fetch("child", JoinType.LEFT);
//        Fetch<SynsetRelation, Sense> senseFetch = synsetFetch.fetch("senses", JoinType.LEFT);
//        Fetch<SynsetRelation, Domain> domainFetch = senseFetch.fetch("domain", JoinType.LEFT);
//        Fetch<SynsetRelation, Lexicon> lexiconFetch = senseFetch.fetch("lexicon", JoinType.LEFT);
//        Fetch<SynsetRelation, PartOfSpeech> partOfSpeechFetch = senseFetch.fetch("partOfSpeech", JoinType.LEFT);
//        query.select(relationRoot);
//        query.where(criteriaBuilder.equal(relationRoot.get("parent"),synset.getId()));
//        return em.createQuery(query).getResultList();

        /*Query query = getEntityManager().createQuery("FROM SynsetRelation sr LEFT JOIN FETCH sr.child AS synset " +
                "LEFT JOIN FETCH synset.senses AS sense " +
                "LEFT JOIN FETCH sense.domain " +
                "LEFT JOIN FETCH sense.lexicon AS lexicon " +
                "WHERE sr.parent.id = :id AND sense.synsetPosition = 0 AND lexicon.id IN (:lexicons)", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .setParameter("lexicons", lexicons);
        return query.getResultList();*/


        //TO było dobre
//        return findRelations(synset, lexicons, false);


//        List<SynsetRelation>  resultList = query.getResultList();
//        for(SynsetRelation relation : resultList){
//            relation.setParent(synset);
//        }
//        return resultList;
        /*return getEntityManager().createQuery("FROM SynsetRelation sr WHERE sr.parent.id = :id", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .getResultList();*/
    }

    private List<SynsetRelation> findRelations(Synset synset, List<Long> lexicons, boolean synsetIsChild) {
        final String CHILD = "child";
        final String PARENT = "parent";
        String synsetFetchColumn = CHILD;
        String synsetWhereColumn = PARENT;
        if(synsetIsChild){
            synsetFetchColumn = PARENT;
            synsetWhereColumn = CHILD;
        }
        Query query = getEntityManager().createQuery("FROM SynsetRelation sr LEFT JOIN FETCH sr."+synsetFetchColumn+" AS synset " +
                "LEFT JOIN FETCH synset.senses AS sense " +
                "LEFT JOIN FETCH sense.domain " +
                "LEFT JOIN FETCH sense.lexicon AS lexicon " +
                "WHERE sr."+synsetWhereColumn+".id =:id AND sense.synsetPosition = 0 AND lexicon.id IN (:lexicons)", SynsetRelation.class)
                .setParameter("id", synset.getId())
                .setParameter("lexicons", lexicons);
        return query.getResultList();
    }

    public List<SynsetRelation> findSimpleRelationsWhereSynsetIsParent(Synset synset, List<Long> lexicons) {
        return findSimpleRelations(synset, lexicons, false);
    }

    public List<SynsetRelation> findSimpleRelationsWhereSynsetIsChild(Synset synset, List<Long> lexicons) {
        return findSimpleRelations(synset, lexicons, true);
    }

    private List<SynsetRelation> findSimpleRelations(Synset synset, List<Long> lexicons, boolean synsetIsChild){
        final String CHILD = "child";
        final String PARENT = "parent";
        String synsetFetchColumn = PARENT;
        if(synsetIsChild){
            synsetFetchColumn = CHILD;
        }
        Query query = getEntityManager().createQuery("SELECT new SynsetRelation(sr.id,sr.relationType.id, sr.parent.id, sr.child.id) FROM SynsetRelation sr " +
                "WHERE sr."+synsetFetchColumn+".id = :id " +
                "AND sr."+synsetFetchColumn+".lexicon.id IN  (:lexicons)")
                .setParameter("id", synset.getId())
                .setParameter("lexicons", lexicons);
        return query.getResultList();
    }
    /** Metoda pobierająca relację synsetów, gdzie podany synset jest albo rodzicem, albo dzieckiem
     * @param synset synset dla którego zostaną znalezione relacje
     * @return wszystkie relację podanego synsetu
     */
    public List<SynsetRelation> findRelations(Synset synset){
        return getEntityManager().createQuery("SELECT sr.FROM SynsetRelation WHERE sr.parent.id =:id OR sr.child.id =:id", SynsetRelation.class)
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
