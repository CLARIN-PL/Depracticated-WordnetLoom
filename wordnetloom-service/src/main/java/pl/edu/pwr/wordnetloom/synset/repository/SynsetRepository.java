package pl.edu.pwr.wordnetloom.synset.repository;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.repository.SenseRepository;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.synsetrelation.repository.SynsetRelationRepository;
import pl.edu.pwr.wordnetloom.word.model.Word;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

@Stateless
public class SynsetRepository extends GenericRepository<Synset> {

    @Inject
    EntityManager em;

    @Inject
    SynsetRelationRepository synsetRelationRepository;

    @Inject
    SenseRepository senseRepository;

    @Override
    public void delete(Synset synset){
        Synset loadedSynset = synset;
        synsetRelationRepository.deleteConnection(synset);
        getEntityManager().createQuery("DELETE FROM Synset WHERE id = :id")
                .setParameter("id", synset.getId())
                .executeUpdate();
//        synset.getOutgoingRelations().clear();
//        synset.getIncomingRelations().clear();
//        if(!getEntityManager().contains(synset)) {
//            loadedSynset = getEntityManager().merge(synset);
//        }
//        getEntityManager().remove(loadedSynset);
    }

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

    public Synset updateSynset(Synset synset){
        if(getEntityManager().contains(synset)){
            getEntityManager().persist(synset);
            return synset;
        } else {
            return getEntityManager().merge(synset);
        }
    }

    public void addSenseToSynset(Sense unit, Synset synset) {
        // pobranie wszystkich elementow synsetu
        List<Sense> sensesInSynset = senseRepository.findBySynset(synset.getId());
//        int index = 0;
//        for(Sense sense :  sensesInSynset){
//            //TODO tutaj było jakieś sprawdzenie, czy nie sa identyczne
//            sense.setSynsetPosition(index++);
//            getEntityManager().merge(sense);
//        }
        //dodanie jednostki do synsetu
        unit.setSynset(synset);
        unit.setSynsetPosition(sensesInSynset.size());
        getEntityManager().merge(unit);
    }

    public void deleteSensesFromSynset(Collection<Sense> senses, Synset synset) {
        // usunięcie jednostek z synsetu
        for(Sense sense : senses){
            sense.setSynset(null);
            sense.setSynsetPosition(null);
            getEntityManager().merge(sense);
        }
        reindexSensesInSynset(synset);
    }

