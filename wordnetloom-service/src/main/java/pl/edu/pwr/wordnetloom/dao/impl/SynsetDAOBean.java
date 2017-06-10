package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.LexicalUnitDAOLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetAttributeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import pl.edu.pwr.wordnetloom.model.wordnet.SynsetAttribute;
import pl.edu.pwr.wordnetloom.model.wordnet.SynsetRelation;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;

@Stateless
public class SynsetDAOBean extends DAOBean implements SynsetDAOLocal {

    public SynsetDAOBean() {
    }

    @EJB
    private DAOLocal dao;

    @EJB
    private SynsetAttributeDaoLocal synsetAttributeDao;

    @EJB
    private LexicalUnitDAOLocal lexicalUnitDAO;

    /**
     * powielenie synsetu
     *
     * @param synset - synset do sklonowania
     * @param lexicons
     */
    @Override
    public void dbClone(Synset synset, List<Long> lexicons) {

        Synset newSynset = new Synset();
        newSynset.setSplit(0);
        dao.persistObject(newSynset);
        setSynsetAtrribute(newSynset, Synset.ISABSTRACT, getSynsetAtrribute(newSynset, Synset.ISABSTRACT));
        setSynsetAtrribute(newSynset, Synset.COMMENT, getSynsetAtrribute(newSynset, Synset.COMMENT));
        setSynsetAtrribute(newSynset, Synset.DEFINITION, getSynsetAtrribute(newSynset, Synset.DEFINITION));

        int index = 0;
        for (Sense unit : lexicalUnitDAO.dbFullGetUnits(synset, 0, lexicons)) {
            Sense newUnit = lexicalUnitDAO.dbClone(unit);
            SenseToSynset newRel = new SenseToSynset();
            newRel.setIdSynset(newSynset.getId());
            newRel.setIdSense(newUnit.getId());
            newRel.setSenseIndex(index++);
            dao.persistObject(newRel);
        }
    }

