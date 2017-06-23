package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.ExtGraphDAOLocal;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class ExtGraphDAOBean implements ExtGraphDAOLocal {

    @EJB
    private DAOLocal local;

    @Override
    public Collection<ExtGraph> dbFullGet() {
        return local.getEM().createNamedQuery("ExtGraph.dbFullGet", ExtGraph.class)
                .getResultList();
    }

    @Override
    public Collection<ExtGraph> dbFullGet(String word) {
        return local.getEM().createNamedQuery("ExtGraph.dbFullGetWord", ExtGraph.class)
                .setParameter("word", word)
                .getResultList();
    }

    @Override
    public Collection<ExtGraph> dbFullGet(Long[] extgraph_ids) {
        return local.getEM().createNamedQuery("ExtGraph.dbFullGetIDs", ExtGraph.class)
                .setParameter("ids", Arrays.asList(extgraph_ids))
                .getResultList();
    }

    @Override
    public Collection<ExtGraph> dbFullGet(String word, int packageno) {

        Long pkg = new Long(packageno);
        CriteriaBuilder criteriaBuilder = local.getEM().getCriteriaBuilder();
        CriteriaQuery<ExtGraph> criteriaQuery = criteriaBuilder.createQuery(ExtGraph.class);

        Root<ExtGraph> root = criteriaQuery.from(ExtGraph.class);
        Join<ExtGraph, Synset> synset = root.join("synset", JoinType.INNER);
        Join<Synset, SenseToSynset> sts = synset.join("senseToSynset", JoinType.LEFT);
        List<Predicate> criteriaList = new ArrayList<>();

        Predicate firstCondition = criteriaBuilder.equal(root.get("word"), word);
        criteriaList.add(firstCondition);

        Predicate secondCondition = criteriaBuilder.equal(root.get("packageno"), pkg);
        criteriaList.add(secondCondition);

        Predicate thirdCondition = criteriaBuilder.equal(sts.get("senseIndex"), 0);
        criteriaList.add(thirdCondition);

        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
        final TypedQuery<ExtGraph> query = local.getEM().createQuery(criteriaQuery);

        return query.getResultList();
    }

    // FIXME: later, delete all occurencies of this `fast` legacies
    // TODO: modify entities LAZY<->EAGER to boost database
    @Override
    public Collection<ExtGraph> dbFastGet(String word, int packageno) {
        return dbFullGet(word, packageno);
    }

    @Override
    public Collection<String> dbGetNewWords(int packageno, PartOfSpeech pos) {
        Long pkg = new Long(packageno);
        Query query = local.getEM().createQuery("SELECT e.word FROM ExtGraph e where e.packageno = :packageno AND e.pos = :pos");
        query.setParameter("packageno", pkg).setParameter("pos", pos);
        return query.getResultList();
    }

    @Override
    public List<Integer> GetPackages(PartOfSpeech pos) {
        if (pos == null) {
            return null;
        }

        List<Long> longs = local.getEM().createNamedQuery("ExtGraph.GetPackages", Long.class)
                .setParameter("pos", pos)
                .getResultList();
        List<Integer> ints = new ArrayList<>();
        for (Long l : longs) {
            ints.add(l.intValue());
        }
        return ints;
    }

    @Override
    public int GetMaxPackageNo(PartOfSpeech pos) {
        if (pos == null) {
            return 0;
        }
        List<Long> result = local.getEM().createNamedQuery("ExtGraph.GetMaxPackageNo", Long.class)
                .setParameter("pos", pos).setMaxResults(1)
                .getResultList();
        return result.isEmpty() ? 0 : result.get(0).intValue();
    }

    @Override
    public List<Long> getIDsFromWord(String word) {
        return local.getEM().createNamedQuery("ExtGraph.getIDsFromWord", Long.class)
                .setParameter("word", word)
                .getResultList();
    }

    @Override
    public List<Long> getIDsFromWord(String word, int packageno) {
        Long pkg = new Long(packageno);
        return local.getEM().createNamedQuery("ExtGraph.getIDsFromWordAndPkgNo", Long.class)
                .setParameter("word", word)
                .setParameter("packageno", pkg)
                .getResultList();
    }

    @Override
    public void deleteForSynset(Synset synset) {
        local.getEM().createNamedQuery("ExtGraph.deleteForSynsetID").setParameter("synset", synset.getId()).executeUpdate();
    }

}
