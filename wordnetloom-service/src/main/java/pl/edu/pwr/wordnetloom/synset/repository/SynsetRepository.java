package pl.edu.pwr.wordnetloom.synset.repository;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.repository.SenseRepository;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.synsetrelation.repository.SynsetRelationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class SynsetRepository extends GenericRepository<Synset> {

    @Inject
    EntityManager em;

    @Inject
    SynsetRelationRepository synsetRelationRepository;

    @Inject
    SenseRepository senseRepository;

    //    @NamedQuery(name = "Synset.findSynsetBySensID",
//            query = "SELECT s.synset FROM SenseToSynset s WHERE s.idSense = :senseID AND s.sense.lexicon.id IN (:lexicons)"),
//    @NamedQuery(name = "Synset.findListSynsetByID",
//            query = "SELECT s FROM Synset s WHERE s.id IN ( :synsetsID )"),
//    @NamedQuery(name = "Synset.getAllIDs",
//            query = "SELECT s FROM Synset s"),
//    @NamedQuery(name = "Synset.dbGetUnit",
//            query = "SELECT s FROM Synset s JOIN s.senseToSynset AS sts LEFT JOIN sts.sense AS sen LEFT JOIN sen.senseAttributes AS sea LEFT JOIN s.synsetAttributes AS sa WHERE sen.lexicon.id IN( :lexicons) AND  s.id = :synsetID ORDER BY s.id, sts.senseIndex"),
//    @NamedQuery(name = "Synset.dbGetUnitsByIDs",
//            query = "SELECT s FROM Synset s JOIN s.senseToSynset AS sts LEFT JOIN sts.sense AS sen LEFT JOIN sen.senseAttributes AS sea LEFT JOIN s.synsetAttributes AS sa WHERE s.id IN ( :synsetsIDs ) ORDER BY s.id, sts.senseIndex"),
//    @NamedQuery(name = "Synset.dbGetSynsetRels",
//            query = "SELECT sr.synsetFrom FROM SynsetRelation sr WHERE sr.synsetFrom.id IN ( SELECT sr.synsetFrom.id FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synsetID ) OR sr.synsetFrom.id IN ( SELECT sr.synsetTo.id FROM SynsetRelation sr WHERE sr.synsetTo.id = :synsetID )"),
//    @NamedQuery(name = "Synset.dbGetSynsetsRels",
//            query = "SELECT sr.synsetFrom FROM SynsetRelation sr WHERE sr.id IN ( SELECT sr.id FROM SynsetRelation sr WHERE sr.synsetFrom IN ( :synsetList ) ) OR sr.id IN ( SELECT sr.id FROM SynsetRelation sr WHERE sr.synsetTo IN ( :synsetList ) )"),
//    @NamedQuery(name = "Synset.fastGetPOSID",
//            query = "SELECT s.sense.partOfSpeech.id FROM SenseToSynset s WHERE s.idSynset = :idSynset ORDER BY s.senseIndex"),})
    public List<Synset> findSynsetsByWord(String word, List<Long> lexicons) {

        Query query = getEntityManager().createQuery("SELECT s.synset FROM Sense s WHERE s.word.word = :word AND s.lexicon.id IN (:lexicon)");
        query.setParameter("word", word);
        query.setParameter("lexicon", lexicons);
        return query.getResultList();

    }

    public Synset findSynset(Synset synset, List<Long> lexicons) {
        return getEntityManager().createQuery("SELECT s FROM Synset s JOIN FETCH s.senses JOIN FETCH s.synsetAttributes WHERE s.lexicon.id IN ( :lexicons ) AND  s.id = :id", Synset.class)
                .setParameter("id", synset.getId())
                .setParameter("lexicons", lexicons)
                .getSingleResult();
    }

    public List<Synset> findSynsets(List<Synset> synsets) {
        if (synsets.isEmpty()) {
            return synsets;
        }

        ArrayList<Long> ids = new ArrayList<>();
        for (Synset s : synsets) {
            ids.add(s.getId());
        }

        return findSynsetsByIds(ids);
    }

    public List<Synset> findSynsetsByIds(Collection<Long> iDs) {
        if (iDs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Synset> list = getEntityManager().createQuery("SELECT s FROM Synset s JOIN FETCH s.senses JOIN FETCH s.synsetAttributes WHERE s.id IN ( :ids )", Synset.class)
                .setParameter("ids", iDs)
                .getResultList();
        if (!list.isEmpty() && list.get(0) != null) {
            list.get(0).getSynsetAttributes();
        }
        return list;
    }

    public Integer findSynsetSenseCount(Synset synset) {
        return findById(synset.getId()).getSenses().size();
    }

    public String rebuildUnitsStr(Synset synset, List<Long> lexicons) {
        return rebuildUnitsStr(synset, lexicons);
    }

    public PartOfSpeech findSynsetPartOfSpeech(Synset synset) {
        List<Sense> senses = findById(synset.getId()).getSenses();
        if (senses.isEmpty() || senses.get(0) == null) {
            return null;
        }

        return senses.get(0).getPartOfSpeech();
    }

    public List<Synset> dbFastGetSynsets(String filter, List<Long> lexicons) {
        return dbFastGetSynsets(filter, null, null, 0, lexicons);
    }

    public List<Synset> dbFastGetSynsets(String filter, Domain domain, RelationType relationType, int limitSize, List<Long> lexicons) {
        return dbFastGetSynsets(filter, domain, relationType, limitSize, -1, lexicons);
    }

    public List<Sense> dbFastGetSenseBySynset(String filter, Domain domain, RelationType relationType, String definition, String comment, String artificial, int limitSize, long posIndex, List<Long> lexicons) {

//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
//        Root<SenseToSynset> stsRoot = criteriaQuery.from(SenseToSynset.class);
//        Join<SenseToSynset, Sense> sense = stsRoot.join("sense", JoinType.LEFT);
//        Join<Sense, Word> word = sense.join("lemma", JoinType.LEFT);
//
//        List<Predicate> criteriaList = new ArrayList<>();
//
//        Predicate first_predicate = criteriaBuilder.like(
//                criteriaBuilder.lower(word.<String>get("word")), "%" + filter.toLowerCase() + "%");
//        criteriaList.add(first_predicate);
//
//        if (domain != null) {
//            Predicate secend_predicate = criteriaBuilder.equal(sense.get("domain").get("id"), domain.getId());
//            criteriaList.add(secend_predicate);
//        }
//        if (posIndex > 0) {
//            Predicate third_predicate = criteriaBuilder.equal(sense.get("partOfSpeech").get("id"), posIndex);
//            criteriaList.add(third_predicate);
//        }
//        if (relationType != null) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetRelation> relRoot = subquery.from(SynsetRelation.class);
//            subquery.select(relRoot.get("synsetFrom").<Long>get("id"));
//            subquery.where(criteriaBuilder.equal(relRoot.get("relation").get("id"), relationType.getId()));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//
//        }
//        if (!definition.isEmpty()) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
//            subquery.select(relRoot.get("synset").<Long>get("id"));
//            List<Predicate> predicates = new ArrayList<>();
//            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.DEFINITION);
//            predicates.add(type);
//            Predicate value = criteriaBuilder.like(relRoot.<String>get("value").<String>get("text"), "%" + definition + "%");
//            predicates.add(value);
//            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        if (!comment.isEmpty()) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
//            subquery.select(relRoot.get("synset").<Long>get("id"));
//            List<Predicate> predicates = new ArrayList<>();
//            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.COMMENT);
//            predicates.add(type);
//            Predicate value = criteriaBuilder.like(relRoot.get("value").<String>get("text"), "%" + comment + "%");
//            predicates.add(value);
//            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        if (!artificial.isEmpty()) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
//            subquery.select(relRoot.get("synset").<Long>get("id"));
//            List<Predicate> predicates = new ArrayList<>();
//            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.ISABSTRACT);
//            predicates.add(type);
//            Predicate value = criteriaBuilder.equal(relRoot.get("value").<String>get("text"), artificial);
//            predicates.add(value);
//            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        criteriaQuery.select(stsRoot.<Long>get("idSynset")).distinct(true);
//        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
//
//        final TypedQuery<Long> query = getEM().createQuery(criteriaQuery);
//        query.setFirstResult(0);
//        if (limitSize > 0) {
//            query.setMaxResults(limitSize);
//        }
//        List<Long> synsetIds = query.getResultList();
        List<Sense> result = new ArrayList<>();
//        synsetIds.stream().forEach((id) -> {
//            result.addAll(dbFastGetUnits(id, lexicons));
//        });

        return result;
    }

    public List<Synset> dbFastGetSynsets(String filter, Domain domain, RelationType relationType, int limitSize, long posIndex, List<Long> lexicon) {

//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<Synset> criteriaQuery = criteriaBuilder.createQuery(Synset.class);
//        Root<SenseToSynset> stsRoot = criteriaQuery.from(SenseToSynset.class);
//        Join<SenseToSynset, Sense> sense = stsRoot.join("sense", JoinType.LEFT);
//        Join<Sense, Word> word = sense.join("lemma", JoinType.LEFT);
//
//        List<Predicate> criteriaList = new ArrayList<>();
//
//        Predicate first_predicate = criteriaBuilder.like(
//                criteriaBuilder.lower(word.<String>get("word")), "%" + filter.toLowerCase() + "%");
//        criteriaList.add(first_predicate);
//
//        if (domain != null) {
//            Predicate secend_predicate = criteriaBuilder.equal(sense.get("domain").get("id"), domain.getId());
//            criteriaList.add(secend_predicate);
//        }
//        if (posIndex > 0) {
//            Predicate third_predicate = criteriaBuilder.equal(sense.get("partOfSpeech").get("id"), posIndex);
//            criteriaList.add(third_predicate);
//        }
//        if (relationType != null) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetRelation> relRoot = subquery.from(SynsetRelation.class);
//            subquery.select(relRoot.get("synsetFrom").<Long>get("id"));
//            subquery.where(criteriaBuilder.equal(relRoot.get("relation").get("id"), relationType.getId()));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        Predicate last = criteriaBuilder.in(sense.get("lexicon").get("id")).value(lexicon);
//        criteriaList.add(last);
//
//        criteriaQuery.select(stsRoot.<Synset>get("synset"));
//        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
//
//        final TypedQuery<Synset> query = getEntityManager().createQuery(criteriaQuery);
//        query.setFirstResult(0);
//        if (limitSize > 0) {
//            query.setMaxResults(limitSize);
//        }
//        return query.getResultList();
        return null;
    }

    public Boolean areSynsetsInSameLexicon(Long synset1, Long synset2) {
        Synset s1 = findById(synset1);
        Synset s2 = findById(synset2);
        return s1.getLexicon().getId().equals(s2.getLexicon().getId());
    }

    public List<Synset> findSynsetsByIds(Long[] synsetIds) {
        if (synsetIds == null || synsetIds.length == 0) {
            return null;
        }
        return getEntityManager().createQuery("SELECT s FROM Synset s WHERE s.id IN ( :synsetsID )", Synset.class)
                .setParameter("synsetsID", Arrays.asList(synsetIds))
                .getResultList();
    }

    public List<Synset> findSynsets(String filter, Sense filterObject, List<Long> lexicons) {
        if (filterObject == null) {
            return dbFastGetSynsets(filter, lexicons);
        }

        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append("SELECT s FROM Synset s"
                + " WHERE s IN ("
                + " SELECT DISTINCT( sts.synset ) FROM SenseToSynset sts"
                + " WHERE sts.sense.partOfSpeech = :pos ");
        params.put("pos", filterObject.getPartOfSpeech());
        if (null != filter && !"".equals(filter)) {
            sb.append("AND sts.sense.lexicon.id IN(:lexicons) AND sts.sense.lemma.word LIKE :lemma OR sts.sense.lemma.word LIKE :lemma1 ");
            params.put("lemma", filter + "%");
            params.put("lemma1", "%" + filter + "%");
            params.put("lexicons", lexicons);
        }
        sb.append(" ORDER BY sts.sense.lemma.word) ");

        TypedQuery<Synset> q = getEntityManager().createQuery(sb.toString(), Synset.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return q.getResultList();
    }

    public int deleteEmpty() {
        return getEntityManager().createQuery("DELETE FROM Synset s WHERE s.id NOT IN (SELECT DISTINCT s.synset.id FROM Sense s)")
                .executeUpdate();
    }

    public List<Synset> findSynsets(String filter) {
        Map<String, Object> params = new HashMap<>();

        String selectString = "SELECT s.synset FROM Sense s";
        if (filter != null && !"".equals(filter)) {
            selectString += " WHERE s.word.word LIKE :param1 OR s.word.word LIKE :param2 ";
            params.put("param1", filter + "%");
            params.put("param2", "%" + filter + "%");
        }
        selectString += " ORDER BY s.word.word";

        TypedQuery<Synset> query = getEntityManager().createQuery(selectString, Synset.class);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.getResultList();
    }

    public List<Synset> findNotEmptySynsets(String filter) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT s.synset FROM Sense s ");

        if (filter != null && !"".equals(filter)) {
            sb.append(" WHERE s.word.word LIKE :param1 OR s.word.word LIKE :param2 ");
            sb.append(" AND s.synset IS NOT NULL ");
            sb.append(" ORDER BY s.word.word");
            params.put("param1", filter + "%");
            params.put("param2", "%" + filter + "%");
        } else {
            sb.append(" WHERE AND s.synset IS NOT NULL ");
        }

        TypedQuery<Synset> query = getEntityManager().createQuery(sb.toString(), Synset.class);
        params.entrySet().stream().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        return query.getResultList();
    }

    public Synset findSynsetBySense(Sense sense, List<Long> lexicons) {
        return getEntityManager().createQuery("SELECT s.synset FROM Sense s WHERE s.id = :senseID AND s.lexicon.id IN ( :lexicons )", Synset.class)
                .setParameter("senseID", sense.getId())
                .setParameter("lexicons", lexicons)
                .getSingleResult();
    }

    public Synset findSynsetRelatedSynsets(Synset synset) {
        List<Synset> list = getEntityManager().createQuery(
                "SELECT sr.parent FROM SynsetRelation sr WHERE sr.parent.id IN ( SELECT sr.parent.id FROM SynsetRelation sr WHERE sr.parent.id = :synsetID ) OR sr.parent.id IN ( SELECT sr.child.id FROM SynsetRelation sr WHERE sr.child.id = :synsetID )", Synset.class)
                .setParameter("synsetID", synset.getId())
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    public List<Synset> dbGetSynsetsRels(List<Synset> synsets) {
        return getEntityManager().createQuery("SELECT sr.parent FROM SynsetRelation sr WHERE sr.id IN ( SELECT sr.id FROM SynsetRelation sr WHERE sr.parent IN ( :synsetList ) ) OR sr.id IN ( SELECT sr.id FROM SynsetRelation sr WHERE sr.child IN ( :synsetList ) )", Synset.class)
                .setParameter("synsetList", synsets)
                .getResultList();
    }

    public Long findSynsetsCount() {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM Synset s", Long.class)
                .getSingleResult();

    }

    public Map<Long, String> findSynsetsDescriptionIdx(List<Long> idx, List<Long> lexicons) {
        Map<Long, String> synsetsDescriptions = new HashMap<>();

        for (Long idSynset : idx) {
            String unitStr = rebuildUnitsStr(findById(idSynset), lexicons);
            synsetsDescriptions.put(idSynset, unitStr);
        }

        return synsetsDescriptions;
    }

    public String rebuildUnitsStr(Integer split, String unitsstr, List<Sense> senses, String locale) {
        // jest pusty synset
        if (senses == null || senses.isEmpty()) {
            unitsstr = "! S.y.n.s.e.t p.u.s.t.y !";
            split = 0;
        } else {
            StringBuilder temp = new StringBuilder();

            temp.append("(");

            int index = 0;
            int size = senses.size();
            int pos = split;
            if (pos > size) {
                pos = size;
            } else if (pos == 0) {
                pos = 1;
            }
            split = pos;

            for (Sense lexicalUnit : senses) {
                temp.append(lexicalUnit.toString());
                if (index == 0) {
                    temp.append(" [");
                    temp.append(lexicalUnit.getDomain().getName());
                    temp.append("]");
                }
                index++;
                if (index == pos) {
                    temp.append(" | ");
                } else {
                    temp.append(index < size ? ", " : "");
                }
            }
            temp.append(")");
            unitsstr = temp.toString();
        }
        return unitsstr;
    }

    public boolean contains(Sense unit, List<Sense> senses) {
        if (senses != null) {
            if (senses.stream().anyMatch((base) -> (base.getId().equals(unit.getId())))) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String lemma, List<Sense> senses) {
        if (senses != null) {
            for (Sense base : senses) {
                if (base.getWord().getWord().equals(lemma)) {
                    return true;
                }
            }
        }
        return false;
    }

    //    public String rebuildUnitsStr(Synset synset, List<Long> lexicons) {
//        // jest pusty synset
//        String unitsstr = "";
//        List<Sense> senses = synset.getSenses();
//        if (senses.isEmpty()) {
//            unitsstr = "! S.y.n.s.e.t p.u.s.t.y !";
//            synset.setSplit(0);
//        } else {
//            StringBuilder temp = new StringBuilder();
//
//            temp.append("(");
//
//            int index = 0, size = senses.size();
//            int pos = synset.getSplit();
//            if (pos > size) // czy nie jest za duzo
//            {
//                pos = size;
//            } else if (pos == 0) // jesli jest na zerowej pozycji, to przenoszony na 1
//            {
//                pos = 1;
//            }
//            synset.setSplit(pos);
//
//            for (Sense sense : senses) {
//                temp.append(sense.toString());
//                index++;
//                if (index == pos) {
//                    temp.append(" | ");
//                } else {
//                    temp.append(index < size ? ", " : "");
//                }
//            }
//            temp.append(")");
//            unitsstr = temp.toString();
//        }
//        return unitsstr;
//    }
    public Long fastGetPOSID(Synset synset) {
//        List<Long> ids = getEM().createNamedQuery("Synset.fastGetPOSID", Long.class)
//                .setParameter("idSynset", synset.getId())
//                .getResultList();
//
//        if (ids != null && ids.size() > 0 && ids.get(0) != null) {
//            return ids.get(0);
//        }
        return null;
    }

    public boolean exchangeSenses(Synset synset, Sense firstUnit, Sense secondUnit) {
        // pobranie wszystkich elementow dla synsetu
//        List<SenseToSynset> old = dbGetConnections(synset);
//
//        // zamiana numerow indeksow
//        for (SenseToSynset synsetDTO : old) {
//            // zamienieni indeksow
//            if (synsetDTO.getIdSense().longValue() == firstUnit.getId().longValue()) {
//                synsetDTO.setSenseIndex(synsetDTO.getSenseIndex() + 1);
//                dao.mergeObject(synsetDTO);
//            } else if (synsetDTO.getIdSense().longValue() == secondUnit.getId().longValue()) {
//                synsetDTO.setSenseIndex(synsetDTO.getSenseIndex() - 1);
//                dao.mergeObject(synsetDTO);
//            }
//        }
        return true;
    }

    public boolean addSenseToSynset(Sense unit, Synset synset, boolean rebuildUnitsStr) {

        // pobranie wszystkich elementow synsetu
//        List<SenseToSynset> old = dbGetConnections(synset);
//
//        // przeindeksowanie
//        int index = 0;
//        for (SenseToSynset synsetOld : old) {
//            // czy nie sa identyczne
//            if (synsetOld.getSynset().getId().equals(synset.getId())
//                    && synsetOld.getSense().getId().equals(unit.getId())) {
//                return false;
//            }
//            synsetOld.setSenseIndex(index++);
//            dao.mergeObject(synsetOld);
//        }
//
//        // dodanie nowego
//        SenseToSynset newRel = new SenseToSynset();
//        newRel.setSynset(synset);
//        newRel.setSense(unit);
//        newRel.setIdSense(unit.getId());
//        newRel.setIdSynset(synset.getId());
//        newRel.setSenseIndex(index++);
//
//        dao.persistObject(newRel);
        return true;
    }

    public Synset deleteSenseFromSynset(Sense unit, Synset synset) {
        // usuniecie jednego powiazania
//        dao.getEM()
//                .createNamedQuery("SenseToSynset.DeleteBySynsetIdAndSenseId")
//                .setParameter("idSynset", synset.getId())
//                .setParameter("idSense", unit.getId())
//                .executeUpdate();
//
//        // pobranie wszystkich elementow dla synsetu
//        List<SenseToSynset> rest = dbGetConnections(synset);
//
//        // przeindeksowanie
//        int index = 0;
//        for (SenseToSynset synsetDTO : rest) {
//            synsetDTO.setSenseIndex(index++);
//            dao.mergeObject(synsetDTO);
//        }
//
//        // synset jest pusty, nastepuje usuniecie takiego synsetu z bazy danych
//        if (rest.isEmpty()) {
//            saDAO.deleteAttributesFor(synset);
//            exeDAO.dbDeleteForSynset(synset);
//            exDAO.deleteForSynset(synset);
//            srDAO.dbDeleteConnection(synset);
//            dao.deleteObject(Synset.class, synset.getId());
//            return null;
//        }

        return synset;
    }

    public List<Sense> dbFastGetSenseBySynset(String filter, Domain domain,
                                              RelationType relationType, String definition, String comment,
                                              String artificial, int limitSize, List<Long> lexicons) {

//        CriteriaBuilder criteriaBuilder = getEM().getCriteriaBuilder();
//        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
//        Root<SenseToSynset> stsRoot = criteriaQuery.from(SenseToSynset.class);
//        Join<SenseToSynset, Sense> sense = stsRoot.join("sense", JoinType.LEFT);
//        Join<Sense, Word> word = sense.join("lemma", JoinType.LEFT);
//
//        List<Predicate> criteriaList = new ArrayList<>();
//
//        Predicate first_predicate = criteriaBuilder.like(
//                criteriaBuilder.lower(word.<String>get("word")), "%" + filter.toLowerCase() + "%");
//        criteriaList.add(first_predicate);
//
//        if (domain != null) {
//            Predicate secend_predicate = criteriaBuilder.equal(sense.get("domain").get("id"), domain.getId());
//            criteriaList.add(secend_predicate);
//        }
//        if (pos != null) {
//            Predicate third_predicate = criteriaBuilder.equal(sense.get("partOfSpeech").get("ubyLmfType"), pos);
//            criteriaList.add(third_predicate);
//        }
//        if (relationType != null) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetRelation> relRoot = subquery.from(SynsetRelation.class);
//            subquery.select(relRoot.get("synsetFrom").<Long>get("id"));
//            subquery.where(criteriaBuilder.equal(relRoot.get("relation").get("id"), relationType.getId()));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//
//        }
//        if (!definition.isEmpty()) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
//            subquery.select(relRoot.get("synset").<Long>get("id"));
//            List<Predicate> predicates = new ArrayList<>();
//            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.DEFINITION);
//            predicates.add(type);
//            Predicate value = criteriaBuilder.like(relRoot.<String>get("value").<String>get("text"), "%" + definition + "%");
//            predicates.add(value);
//            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        if (!comment.isEmpty()) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
//            subquery.select(relRoot.get("synset").<Long>get("id"));
//            List<Predicate> predicates = new ArrayList<>();
//            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.COMMENT);
//            predicates.add(type);
//            Predicate value = criteriaBuilder.like(relRoot.get("value").<String>get("text"), "%" + comment + "%");
//            predicates.add(value);
//            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        if (!artificial.isEmpty()) {
//            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
//            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
//            subquery.select(relRoot.get("synset").<Long>get("id"));
//            List<Predicate> predicates = new ArrayList<>();
//            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.ISABSTRACT);
//            predicates.add(type);
//            Predicate value = criteriaBuilder.equal(relRoot.get("value").<String>get("text"), artificial);
//            predicates.add(value);
//            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
//            criteriaList.add(subquery_predicate);
//        }
//        criteriaQuery.select(stsRoot.<Long>get("idSynset")).distinct(true);
//        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
//
//        final TypedQuery<Long> query = getEM().createQuery(criteriaQuery);
//        query.setFirstResult(0);
//        if (limitSize > 0) {
//            query.setMaxResults(limitSize);
//        }
//        List<Long> synsetIds = query.getResultList();
        List<Sense> result = new ArrayList<>();
//        for (Long id : synsetIds) {
//            result.addAll(dbFastGetUnits(id, lexicons));
//        }
        return result;
    }

    private String buildDataEntryLabel(Sense sense){
        StringBuilder labelBuilder = new StringBuilder();
        labelBuilder.append(sense.getWord().getWord()).append(" ")
                .append(sense.getVariant()).append(" ")
                .append(sense.getDomain().getName()); // TODO przetłumaczyć domene
        return labelBuilder.toString();
    }

    private DataEntry getDataEntry(Synset synset, Sense sense, List<SynsetRelation> relationsFrom, List<SynsetRelation> relationsTo){
        DataEntry dataEntry = new DataEntry();
        dataEntry.setSynset(synset);
        dataEntry.setRelsFrom(relationsFrom);
        dataEntry.setRelsTo(relationsTo);
        dataEntry.setLexicon(sense.getLexicon().getIdentifier());
        dataEntry.setPosID(sense.getPartOfSpeech().getId());
        dataEntry.setLabel(buildDataEntryLabel(sense));
        return dataEntry;
    }

    private void putDataEntryFromSynsetRelation(Map<Long, DataEntry> map, List<SynsetRelation> relationsList, boolean isRelationsFrom)
    {
        Synset relatedSynset;
        Sense sense;
        DataEntry dataEntry;
        for(SynsetRelation relation : relationsList){
            if(isRelationsFrom){
                relatedSynset = relation.getChild();
            } else {
                relatedSynset = relation.getParent();
            }
            sense = relatedSynset.getSenses().get(0);
            Hibernate.initialize(sense.getIncomingRelations());
            Hibernate.initialize(sense.getOutgoingRelations());
            dataEntry = getDataEntry(relatedSynset, sense, relatedSynset.getOutgoingRelations(), relatedSynset.getIncomingRelations());
            map.put(relatedSynset.getId(), dataEntry);
        }
    }

    public Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons) {
        Synset relatedSynset = synset;
        Sense sense;
        Map<Long, DataEntry> resultMap = new HashMap<>();
        sense = senseRepository.findHeadSenseOfSynset(synset.getId());
        List<SynsetRelation> relationsFrom  = synsetRelationRepository.findRelationsWhereSynsetIsParent(relatedSynset, lexicons);
        List<SynsetRelation> relationsTo = synsetRelationRepository.findRelationsWhereSynsetIsChild(relatedSynset, lexicons);
        relatedSynset.setOutgoingRelations(relationsFrom); // TODO przyjrzeć sie temu, prawdopodobnie jest to niepotrzebne
        relatedSynset.setIncomingRelations(relationsTo);
        DataEntry dataEntry = getDataEntry(relatedSynset, sense, relationsFrom, relationsTo);
        resultMap.put(relatedSynset.getId(), dataEntry);
        putDataEntryFromSynsetRelation(resultMap, relationsFrom, true);
        putDataEntryFromSynsetRelation(resultMap, relationsTo, false);

        return resultMap;
//        if (!rootEntry.getRelsFrom().isEmpty() || !rootEntry.getRelsFrom().isEmpty()) {
//            List<SynsetRelation> relations = synsetRelationRepository.findRelations(synset);
//            map.put(rootEntry.getSynset().getId(), rootEntry);

            //TODO odkomentować i przerobić
//            List<SynsetInfo> infos = em.createQuery(
//                    "SELECT NEW pl.edu.pwr.wordnetloom.model.dto.SynsetInfo(sy.id, se.partOfSpeech.id, name.text, lemma.word, syt.value.text, se.senseNumber, lexId.text) " +
//                            "FROM Sense AS se JOIN se.synset AS sy  " +
//                            "JOIN se.domain AS dom " +
//                            "JOIN dom.nameStrings AS name " +
//                            "JOIN sy.synsetAttributes AS syt " +
//                            "JOIN se.word as lemma " +
//                            "JOIN se.lexicon as lex " +
//                            "JOIN lex.identifier as lexId " +
//                            "WHERE se.synset_position = 0 AND syt.type.typeName.text = :abstractName AND sy.id IN (:ids)",
//                    SynsetInfo.class)
//                    .setParameter("abstractName", 0) // TODO zobaczyć, czy 0 to dobra wartość
//                    .setParameter("ids", lexicons)
//                    .getResultList();
//            List<CountInfo> counts = em.createQuery("SELECT NEW pl.edu.pwr.wordnetloom.model.dto.CountInfo(sy.id, count(se)) " +
//                    "FROM Synset AS sy JOIN sy.senseToSynset AS sts JOIN sts.sense AS se " +
//                    "WHERE sy.id IN (:ids) GROUP BY sy.id", CountInfo.class)
//                    .setParameter("ids", lexicons)
//                    .getResultList();
//            Map<Long, CountInfo> counter = counts.stream().collect(Collectors.toMap(CountInfo::getSynsetID, p -> p));
//
//            for (SynsetInfo synsetInfo : infos) {
//                DataEntry dataEntry = map.get(synsetInfo.getSynsetID());
//                StringBuilder stringBuilder = new StringBuilder();
//
//            }
//        }
//
//        return null;
    }

    @Override
    protected Class<Synset> getPersistentClass() {
        return Synset.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
