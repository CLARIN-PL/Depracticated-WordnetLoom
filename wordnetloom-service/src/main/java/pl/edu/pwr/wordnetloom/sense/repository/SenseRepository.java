package pl.edu.pwr.wordnetloom.sense.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.word.model.Word;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class SenseRepository extends GenericRepository<Sense> {

    @Inject
    EntityManager em;

    public Sense clone(Sense sense) {
        return persist(new Sense(sense));
    }

    @Override
    public void delete(Sense sense) {
        Sense s = findById(sense.getId());

        if (s != null) {
            SenseAttributes senseAttributes = getEntityManager().find(SenseAttributes.class, s.getId());
            getEntityManager().remove(senseAttributes);
        }
        super.delete(s);
    }

    public void deleteAll() {
        List<Sense> senses = findAll("id");
        delete(senses);
    }

    public List<Sense> findByCriteria(SenseCriteriaDTO dto) {
        List<Sense> senses = getSensesByCriteria(dto);
        return senses;
    }

    private final String WORD = "word";
    private final String DOMAIN = "domain";
    private final String LEXICON = "lexicon";
    private final String PART_OF_SPEECH = "partOfSpeech";
    private final String VARIANT = "variant";
    private final String INCOMING_RELATIONS = "incomingRelations";
    private final String OUTGOING_RELATIONS = "outgoingRelations";
    private final String RELATION_TYPE = "relationType";
    private final String REGISTER = "register";
    private final String COMMENT = "comment";
    private final String EXAMPLES = "examples";
    private final String SYNSET = "synset";

    private List<Sense> getSensesByCriteria(SenseCriteriaDTO dto) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Sense> query = criteriaBuilder.createQuery(Sense.class);
        Root<Sense> senseRoot = query.from(Sense.class);
        Join<Sense, Word> wordJoin = senseRoot.join(WORD);
        senseRoot.fetch(WORD);
        senseRoot.fetch(DOMAIN);
        senseRoot.fetch(LEXICON);

        query.select(senseRoot);
        query.where(getPredicatesByCriteria(dto, senseRoot, wordJoin, criteriaBuilder));

        List<Order> orders = new ArrayList<>();
        orders.add(criteriaBuilder.asc(wordJoin.get(WORD)));
        orders.add(criteriaBuilder.asc(senseRoot.get(PART_OF_SPEECH)));
        orders.add(criteriaBuilder.asc(senseRoot.get(VARIANT)));
        orders.add(criteriaBuilder.asc(senseRoot.get(LEXICON)));

        query.orderBy(orders);
        Query selectQuery = em.createQuery(query);
        if (dto.getLimit() > 0) {
            selectQuery.setMaxResults(dto.getLimit());
        }
        if (dto.getOffset() > 0) {
            selectQuery.setFirstResult(dto.getOffset());
        }

        // remove duplicates
        List<Sense> result = selectQuery.getResultList();
        Set<Sense> resultSet = new LinkedHashSet<>(result);
        return new ArrayList<>(resultSet);
    }

    //TODO Refactor to Specifications
    private Predicate[] getPredicatesByCriteria(SenseCriteriaDTO dto, Root senseRoot, Join wordJoin, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();

        if (dto.getLemma() != null && !dto.getLemma().isEmpty()) {
            Predicate lemmaPredicate = criteriaBuilder.like(wordJoin.get(WORD), dto.getLemma() + "%");
            predicateList.add(lemmaPredicate);
        }

        Predicate lexiconPredicate = senseRoot.get(LEXICON).in(dto.getLexicons());
        predicateList.add(lexiconPredicate);

        if (dto.getPartOfSpeechId() != null) {
            Predicate partOfSpeechPredicate = criteriaBuilder.equal(senseRoot.get(PART_OF_SPEECH), dto.getPartOfSpeechId());
            predicateList.add(partOfSpeechPredicate);
        }

        if (dto.getDomainId() != null) {
            Predicate domainPredicate = criteriaBuilder.equal(senseRoot.get(DOMAIN), dto.getDomainId());
            predicateList.add(domainPredicate);
        }

        if (dto.getRelationTypeId() != null) {
            Join<Sense, SenseRelation> incomingRelationsJoin = senseRoot.join(INCOMING_RELATIONS, JoinType.LEFT);
            Join<Sense, SenseRelation> outgoingRelation = senseRoot.join(OUTGOING_RELATIONS, JoinType.LEFT);
            Predicate incomingRelationsPredicate = criteriaBuilder.equal(incomingRelationsJoin.get(RELATION_TYPE), dto.getRelationTypeId());
            Predicate outgoinRelationsPredicate = criteriaBuilder.equal(outgoingRelation.get(RELATION_TYPE), dto.getRelationTypeId());
            predicateList.add(incomingRelationsPredicate);
            predicateList.add(outgoinRelationsPredicate);
        }

        if(dto.getRegisterId() != null || dto.getComment() != null || dto.getExample() != null){

            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<SenseAttributes> senseAttributesRoot = subquery.from(SenseAttributes.class);
            subquery.select(senseAttributesRoot.get("sense").get("id"));

            List<Predicate> predicates = new ArrayList<>();


            if(dto.getRegisterId() != null){
                Predicate senseAttributesPredicate = criteriaBuilder.equal(senseAttributesRoot.get(REGISTER), dto.getRegisterId());
                predicates.add(senseAttributesPredicate);
            }

            if(dto.getComment() != null && !dto.getComment().isEmpty()){
                Predicate commentPredicate = criteriaBuilder.like(senseAttributesRoot.get(COMMENT), "%"+dto.getComment()+"%");
                predicates.add(commentPredicate);
            }

            if(dto.getExample() != null){

                Join<SenseAttributes, SenseExample> senseExampleJoin = senseAttributesRoot.join(EXAMPLES);
                Predicate examplePredicate = criteriaBuilder.like(senseExampleJoin.get(EXAMPLES), dto.getExample());
                predicates.add(examplePredicate);
            }

            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            Predicate subquery_predicate = criteriaBuilder.in(senseRoot.get("id")).value(subquery);
            predicateList.add(subquery_predicate);
        }

        if(dto.getSynsetId() != null){
            Predicate synsetPredicate  = criteriaBuilder.equal(senseRoot.get(SYNSET), dto.getSynsetId());

            predicateList.add(synsetPredicate);
        }
        if (dto.getVariant() != null) {
            Predicate variantPredicate = criteriaBuilder.equal(senseRoot.get(VARIANT), dto.getVariant());
            predicateList.add(variantPredicate);
        }

        return predicateList.toArray(new Predicate[0]);
    }

    public int getCountUnitsByCriteria(SenseCriteriaDTO dto) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);

        Root<Sense> senseRoot = query.from(Sense.class);
        Join<Sense, Word> wordJoin = senseRoot.join(WORD);
        query.select(criteriaBuilder.count(senseRoot));

        Predicate[] predicates = getPredicatesByCriteria(dto, senseRoot, wordJoin, criteriaBuilder);
        query.where(predicates);

        return Math.toIntExact(getEntityManager().createQuery(query).getSingleResult());
    }



    public List<Sense> filterSenseByLexicon(List<Sense> senses, List<Long> lexicons) {
        List<Sense> result = new ArrayList<>();
        senses.stream().filter((sense) -> (lexicons.contains(sense.getLexicon().getId()))).forEach((sense) -> {
            result.add(sense);
        });
        return result;
    }

    public List<Sense> findByLikeLemma(String lemma, List<Long> lexicons) {
        return getEntityManager().createQuery("SELECT s FROM Sense s WHERE s.lexicon.id IN (:lexicons) AND LOWER(s.word.word) LIKE :lemma ORDER BY s.word.word asc", Sense.class)
                .setParameter("lemma", lemma.toLowerCase())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public Integer findCountByLikeLemma(String filter) {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM Sense s WHERE s.lexicon.id IN (:lexicons) AND LOWER(s.word.word) LIKE :lemma")
                .setParameter("lemma", filter.toLowerCase())
                .getFirstResult();
    }

    public List<Sense> findBySynset(Synset synset, List<Long> lexicons) {
        return getEntityManager().createQuery("FROM Sense s LEFT JOIN FETCH s.domain " +
                "WHERE s.synset.id = :synsetId AND s.lexicon.id IN (:lexicons)", Sense.class)
                .setParameter("synsetId", synset.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    public List<Sense> findBySynset(Long synsetId) {
        return getEntityManager().createQuery("FROM Sense s WHERE s.synset.id = :synsetId")
                .setParameter("synsetId", synsetId)
                .getResultList();
    }

    public int findCountBySynset(Synset synset, List<Long> lexicons) {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM Sense s WHERE s.synset.id = :synsetId AND s.lexicon.id IN (:lexicons)")
                .setParameter("synsetId", synset.getId())
                .setParameter("lexicon", lexicons)
                .getMaxResults();
    }

    public int findNextVariant(String lemma, PartOfSpeech pos) {
        int odp = 0;
        TypedQuery<Integer> q = getEntityManager().createQuery("SELECT MAX(s.variant) FROM Sense AS s WHERE LOWER(s.word.word) = :word AND s.partOfSpeech.id = :pos", Integer.class)
                .setParameter("word", lemma.toLowerCase())
                .setParameter("pos", pos.getId());
        try {
            odp = q.getSingleResult();
        } catch (Exception e) {
            return 1;
        }
        odp = Math.max(0, odp);
        return odp + 1;
    }

    public int delete(List<Sense> list) {
        int odp = list.size();
        list.stream().forEach((sense) -> {
            delete(sense);
        });
        return odp;
    }

    public int findInSynsetsCount() {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM Sense s WHERE s.synset IS NOT NULL", Integer.class)
                .getSingleResult();

    }

    public Integer countAll() {
        return getEntityManager().createQuery("SELECT COUNT(s) FROM Sense s", Integer.class)
                .getFirstResult();

    }

    public int findHighestVariant(String word, Lexicon lexicon) {
        return getEntityManager().createQuery("SELECT MAX(s.variant) FROM Sense s WHERE s.word.word = :word AND s.lexicon.id = :lexicon", Integer.class)
                .setParameter("word", word)
                .setParameter("lexicon", lexicon.getId())
                .getFirstResult();
    }

    public List<Sense> findNotInAnySynset(String filter, PartOfSpeech pos) {
        Map<String, Object> params = new HashMap<>();
        String queryString = "SELECT s FROM Sense s WHERE s.synset is null AND s.word.word like :filter";
        if (filter != null && !"".equals(filter)) {
            if (filter.startsWith("^")) {
                params.put("filter", filter.substring(1));
            } else {
                params.put("filter", filter + "%");
            }
        }
        if (pos != null) {
            queryString += " AND s.partOfSpeech.id = :pos";
            params.put("pos", pos.getId());
        }
        TypedQuery<Sense> query = getEntityManager().createQuery(queryString, Sense.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Sense> sense = query.getResultList();

        sense.size();
        for (Sense s : sense) {
            s.getId();
        }
        return sense;
    }

    public boolean checkIsInSynset(Sense unit) {
        Sense s = findById(unit.getId());
        return s.getSynset() != null;
    }

    public List<Long> getAllLemmasForLexicon(List<Long> lexicon) {
        Query query = getEntityManager().createQuery("SELECT s.word.id FROM Sense s WHERE s.lexicon.id IN (:lexicon) GROUP BY s.word.id");
        query.setParameter("lexicon", lexicon);
        return query.getResultList();
    }

    public List<Sense> findSensesByWordId(Long id, Long lexicon) {
        Query query = getEntityManager().createQuery("FROM Sense s WHERE s.word.id = :id AND s.lexicon.id = :lexicon");
        query.setParameter("id", id);
        query.setParameter("lexicon", lexicon);
        return query.getResultList();
    }

    public Sense findHeadSenseOfSynset(Long synsetId) {
        Query query = getEntityManager().createQuery("FROM Sense s JOIN FETCH s.domain JOIN FETCH s.lexicon WHERE s.synset.id = :id AND s.synsetPosition = 0")
                .setParameter("id", synsetId);
        List<Sense> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public Sense fetchSense(Long senseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sense> query = criteriaBuilder.createQuery(Sense.class);
        Root<Sense> senseRoot = query.from(Sense.class);
        senseRoot.fetch("domain", JoinType.LEFT);
        senseRoot.fetch("lexicon", JoinType.LEFT);
        senseRoot.fetch("partOfSpeech", JoinType.LEFT);
        query.where(criteriaBuilder.equal(senseRoot.get("id"), senseId));

        return getEntityManager().createQuery(query).getSingleResult();
    }


    @Override
    protected Class<Sense> getPersistentClass() {
        return Sense.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SenseAttributes fetchSenseAttribute(Long senseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SenseAttributes> query = criteriaBuilder.createQuery(SenseAttributes.class);

        Root<SenseAttributes> root = query.from(SenseAttributes.class);
        root.fetch("examples", JoinType.LEFT);

        Predicate predicate = criteriaBuilder.equal(root.get("id"), senseId);
        query.where(predicate);

        return getEntityManager().createQuery(query).getSingleResult();
    }
}
