package pl.edu.pwr.wordnetloom.dao;

import org.hibernate.Hibernate;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dto.*;
import pl.edu.pwr.wordnetloom.model.*;
import pl.edu.pwr.wordnetloom.model.yiddish.Transcription;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishDomain;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.*;
import java.util.regex.Pattern;

@Stateless
public class LexicalUnitDAOBean extends DAOBean implements LexicalUnitDAOLocal {

    @EJB
    private DAOLocal dao;

    @EJB
    private SenseAttributeDaoLocal senseAttributeDao;
    @EJB
    private UnitAndSynsetDAOLocal unitAndSynsetDAO;
    @EJB
    private LexicalRelationDAOLocal lexicalRelationDAO;
    @EJB
    private SynsetDAOLocal synsetDAO;

    @EJB
    private DictionaryDaoLocal dicDAO;

    @EJB
    private TrackerDaoLocal tracker;

    public LexicalUnitDAOBean() {
    }

    @Override
    public Sense dbClone(Sense sense) {
        Sense clone = new Sense(sense);
        clone = dbSave(clone);

        setSenseAtrribute(clone, Sense.COMMENT, getSenseAtrribute(clone, Sense.COMMENT));
        setSenseAtrribute(clone, Sense.DEFINITION, getSenseAtrribute(clone, Sense.DEFINITION));

        String reg = getSenseAtrribute(clone, Sense.REGISTER) != null ? getSenseAtrribute(clone, Sense.REGISTER) : "brak rejestru";
        setSenseAtrribute(clone, Sense.REGISTER, reg);
        setSenseAtrribute(clone, Sense.USE_CASES, getSenseAtrribute(clone, Sense.USE_CASES));
        setSenseAtrribute(clone, Sense.LINK, getSenseAtrribute(clone, Sense.LINK));

        for (YiddishSenseExtension ext : sense.getYiddishSenseExtension()) {

            YiddishSenseExtension newExt = new YiddishSenseExtension(ext);
            newExt.setSense(clone);
            save(newExt);

            for (Particle p : ext.getParticels()) {
                Particle np = null;
                if (p instanceof InterfixParticle) {
                    np = new InterfixParticle((InterfixParticle) p, newExt);
                }
                if (p instanceof PrefixParticle) {
                    np = new PrefixParticle((PrefixParticle) p, newExt);
                }
                if (p instanceof RootParticle) {
                    np = new RootParticle((RootParticle) p, newExt);
                }
                if (p instanceof SuffixParticle) {
                    np = new SuffixParticle((SuffixParticle) p, newExt);
                }
                if (np != null)
                    saveParticle(np);
            }
        }
        return clone;
    }

    @Override
    public Sense dbSave(Sense sense) {
        if (sense.getId() == null)
            sense.setSenseNumber(dbGetNextVariant(sense.getLemma().getWord(), sense.getPartOfSpeech()));
        dao.persistObject(sense);
        dao.refresh(sense);
        return sense;
    }

    @Override
    public Lexicon dbSaveLexicon(Lexicon lexicon) {
        dao.persistObject(lexicon);
        return lexicon;
    }

    @Override
    public boolean dbDelete(Sense sense, String owner) {
        Sense s = dbGet(sense.getId());
        SenseAttribute sa = senseAttributeDao.getSenseAttributeForName(sense, Sense.COMMENT);
        String sas = sa == null ? null : sa.getValue() == null ? null : "" + sa.getValue();
        tracker.deletedLexicalUnit(s, sas, owner);

        // usuniecie z synsetow
        unitAndSynsetDAO.dbDeleteConnection(sense);
        // usuniecei relacji
        lexicalRelationDAO.dbDeleteConnection(sense);
        // usuniecie atrybutow dynamicznych
        senseAttributeDao.removeSenseAttribute(sense);
        // usuniecie jednostki
        dao.deleteObject(Sense.class, sense.getId());
        return true;
    }

    @Override
    public Sense dbGet(Long id) {
        List<Sense> list = dao.getEM().createNamedQuery("Sense.findSenseByID", Sense.class).setParameter("id", id)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return null;
        return list.get(0);
    }

