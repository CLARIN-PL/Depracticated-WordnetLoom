package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.ExtGraphDAOLocal;
import pl.edu.pwr.wordnetloom.dao.ExtGraphExtensionDAOLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetAttributeDaoLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetDAOLocal;
import pl.edu.pwr.wordnetloom.dao.SynsetRelationDAOLocal;
import pl.edu.pwr.wordnetloom.dao.UnitAndSynsetDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

/**
 * klasa odpowiadajaca za zarzadzanie danymi o polaczeniach
 *
 * @author Max
 *
 */
@Stateless
public class UnitAndSynsetDAOBean extends DAOBean implements UnitAndSynsetDAOLocal {

    @EJB
    private DAOLocal dao;

    @EJB
    private SynsetDAOLocal synsetDAO;

    @EJB
    private SynsetAttributeDaoLocal saDAO;

    @EJB
    private ExtGraphDAOLocal exDAO;

    @EJB
    private ExtGraphExtensionDAOLocal exeDAO;

    @EJB
    private SynsetRelationDAOLocal srDAO;

    public UnitAndSynsetDAOBean() {
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
        String queryString = "select count(a.idSense) from SenseToSynset a,"
                + " SenseToSynset b "
                + "where a.synset = :a "
                + "and b.idSynset = :b "
                + "and a.idSense = b.idSense";
        TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);
        q.setParameter("a", a.getId());
        q.setParameter("b", b.getId());

        List<Long> list = q.getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * dodanie nowego połączenia jednostki i synsetu
     *
     * @param unit - jednostka
     * @param synset - synset
     * @param rebuildUnitsStr - czy odbudować opis jakie jednostki sa w synsecie
     * @return TRUE jesli sie udalo
     */
    @Override
    public boolean dbAddConnection(Sense unit, Synset synset, boolean rebuildUnitsStr) {

        // pobranie wszystkich elementow synsetu
        List<SenseToSynset> old = dbGetConnections(synset);

        // przeindeksowanie
        int index = 0;
        for (SenseToSynset synsetOld : old) {
            // czy nie sa identyczne
            if (synsetOld.getSynset().getId().equals(synset.getId())
                    && synsetOld.getSense().getId().equals(unit.getId())) {
                return false;
            }
            synsetOld.setSenseIndex(index++);
            dao.mergeObject(synsetOld);
        }

        // dodanie nowego
        SenseToSynset newRel = new SenseToSynset();
        newRel.setSynset(synset);
        newRel.setSense(unit);
        newRel.setIdSense(unit.getId());
        newRel.setIdSynset(synset.getId());
        newRel.setSenseIndex(index++);

        dao.persistObject(newRel);

        return true;
    }

    /**
     * dodanie nowego połączenia jednostki i synsetu
     *
     * @param unit - jednostka
     * @param synset - synset
     * @return TRUE jesli sie udalo
     */
    @Override
    public Synset dbAddConnection(Sense unit, Synset synset) {

        if (dbAddConnection(unit, synset, false)) {
            return getObject(Synset.class, synset.getId());
        }
        return dao.getObject(Synset.class, synset.getId());
    }

    /**
     * usuniecie powiazan jednostka - synset z bazy danych
     *
     * @param sense
     */
    @Override
    public void dbDeleteConnection(Sense sense) {
        // pobranie wszystkich elementow w ktorych jest jednostka
        List<SenseToSynset> senseToSynsets = dao.getEM().createNamedQuery("SenseToSynset.findAllBySense", SenseToSynset.class)
                .setParameter("idSense", sense.getId())
                .getResultList();

        // usuniecie wszystki jednostek
        for (SenseToSynset sts : senseToSynsets) {
            dao.deleteObject(SenseToSynset.class, sts.getId());
        }
    }

    /**
     * usuniecie powiazan jednostka - synset z bazy danych
     *
     * @param template - wzor synsetu
     * @param rebuildUnitsStr - TRUE jesli odbudowac opis synsetu
     */
    @Override
    public void dbDeleteConnection(Synset template, boolean rebuildUnitsStr) {

        dao.getEM()
                .createNamedQuery("SenseToSynset.DeleteBySynsetID")
                .setParameter("idSynset", template.getId())
                .executeUpdate();
    }

