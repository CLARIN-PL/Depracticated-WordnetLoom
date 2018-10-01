package pl.edu.pwr.wordnetloom.synset.repository;

import org.hibernate.Hibernate;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
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
import java.util.concurrent.*;
import java.util.logging.Logger;

@Stateless
public class SynsetRepository extends GenericRepository<Synset> {

    @Inject
    EntityManager em;

    @Inject
    SynsetRelationRepository synsetRelationRepository;

    @Inject
    Logger logger;

    private final int FIRST_SYNSET_POSITION = 0;

    private final String SENSES = "senses";
    private final String WORD = "word";
    private final String DOMAIN = "domain";
    private final String LEXICON = "lexicon";
    private final String STATUS = "status";
    private final String PART_OF_SPEECH = "partOfSpeech";
    private final String RELATION_TYPE = "relationType";
    private final String SYNSET_ATTRIBUTE = "synsetAttributes";
    private final String COMMENT = "comment";
    private final String DEFINITION = "definition";
    private final String IS_ABSTRACT = "isAbstract";
    private final String RELATION_PARENT = "parent";
    private final String ID = "id";
    private final String OWNER = "owner";
    private final String SENSE_ATTRIBUTE = "senseAttributes";
    private final String SYNSET_POSITION = "synsetPosition";

    @Override
    public void delete(Synset synset){
        getEntityManager().createQuery("DELETE FROM Synset WHERE id = :id")
                .setParameter("id", synset.getId())
                .executeUpdate();
    }

    public List<Synset> findSynsetsByWord(String word, List<Long> lexicons) {

        Query query = getEntityManager().createQuery("SELECT s.synset FROM Sense s WHERE s.word.word = :word AND s.lexicon.id IN (:lexicon)");
        query.setParameter("word", word);
        query.setParameter("lexicon", lexicons);
        return query.getResultList();
    }

    public Synset findSynset(Synset synset, List<Long> lexicons) {
        return getEntityManager().createQuery("SELECT s FROM Synset s JOIN FETCH s.senses WHERE s.lexicon.id IN ( :lexicons ) AND  s.id = :id", Synset.class)
                .setParameter("id", synset.getId())
                .setParameter("lexicons", lexicons)
                .getSingleResult();
    }


    public Integer findSynsetSenseCount(Synset synset) {
        return findById(synset.getId()).getSenses().size();
    }

    public PartOfSpeech findSynsetPartOfSpeech(Synset synset) {
        List<Sense> senses = findById(synset.getId()).getSenses();
        if (senses.isEmpty() || senses.get(0) == null) {
            return null;
        }

        return senses.get(0).getPartOfSpeech();
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
        return getEntityManager().createQuery("SELECT DISTINCT s FROM Synset s WHERE s.id IN ( :synsetsID )", Synset.class)
                .setParameter("synsetsID", Arrays.asList(synsetIds))
                .getResultList();
    }