    @Override
    public Sense dbGetWithYiddish(Long id) {

        Sense sense = dao.getEM().find(Sense.class, id);

        if (null != sense) {
            for (YiddishSenseExtension ex : sense.getYiddishSenseExtension()) {
                Hibernate.initialize(ex.getYiddishDomains());
                Hibernate.initialize(ex.getParticels());
                Hibernate.initialize(ex.getTranscriptions());
                Hibernate.initialize(ex.getInflection());
                Hibernate.initialize(ex.getYiddishDomains());
            }
        }
        return sense;
    }

    @Override
    public List<Synset> dbFastGetSynsets(Sense sense, List<Long> lexicons) {
        sense.setSynsets(new ArrayList<>(synsetDAO.dbFastGetSynsets(sense, lexicons)));
        return sense.getSynsets();
    }

    @Override // TODO: do sprawdzenia
    public List<Synset> dbFastGetSynsets(String lemma, List<Long> lexicons) {
        // Pobierz synsety zawierające w opisie szukany lemat
        Collection<Synset> synsets = synsetDAO.dbFastGetSynsets(lemma, lexicons);
        Pattern pattern = Pattern.compile("(\\(|, |\\| )" + lemma.replace("^", "") + " \\d", Pattern.DOTALL);

        // Wybierz synsety, które zawierają pełne lematy, a nie tylko jego
        // fragment
        List<Synset> filteredSynsets = new ArrayList<>();
        Iterator<Synset> it = synsets.iterator();
        while (it.hasNext()) {
            Synset synset = it.next();
            String uniStr = synsetDAO.rebuildUnitsStr(synset, lexicons);
            if (pattern.matcher(uniStr).find())
                filteredSynsets.add(synset);
        }

        return filteredSynsets;
    }

    @Override
    public int dbGetSynsetsCount(Sense unit) {
        return synsetDAO.dbGetSynsetsCount(unit);
    }

    @Override
    public void dbDeleteAll() {
        List<Sense> senses = dao.getEM().createNamedQuery("Sense.findAll", Sense.class).getResultList();
        dbDelete(senses, null);
    }

    @Override
    public List<Sense> filterSenseByLexicon(final List<Sense> senses, final List<Long> lexicons) {
        List<Sense> result = new ArrayList<>();
        for (Sense sense : senses) {
            if (lexicons.contains(sense.getLexicon().getId())) {
                result.add(sense);
            }
        }
        return result;
    }

    @Override
    public List<Sense> dbFullGetUnits(String filter, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findByLema", Sense.class).setParameter("lemma", filter.toLowerCase())
                .setParameter("lexicons", lexicons).getResultList();
    }

    @Override // TODO: do sprawdzenia
    public int dbGetUnitsCount(String filter) {
        return dao.getEM().createNamedQuery("Sense.findByLema").setParameter("lemma", filter.toLowerCase())
                .getMaxResults();
    }