    private int reindexSensesInSynset(Synset synset){
        //TODO można dodać jeszcze sprawdzenie, czy indeksowanie jest potrzebne
        List<Sense> sensesInSynset = senseRepository.findBySynset(synset.getId());
        int index = 0;
        for(Sense sense : sensesInSynset) {
            sense.setSynsetPosition(index++);
            getEntityManager().merge(sense);
        }
        return index;
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


    //TODO zmienić nazwę
    private Synset findSynsetWithRelationsAndSenseById(Long id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Synset> cq = getSynsetCriteriaQuery(id, cb, 0);

        TypedQuery<Synset> query = getEntityManager().createQuery(cq);
        try {
           return  query.getSingleResult();
        } catch (NoResultException ex) {
            cq = getSynsetCriteriaQuery(id, cb, 1);
            query = getEntityManager().createQuery(cq);
            return query.getSingleResult();
        }
    }

    private CriteriaQuery<Synset> getSynsetCriteriaQuery(Long id, CriteriaBuilder cb, int pos) {
        CriteriaQuery<Synset> cq = cb.createQuery(Synset.class);

        Root<Synset> root = cq.from(Synset.class);
        Join<Synset, Sense> senseJoin = root.join("senses");
        Fetch<Synset, Sense> senseFetch = root.fetch("senses");
        senseFetch.fetch("word");
        senseFetch.fetch("partOfSpeech");
        List<Predicate> predicatesList = new ArrayList<>();
        Predicate idPredicate = cb.equal(root.get("id"), id);
        predicatesList.add(idPredicate);
        Predicate sensePredicate = cb.equal(senseJoin.get("synsetPosition"), pos);
        predicatesList.add(sensePredicate);
        cq.where(predicatesList.toArray(new Predicate[0]));
        return cq;
    }

    public Map<Long, DataEntry> prepareCacheForRootNode(final Long synsetId, final List<Long> lexicons, int numSynsetOnDirection, NodeDirection[] directions) {
        Map<Long, DataEntry> result = new HashMap<>();
        // łączenie synsetu z jednostką, aby uzyskać opis (wyraz, domene, wariant)
        Synset synset = findSynsetWithRelationsAndSenseById(synsetId);
        // pobranie relacji dla synsetu. Relacje pobierane są wraz z połączonymi synsetami oraz ich opisami (wyraz, domena, wariant)
        List<SynsetRelation> relationsFrom = synsetRelationRepository.findRelationsWhereSynsetIsParent(synset, lexicons, directions);
        List<SynsetRelation> relationsTo = synsetRelationRepository.findRelationsWhereSynsetIsChild(synset, lexicons, directions);
        //szukanie i usuwanie relacji, które pojawiają się na liście relacji "od" i na liście relacji "do"
        deleteRepeatingRelations(relationsFrom, relationsTo);
        relationsFrom.addAll(relationsTo);
        // sortowanie listy alfabetycznie
        relationsFrom.sort(new RelationWordComparator(synsetId));
        // wyszukiwanie kilku pierwszych synsetów z każdego kierunku, które zostaną pokazane na grafie
        List<Integer> indexesRelationsFrom = getIndexRelationsToShow(relationsFrom, numSynsetOnDirection, synsetId);
        // pobranie relacji dla synsetów które zostaną pokazane
        fillRelations(relationsFrom, indexesRelationsFrom, synsetId, lexicons);
        // budowanie wyniku
        DataEntry dataEntry = buildDataEntry(synset, relationsFrom);
        result.put(synset.getId(), dataEntry);
        putDataEntryFromSynsetRelation(result, relationsFrom, synsetId);
        return result;
    }

    private DataEntry buildDataEntry(Synset synset, List<SynsetRelation> relations){
        return getDataEntry(synset, synset.getSenses().get(0), relations);
    }

    private void deleteRepeatingRelations(List<SynsetRelation> relationsFrom, List<SynsetRelation> relationsTo) {
        for(SynsetRelation parent : relationsFrom) {
            for(SynsetRelation child : relationsTo) {
                if(child.getParent().getId().equals(parent.getChild().getId())){
                    relationsTo.remove(child);
                    break;
                }
            }
        }
    }

    private DataEntry getDataEntry(Synset synset, Sense sense, List<SynsetRelation> relations) {
        DataEntry dataEntry = new DataEntry();
        dataEntry.setSynset(synset);
        NodeDirection direction;
        for(SynsetRelation relation : relations) {
            if(relation.getChild().getId().equals(synset.getId())) {
                direction = relation.getRelationType().getNodePosition().getOpposite();
            } else {
                direction = relation.getRelationType().getNodePosition() ;
            }
            dataEntry.addRelation(relation, direction);
        }
        dataEntry.setLexicon(sense.getLexicon().getIdentifier());
        dataEntry.setPosID(sense.getPartOfSpeech().getId());
        dataEntry.setName(sense.getWord().getWord());
        dataEntry.setVariant(String.valueOf(sense.getVariant()));
        dataEntry.setDomain(sense.getDomain().getName());
        return dataEntry;
    }

    private void putDataEntryFromSynsetRelation(Map<Long, DataEntry> map, List<SynsetRelation> relationsList, Long isRelationsFrom) {
        Synset relatedSynset;
        Sense sense;
        DataEntry dataEntry;
        PersistenceUnitUtil unitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil(); // narzedzie do okreslania, czy kolekcja została załadowana
        for (SynsetRelation relation : relationsList) {
            if (isRelationsFrom.equals(relation.getParent().getId())) {
                relatedSynset = relation.getChild();
            } else {
                relatedSynset = relation.getParent();
            }
            sense = relatedSynset.getSenses().get(0);
            if(unitUtil.isLoaded(relatedSynset.getOutgoingRelations())){
                deleteRepeatingRelations(relatedSynset.getOutgoingRelations(), relatedSynset.getIncomingRelations());
                relatedSynset.getOutgoingRelations().addAll(relatedSynset.getIncomingRelations());
                List<SynsetRelation> allRelationsList = new ArrayList<>(relatedSynset.getOutgoingRelations());
                allRelationsList.addAll(relatedSynset.getIncomingRelations());
                dataEntry = getDataEntry(relatedSynset, sense, allRelationsList);

            } else {
                dataEntry = getDataEntry(relatedSynset, sense, new ArrayList<>());
            }

            map.put(relatedSynset.getId(), dataEntry);
        }
    }

    private class RelationWordComparator implements Comparator<SynsetRelation>
    {
        private Long synsetId;

        RelationWordComparator(Long synsetId) {
            this.synsetId = synsetId;
        }

        @Override
        public int compare(SynsetRelation o1, SynsetRelation o2) {
            String word1;
            String word2;
            if(o1.getParent().getId().equals(synsetId))
            {
                word1 = o1.getChild().getSenses().get(0).getWord().getWord();
            } else {
                word1 = o1.getParent().getSenses().get(0).getWord().getWord();
            }

            if(o2.getParent().getId().equals(synsetId)) {
                word2 = o2.getChild().getSenses().get(0).getWord().getWord();
            } else {
                word2 = o2.getParent().getSenses().get(0).getWord().getWord();
            }

            return word1.toLowerCase().compareTo(word2.toLowerCase());
        }
    }

    private List<Integer> getIndexRelationsToShow(List<SynsetRelation> synsetsList, int numRelationsOnDirection, Long synsetId) {
        final int NUM_DIRECTION = 4;
        int[] directionCounter = new int[NUM_DIRECTION];
        Arrays.fill(directionCounter, 0);

        int filledDirectionsCounter = 0;

        List<Integer> resultList = new ArrayList<>();

        for (int i = 0; i < synsetsList.size(); i++) {
            int direction = synsetsList.get(i).getRelationType().getNodePosition().ordinal();
            if(synsetsList.get(i).getChild().getId().equals(synsetId))
            {
                direction = synsetsList.get(i).getRelationType().getNodePosition().getOpposite().ordinal();
            }
            if(direction == NodeDirection.IGNORE.ordinal()){
                continue;
            }
            if (directionCounter[direction] != numRelationsOnDirection) {
                resultList.add(i);
                directionCounter[direction]++;
                if (directionCounter[direction] == numRelationsOnDirection) {
                    filledDirectionsCounter++;
                }
                if (filledDirectionsCounter == NUM_DIRECTION) {
                    return resultList;
                }
            }
        }
        return resultList;
    }

    private void fillRelations(List<SynsetRelation> relations, List<Integer> indexesRelationsToExtend, Long synsetIsParent, List<Long> lexicons) {
        SynsetRelation relation;
        Synset synset;
        for(Integer i : indexesRelationsToExtend)
        {
            relation = relations.get(i);
            if(synsetIsParent.equals(relation.getParent().getId())) {
                synset = relation.getChild();
            } else {
                synset = relation.getParent();
            }
            List<SynsetRelation> relationsFrom = synsetRelationRepository.findSimpleRelationsWhereSynsetIsParent(synset,lexicons);
            List<SynsetRelation> relationsTo = synsetRelationRepository.findSimpleRelationsWhereSynsetIsChild(synset, lexicons);
            synset.setOutgoingRelations(relationsFrom);
            synset.setIncomingRelations(relationsTo);
        }
    }

    public DataEntry findSynsetDataEntry(Long synsetId, List<Long> lexicons) {
        Synset newSynset = findSynsetWithRelationsAndSenseById(synsetId);
        List<SynsetRelation> relationsFrom = synsetRelationRepository.findSimpleRelationsWhereSynsetIsParent(newSynset, lexicons);
        List<SynsetRelation> relationsTo = synsetRelationRepository.findSimpleRelationsWhereSynsetIsChild(newSynset, lexicons);
        relationsFrom.addAll(relationsTo);
        return buildDataEntry(newSynset, relationsFrom);
    }

    //TODO przetestować każdy z warunków
    public List<Synset> findSynsetsByCriteria(SynsetCriteriaDTO criteria){
        CriteriaQuery<Synset> query = getSynsetCriteriaQuery(criteria, false);
        query.distinct(true);
        Query selectQuery = getEntityManager().createQuery(query);
        if(criteria.getLimit() > 0){
            selectQuery.setMaxResults(criteria.getLimit());
        }
        if(criteria.getOffset() > 0){
            selectQuery.setFirstResult(criteria.getOffset());
        }
        return selectQuery.getResultList();
    }

    public int getCountSynsetsByCriteria(SynsetCriteriaDTO criteria) {
        CriteriaQuery<Long> query = getSynsetCriteriaQuery(criteria, true);
        return Math.toIntExact(getEntityManager().createQuery(query).getSingleResult());
    }

    private CriteriaQuery getSynsetCriteriaQuery(SynsetCriteriaDTO criteria, boolean countStatement) {

        final String SENSES = "senses";
        final String WORD = "word";
        final String DOMAIN = "domain";
        final String LEXICON = "lexicon";
        final String PART_OF_SPEECH = "partOfSpeech";
        final String INCOMING_RELATIONS = "incomingRelations";
        final String OUTGOING_RELATIONS = "outgoingRelations";
        final String RELATION_TYPE = "relationType";
        final String SYNSET_ATTRIBUTE = "synsetAttributes";
        final String COMMENT = "comment";
        final String DEFINITION = "definition";
        final String IS_ABSTRACT = "isAbstract";

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery query;

        if(countStatement) {
            query = criteriaBuilder.createQuery(Long.class);
        } else {
            query = criteriaBuilder.createQuery(Synset.class);
        }

        Root<Synset> synsetRoot = query.from(Synset.class);
        Join<Synset, Sense> senseJoin = synsetRoot.join(SENSES);
        Join<Synset, Word> wordJoin = senseJoin.join(WORD);

        if(!countStatement) {
            Fetch<Synset, Sense> sensesFetch = synsetRoot.fetch(SENSES);
            sensesFetch.fetch(WORD);
            sensesFetch.fetch(DOMAIN);
            sensesFetch.fetch(LEXICON);
        }

        List<Predicate> criteriaList = new ArrayList<>();

        if(criteria.getLemma() != null && !criteria.getLemma().isEmpty()) {
            Predicate lemmaPredicate = criteriaBuilder.like(wordJoin.get(WORD), criteria.getLemma() + "%");
            criteriaList.add(lemmaPredicate);
        }

        if(criteria.getLexiconId() != null){
            Predicate lexiconPredicate = criteriaBuilder.equal(senseJoin.get(LEXICON),criteria.getLexiconId());
            criteriaList.add(lexiconPredicate);
        }

        if(criteria.getPartOfSpeechId() != null){
            Predicate partOfSpeechPredicate = criteriaBuilder.equal(senseJoin.get(PART_OF_SPEECH), criteria.getPartOfSpeechId());
            criteriaList.add(partOfSpeechPredicate);
        }

        if(criteria.getRelationTypeId() != null) {
            Join<Synset, SynsetRelation> outgoingRelationsJoin = synsetRoot.join(OUTGOING_RELATIONS);
            Predicate relationsPredicates = criteriaBuilder.equal(outgoingRelationsJoin.get(RELATION_TYPE), criteria.getRelationTypeId());
            criteriaList.add(relationsPredicates);
        }

        if(criteria.getDomainId() != null){
            Predicate domainPredicate = criteriaBuilder.equal(senseJoin.get(DOMAIN), criteria.getDomainId());
            criteriaList.add(domainPredicate);
        }

        if(criteria.getComment() != null || criteria.getDefinition() != null || criteria.isAbstract() != null){
            Join<Synset, SynsetAttributes> synsetAttributeJoin = synsetRoot.join(SYNSET_ATTRIBUTE);
            if(criteria.getComment() != null){
                Predicate commentPredicate = criteriaBuilder.like(synsetAttributeJoin.get(COMMENT), "%"+criteria.getComment()+"%");
                criteriaList.add(commentPredicate);
            }
            if(criteria.getDefinition() != null){
                Predicate definitionPredicate = criteriaBuilder.like(synsetAttributeJoin.get(DEFINITION), "%"+criteria.getDefinition()+"%");
                criteriaList.add(definitionPredicate);
            }
            if(criteria.isAbstract() != null){
                Predicate abstractPredicate = criteriaBuilder.equal(synsetAttributeJoin.get(IS_ABSTRACT), criteria.isAbstract());
                criteriaList.add(abstractPredicate);
            }
        }

        if(countStatement) {
            query.select(criteriaBuilder.count(synsetRoot));
        } else {
            query.select(synsetRoot);
        }

        query.where(criteriaList.toArray(new Predicate[0]));

        return query;
    }

    public Synset fetchSynset(Long synsetId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Synset> query = criteriaBuilder.createQuery(Synset.class);
        Root<Synset> synsetRoot = query.from(Synset.class);
        Join<Synset, Sense> senseJoin = synsetRoot.join("senses");
        Fetch<Synset, SynsetAttributes> attributesFetch = synsetRoot.fetch("synsetAttributes", JoinType.LEFT);
        attributesFetch.fetch("owner", JoinType.LEFT);
        Fetch<Synset, Sense> senseFetch = synsetRoot.fetch("senses", JoinType.LEFT);
        senseFetch.fetch("domain");
        senseFetch.fetch("senseAttributes", JoinType.LEFT);


        Predicate[] predicates = new Predicate[2];
        predicates[0] = criteriaBuilder.equal(synsetRoot.get("id"), synsetId);
        predicates[1] = criteriaBuilder.equal(senseJoin.get("synsetPosition"), 0);
        query.where(predicates);

        return getEntityManager().createQuery(query).getSingleResult();
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