    /**
     * usuniecie powiazan jednostka - synset z bazy danych
     *
     * @param unit - jednostka do usuniecia
     * @param synset - synset do usuniecia
     */
    @Override
    public Synset dbDeleteConnection(Sense unit, Synset synset) {
        // usuniecie jednego powiazania
        dao.getEM()
                .createNamedQuery("SenseToSynset.DeleteBySynsetIdAndSenseId")
                .setParameter("idSynset", synset.getId())
                .setParameter("idSense", unit.getId())
                .executeUpdate();

        // pobranie wszystkich elementow dla synsetu
        List<SenseToSynset> rest = dbGetConnections(synset);

        // przeindeksowanie
        int index = 0;
        for (SenseToSynset synsetDTO : rest) {
            synsetDTO.setSenseIndex(index++);
            dao.mergeObject(synsetDTO);
        }

        // synset jest pusty, nastepuje usuniecie takiego synsetu z bazy danych
        if (rest.isEmpty()) {
            saDAO.deleteAttributesFor(synset);
            exeDAO.dbDeleteForSynset(synset);
            exDAO.deleteForSynset(synset);
            srDAO.dbDeleteConnection(synset);
            dao.deleteObject(Synset.class, synset.getId());
            return null;
        }

        return synset;
    }

    /**
     * zamienienie jednostek w synsecie
     *
     * @param synset - synset
     * @param firstUnit - pierwsza jednostka
     * @param secondUnit - druga jednostka
     * @return TRUE jesli sie udalo
     */
    @Override
    public boolean dbExchangeUnits(Synset synset, Sense firstUnit, Sense secondUnit) {
        // pobranie wszystkich elementow dla synsetu
        List<SenseToSynset> old = dbGetConnections(synset);

        // zamiana numerow indeksow
        for (SenseToSynset synsetDTO : old) {
            // zamienieni indeksow
            if (synsetDTO.getIdSense().longValue() == firstUnit.getId().longValue()) {
                synsetDTO.setSenseIndex(synsetDTO.getSenseIndex() + 1);
                dao.mergeObject(synsetDTO);
            } else if (synsetDTO.getIdSense().longValue() == secondUnit.getId().longValue()) {
                synsetDTO.setSenseIndex(synsetDTO.getSenseIndex() - 1);
                dao.mergeObject(synsetDTO);
            }
        }
        return true;
    }

    /**
     * odczytanie połączeń
     *
     * @param synset - synset dla ktorego maja zostać pobrane połączenia
     * @return połączenia
     */
    @Override
    public List<SenseToSynset> dbGetConnections(Synset synset) {
        return dao.getEM().createNamedQuery("SenseToSynset.findAllBySynset", SenseToSynset.class)
                .setParameter("idSynset", synset.getId()).getResultList();
    }

    /**
     * odczytanie wszystkich powiazan
     *
     * @return lista powiazan
     */
    @Override
    public List<SenseToSynset> dbFullGetConnections() {
        return dao.getEM().createNamedQuery("SenseToSynset.findAll", SenseToSynset.class).getResultList();
    }

    /**
     * odczytanie liczby wykorzystanych jednostek
     *
     * @return liczba wykorzystanych jednostek
     */
    @Override
    public int dbGetUsedUnitsCount() {
        String queryString = "select count(distinct sts.idSense) FROM SenseToSynset sts";
        TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);

        List<Long> list = q.getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * odczytanie liczby wykorzystanych synset
     *
     * @return liczba wykorzystanych synsetow
     */
    @Override
    public int dbGetUsedSynsetsCount() {
        String queryString = "select count(distinct sts.idSynset) FROM SenseToSynset sts";
        TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);

        List<Long> list = q.getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

    /**
     * odczytanie liczby wpisow w tabeli
     *
     * @return liczba wpisow
     */
    @Override
    public int dbGetConnectionsCount() {
        String queryString = "select count(sts.idSense) FROM SenseToSynset sts";
        TypedQuery<Long> q = dao.getEM().createQuery(queryString, Long.class);

        List<Long> list = q.getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }
        return list.get(0).intValue();
    }

}
