package pl.edu.pwr.wordnetloom.extgraph.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class ExtGraphRepository extends GenericRepository<ExtGraph> {

    @Inject
    EntityManager em;

    public Collection<ExtGraph> findByWord(String word) {
        return getEntityManager().createQuery("SELECT e FROM ExtGraph e where e.word = :word", ExtGraph.class)
                .setParameter("word", word)
                .getResultList();
    }

    public Collection<ExtGraph> findByIds(Long[] extgraphIds) {
        return getEntityManager().createQuery("SELECT e FROM ExtGraph e where e.id in ( :ids )", ExtGraph.class)
                .setParameter("ids", Arrays.asList(extgraphIds))
                .getResultList();
    }

    public Collection<ExtGraph> findByWordAndPackageNo(String word, int packageno) {

        Long pkg = new Long(packageno);
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ExtGraph> criteriaQuery = criteriaBuilder.createQuery(ExtGraph.class
        );

        Root<ExtGraph> root = criteriaQuery.from(ExtGraph.class);
        Join<ExtGraph, Synset> synset = root.join("synset", JoinType.INNER);
        Join<Synset, Sense> sts = synset.join("sense", JoinType.LEFT);
        List<Predicate> criteriaList = new ArrayList<>();

        Predicate firstCondition = criteriaBuilder.equal(root.get("word"), word);
        criteriaList.add(firstCondition);

        Predicate secondCondition = criteriaBuilder.equal(root.get("packageno"), pkg);
        criteriaList.add(secondCondition);

        Predicate thirdCondition = criteriaBuilder.equal(sts.get("senseIndex"), 0);
        criteriaList.add(thirdCondition);

        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
        final TypedQuery<ExtGraph> query = getEntityManager().createQuery(criteriaQuery);

        return query.getResultList();
    }

    public Collection<String> findNewWordsByPackageNoAndPartOfSpeech(int packageno, PartOfSpeech pos) {
        Long pkg = new Long(packageno);
        Query query = getEntityManager().createQuery("SELECT e.word FROM ExtGraph e where e.packageno = :packageno AND e.pos = :pos");
        query.setParameter("packageno", pkg).setParameter("pos", pos);
        return query.getResultList();
    }

    public List<Long> findPackagesByPartOfSpeech(PartOfSpeech pos) {
        if (pos == null) {
            return null;

        }
        return getEntityManager().createNamedQuery("SELECT e.packageno FROM ExtGraph e WHERE e.pos = :pos GROUP BY e.packageno ORDER BY e.packageno ASC", Long.class)
                .setParameter("pos", pos)
                .getResultList();
    }

    public int findMaxPackageNoByPartOfSpeech(PartOfSpeech pos) {
        if (pos == null) {
            return 0;
        }
        List<Long> result = getEntityManager().createQuery("SELECT e.packageno FROM ExtGraph e WHERE e.pos = :pos ORDER BY e.packageno desc", Long.class)
                .setParameter("pos", pos).setMaxResults(1)
                .getResultList();
        return result.isEmpty() ? 0 : result.get(0).intValue();
    }

    public List<Long> findIDsByWord(String word) {
        return getEntityManager().createQuery("SELECT e.id FROM ExtGraph e where e.word = :word", Long.class)
                .setParameter("word", word)
                .getResultList();
    }

    public List<Long> findIDsByWordAndPackageNo(String word, int packageno) {
        Long pkg = new Long(packageno);

        return getEntityManager().createQuery("SELECT e.id FROM ExtGraph e where e.word = :word AND e.packageno = :packageno", Long.class)
                .setParameter("word", word)
                .setParameter("packageno", pkg)
                .getResultList();
    }

    public void deleteBySynset(Synset synset) {
        getEntityManager().createQuery("DELETE FROM ExtGraph e WHERE e.synset.id = :synset")
                .setParameter("synset", synset.getId())
                .executeUpdate();
    }

    @Override
    protected Class<ExtGraph> getPersistentClass() {
        return ExtGraph.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
