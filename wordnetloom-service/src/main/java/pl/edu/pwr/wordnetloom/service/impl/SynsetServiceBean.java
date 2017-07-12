package pl.edu.pwr.wordnetloom.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetDAOLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetRelationDAOLocal;
import pl.edu.pwr.wordnetloom.dao.impl.DAOBean;
import pl.edu.pwr.wordnetloom.model.dto.CountInfo;
import pl.edu.pwr.wordnetloom.model.dto.DataEntry;
import pl.edu.pwr.wordnetloom.model.dto.SynsetInfo;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import pl.edu.pwr.wordnetloom.model.wordnet.SynsetRelation;
import pl.edu.pwr.wordnetloom.service.SynsetServiceRemote;

@Stateless
public class SynsetServiceBean extends DAOBean implements SynsetServiceRemote {

    public SynsetServiceBean() {
    }

    @EJB
    private SynsetDAOLocal local;
    @EJB
    private SynsetRelationDAOLocal relations;
    @EJB
    private DAOLocal dao;

    /**
     * powielenie synsetu
     *
     * @param synset - synset do sklonowania
     * @param owner - nowy wlasciciel
     */
    @Override
    public void dbClone(Synset synset, List<Long> lexicons) {
        local.dbClone(synset, lexicons);
    }

    /**
     * usuniecie obiektu
     *
     * @param synset - synset do usuniecia
     * @return TRUE jesli sie udalo
     */
    @Override
    public boolean dbDelete(Synset synset) {
        return local.dbDelete(synset);
    }