    /**
     * usuniecie obiektu
     *
     * @param synset - synset do usuniecia
     * @return TRUE jesli sie udalo
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public boolean dbDelete(Synset synset) {
        EntityManager em = dao.getEM();
        // usuniecie z synsetow
        em.createNamedQuery("SenseToSynset.DeleteBySynsetID")
                .setParameter("idSynset", synset.getId())
                .executeUpdate();
        // usuniecie relacji
        em.createNamedQuery("SynsetRelation.DeleteSynsetRelationBySenseFromORSenseTo")
                .setParameter("synset", synset.getId())
                .executeUpdate();
        // usuniecie jednostki

        dao.deleteObject(Synset.class, synset.getId());
        return true;
    }

    /**
     * odczytanie jednostek leksykalnych podanego synsetu
     *
     * @param synset - synset dla ktorego maja zostac pobrane jednostki
     * @param lexicons
     * @return lista jednostek
     */
    @Override
    public List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons) {
        List<Sense> result = dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
        return result;
    }

    /**
     * odczytanie jednostek leksykalnych dla podanego id synsetu
     *
     * @param synsetId
     * @param lexicons
     * @return lista jednostek
     */
    @Override
    public List<Sense> dbFastGetUnits(Long synsetId, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synsetId)
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    /**
     * odczytanie jednostek leksykalnych podanych synsetów
     *
     * @param synset - synset dla którego mają zostać pobrane jednostki
     * @param lexicons
     * @return
     */
    @Override
    public Synset dbGetUnit(Synset synset, List<Long> lexicons) {
        if (null == synset) {
            return synset;
        }

        List<Synset> list = dao.getEM().createNamedQuery("Synset.dbGetUnit", Synset.class)
                .setParameter("synsetID", synset.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();

        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    /**
     * odczytanie jednostek leksykalnych podanych synsetów
     *
     * @param synsets
     * @param synset - synset dla którego mają zostać pobrane jednostki
     * @return
     */
    @Override
    public List<Synset> dbGetUnits(List<Synset> synsets) {
        if (synsets.isEmpty()) {
            return synsets;
        }

        ArrayList<Long> ids = new ArrayList<>();
        for (Synset s : synsets) {
            ids.add(s.getId());
        }

        return dbGetUnits(ids);
    }

    /**
     * odczytanie jednostek leksykalnych dla ID synsetow
     *
     * @param iDs
     */
    @Override
    public List<Synset> dbGetUnits(Collection<Long> iDs) {
        if (iDs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Synset> list = dao.getEM().createNamedQuery("Synset.dbGetUnitsByIDs", Synset.class)
                .setParameter("synsetsIDs", iDs)
                .getResultList();
        if (!list.isEmpty() && list.get(0) != null) {
            list.get(0).getSynsetAttributes();
        }
        return list;
    }

    /**
     * odczytanie ilosc jednostek leksykalnych zwiazanych z danycm synsetem
     *
     * @param synset - synset dla ktorego ma zostac pobrana ilosc jednostek
     * @return liczba jednostek
     */
    @Override
    public int dbGetUnitsCount(Synset synset) {
        List<Long> list = dao.getEM().createNamedQuery("Sense.CountSenseBySynsetID", Long.class)
                .setParameter("idSynset", synset.getId())
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * odbudowa opisu synsetu
     *
     * @param synset - synset
     * @param lexicons
     * @return
     */
    @Override
    public String dbRebuildUnitsStr(Synset synset, List<Long> lexicons) {
        return rebuildUnitsStr(synset, lexicons);
    }

    /**
     * odczytanie czesci mowy synsetu
     *
     * @param synset - synset
     * @return czesc mowy albo NULL gdy nie zdefiniowana
     */
    @Override // TODO: check me
    public PartOfSpeech dbGetPos(Synset synset, List<Long> lexicons) {
        // pobranie wszystkich danych o jednej jednostce z synsetu
        // jesli taka istnieje
        List<Sense> senses = dbFastGetUnits(synset, lexicons);
        if (senses.isEmpty() || senses.get(0) == null) {
            return null;
        }

        return senses.get(0).getPartOfSpeech();
    }

    /**
     * odczytanie domeny synsetu
     *
     * @param synset - synset
     * @param lexicons
     * @return domena albo NULL gdy nie zdefiniowana
     */
    @Override // TODO: check me
    public Domain dbGetDomain(Synset synset, List<Long> lexicons) {
        // pobranie wszystkich danych o jednej jednostce z synsetu
        // jesli taka istnieje

        List<Sense> senses = dbFastGetUnits(synset, lexicons);
        if (senses.isEmpty() || senses.get(0) == null) {
            return null;
        }

        return senses.get(0).getDomain();
    }

    @Override
    public void dbSaveConnections(Map<Synset, List<Sense>> map) {
        EntityManager em = dao.getEM();

        for (Map.Entry<Synset, List<Sense>> entry : map.entrySet()) {
            Synset synset = entry.getKey();
            List<Sense> senses = entry.getValue();
            int senseIndex = 0;
            for (Sense sense : senses) {
                SenseToSynset sts = new SenseToSynset();
                sts.setSynset(synset);
                sts.setSense(sense);
                sts.setSenseIndex(senseIndex);
                senseIndex++;
                em.persist(sts);
            }

        }
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @return lista synsetow (bez detali)
     */
    @Override
    public List<Synset> dbFastGetSynsets(String filter, List<Long> lexicons) {
        return dbFastGetSynsets(filter, null, null, 0, lexicons);
    }

    /**
     * odczytanie sysnetow i zwrócenie głownie jednostki
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @param workStates - akceptowalne statusy lub NULL akceptuje wszystkie
     * @param domain - akceptowalna domena lub NULL aby akceptować wszystko
     * @param relationType - typ relacji jakie musza byc zdefiniowane dla
     * synsetow wynikowych
     * @param limitSize - maksymalna liczba zwroconych elementów
     * @param realSize - obiekt w którym zapisywana jest prawdziwa wielkość
     * kolekcji
     * @return lista synsetow (bez detali)
     */
    @Override
    public List<Synset> dbFastGetSynsets(String filter, Domain domain, RelationType relationType, int limitSize, List<Long> lexicons) {
        return dbFastGetSynsets(filter, domain, relationType, limitSize, -1, lexicons);
    }

    @Override
    public List<Sense> dbFastGetSenseBySynset(String filter, Domain domain, RelationType relationType, String definition, String comment, String artificial, int limitSize, long posIndex, List<Long> lexicons) {

        CriteriaBuilder criteriaBuilder = getEM().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<SenseToSynset> stsRoot = criteriaQuery.from(SenseToSynset.class);
        Join<SenseToSynset, Sense> sense = stsRoot.join("sense", JoinType.LEFT);
        Join<Sense, Word> word = sense.join("lemma", JoinType.LEFT);

        List<Predicate> criteriaList = new ArrayList<>();

        Predicate first_predicate = criteriaBuilder.like(
                criteriaBuilder.lower(word.<String>get("word")), "%" + filter.toLowerCase() + "%");
        criteriaList.add(first_predicate);

        if (domain != null) {
            Predicate secend_predicate = criteriaBuilder.equal(sense.get("domain").get("id"), domain.getId());
            criteriaList.add(secend_predicate);
        }
        if (posIndex > 0) {
            Predicate third_predicate = criteriaBuilder.equal(sense.get("partOfSpeech").get("id"), posIndex);
            criteriaList.add(third_predicate);
        }
        if (relationType != null) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetRelation> relRoot = subquery.from(SynsetRelation.class);
            subquery.select(relRoot.get("synsetFrom").<Long>get("id"));
            subquery.where(criteriaBuilder.equal(relRoot.get("relation").get("id"), relationType.getId()));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);

        }
        if (!definition.isEmpty()) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
            subquery.select(relRoot.get("synset").<Long>get("id"));
            List<Predicate> predicates = new ArrayList<>();
            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.DEFINITION);
            predicates.add(type);
            Predicate value = criteriaBuilder.like(relRoot.<String>get("value").<String>get("text"), "%" + definition + "%");
            predicates.add(value);
            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        if (!comment.isEmpty()) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
            subquery.select(relRoot.get("synset").<Long>get("id"));
            List<Predicate> predicates = new ArrayList<>();
            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.COMMENT);
            predicates.add(type);
            Predicate value = criteriaBuilder.like(relRoot.get("value").<String>get("text"), "%" + comment + "%");
            predicates.add(value);
            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        if (!artificial.isEmpty()) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
            subquery.select(relRoot.get("synset").<Long>get("id"));
            List<Predicate> predicates = new ArrayList<>();
            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.ISABSTRACT);
            predicates.add(type);
            Predicate value = criteriaBuilder.equal(relRoot.get("value").<String>get("text"), artificial);
            predicates.add(value);
            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        criteriaQuery.select(stsRoot.<Long>get("idSynset")).distinct(true);
        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));

        final TypedQuery<Long> query = getEM().createQuery(criteriaQuery);
        query.setFirstResult(0);
        if (limitSize > 0) {
            query.setMaxResults(limitSize);
        }
        List<Long> synsetIds = query.getResultList();
        List<Sense> result = new ArrayList<>();
        synsetIds.stream().forEach((id) -> {
            result.addAll(dbFastGetUnits(id, lexicons));
        });

        return result;
    }

    /**
     * odczytanie sysnetow z filtrowaniem po części mowy
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @param domain - akceptowalna domena lub NULL aby akceptować wszystko
     * @param relationType - typ relacji jakie musza byc zdefiniowane dla
     * synsetow wynikowych
     * @param limitSize - maksymalna liczba zwroconych elementów
     * @param lexicon
     * @param posIndex - indeks części mowy (-1 wszystkie, 0 nieznany, itd.
     * zgodnie z enum Pos)
     * @return lista synsetow (bez detali)
     */
    public List<Synset> dbFastGetSynsets(String filter, Domain domain, RelationType relationType, int limitSize, long posIndex, List<Long> lexicon) {

        CriteriaBuilder criteriaBuilder = getEM().getCriteriaBuilder();
        CriteriaQuery<Synset> criteriaQuery = criteriaBuilder.createQuery(Synset.class);
        Root<SenseToSynset> stsRoot = criteriaQuery.from(SenseToSynset.class);
        Join<SenseToSynset, Sense> sense = stsRoot.join("sense", JoinType.LEFT);
        Join<Sense, Word> word = sense.join("lemma", JoinType.LEFT);

        List<Predicate> criteriaList = new ArrayList<>();

        Predicate first_predicate = criteriaBuilder.like(
                criteriaBuilder.lower(word.<String>get("word")), "%" + filter.toLowerCase() + "%");
        criteriaList.add(first_predicate);

        if (domain != null) {
            Predicate secend_predicate = criteriaBuilder.equal(sense.get("domain").get("id"), domain.getId());
            criteriaList.add(secend_predicate);
        }
        if (posIndex > 0) {
            Predicate third_predicate = criteriaBuilder.equal(sense.get("partOfSpeech").get("id"), posIndex);
            criteriaList.add(third_predicate);
        }
        if (relationType != null) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetRelation> relRoot = subquery.from(SynsetRelation.class);
            subquery.select(relRoot.get("synsetFrom").<Long>get("id"));
            subquery.where(criteriaBuilder.equal(relRoot.get("relation").get("id"), relationType.getId()));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        Predicate last = criteriaBuilder.in(sense.get("lexicon").get("id")).value(lexicon);
        criteriaList.add(last);

        criteriaQuery.select(stsRoot.<Synset>get("synset"));
        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));

        final TypedQuery<Synset> query = getEM().createQuery(criteriaQuery);
        query.setFirstResult(0);
        if (limitSize > 0) {
            query.setMaxResults(limitSize);
        }
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Boolean areSynsetsInSameLexicon(long synset1, long synset2) {
        List<Sense> sense1 = dao.getEM().createQuery("Select sts.sense from SenseToSynset sts where sts.idSynset = :syn1")
                .setParameter("syn1", synset1)
                .getResultList();
        List<Sense> sense2 = dao.getEM().createQuery("Select sts.sense from SenseToSynset sts where sts.idSynset = :syn2")
                .setParameter("syn2", synset2)
                .getResultList();
        return sense1.get(0).getLexicon().getId().equals(sense2.get(0).getLexicon().getId());
    }

    /**
     * Pobiera kolekcje synsetów o id zadanych w parametrze
     *
     * @param synset_ids tablica indeksów synsetów, które mają zostać pobrane
     * @return kolekcje synsetów o zadanych id | null jeśli nie podano poprawnej
     * tablicy
     * @author lburdka
     */
    @Override
    public List<Synset> dbFullGet(Long[] synset_ids) {
        if (synset_ids == null || synset_ids.length == 0) {
            return null;
        }

        List<Long> idList = Arrays.asList(synset_ids);

        return dao.getEM().createNamedQuery("Synset.findListSynsetByID", Synset.class)
                .setParameter("synsetsID", idList)
                .getResultList();
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @param filterObject - filtr obietkowy, musi miec taki sam POS
     * @param lexicons
     * @return lista synsetow (bez detali)
     */
    @Override // TODO: check me // FIXME
    public List<Synset> dbFastGetSynsets(String filter, Sense filterObject, List<Long> lexicons) {
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

        TypedQuery<Synset> q = dao.getEM().createQuery(sb.toString(), Synset.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return q.getResultList();
    }

    /**
     * usuniecie pustych synsetow
     *
     * @return liczba usunietych wierszy
     *
     */
    @Override
    public int dbDeleteEmpty() {
        return dao.getEM().createQuery("DELETE FROM Synset s WHERE s.id NOT IN (SELECT DISTINCT sts.idSynset FROM SenseToSynset sts)")
                .executeUpdate();
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @return lista synsetow (z detalimi)
     */
    @Override // TODO: refactor
    public List<Synset> dbFullGetSynsets(String filter) {
        Map<String, Object> params = new HashMap<>();

        String selectString = "SELECT s.senseToSynset.synset FROM Sense s";
        if (filter != null && !"".equals(filter)) {
            selectString += " WHERE s.lemma.word LIKE :param1 OR s.lemma.word LIKE :param2 ";
            params.put("param1", filter + "%");
            params.put("param2", "%" + filter + "%");
        }
        selectString += " ORDER BY s.lemma.word";

        TypedQuery<Synset> query = dao.getEM().createQuery(selectString, Synset.class);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.getResultList();
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @return lista synsetow (z detalimi)
     */
    @Override
    public List<Synset> dbGetNotEmptySynsets(String filter) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT s.senseToSynset.synset FROM Sense s ");

        if (filter != null && !"".equals(filter)) {
            sb.append(" WHERE s.lemma.word LIKE :param1 OR s.lemma.word LIKE :param2 ");
            sb.append(" AND s.senseToSynset.idSynset IN (SELECT DISCTINCT sts.idSynset FROM SenseToSynset sts ORDER BY sts.idSynset) ");
            sb.append(" ORDER BY s.lemma.word");
            params.put("param1", filter + "%");
            params.put("param2", "%" + filter + "%");
        } else {
            sb.append(" WHERE s.senseToSynset.idSynset IN (SELECT DISTINCT sts.idSynset FROM SenseToSynset sts ORDER BY sts.idSynset) ");
        }

        TypedQuery<Synset> query = dao.getEM().createQuery(sb.toString(), Synset.class);
        params.entrySet().stream().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });

        return query.getResultList();
    }

    /**
     * odczytanie synsetow zwiazanych z podana jednostka leksykalna
     *
     * @param unit - jednostka leksykalna
     * @return lista synsetow
     */
    @Override
    public List<Synset> dbFastGetSynsets(Sense sense, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Synset.findSynsetBySensID", Synset.class)
                .setParameter("senseID", sense.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    /**
     * odczytanie liczby synsetow zwiazanych z podana jednostka leksykalna
     *
     * @param unit - jednostka leksykalna
     * @return liczba synsetow
     */
    @Override
    public int dbGetSynsetsCount(Sense unit) {
        List<Long> list = dao.getEM().createNamedQuery("SenseToSynset.CountSynsetBySense", Long.class)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * pobranie synsetu o podanym ID
     *
     * @param id - id synsetu
     * @return synset albo NULL
     */
    @Override
    public Synset dbGet(Long id) {
        return dao.getEM().find(Synset.class, id);
    }

    @Override
    public Synset dbGetSynsetRels(Synset synset) {
        List<Synset> list = dao.getEM().createNamedQuery("Synset.dbGetSynsetRels", Synset.class)
                .setParameter("synsetID", synset.getId())
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        list.get(0).getSynsetAttributes().size();
        return list.get(0);
    }

    @Override
    public List<Synset> dbGetSynsetsRels(List<Synset> synsets) {
        return dao.getEM().createNamedQuery("Synset.dbGetSynsetsRels", Synset.class)
                .setParameter("synsetList", synsets)
                .getResultList();
    }

    /**
     * @param ind collection of synsets indices
     * @return collection of synset objects
     */
    @Override
    public List<Synset> dbGetSynsetsUnitsRels(Collection<Long> ind) {
        return dbGetUnits(ind);

    }

    /**
     * odczytanie liczby synsetow w bazie
     *
     * @return liczba synsetow
     */
    @Override
    public int dbGetSynsetsCount() {
        List<Long> list = dao.getEM().createNamedQuery("Synset.findCountAll", Long.class)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * odczytanie liczby identycznych jednostek w synsetach
     *
     * @param a - synset A
     * @param b - synset B
     * @return liczba identycznych jednostek
     */
    @Override
    public int dbGetSimilarityCount(Synset a, Synset b) {
        List<Long> list = dao.getEM().createNamedQuery("Synset.dbGetSimilarityCount", Long.class)
                .setParameter("idA", a.getId())
                .setParameter("idB", b.getId())
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * Odczytywanie opisów synsetów o danym ID
     *
     * @param idx - lista ID sysnetów
     * @return mapa opisow synsetow
     */
    @Override
    public Map<Long, String> dbGetSynsetsDescriptionIdx(List<Long> idx, List<Long> lexicons) {
        Map<Long, String> synsetsDescriptions = new HashMap<Long, String>();

        for (Long idSynset : idx) {
            String unitStr = rebuildUnitsStr(dbGet(idSynset), lexicons);
            synsetsDescriptions.put(idSynset, unitStr);
        }

        return synsetsDescriptions;
    }

    @Override
    public String rebuildUnitsStr(Integer split, String unitsstr, List<Sense> senses) {
        // jest pusty synset
        if (senses == null || senses.isEmpty()) {
            unitsstr = "! S.y.n.s.e.t p.u.s.t.y !";
            split = 0;
        } else {
            StringBuilder temp = new StringBuilder();

            temp.append("(");

            int index = 0, size = senses.size();
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
                    temp.append(lexicalUnit.getDomain().getName().getText());
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

    /**
     * sprwadzenie czy synset zawiera jednostke
     *
     * @param unit - jednostka do sprawdzenia
     * @param senses
     * @return TRUE jesli ja zawiera
     */
    @Override
    public boolean contains(Sense unit, List<Sense> senses) {
        if (senses != null) {
            if (senses.stream().anyMatch((base) -> (base.getId().equals(unit.getId())))) {
                return true;
            }
        }
        return false;
    }

    /**
     * sprwadzenie czy synset zawiera jednostke o danym lemacie
     *
     * @param lemma - lemat jednostki
     * @param senses
     * @return TRUE jesli ja zawiera
     */
    @Override
    public boolean contains(String lemma, List<Sense> senses) {
        if (senses != null) {
            for (Sense base : senses) {
                if (base.getLemma().getWord().equals(lemma)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * pobiera dynamiczne atrybuty - definition, comment, isabstract itd
     *
     * @param synset
     * @param fieldName
     * @return value - wartosc pola
     */
    @Override
    public String getSynsetAtrribute(Synset synset, String fieldName) {
        SynsetAttribute synsetAttribute = synsetAttributeDao.getSynsetAttributeForName(synset, fieldName);
        if (null == synsetAttribute
                || null == synsetAttribute.getValue()
                || null == synsetAttribute.getValue().getText()) {
            return "";
        }
        return synsetAttribute.getValue().getText();
    }

    /**
     * zapisuje dynamiczne atrybuty - definition, comment, isabstract itd
     *
     * @param synset
     * @param key - klucz (nazwa pola dynamicznego)
     * @param value - wartosc pola
     */
    @Override
    public void setSynsetAtrribute(Synset synset, String key, String value) {
        synsetAttributeDao.saveOrUpdateSynsetAttribute(synset, key, value);
    }

    @Override
    public Synset updateSynset(Synset synset) {
        if (null == synset.getId()) {
            persistObject(synset);
            refresh(synset);
        }

        try {
            List<SynsetAttribute> attributes = synset.getSynsetAttributes();

            if (attributes != null) {
                for (SynsetAttribute synsetAttribute : attributes) {
                    setSynsetAtrribute(synset, synsetAttribute.getType().getTypeName().getText(), synsetAttribute.getValue().getText());
                }
            }
        } catch (Exception e) {

        }

        return mergeObject(synset);
    }

    /**
     * odbudowanie opisu
     *
     * @param synset
     * @param lexicons
     * @return
     */
    @Override
    public String rebuildUnitsStr(Synset synset, List<Long> lexicons) {
        // jest pusty synset
        String unitsstr = "";
        List<Sense> senses = dbFastGetUnits(synset, lexicons);
        if (senses.isEmpty()) {
            unitsstr = "! S.y.n.s.e.t p.u.s.t.y !";
            synset.setSplit(0);
        } else {
            StringBuilder temp = new StringBuilder();

            temp.append("(");

            int index = 0, size = senses.size();
            int pos = synset.getSplit();
            if (pos > size) // czy nie jest za duzo
            {
                pos = size;
            } else if (pos == 0) // jesli jest na zerowej pozycji, to przenoszony na 1
            {
                pos = 1;
            }
            synset.setSplit(pos);

            for (Sense sense : senses) {
                temp.append(sense.toString());
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

    @Override
    public List<SenseToSynset> getSenseToSynsetBySynset(Synset synset) {
        return getEM().
                createNamedQuery("SenseToSynset.findAllBySynsets", SenseToSynset.class)
                .setParameter("ids", synset.getId())
                .getResultList();
    }

    @Override
    public Long fastGetPOSID(Synset synset) {
        List<Long> ids = getEM().createNamedQuery("Synset.fastGetPOSID", Long.class)
                .setParameter("idSynset", synset.getId())
                .getResultList();

        if (ids != null && ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    @Override
    public List<Sense> dbFastGetSenseBySynset(String filter, Domain domain,
            RelationType relationType, String definition, String comment,
            String artificial, int limitSize,
            pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos,
            List<Long> lexicons) {

        CriteriaBuilder criteriaBuilder = getEM().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<SenseToSynset> stsRoot = criteriaQuery.from(SenseToSynset.class);
        Join<SenseToSynset, Sense> sense = stsRoot.join("sense", JoinType.LEFT);
        Join<Sense, Word> word = sense.join("lemma", JoinType.LEFT);

        List<Predicate> criteriaList = new ArrayList<>();

        Predicate first_predicate = criteriaBuilder.like(
                criteriaBuilder.lower(word.<String>get("word")), "%" + filter.toLowerCase() + "%");
        criteriaList.add(first_predicate);

        if (domain != null) {
            Predicate secend_predicate = criteriaBuilder.equal(sense.get("domain").get("id"), domain.getId());
            criteriaList.add(secend_predicate);
        }
        if (pos != null) {
            Predicate third_predicate = criteriaBuilder.equal(sense.get("partOfSpeech").get("ubyLmfType"), pos);
            criteriaList.add(third_predicate);
        }
        if (relationType != null) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetRelation> relRoot = subquery.from(SynsetRelation.class);
            subquery.select(relRoot.get("synsetFrom").<Long>get("id"));
            subquery.where(criteriaBuilder.equal(relRoot.get("relation").get("id"), relationType.getId()));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);

        }
        if (!definition.isEmpty()) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
            subquery.select(relRoot.get("synset").<Long>get("id"));
            List<Predicate> predicates = new ArrayList<>();
            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.DEFINITION);
            predicates.add(type);
            Predicate value = criteriaBuilder.like(relRoot.<String>get("value").<String>get("text"), "%" + definition + "%");
            predicates.add(value);
            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        if (!comment.isEmpty()) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
            subquery.select(relRoot.get("synset").<Long>get("id"));
            List<Predicate> predicates = new ArrayList<>();
            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.COMMENT);
            predicates.add(type);
            Predicate value = criteriaBuilder.like(relRoot.get("value").<String>get("text"), "%" + comment + "%");
            predicates.add(value);
            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        if (!artificial.isEmpty()) {
            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<SynsetAttribute> relRoot = subquery.from(SynsetAttribute.class);
            subquery.select(relRoot.get("synset").<Long>get("id"));
            List<Predicate> predicates = new ArrayList<>();
            Predicate type = criteriaBuilder.equal(relRoot.<String>get("type").get("typeName").get("text"), Synset.ISABSTRACT);
            predicates.add(type);
            Predicate value = criteriaBuilder.equal(relRoot.get("value").<String>get("text"), artificial);
            predicates.add(value);
            subquery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            Predicate subquery_predicate = criteriaBuilder.in(stsRoot.get("idSynset")).value(subquery);
            criteriaList.add(subquery_predicate);
        }
        criteriaQuery.select(stsRoot.<Long>get("idSynset")).distinct(true);
        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));

        final TypedQuery<Long> query = getEM().createQuery(criteriaQuery);
        query.setFirstResult(0);
        if (limitSize > 0) {
            query.setMaxResults(limitSize);
        }
        List<Long> synsetIds = query.getResultList();
        List<Sense> result = new ArrayList<>();
        for (Long id : synsetIds) {
            result.addAll(dbFastGetUnits(id, lexicons));
        }
        return result;
    }
}
