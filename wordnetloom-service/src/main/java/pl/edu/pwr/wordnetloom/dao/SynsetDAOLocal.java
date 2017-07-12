package pl.edu.pwr.wordnetloom.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.SenseToSynset;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

@Local
public interface SynsetDAOLocal extends DAOLocal {

    void dbSaveConnections(Map<Synset, List<Sense>> map);

    int dbDeleteEmpty();

    List<Synset> dbFullGetSynsets(String filter);

    List<Synset> dbGetNotEmptySynsets(String filter);

    Synset dbGet(Long id);

    int dbGetSynsetsCount();

    int dbGetSimilarityCount(Synset a, Synset b);

    boolean dbDelete(Synset synset);

    List<Synset> dbGetUnits(List<Synset> synsets);

    List<Synset> dbGetUnits(Collection<Long> iDs);

    int dbGetUnitsCount(Synset synset);

    List<Synset> dbFullGet(Long[] synset_ids);

    int dbGetSynsetsCount(Sense unit);

    Synset dbGetSynsetRels(Synset synset);

    List<Synset> dbGetSynsetsRels(List<Synset> synsets);

    List<Synset> dbGetSynsetsUnitsRels(Collection<Long> ind);

    String rebuildUnitsStr(Integer split, String unitsstr, List<Sense> senses);

    boolean contains(Sense unit, List<Sense> senses);

    boolean contains(String lemma, List<Sense> senses);

    String getSynsetAtrribute(Synset synset, String nazwaPola);

    void setSynsetAtrribute(Synset synset, String key, String value);

    Synset updateSynset(Synset synset);

    List<SenseToSynset> getSenseToSynsetBySynset(Synset synset);

    Long fastGetPOSID(Synset synset);

    List<Synset> dbFastGetSynsets(String filter, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String filter, Domain domain, RelationType relationType, int limitSize, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String filter, Sense filterObject, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);

    PartOfSpeech dbGetPos(Synset synset, List<Long> lexicons);

    Domain dbGetDomain(Synset synset, List<Long> lexicons);

    String rebuildUnitsStr(Synset synset, List<Long> lexicons);

    Map<Long, String> dbGetSynsetsDescriptionIdx(List<Long> idx, List<Long> lexicons);

    String dbRebuildUnitsStr(Synset synset, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Long synsetId, List<Long> lexicons);

    Synset dbGetUnit(Synset synset, List<Long> lexicons);

    void dbClone(Synset synset, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(Sense sense, List<Long> lexicons);

    List<Sense> dbFastGetSenseBySynset(String filter, Domain domain,
            RelationType relationType, String definition, String comment,
            String artificial, int limitSize, long posIndex, List<Long> lexicons, StatusDictionary status);

    List<Sense> dbFastGetSenseBySynset(String filter, Domain domain,
            RelationType relationType, String definition, String comment,
            String artificial, int limitSize,
            pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos,
            List<Long> lexicons, StatusDictionary status);

    Boolean areSynsetsInSameLexicon(long synset1, long synset2);
}
