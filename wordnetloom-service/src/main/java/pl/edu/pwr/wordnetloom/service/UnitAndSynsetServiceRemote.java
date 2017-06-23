package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Remote
public interface UnitAndSynsetServiceRemote extends DAORemote {

    boolean dbAddConnection(Sense unit, Synset synset, boolean rebuildUnitsStr);

    Synset dbAddConnection(Sense unit, Synset synset);

    void dbDeleteConnection(Sense template);

    void dbDeleteConnection(Synset template, boolean rebuildUnitsStr);

    Synset dbDeleteConnection(Sense unit, Synset synset);

    boolean dbExchangeUnits(Synset synset, Sense firstUnit, Sense secondUnit);

    List<SenseToSynset> dbGetConnections(Synset synset);

    List<SenseToSynset> dbFullGetConnections();

    int dbGetSimilarityCount(Synset a, Synset b);

    int dbGetUsedUnitsCount();

    int dbGetUsedSynsetsCount();

    int dbGetConnectionsCount();

}