    public int deleteEmpty() {
        return getEntityManager().createQuery("DELETE FROM Synset s WHERE s.id NOT IN (SELECT DISTINCT s.synset.id FROM Sense s)")
                .executeUpdate();
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

    //TODO zmienić nazwę
    private Synset findSynsetWithRelationsAndSenseById(Long id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Synset> cq = getSynsetCriteriaQuery(id, cb, FIRST_SYNSET_POSITION);

        TypedQuery<Synset> query = getEntityManager().createQuery(cq);
        return query.getSingleResult();
    }

    private CriteriaQuery<Synset> getSynsetCriteriaQuery(Long id, CriteriaBuilder cb, int pos) {
        CriteriaQuery<Synset> cq = cb.createQuery(Synset.class);

        Root<Synset> root = cq.from(Synset.class);
        Join<Synset, Sense> senseJoin = root.join("senses");

        Fetch<Synset, Sense> senseFetch = root.fetch("senses");
        senseFetch.fetch("partOfSpeech");

        List<Predicate> predicatesList = new ArrayList<>();
        Predicate idPredicate = cb.equal(root.get("id"), id);
        predicatesList.add(idPredicate);

        Predicate sensePredicate = cb.equal(senseJoin.get("synsetPosition"), pos);
        predicatesList.add(sensePredicate);

        cq.where(predicatesList.toArray(new Predicate[0]));
        return cq;
    }

    public Map<Long, DataEntry> prepareCacheForRootNode(final Long synsetId, final List<Long> lexicons, int numSynsetOnDirection, NodeDirection[] directions) throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
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

        logger.info("Loading Synset: " + synsetId+" took: "+ + (System.currentTimeMillis() - now) +"ms");
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
        dataEntry.setLexicon(sense.getLexicon().getLanguageShortcut()+".png");
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

            if(synsetsList.get(i).getChild().getId().equals(synsetId)) {
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
        for(Integer i : indexesRelationsToExtend){

            Synset synset;

            SynsetRelation relation = relations.get(i);

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

    public List<Synset> findSynsetsByCriteria(SynsetCriteriaDTO criteria){

        List<Synset> result;
        // when criteria have id, searching only by id
        if(criteria.getSynsetId() != null){
            // TODO do przetestowania
            result = findSynsetsByIds(new Long[]{criteria.getSynsetId()});
        } else {
            CriteriaQuery<Synset> query = getSynsetCriteriaQuery(criteria, false);
            query.distinct(true);
            Query selectQuery = getEntityManager().createQuery(query);
            if(criteria.getLimit() > 0){
                selectQuery.setMaxResults(criteria.getLimit());
            }
            if(criteria.getOffset() > 0){
                selectQuery.setFirstResult(criteria.getOffset());
            }

            result = selectQuery.getResultList();
        }
        //loading lazy objects. Loading objects for result in this moment is faster than fetching in query
        fetchLazyObject(result);
        return result;
    }

    private void fetchLazyObject(List<Synset> result) {
        for(Synset synset : result){
            Hibernate.initialize(synset.getSenses());
            for(Sense sense : synset.getSenses()){
                Hibernate.initialize(sense.getDomain());
                Hibernate.initialize(sense.getLexicon());
            }
        }
    }

    public int getCountSynsetsByCriteria(SynsetCriteriaDTO criteria) {
        if (criteria.getSynsetId() != null){
            return 1;
        }
        CriteriaQuery<Long> query = getSynsetCriteriaQuery(criteria, true);
        return Math.toIntExact(getEntityManager().createQuery(query).getSingleResult());
    }


    private CriteriaQuery getSynsetCriteriaQuery(SynsetCriteriaDTO criteria, boolean countStatement) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery query;

        if(countStatement) {
            query = criteriaBuilder.createQuery(Long.class); // count query
        } else {
            query = criteriaBuilder.createQuery(Synset.class); // select synsets query
        }

        Root<Synset> synsetRoot = query.from(Synset.class);
        List<Predicate> criteriaList = new ArrayList<>();

        if(criteria.getStatus() != null){
            Predicate statusPredicate = criteriaBuilder.equal(synsetRoot.get(STATUS), criteria.getStatusId());
            criteriaList.add(statusPredicate);
        }

        if(criteria.getLemma()!=null || criteria.getLexiconId() != null || criteria.getPartOfSpeechId() != null || criteria.getDomainId() != null){
            Join<Synset, Sense> senseJoin = synsetRoot.join(SENSES, JoinType.LEFT);

            if(criteria.getLemma() != null && !criteria.getLemma().isEmpty()) {
                Join<Sense, Word> wordJoin = senseJoin.join(WORD, JoinType.LEFT);
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

            if(criteria.getDomainId() != null){
                Predicate domainPredicate = criteriaBuilder.equal(senseJoin.get(DOMAIN), criteria.getDomainId());
                criteriaList.add(domainPredicate);
            }
        }

        if(criteria.getComment() != null || criteria.getDefinition() != null){

            CriteriaQuery<Long> q = criteriaBuilder.createQuery(Long.class);
            Subquery<Long> subquery = q.subquery(Long.class);
            Root<SynsetAttributes> attributesRoot = subquery.from(SynsetAttributes.class);
            subquery.select(attributesRoot.get("synset").get("id"));

            List<Predicate> predicates = new ArrayList<>();

            if(criteria.getComment() != null && ! criteria.getComment().isEmpty()){
               Predicate commentPredicate = criteriaBuilder.like(attributesRoot.get(COMMENT), "%"+criteria.getComment()+"%");
               predicates.add(commentPredicate);
            }

            if(criteria.getDefinition() != null && ! criteria.getDefinition().isEmpty()){
                Predicate definitionPredicate = criteriaBuilder.like(attributesRoot.get(DEFINITION), "%"+criteria.getDefinition()+"%");
                predicates.add(definitionPredicate);
            }

            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            Predicate subquery_predicate = criteriaBuilder.in(synsetRoot.get("id")).value(subquery);
            criteriaList.add(subquery_predicate);
        }

        if(criteria.getRelationTypeId() != null) {
            Subquery<Long> relationsSubquery = query.subquery(Long.class);
            Root<SynsetRelation> relationsRoot = relationsSubquery.from(SynsetRelation.class);
            relationsSubquery.distinct(true);
            relationsSubquery.select(relationsRoot.get(RELATION_PARENT));
            relationsSubquery.where(criteriaBuilder.equal(relationsRoot.get(RELATION_TYPE), criteria.getRelationTypeId()));
            criteriaList.add(synsetRoot.get(ID).in(relationsSubquery));
        }

        if(criteria.isAbstract() != null){
            Predicate abstractPredicate = criteriaBuilder.equal(synsetRoot.get(IS_ABSTRACT), criteria.isAbstract());
            criteriaList.add(abstractPredicate);
        }

        if(countStatement) {
            query.select(criteriaBuilder.countDistinct(synsetRoot.get("id")));
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
        Join<Synset, Sense> senseJoin = synsetRoot.join(SENSES);

        Fetch<Synset, Sense> senseFetch = synsetRoot.fetch(SENSES, JoinType.LEFT);
        senseFetch.fetch(DOMAIN);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = criteriaBuilder.equal(synsetRoot.get(ID), synsetId);
        predicates[1] = criteriaBuilder.equal(senseJoin.get(SYNSET_POSITION), FIRST_SYNSET_POSITION);
        query.where(predicates);

        return getEntityManager().createQuery(query).getSingleResult();
    }

    public SynsetAttributes fetchSynsetAttributes(Long synsetId){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SynsetAttributes> query = criteriaBuilder.createQuery(SynsetAttributes.class);

        Root<SynsetAttributes> root = query.from(SynsetAttributes.class);
        root.fetch("owner", JoinType.LEFT);
        root.fetch( "examples", JoinType.LEFT);

        Predicate predicate =  criteriaBuilder.equal(root.get("id"), synsetId);
        query.where(predicate);

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