package pl.edu.pwr.wordnetloom.sense.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationRepository;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.word.repository.WordRepository;

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

        if(s != null) {
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
//        Collections.sort(senses, getSenseComparator());
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
    private final String SENSE_ATTRIBUTES = "senseAttributes";

    private List<Sense> getSensesByCriteria(SenseCriteriaDTO dto){

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Sense> query = criteriaBuilder.createQuery(Sense.class);
        Root<Sense> senseRoot = query.from(Sense.class);
        Join<Sense, Word> wordJoin = senseRoot.join(WORD);
        senseRoot.fetch(WORD);
        senseRoot.fetch(DOMAIN);
        senseRoot.fetch(LEXICON);

        query.select(senseRoot);
//        query.distinct(true);
        query.where(getPredicatesByCriteria(dto, senseRoot, wordJoin, criteriaBuilder));

        List<Order> orders = new ArrayList<>();
        orders.add(criteriaBuilder.asc(wordJoin.get(WORD)));
        orders.add(criteriaBuilder.asc(senseRoot.get(PART_OF_SPEECH)));
        orders.add(criteriaBuilder.asc(senseRoot.get(VARIANT)));
        orders.add(criteriaBuilder.asc(senseRoot.get(LEXICON)));

        query.orderBy(orders);
        Query selectQuery = em.createQuery(query);
        if(dto.getLimit() > 0){
            selectQuery.setMaxResults(dto.getLimit());
        }
        if(dto.getOffset() > 0){
            selectQuery.setFirstResult(dto.getOffset());
        }

        // remove duplicates
        List<Sense> result = selectQuery.getResultList();
        Set<Sense> resultSet = new LinkedHashSet<>(result);
        return new ArrayList<>(resultSet);

//        return selectQuery.getResultList();
    }

    private Predicate[] getPredicatesByCriteria(SenseCriteriaDTO dto, Root senseRoot, Join wordJoin, CriteriaBuilder criteriaBuilder){
        List<Predicate> predicateList = new ArrayList<>();

        if(dto.getLemma() != null && !dto.getLemma().isEmpty()){
            Predicate lemmaPredicate = criteriaBuilder.like(wordJoin.get(WORD), dto.getLemma()+"%");
            predicateList.add(lemmaPredicate);
        }

        Predicate lexiconPredicate = senseRoot.get(LEXICON).in(dto.getLexicons());
        predicateList.add(lexiconPredicate);

        if(dto.getPartOfSpeechId() != null){
            Predicate partOfSpeechPredicate = criteriaBuilder.equal(senseRoot.get(PART_OF_SPEECH), dto.getPartOfSpeechId());
            predicateList.add(partOfSpeechPredicate);
        }

        if(dto.getDomainId() != null){
            Predicate domainPredicate = criteriaBuilder.equal(senseRoot.get(DOMAIN), dto.getDomainId());
            predicateList.add(domainPredicate);
        }

        if(dto.getRelationTypeId() != null){
            Join<Sense, SenseRelation> incomingRelationsJoin = senseRoot.join(INCOMING_RELATIONS, JoinType.LEFT);
            Join<Sense, SenseRelation> outgoingRelation = senseRoot.join(OUTGOING_RELATIONS, JoinType.LEFT);
            Predicate incomingRelationsPredicate = criteriaBuilder.equal(incomingRelationsJoin.get(RELATION_TYPE), dto.getRelationTypeId());
            Predicate outgoinRelationsPredicate = criteriaBuilder.equal(outgoingRelation.get(RELATION_TYPE), dto.getRelationTypeId());
            predicateList.add(incomingRelationsPredicate);
            predicateList.add(outgoinRelationsPredicate);
        }

        if(dto.getRegisterId() != null || dto.getComment() != null){
            Join<Sense, SenseAttributes> senseSenseAttributesJoin = senseRoot.join(SENSE_ATTRIBUTES, JoinType.LEFT);
            if(dto.getRegisterId() != null){
                Predicate senseAttributesPredicate = criteriaBuilder.equal(senseSenseAttributesJoin.get(REGISTER), dto.getRegisterId());
                predicateList.add(senseAttributesPredicate);
            }
            if(dto.getComment() != null && !dto.getComment().isEmpty()){
                Predicate commentPredicate = criteriaBuilder.like(senseSenseAttributesJoin.get(COMMENT), "%"+dto.getComment()+"%");
                predicateList.add(commentPredicate);
            }
        }

        if(dto.getExample() != null){
            Join<Sense, SenseExample> senseExampleJoin = senseRoot.join(EXAMPLES);
            Predicate examplePredicate = criteriaBuilder.like(senseExampleJoin.get(EXAMPLES), dto.getExample());
            predicateList.add(examplePredicate);
        }
        if(dto.getSynsetId() != null){
            Predicate synsetPredicate  = criteriaBuilder.equal(senseRoot.get(SYNSET), dto.getSynsetId());
            predicateList.add(synsetPredicate);
        }
        if(dto.getVariant() != null){
            Predicate variantPredicate = criteriaBuilder.equal(senseRoot.get(VARIANT), dto.getVariant());
            predicateList.add(variantPredicate);
        }

        return predicateList.toArray(new Predicate[0]);
    }

    public int getCountUnitsByCriteria(SenseCriteriaDTO dto){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Sense> senseRoot = query.from(Sense.class);
        Join<Sense, Word> wordJoin = senseRoot.join(WORD);
        query.select(criteriaBuilder.count(senseRoot));
        Predicate[] predicates = getPredicatesByCriteria(dto, senseRoot, wordJoin, criteriaBuilder);
        query.where(predicates);
        return Math.toIntExact(getEntityManager().createQuery(query).getSingleResult());
    }

    /** Zwraca komparator, który porównuje jednostki według nastepujących kryteriów
     *  1. słówko
     *  2. częśc mowy
     *  3. numer jednostki - wariant
     *  4. leksykon
     * @return komparator porównujący jednostki
     */
    private Comparator<Sense> getSenseComparator(final String lemma) {
        Collator collator = Collator.getInstance(Locale.US);
        String rules = ((RuleBasedCollator) collator).getRules();
        try {
            collator = new RuleBasedCollator(rules.replaceAll("<'\u005f'", "<' '<'\u005f'"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.NO_DECOMPOSITION);

        final Collator finalCollator = collator;

        Comparator<Sense> senseComparator = (Sense a, Sense b) ->{

            String valueA = a.getWord().getWord().toLowerCase();
            String valueB = b.getWord().getWord().toLowerCase();

            int compareResult = finalCollator.compare(valueA, valueB);
            if(compareResult == 0){
                Long longValueA = a.getPartOfSpeech().getId();
                Long longValueB = b.getPartOfSpeech().getId();
                compareResult = longValueA.compareTo(longValueB); //TODO sprawdzić czy kolejnośc jest dobra
            }
            if(compareResult == 0){
                compareResult = a.getVariant().compareTo(b.getVariant());
            }
            if(compareResult == 0){
                compareResult = a.getLexicon().getId().compareTo(b.getLexicon().getId());
            }

            return compareResult;
        };

        return senseComparator;
    }

    private List<Sense> getSenses(String filter, PartOfSpeech pos, Domain domain, RelationType relationType,
                                  String register, String comment, String example, int limitSize, List<Long> lexicons) {

        String wordQuery = "";
        String senseQuery = "SELECT s FROM Sense s JOIN FETCH s.domain JOIN FETCH s.lemma JOIN FETCH s.partOfSpeech WHERE ";

        boolean existFilter = filter != null && !filter.isEmpty();
        boolean existCriteria = pos != null || domain != null || relationType != null
                || (register != null && !register.isEmpty())
                || (comment != null && !comment.isEmpty())
                || (example != null && !example.isEmpty());

        if (existFilter && !filter.endsWith("%")) {
            filter += "%";
        }

        Collator collator = Collator.getInstance(Locale.US);
        String rules = ((RuleBasedCollator) collator).getRules();
        try {
            RuleBasedCollator correctedCollator
                    = new RuleBasedCollator(rules.replaceAll("<'\u005f'", "<' '<'\u005f'"));
            collator = correctedCollator;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.NO_DECOMPOSITION);

        Collator myFavouriteCollator = collator;

        Comparator<Sense> senseComparator = (Sense a, Sense b) -> {

            String aa = a.getWord().getWord().toLowerCase();
            String bb = b.getWord().getWord().toLowerCase();

            int c = myFavouriteCollator.compare(aa, bb);
            if (c == 0) {
                aa = a.getPartOfSpeech().getId().toString();
                bb = b.getPartOfSpeech().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            if (c == 0) {
                if (Objects.equals(a.getVariant(), b.getVariant())) {
                    c = 0;
                }
                if (a.getVariant() > b.getVariant()) {
                    c = 1;
                }
                if (a.getVariant() < b.getVariant()) {
                    c = -1;
                }
            }
            if (c == 0) {
                aa = a.getLexicon().getId().toString();
                bb = b.getLexicon().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            return c;
        };

        if (existCriteria) {
            Map<String, Object> parameters = new HashMap<>();

            int critCounter = (domain == null ? 0 : 1) + (pos == null ? 0 : 1) + (relationType == null ? 0 : 1);

            // TODO: better to find just COUNT of these ...
            if (((existFilter && filter.length() > 3) && critCounter > 0)) {
                // można przypuszczać, że słów jest relatywnie mało (/^?.*{3,}%/i) LUB kryteria szukania są bardziej szczegółowe
                // dlatego najoptymalniej będzie fetchnąć najpierw same, posortowane już, słowa
                if (existFilter) {
                    // szukanie po wordach
                    wordQuery = "SELECT w.id FROM Word w WHERE lower(w.word) LIKE :param ORDER BY w.word ASC";
                } else {
                    // szukanie po wszystkim
                    wordQuery = "SELECT w.id FROM Word w ORDER BY w.word ASC";
                }

                TypedQuery<Long> wordIDQuery = getEntityManager().createQuery(wordQuery, Long.class);

                if (existFilter) {
                    wordIDQuery.setParameter("param", filter.toLowerCase());
                }

                if (limitSize != 0) {
                    wordIDQuery.setMaxResults(limitSize);
                }

                List<Long> wordsIDs = wordIDQuery.getResultList();

                if (wordsIDs.isEmpty()) {
                    return new ArrayList<>();
                }

                senseQuery += "s.lemma.word.id IN (:wordsID) AND ";
                parameters.put("wordsID", wordsIDs);

                // to optimize
                if (pos != null) {
                    senseQuery += "s.partOfSpeech.id = :pos AND ";
                    parameters.put("pos", pos.getId());
                }
                if (domain != null) {
                    senseQuery += "s.domain.id = :domain AND ";
                    parameters.put("domain", domain.getId());
                }

                if (relationType != null) {
                    senseQuery += "s.id IN (SELECT r.sense_from FROM  SenseRelation r  WHERE r.relation.id = :relationTypeID)) AND ";
                    parameters.put("relationTypeID", relationType.getId());
                }
                if (register != null) {
                    senseQuery += "s.id IN( SELECT sa.sense.id FROM SenseAttribute sa WHERE sa.type = (SELECT att.id FROM AttributeType att WHERE att.typeName.text = 'register')"
                            + " AND sa.value.text = :register) AND ";
                    parameters.put("register", register);
                }
                if (comment != null && !comment.isEmpty()) {
                    senseQuery += "s.id IN( SELECT sa.sense.id FROM SenseAttribute sa WHERE sa.type = (SELECT att.id FROM AttributeType att WHERE att.tableName='sense' AND att.typeName.text = 'comment')"
                            + " AND sa.value.text LIKE :comment ) AND ";
                    parameters.put("comment", "%" + comment + "%");
                }
                if (example != null && !example.isEmpty()) {
                    senseQuery += "s.id IN( SELECT sa.sense.id FROM SenseAttribute sa WHERE sa.type = (SELECT att.id FROM AttributeType att WHERE att.typeName.text = 'use_cases')"
                            + " AND sa.value.text LIKE :example ) AND ";
                    parameters.put("example", "%" + example + "%");
                }
                senseQuery = senseQuery.substring(0, senseQuery.length() - 4);

                TypedQuery<Sense> q = getEntityManager().createQuery(senseQuery, Sense.class);

                for (Entry<String, Object> pair : parameters.entrySet()) {
                    q.setParameter(pair.getKey(), pair.getValue());
                }

                List<Sense> senses = q.setMaxResults(limitSize).getResultList();
                Collections.sort(senses, senseComparator);
                senses = filterSenseByLexicon(senses, lexicons);
                return senses;
            }
            // słów jest zdecydowanie za dużo by przekazać je jako parametr, trzeba najpierw szukać po kryteriach

            if (pos != null) {
                senseQuery += "s.partOfSpeech.id = :pos AND ";
                parameters.put("pos", pos.getId());
            }
            if (domain != null) {
                senseQuery += "s.domain.id = :domain AND ";
                parameters.put("domain", domain.getId());
            }

            if (relationType != null) {
                senseQuery += "s.id IN (SELECT r.sense_from FROM  SenseRelation r  WHERE r.relation.id = :relationTypeID)) AND ";
                parameters.put("relationTypeID", relationType.getId());
            }
            if (register != null) {
                senseQuery += "s.id IN( SELECT sa.sense.id FROM SenseAttribute sa WHERE sa.type = (SELECT att.id FROM AttributeType att WHERE att.typeName.text = 'register')"
                        + " AND sa.value.text = :register) AND ";
                parameters.put("register", register);
            }
            if (comment != null && !comment.isEmpty()) {
                senseQuery += "s.id IN( SELECT sa.sense.id FROM SenseAttribute sa WHERE sa.type = (SELECT att.id FROM AttributeType att WHERE att.tableName='sense' AND att.typeName.text = 'comment')"
                        + " AND sa.value.text LIKE :comment ) AND ";
                parameters.put("comment", "%" + comment + "%");
            }
            if (example != null && !example.isEmpty()) {
                senseQuery += "s.id IN( SELECT sa.sense.id FROM SenseAttribute sa WHERE sa.type = (SELECT att.id FROM AttributeType att WHERE att.typeName.text = 'use_cases')"
                        + " AND sa.value.text LIKE :example ) AND ";
                parameters.put("example", "%" + example + "%");
            }
            senseQuery = senseQuery.substring(0, senseQuery.length() - 4);

            TypedQuery<Sense> q = getEntityManager().createQuery(senseQuery, Sense.class);

            for (Entry<String, Object> pair : parameters.entrySet()) {
                q.setParameter(pair.getKey(), pair.getValue());
            }

            List<Sense> senses = q.getResultList(); // nie można oznaczyć limitu dla nieposortowanej kolekcji!

            if (existFilter) {
                ListIterator<Sense> iterator = senses.listIterator();
                Pattern p = Pattern.compile(filter.replaceAll("\\.", "\\\\.").replaceAll("\\[", "\\\\[").replaceAll("%", ".*?"),
                        Pattern.CASE_INSENSITIVE);
                while (iterator.hasNext()) {
                    Sense s = iterator.next();
                    String name = s.getWord().getWord();

                    Matcher match = p.matcher(name);
                    if (!match.matches()) {
                        iterator.remove();
                    }
                }
            }
            Collections.sort(senses, senseComparator);

            if (limitSize != 0 && senses.size() > limitSize) {
                senses = senses.subList(0, limitSize);
                senses = new ArrayList<>(senses);
            }
            senses = filterSenseByLexicon(senses, lexicons);
            return senses;
        }
        // najpierw szukanie przez pobranie id-ków wordów
        if (existFilter) {
            // szukanie po wordach
            wordQuery = "SELECT w.id FROM Word w WHERE lower(w.word) LIKE :param ORDER BY w.word ASC";
        } else {
            // szukanie po wszystkim
            wordQuery = "SELECT w.id FROM Word w ORDER BY lower(w.word) ASC";
        }

        TypedQuery<Long> wordIDQuery = getEntityManager().createQuery(wordQuery, Long.class);

        if (existFilter) {
            wordIDQuery.setParameter("param", filter.toLowerCase());
        }

        if (limitSize == 0) {
            wordIDQuery.setMaxResults(10000); // TODO: remove hard-limit
        } else {
            wordIDQuery.setMaxResults(limitSize);
        }

        List<Long> wordsIDs = wordIDQuery.getResultList(); // lista idków

        if (wordsIDs.isEmpty()) {
            return new ArrayList<>();
        }

        senseQuery += "s.lemma.word.id IN (:wordsID)";

        TypedQuery<Sense> lastQuery = getEntityManager().createQuery(senseQuery, Sense.class);

        lastQuery.setParameter("wordsID", wordsIDs);

        if (limitSize == 0) {
            lastQuery.setMaxResults(10000); // TODO: remove hard-limit
        } else {
            lastQuery.setMaxResults(limitSize);
        }
        List<Sense> senses = lastQuery.getResultList();
        Collections.sort(senses, senseComparator);
        senses = filterSenseByLexicon(senses, lexicons);
        return senses;
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

    public List<Sense> findBySynset(Long synsetId){
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

    public Sense findHeadSenseOfSynset(Long synsetId){
        Query query = getEntityManager().createQuery("FROM Sense s JOIN FETCH s.domain JOIN FETCH s.lexicon WHERE s.synset.id = :id AND s.synsetPosition = 0")
                .setParameter("id", synsetId);
        List<Sense> resultList = query.getResultList();
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }
        return null;
    }

    public Sense fetchSense(Long senseId) {
        CriteriaBuilder  criteriaBuilder = getEntityManager().getCriteriaBuilder();
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
