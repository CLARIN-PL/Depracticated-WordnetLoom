package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.*;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

@Local
public interface UnitAndSynsetDAOLocal extends DAOLocal {

    void dbDeleteConnection(Sense sense);

    int dbGetSimilarityCount(Synset a, Synset b);

    int dbGetUsedUnitsCount();

    int dbGetUsedSynsetsCount();

    int dbGetConnectionsCount();

    boolean dbAddConnection(Sense unit, Synset synset, boolean rebuildUnitsStr);

    Synset dbAddConnection(Sense unit, Synset synset);

    void dbDeleteConnection(Synset template, boolean rebuildUnitsStr);

    Synset dbDeleteConnection(Sense unit, Synset synset);

    boolean dbExchangeUnits(Synset synset, Sense firstUnit, Sense secondUnit);

    List<SenseToSynset> dbGetConnections(Synset synset);

    List<SenseToSynset> dbFullGetConnections();

}