    @Override
    public List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId()).setParameter("lexicons", lexicons).getResultList();
    }

    @Override
    public List<Sense> dbFullGetUnits(Synset synset, int limit, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId()).setParameter("lexicons", lexicons).setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Sense> findBySynsetWithYiddish(Synset synset) {

        List<Sense> s = dao.getEM().createQuery("select s.sense from SenseToSynset s where s.idSynset = :idSynset order by s.senseIndex", Sense.class)
                .setParameter("idSynset", synset.getId())
                .getResultList();

        return s;
    }

    @Override
    public List<Sense> dbFastGetUnits(Synset synset, int limit, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId()).setParameter("lexicons", lexicons).setMaxResults(limit)
                .getResultList();
    }

    @Override
    public int dbGetUnitsCount(Synset synset, List<Long> lexicons) {
        return dao.getEM()
                .createNamedQuery("Sense.findSenseBySynsetID")
                .setParameter("idSynset", synset.getId())
                .setParameter("lexicons", lexicons)
                .getMaxResults();
    }

    @Override
    public Domain[] dbGetUsedDomains() {
        return dao.getEM().createNamedQuery("Domain.getFromSenses", Domain.class).getResultList()
                .toArray(new Domain[]{});
    }

    @Override
    public int dbGetNextVariant(String lemma, PartOfSpeech pos) {
        int odp = 0;
        TypedQuery<Integer> q = dao.getEM().createNamedQuery("Sense.dbGetNextVariant", Integer.class)
                .setParameter("word", lemma.toLowerCase()).setParameter("pos", pos.getId());
        try {
            odp = q.getSingleResult();
        } catch (Exception e) {
            return 1;
        }
        odp = Math.max(0, odp);
        return odp + 1;
    }

    @Override
    public int dbDelete(List<Sense> list, String owner) {
        int odp = list.size();
        for (Sense sense : list) {
            dbDelete(sense, owner);
        }
        return odp;
    }

    @Override // TODO: sprawdź
    public int dbUsedUnitsCount() {
        List<Long> list = dao.getEM().createNamedQuery("Sense.dbUsedUnitsCount", Long.class).getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return 0;
        return list.get(0).intValue();
    }

    @Override
    public Set<Long> dbUsedUnitsIDs() {
        return new HashSet<>(
                dao.getEM().createNamedQuery("SenseToSynset.dbUsedUnitsIDs", Long.class).getResultList());
    }

    @Override
    public int dbGetUnitsCount() {
        List<Long> list = dao.getEM().createNamedQuery("Sense.findCountAll", Long.class).getResultList();
        if (list.isEmpty() || list.get(0) == null)
            return 0;
        return list.get(0).intValue();
    }

    /**
     * odczytanie jednostek nie majacych odmienionych form
     *
     * @return lista jednostek
     */
    @Override // TODO: check me
    public List<Sense> dbGetUnitsWithoutForms() {
        return new ArrayList<>();
    }

    /**
     * Odczytuje najwyższy numerek wariantu dla podanego lematu
     *
     * @param word
     * @return najwyższy numer wariantu lematu
     */
    @Override
    public int dbGetHighestVariant(String word, List<Long> lexicons) {
        int highest = 0;
        for (Sense unit : dbFastGetUnits(new CriteriaDTO(word), null, 0, lexicons)) {
            if (unit.getSenseNumber() > highest) {
                highest = unit.getSenseNumber();
            }
        }
        return highest;
    }

    @Override // TODO: check me
    public List<Sense> dbGetUnitsNotInAnySynset(String filter, PartOfSpeech pos) {
        Map<String, Object> params = new HashMap<>();
        String queryString = "SELECT s FROM SenseToSynset sts right join sts.sense s "
                + "WHERE sts.idSynset is null AND s.lemma.word like :filter";
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
        TypedQuery<Sense> query = dao.getEM().createQuery(queryString, Sense.class);
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

    @Override
    public List<Sense> dbGetUnitsAppearingInMoreThanOneSynset() {
        return dao.getEM().createNamedQuery("SenseToSynset.dbGetUnitsAppearingInMoreThanOneSynset", Sense.class)
                .getResultList();
    }

    @Override
    public boolean dbInAnySynset(Sense unit) {
        if (null == unit || null == unit.getId())
            return false;

        List<Long> list = dao.getEM().createNamedQuery("SenseToSynset.CountSenseBySense", Long.class)
                .setParameter("idSense", unit.getId()).getResultList();

        if (list.isEmpty() || list.get(0) == null)
            return false;
        return list.get(0).intValue() > 0;
    }

    @Override
    public String getSenseAtrribute(Sense sense, String nazwaPola) {
        SenseAttribute senseAttribute = senseAttributeDao.getSenseAttributeForName(sense, nazwaPola);
        if (null == senseAttribute || null == senseAttribute.getValue() || null == senseAttribute.getValue().getText())
            return "";
        return senseAttribute.getValue().getText();
    }

    @Override
    public void setSenseAtrribute(Sense sense, String key, String value) {
        senseAttributeDao.saveOrUpdateSenseAttribute(sense, key, value);
    }

    @Override
    public Sense updateSense(Sense sense) {
        if (null == sense.getId()) {
            sense.setLemma(seekOrSaveWord(sense.getLemma()));
            persistObject(sense);
        }
        List<SenseAttribute> atrributes = senseAttributeDao.getSenseAttributes(sense);

        if (atrributes != null)
            for (SenseAttribute senseAttribute : atrributes)
                setSenseAtrribute(sense, senseAttribute.getType().getTypeName().getText(),
                        senseAttribute.getValue().getText());
        return mergeObject(sense);
    }

    @Override
    public Lexicon getLexiconById(Long id) {
        try {
            return getEM().createNamedQuery("Lexicon.findLexById", Lexicon.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Lexicon> getLexiconsByIds(List<Long> lexiconsIds) {
        Query query = getEM().createQuery("SELECT l FROM Lexicon l JOIN FETCH l.name WHERE l.id IN (:ids)");
        return query.setParameter("ids", lexiconsIds).getResultList();
    }

    @Override
    public List<Lexicon> getAllLexicons() {
        return getEM().createNamedQuery("Lexicon.allLexicons", Lexicon.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getAllLexiconIds() {
        return getEM().createQuery("SELECT l.id FROM Lexicon l").getResultList();
    }

    @Override
    public Word seekOrSaveWord(Word word) {
        List<Word> list = getEM().createNamedQuery("Word.getWordByLemma", Word.class)
                .setParameter("lemma", word.getWord()).getResultList();
        if (list == null || list.isEmpty() || list.get(0) == null) {
            return mergeObject(word);
        }
        return list.get(0);
    }

    @Override
    public Word saveWord(Word word) {
        return mergeObject(word);
    }

    @Override
    public List<Long> getAllLemmasForLexicon(List<Long> lexicon) {
        Query query = getEM()
                .createQuery("SELECT s.lemma.id FROM Sense s WHERE s.lexicon.id IN (:lexicon) GROUP BY s.lemma.id");
        query.setParameter("lexicon", lexicon);
        return query.getResultList();
    }

    @Override
    public List<Sense> getSensesForLemmaID(long id, long lexicon) {
        Query query = getEM().createQuery("SELECT s FROM Sense s WHERE s.lemma.id = :id AND s.lexicon.id = :lexicon");
        query.setParameter("id", id);
        query.setParameter("lexicon", lexicon);
        return query.getResultList();
    }

    @Override
    public List<Sense> dbFastGetUnits(CriteriaDTO dto, pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos,
                                      int limitSize, List<Long> lexicons) {

        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<Sense> qc = cb.createQuery(Sense.class);

        Root<Sense> sense = qc.from(Sense.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(SenseSpecification.search(dto, pos).toPredicate(sense, qc, cb));
        predicates.add(SenseSpecification.byLexiconId(lexicons).toPredicate(sense, qc, cb));

        qc.select(sense).distinct(true);
        qc.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<Sense> q = getEM().createQuery(qc);
        List<Sense> result = q.setMaxResults(3000).getResultList();
        sort(result);
        return result;
    }

    @Override
    public PaginatedData<Sense> findByFilter(SenseFilter filter) {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<Sense> qc = cb.createQuery(Sense.class);

        Root<Sense> sense = qc.from(Sense.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(SenseSpecification.byFilter(filter).toPredicate(sense, qc, cb));

        qc.select(sense).distinct(true);
        qc.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<Sense> q = getEM().createQuery(qc);

        List<Sense> result = q
                .setFirstResult(filter.getPaginationData().getFirstResult())
                .setMaxResults(filter.getPaginationData().getMaxResults())
                .getResultList();
        sort(result);

        return new PaginatedData<>(countWithFilter(filter).intValue(), result);
    }

    private Long countWithFilter(SenseFilter filter) {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Sense> sense = cq.from(Sense.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(SenseSpecification.byFilter(filter).toPredicate(sense, cq, cb));

        cq.select(cb.count(sense));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        return getEM().createQuery(cq).getSingleResult();
    }

    private void sort(final List<Sense> list) {

        Collator collator = Collator.getInstance(Locale.US);
        String rules = ((RuleBasedCollator) collator).getRules();
        try {
            RuleBasedCollator correctedCollator = new RuleBasedCollator(rules.replaceAll("<'\u005f'", "<' '<'\u005f'"));
            collator = correctedCollator;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.NO_DECOMPOSITION);

        final Collator myFavouriteCollator = collator;

        Comparator<Sense> senseComparator = (a, b) -> {
            String aa = a.getLemma().getWord().toLowerCase();
            String bb = b.getLemma().getWord().toLowerCase();

            int c = myFavouriteCollator.compare(aa, bb);
            if (c == 0) {
                aa = a.getPartOfSpeech().getId().toString();
                bb = b.getPartOfSpeech().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            if (c == 0) {
                if (a.getSenseNumber() == b.getSenseNumber())
                    c = 0;
                if (a.getSenseNumber() > b.getSenseNumber())
                    c = 1;
                if (a.getSenseNumber() < b.getSenseNumber())
                    c = -1;
            }
            if (c == 0) {
                aa = a.getLexicon().getId().toString();
                bb = b.getLexicon().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            return c;
        };
        Collections.sort(list, senseComparator);
    }

    public Particle saveParticle(Particle p) {
        if (p.getId() != null) {
            return dao.mergeObject(p);
        }
        dao.persistObject(p);
        return p;
    }

    public void removeParticle(Particle p) {
        dao.refresh(p);
        dao.deleteObject(p);
    }

    public void dbRemoveYiddishSenseExtension(YiddishSenseExtension sense) {
        dao.deleteObject(sense);
    }

    public YiddishSenseExtension save(YiddishSenseExtension ext) {

        Set<SourceDictionary> l = ext.getSource();
        ext.setSource(new HashSet<>());
        l.forEach(d -> {
            if (d.getId() == null) {
                dao.persistObject(d);
            }
            ext.getSource().add(dicDAO.findById(d));
        });

        Set<YiddishDomain> dom = ext.getYiddishDomains();
        ext.setYiddishDomains(new HashSet<>());
        dom.forEach(d -> {
            if (d.getId() == null) {
                dao.persistObject(d);
            }
            ext.getYiddishDomains().add(dao.getObject(YiddishDomain.class, d.getId()));
        });

        Set<Transcription> trans = ext.getTranscriptions();
        ext.setTranscriptions(new HashSet<>());
        trans.forEach(t -> {
            if (t.getId() == null) {
                dao.persistObject(t);
            }
            ext.getTranscriptions().add(dao.getObject(Transcription.class, t.getId()));
        });

        if (ext.getId() == null) {
            dao.persistObject(ext);
            return ext;
        } else {
            return dao.mergeObject(ext);
        }
    }

    @Override
    public int dbGetUnitCountByDomain(final String domain) {
        CriteriaBuilder criteriaBuilder = getEM().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Sense> root = query.from(Sense.class);
        Join<Sense, Domain> domainJoin = root.join("domain");
        Join<Sense, Text> textJoin = domainJoin.join("name");

        query.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.equal(textJoin.get("text"), domain));
        return getEM().createQuery(query).getSingleResult().intValue();
    }

    @Override
    public List<CountModel> dbGetEtymologicalRootsCount() {
        CriteriaBuilder criteriaBuilder = getEM().getCriteriaBuilder();
        CriteriaQuery<CountModel> query = criteriaBuilder.createQuery(CountModel.class);
        Root<YiddishSenseExtension> root = query.from(YiddishSenseExtension.class);
        query.select(criteriaBuilder.construct(CountModel.class, root.get("etymologicalRoot"), criteriaBuilder.count(root)));
        query.groupBy(root.get("etymologicalRoot"));
        return getEM().createQuery(query).getResultList();
    }

    @Override
    public SenseGraphDTO getGraphForSense(Long id) {


        Sense s = dbGet(id);
        List<SenseRelation> incoming = lexicalRelationDAO.dbGetUpperRelations(s, null);
        List<SenseRelation> outgoing = lexicalRelationDAO.dbGetSubRelations(s, null);

        Set<SenseDTO> in = new HashSet<>();
        Set<SenseDTO> out = new HashSet<>();

        incoming.stream().forEach(r -> {
            in.add(buildSenselDTO(r.getSense_from()));
        });

        outgoing.stream().forEach(r -> {
            out.add(buildSenselDTO(r.getSense_to()));
        });

        String lemma = s.getLemma().getWord() + " " + s.getSenseNumber();

        SenseGraphDTO dto = new SenseGraphDTO(s.getId(), lemma, s.getPartOfSpeech().getId(), in, out);

        return dto;
    }

    private SenseDTO buildSenselDTO(Long id) {

        Sense s = dbGet(id);
        Map<String, Set<RelationDTO>> incomming = lexicalRelationDAO.dbGetUpperRelations(id);
        Map<String, Set<RelationDTO>> outgoing = lexicalRelationDAO.dbGetSubRelations(id);

        String lemma = s.getLemma().getWord() + " " + s.getSenseNumber();
        return new SenseDTO(s.getId(), lemma, s.getPartOfSpeech().getId(), incomming, outgoing);
    }
}