    /**
     * odczytanie jednostek leksykalnych podanego synsetu
     *
     * @param synset - synset dla ktorego maja zostac pobrane jednostki
     * @return lista jednostek
     */
    @Override
    public List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons) {
        return local.dbFastGetUnits(synset, lexicons);
    }

    /**
     * odczytanie jednostek leksykalnych podanych synsetów
     *
     * @param synset - synset dla którego mają zostać pobrane jednostki
     */
    @Override
    public Synset dbGetUnit(Synset synset, List<Long> lexicons) {
        return local.dbGetUnit(synset, lexicons);
    }

    /**
     * odczytanie jednostek leksykalnych podanych synsetów
     *
     * @param synset - synsety dla których mają zostać pobrane jednostki
     */
    @Override
    public List<Synset> dbGetUnits(List<Synset> synsets) {
        return local.dbGetUnits(synsets);
    }

    /**
     * odczytanie ilosc jednostek leksykalnych zwiazanych z danycm synsetem
     *
     * @param synset - synset dla ktorego ma zostac pobrana ilosc jednostek
     * @return liczba jednostek
     */
    @Override
    public int dbGetUnitsCount(Synset synset) {
        return local.dbGetUnitsCount(synset);
    }

    /**
     * odbudowa opisu synsetu
     *
     * @param synset - synset
     */
    @Override
    public String dbRebuildUnitsStr(Synset synset, List<Long> lexicons) {
        return local.dbRebuildUnitsStr(synset, lexicons);
    }

    /**
     * odczytanie czesci mowy synsetu
     *
     * @param synset - synset
     * @return czesc mowy albo NULL gdy nie zdefiniowana
     */
    @Override
    public PartOfSpeech dbGetPos(Synset synset, List<Long> lexicons) {
        return local.dbGetPos(synset, lexicons);
    }

    /**
     * odczytanie domeny synsetu
     *
     * @param synset - synset
     * @return domena albo NULL gdy nie zdefiniowana
     */
    @Override
    public Domain dbGetDomain(Synset synset, List<Long> lexicons) {
        return local.dbGetDomain(synset, lexicons);
    }

    /**
     * zapisanie polaczen jednostek z synsetami w bazie danych
     *
     * @param synsets - lista synsetow, ktorych jednostki maja zostac dodane do
     * polaczen
     */
    //	ZMIANA SYGNATURY METODY: Z KOLEKCJI SYNSETOW NA MAPE
    @Override
    public void dbSaveConnections(HashMap<Synset, List<Sense>> map) {
        local.dbSaveConnections(map);
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @return lista synsetow (bez detali)
     */
    @Override
    public List<Synset> dbFastGetSynsets(String filter, List<Long> lexicons) {
        return local.dbFastGetSynsets(filter, lexicons);
    }

    /**
     * odczytanie sysnetow
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
        return local.dbFastGetSynsets(filter, domain, relationType, limitSize, lexicons);
    }

    /**
     * odczytanie sysnetow z filtrowaniem po części mowy
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @param workStates - akceptowalne statusy lub NULL akceptuje wszystkie
     * @param domain - akceptowalna domena lub NULL aby akceptować wszystko
     * @param relationType - typ relacji jakie musza byc zdefiniowane dla
     * synsetow wynikowych
     * @param limitSize - maksymalna liczba zwroconych elementów
     * @param realSize - obiekt w którym zapisywana jest prawdziwa wielkość
     * kolekcji
     * @param posIndex - indeks części mowy (-1 wszystkie, 0 nieznany, itd.
     * zgodnie z enum Pos)
     * @return lista synsetow (bez detali)
     */
    @Override
    public List<Synset> dbFastGetSynsets(String filter, Domain domain, RelationType relationType, int limitSize, long posIndex, List<Long> lexicons) {
        return local.dbFastGetSynsets(filter, domain, relationType, limitSize, lexicons);
    }

    @Override
    public List<Sense> dbFastGetSenseBySynset(String filter, Domain domain, RelationType relationType, String definition, String comment, String artificial, int limitSize, long posIndex, List<Long> lexicons, StatusDictionary status) {
        return local.dbFastGetSenseBySynset(filter, domain, relationType, definition, comment, artificial, limitSize, posIndex, lexicons, status);
    }

    @Override
    public List<Sense> dbFastGetSenseBySynsetUbyPose(String filter, Domain domain, RelationType relationType, String definition, String comment, String artificial, int limitSize, pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos, List<Long> lexicons, StatusDictionary status) {
        return local.dbFastGetSenseBySynset(filter, domain, relationType, definition, comment, artificial, limitSize, pos, lexicons, status);
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
        return local.dbFullGet(synset_ids);
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @param filterObject - filtr obietkowy, musi miec taki sam POS
     * @return lista synsetow (bez detali)
     */
    @Override
    public List<Synset> dbFastGetSynsets(String filter, Sense filterObject, List<Long> lexicons) {
        return local.dbFastGetSynsets(filter, filterObject, lexicons);
    }

    /**
     * usuniecie pustych synsetow
     *
     * @return liczba usunietych wierszy
     *
     */
    @Override
    public int dbDeleteEmpty() {
        return local.dbDeleteEmpty();
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @return lista synsetow (z detalimi)
     */
    @Override
    public List<Synset> dbFullGetSynsets(String filter) {
        return local.dbFullGetSynsets(filter);
    }

    /**
     * odczytanie sysnetow
     *
     * @param filter - filtr dla przechowywanych jednostek
     * @return lista synsetow (z detalimi)
     */
    @Override
    public List<Synset> dbGetNotEmptySynsets(String filter) {
        return local.dbGetNotEmptySynsets(filter);
    }

    /**
     * odczytanie synsetow zwiazanych z podana jednostka leksykalna
     *
     * @param unit - jednostka leksykalna
     * @return lista synsetow
     */
    @Override
    public List<Synset> dbFastGetSynsets(Sense unit, List<Long> lexicons) {
        return local.dbFastGetSynsets(unit, lexicons);
    }

    /**
     * odczytanie liczby synsetow zwiazanych z podana jednostka leksykalna
     *
     * @param unit - jednostka leksykalna
     * @return liczba synsetow
     */
    @Override
    public int dbGetSynsetsCount(Sense unit) {
        return local.dbGetSynsetsCount(unit);
    }

    /**
     * pobranie synsetu o podanym ID
     *
     * @param id - id synsetu
     * @return synset albo NULL
     */
    @Override
    public Synset dbGet(Long id) {
        return local.dbGet(id);
    }

    @Override
    public Synset dbGetSynsetRels(Synset synset) {
        return local.dbGetSynsetRels(synset);
    }

    @Override
    public List<Synset> dbGetSynsetsRels(List<Synset> synsets) {
        return local.dbGetSynsetsRels(synsets);
    }

    /**
     * @param ind collection of synsets indices
     * @return collection of synset objects
     */
    @Override
    public List<Synset> dbGetSynsetsUnitsRels(Collection<Long> ind) {
        return local.dbGetSynsetsUnitsRels(ind);
    }

    /**
     * odczytanie liczby synsetow w bazie
     *
     * @return liczba synsetow
     */
    @Override
    public int dbGetSynsetsCount() {
        return local.dbGetSynsetsCount();
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
        return local.dbGetSimilarityCount(a, b);
    }

    //	/**
    //	 * odczytanie uzytkownikow wystepujacych w synsetach
    //	 * @return uzytkownicy wystepujacy w synsetach
    //	 */
    //	public List<String> dbGetUsers() {
    //		return local.dbGetUsers();
    //	}
    //
    //	/**
    //	 * odczytanie uzytkownikow wystepujacych w synsetach
    //	 * @param domain - zaweza sprawdzenie do synsetow, ktore naleza do okreslonej dziedziny
    //	 * @return uzytkownicy wystepujacy w synsetach
    //	 */
    //	public List<String> dbGetUsers(Domain domain) {
    //		return local.dbGetUsers(domain);
    //	}
    //
    //	/**
    //	 * odczytanie statusow dla konkretnego uzytkownika i domeny
    //	 * @param owner - uzytkownik
    //	 * @param domain - domena
    //	 * @return liczba synsetow blednych, poprawnych i niesprawdzonych
    //	 */
    //	public int[] dbGetUserStats(String owner,Domain domain) {
    //		return local.dbGetUserStats(owner, domain);
    //	}
    //	/**
    //	 * Odczytanie opisow synsetow
    //	 * @return mapa opisow synsetow
    //	 */
    //	public Map<Long,String> dbGetSynsetsDescription() {
    //		return local.dbGetSynsetsDescription();
    //	}
    //	/**
    //	 * Odczytywanie opisów synsetów o danym ID
    //	 * @param idx - lista ID sysnetów
    //	 * @return mapa opisow synsetow
    //	 */
    @Override
    public Map<Long, String> dbGetSynsetsDescriptionIdx(List<Long> idx, List<Long> lexicons) {
        return local.dbGetSynsetsDescriptionIdx(idx, lexicons);
    }

    @Override
    public String getSynsetAtrribute(Synset synset, String nazwaPola) {
        return local.getSynsetAtrribute(synset, nazwaPola);
    }

    @Override
    public void setSynsetAtrribute(Synset synset, String key, String value) {
        local.setSynsetAtrribute(synset, key, value);
    }

    @Override
    public Synset updateSynset(Synset synset) {
        return local.updateSynset(synset);
    }

    @Override
    public Synset fetchSynsetForSense(Sense sense, List<Long> lexicons) {
        List<Synset> synsets = local.dbFastGetSynsets(sense, lexicons);
        if (synsets == null || synsets.isEmpty() || synsets.get(0) == null) {
            return null;
        }
        return synsets.get(0);
    }

    @Override
    public List<SenseToSynset> getSenseToSynsetBySynset(Synset synset) {
        return local.getSenseToSynsetBySynset(synset);
    }

    @Override
    public Long fastGetPOSID(Synset synset) {
        return local.fastGetPOSID(synset);
    }

    @Override
    public HashMap<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons) {
        HashMap<Long, DataEntry> map = new HashMap<Long, DataEntry>();

        long start = System.currentTimeMillis();
        // step 1 - synset relations from and to root synset (synset)
        List<SynsetRelation> rels = relations.getRelatedRelations(synset, lexicons); // fetch
        DataEntry rootEntry = new DataEntry();
        rootEntry.setSynset(synset);

        HashMap<Long, Synset> synsets = new HashMap<Long, Synset>(); // wszystkie synsety do wyświetlenia (z relacji 1wszego stopnia)

        // step 1.5 - setting associations
        for (SynsetRelation s : rels) {
            if (s.getSynsetFrom().getId().equals(synset.getId())) {
                rootEntry.getRelsFrom().add(s);
            } else {
                rootEntry.getRelsTo().add(s);
            }

            synsets.put(s.getSynsetFrom().getId(), s.getSynsetFrom());
            synsets.put(s.getSynsetTo().getId(), s.getSynsetTo());
        }

        synsets.remove(synset.getId());

        // step 2 - synset relations from and to related synsets
        if (!synsets.isEmpty()) { // otherwise Unexpected end of Subtree exception
            rels = relations.getRelatedRelations(synsets.keySet());

//			for(SynsetRelation r : rels){ // iterowanie po wszystkich relacjach 2giego stopnia
//				Long from = r.getSynsetFrom().getId();
//				Long to = r.getSynsetTo().getId();
//
//				DataEntry e1 = map.get(from);
//				if(e1==null){
//					e1 = new DataEntry();
//					e1.setSynset(r.getSynsetFrom());
//					map.put(from, e1);
//				}
//				e1.getRelsFrom().add(r);
//
//				e1 = map.get(to);
//				if(e1==null){
//					e1 = new DataEntry();
//					e1.setSynset(r.getSynsetTo());
//					map.put(to, e1);
//				}
//				e1.getRelsTo().add(r);
//			}
            map.put(rootEntry.getSynset().getId(), rootEntry);
            synsets.put(rootEntry.getSynset().getId(), rootEntry.getSynset());

            List<SynsetInfo> infos = dao.getEM().
                    createQuery("SELECT NEW pl.edu.pwr.wordnetloom.model.dto.SynsetInfo(sy.id, se.partOfSpeech.id, name.text, lemma.word, syt.value.text, se.senseNumber, lexId.text) FROM Synset sy JOIN sy.senseToSynset AS sts JOIN sts.sense AS se JOIN se.domain AS dom JOIN dom.name AS name JOIN sy.synsetAttributes AS syt JOIN se.lemma as lemma JOIN se.lexicon as lex JOIN lex.lexiconIdentifier as lexId WHERE sts.senseIndex = 0 AND syt.type.typeName.text = :abstractName AND sy.id IN (:ids)", SynsetInfo.class)
                    .setParameter("abstractName", Synset.ISABSTRACT)
                    .setParameter("ids", lexicons)
                    .getResultList();

            List<CountInfo> counts = dao.getEM()
                    .createQuery("SELECT NEW pl.edu.pwr.wordnetloom.model.dto.CountInfo(sy.id, count(se)) FROM Synset AS sy JOIN sy.senseToSynset AS sts JOIN sts.sense AS se WHERE sy.id IN (:ids) GROUP BY sy.id", CountInfo.class)
                    .setParameter("ids", lexicons)
                    .getResultList();

            HashMap<Long, CountInfo> counter = new HashMap<Long, CountInfo>();
            for (CountInfo c : counts) {
                counter.put(c.getSynsetID(), c);
            }

            for (SynsetInfo sysInf : infos) {
                DataEntry e = map.get(sysInf.getSynsetID());
                StringBuilder sb = new StringBuilder();
                if ("1".equals(sysInf.getIsAbstract())) {
                    sb.append("S ");
                }
                sb.append(sysInf.getWord());
                sb.append(" ");
                sb.append(sysInf.getSenseNumber());
                sb.append(" (");
                sb.append(sysInf.getDomain());
                sb.append(")");

                CountInfo c = counter.get(sysInf.getSynsetID());
                if (c != null && c.getCount() != null && c.getCount().intValue() > 1) {
                    sb.append(" ...");
                }

                e.setLabel(sb.toString());
                e.setLexicon(sysInf.getLexicon());
                e.setPosID(sysInf.getPosID());
            }
        }

        // step 4 - finish
        long end = System.currentTimeMillis();
        System.out.println("Time: " + Long.toString(end - start));
        return map;
    }

    @Override
    public List<Sense> dbGetUnits(Long synsetId, List<Long> lexicons) {
        return local.dbFastGetUnits(synsetId, lexicons);
    }

    @Override
    public Boolean areSynsetsInSameLexicon(long synset1, long synset2) {
        return local.areSynsetsInSameLexicon(synset1, synset2);
    }

}
