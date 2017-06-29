package pl.edu.pwr.wordnetloom.sense.repository;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class SenseRepository extends GenericRepository<Sense> {

    @PersistenceContext
    EntityManager em;

    public Sense clone(Sense sense) {
        return save(new Sense(sense));
    }

    @Override
    public Sense save(Sense sense) {
        if (sense.getId() == null) {
            sense.setVariant(findNextVariant(sense.getWord().getWord(), sense.getPartOfSpeech()));
        }
        return super.save(sense);
    }

    @Override
    public void delete(Sense sense) {
        Sense s = findById(sense.getId());
        // usuniecei relacji
        lexicalRelationDAO.dbDeleteConnection(sense);
        super.delete(s);
    }

    @Override
    public List<Synset> dbFastGetSynsets(Sense sense, List<Long> lexicons) {
        sense.setSynsets(new ArrayList<Synset>(synsetDAO.dbFastGetSynsets(sense, lexicons)));
        return sense.getSynsets();
    }

    @Override // TODO: do sprawdzenia
    public List<Synset> dbFastGetSynsets(String lemma, List<Long> lexicons) {
        // Pobierz synsety zawierające w opisie szukany lemat
        Collection<Synset> synsets = synsetDAO.dbFastGetSynsets(lemma, lexicons);
        Pattern pattern = Pattern.compile("(\\(|, |\\| )" + lemma.replace("^", "") + " \\d", Pattern.DOTALL);

        // Wybierz synsety, które zawierają pełne lematy, a nie tylko jego fragment
        List<Synset> filteredSynsets = new ArrayList<Synset>();
        Iterator<Synset> it = synsets.iterator();
        while (it.hasNext()) {
            Synset synset = it.next();
            String uniStr = synsetDAO.rebuildUnitsStr(synset, lexicons);
            if (pattern.matcher(uniStr).find()) {
                filteredSynsets.add(synset);
            }
        }

        return filteredSynsets;
    }

    @Override
    public int dbGetSynsetsCount(Sense unit) {
        return synsetDAO.dbGetSynsetsCount(unit);
    }

    public void deleteAll() {
        List<Sense> senses = findAll("id");
        delete(senses);
    }

    public List<Sense> dbFastGetUnits(String filter, List<Long> lexicons) {
        return dbFastGetUnits(filter, null, null, null, null, null, null, 0, lexicons);
    }

    @Override
    public List<Sense> dbFastGetUnits(String filter, PartOfSpeech pos, Domain domain, List<Long> lexicons) {
        return dbFastGetUnits(filter, pos, domain, null, null, null, null, 0, lexicons);
    }

    /**
     * odczytanie jednostek spelniajacych kryterium
     *
     * @param filter - filtr
     * @param pos - czesc mowy albo NULL
     * @param domain - domana albo NULL
     * @param workStates - akceptiowanlen statusy lub NULL
     * @param relationType - typ relacji jakie musza byc zdefiniowane dla
     * jednostek wynikowych
     * @param limitSize - maksymalna liczba zwroconych elementów
     * @param realSize - obiekt w którym zapisywana jest prawdziwa wielkość
     * kolekcji
     * @return lista jednostek leksykalnych
     */
    @Override
    public List<Sense> dbFastGetUnits(String filter, PartOfSpeech pos, Domain domain, RelationType relationType,
            String register, String comment, String example, int limitSize, List<Long> lexicons) {
        return getSenses(filter, pos, domain, relationType, register, comment, example, limitSize, lexicons, null);
    }

    private List<Sense> getSenses(String filter, PartOfSpeech pos, Domain domain, RelationType relationType,
            String register, String comment, String example, int limitSize, List<Long> lexicons, pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech posUby) {

        System.out.println("---> USER: " + context.getCallerPrincipal().getName());

        String wordQuery = "";
        String senseQuery = "SELECT s FROM Sense s JOIN FETCH s.domain JOIN FETCH s.lemma JOIN FETCH s.partOfSpeech WHERE ";

        boolean existFilter = filter != null && !filter.isEmpty();
        boolean existCriteria = pos != null || domain != null || relationType != null
                || (register != null && !register.isEmpty())
                || (comment != null && !comment.isEmpty())
                || (example != null && !example.isEmpty())
                || (posUby != null);

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

        final Collator myFavouriteCollator = collator;

        Comparator<Sense> senseComparator = (Sense a, Sense b) -> {

            String aa = a.getLemma().getWord().toLowerCase();
            String bb = b.getLemma().getWord().toLowerCase();

            int c = myFavouriteCollator.compare(aa, bb);
            if (c == 0) {
                aa = a.getPartOfSpeech().getId().toString();
                bb = b.getPartOfSpeech().getId().toString();
                c = myFavouriteCollator.compare(aa, bb);
            }
            if (c == 0) {
                if (a.getSenseNumber() == b.getSenseNumber()) {
                    c = 0;
                }
                if (a.getSenseNumber() > b.getSenseNumber()) {
                    c = 1;
                }
                if (a.getSenseNumber() < b.getSenseNumber()) {
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

            int critCounter = (domain == null ? 0 : 1) + (pos == null ? 0 : 1) + (posUby == null ? 0 : 1) + (relationType == null ? 0 : 1);

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

                TypedQuery<Long> wordIDQuery = getEM().createQuery(wordQuery, Long.class);

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
                if (posUby != null) {
                    senseQuery += "s.partOfSpeech.ubyLmfType = :pos AND ";
                    parameters.put("pos", posUby);
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

                TypedQuery<Sense> q = dao.getEM().createQuery(senseQuery, Sense.class);

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
            if (posUby != null) {
                senseQuery += "s.partOfSpeech.ubyLmfType = :pos AND ";
                parameters.put("pos", posUby);
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

            TypedQuery<Sense> q = dao.getEM().createQuery(senseQuery, Sense.class);

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
                    String name = s.getLemma().getWord();

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

        TypedQuery<Long> wordIDQuery = getEM().createQuery(wordQuery, Long.class);

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

        TypedQuery<Sense> lastQuery = dao.getEM().createQuery(senseQuery, Sense.class);

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

    @Override
    public List<Sense> filterSenseByLexicon(final List<Sense> senses, final List<Long> lexicons) {
        List<Sense> result = new ArrayList<>();
        senses.stream().filter((sense) -> (lexicons.contains(sense.getLexicon().getId()))).forEach((sense) -> {
            result.add(sense);
        });
        return result;
    }

    @Override
    public List<Sense> dbFullGetUnits(String filter, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findByLema", Sense.class)
                .setParameter("lemma", filter.toLowerCase())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Override // TODO: do sprawdzenia
    public int dbGetUnitsCount(String filter) {
        return dao.getEM().createNamedQuery("Sense.findByLema")
                .setParameter("lemma", filter.toLowerCase())
                .getMaxResults();
    }

    @Override
    public List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId())
                .setParameter("lexicons", lexicons)
                .getResultList();
    }

    @Override
    public List<Sense> dbFullGetUnits(Synset synset, int limit, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId())
                .setParameter("lexicons", lexicons)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Sense> dbFastGetUnits(Synset synset, int limit, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID", Sense.class)
                .setParameter("idSynset", synset.getId())
                .setParameter("lexicons", lexicons)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public int dbGetUnitsCount(Synset synset, List<Long> lexicons) {
        return dao.getEM().createNamedQuery("Sense.findSenseBySynsetID")
                .setParameter("idSynset", synset.getId())
                .setParameter("lexicon", lexicons)
                .getMaxResults();
    }

    public int findNextVariant(String lemma, PartOfSpeech pos) {
        int odp = 0;
        TypedQuery<Integer> q = em.createQuery("SELECT MAX(s.senseNumber) FROM Sense AS s WHERE LOWER(s.word.word) = :word AND s.partOfSpeech.id = :pos", Integer.class)
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

    @Override
    public int dbDelete(List<Sense> list, String owner) {
        int odp = list.size();
        list.stream().forEach((sense) -> {
            dbDelete(sense, owner);
        });
        return odp;
    }

    @Override // TODO: sprawdź
    public int dbUsedUnitsCount() {
        List<Long> list = dao.getEM().createNamedQuery("Sense.dbUsedUnitsCount", Long.class)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    @Override
    public Set<Long> dbUsedUnitsIDs() {
        return new HashSet<>(
                dao.getEM().createNamedQuery("SenseToSynset.dbUsedUnitsIDs", Long.class)
                .getResultList()
        );
    }

    @Override
    public int dbGetUnitsCount() {
        List<Long> list = dao.getEM().createNamedQuery("Sense.findCountAll", Long.class)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * Odczytuje najwyższy numerek wariantu dla podanego lematu
     *
     * @param word
     * @param lexicons
     * @return najwyższy numer wariantu lematu
     */
    @Override
    public int dbGetHighestVariant(String word, List<Long> lexicons) {
        int highest = 0;
        for (Sense unit : dbFastGetUnits(word, null, null, null, null, null, null, 0, lexicons)) {
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
        if (null == unit || null == unit.getId()) {
            return false;
        }

        List<Long> list = dao.getEM().createNamedQuery("SenseToSynset.CountSenseBySense", Long.class)
                .setParameter("idSense", unit.getId())
                .getResultList();

        if (list.isEmpty() || list.get(0) == null) {
            return false;
        }
        return list.get(0).intValue() > 0;
    }

    @Override
    public Sense updateSense(Sense sense) {

        if (null == sense.getId()) {
            sense.setLemma(seekOrSaveWord(sense.getLemma()));
            persistObject(sense);
        }

        List<SenseAttributes> atrributes = senseAttributeDao.getSenseAttributes(sense);

        if (atrributes != null) {
            atrributes.stream().forEach((senseAttribute) -> {
                setSenseAtrribute(sense, senseAttribute.getType().getTypeName().getText(), senseAttribute.getValue().getText());
            });
        }

        return mergeObject(sense);
    }

    @Override
    public List<Long> getAllLemmasForLexicon(List<Long> lexicon) {
        Query query = getEM().createQuery("SELECT s.lemma.id FROM Sense s WHERE s.lexicon.id IN (:lexicon) GROUP BY s.lemma.id");
        query.setParameter("lexicon", lexicon);
        return query.getResultList();
    }

    public List<Sense> findSensesByWordId(Long id, Long lexicon) {
        Query query = em.createQuery("FROM Sense s WHERE s.word.id = :id AND s.lexicon.id = :lexicon");
        query.setParameter("id", id);
        query.setParameter("lexicon", lexicon);
        return query.getResultList();
    }

    @Override
    protected Class<Sense> getPersistentClass() {
        return Sense.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
