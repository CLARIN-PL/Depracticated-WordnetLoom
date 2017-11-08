package pl.edu.pwr.wordnetloom.service;

import pl.edu.pwr.wordnetloom.dto.SynsetDataEntry;
import pl.edu.pwr.wordnetloom.model.*;

import javax.ejb.Remote;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remote
public interface SynsetServiceRemote extends DAORemote {

    boolean dbDelete(Synset synset);

    List<Synset> dbGetUnits(List<Synset> synsets);

    void dbSaveConnections(HashMap<Synset, List<Sense>> map);

    List<Synset> dbFullGet(Long[] synset_ids);

    int dbDeleteEmpty();

    List<Synset> dbFullGetSynsets(String filter);

    List<Synset> dbGetNotEmptySynsets(String filter);

    int dbGetSynsetsCount(Sense unit);

    Synset dbGet(Long id);

    Synset dbGetSynsetRels(Synset synset);

    List<Synset> dbGetSynsetsRels(List<Synset> synsets);

    List<Synset> dbGetSynsetsUnitsRels(Collection<Long> ind);

    int dbGetSynsetsCount();

    int dbGetSimilarityCount(Synset a, Synset b);

    String getSynsetAtrribute(Synset synset, String nazwaPola);

    void setSynsetAtrribute(Synset synset, String key, String value);

    int dbGetUnitsCount(Synset synset);

    Synset updateSynset(Synset synset);

    List<SenseToSynset> getSenseToSynsetBySynset(Synset synset);

    Long fastGetPOSID(Synset synset);

    List<Synset> dbFastGetSynsets(String filter, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String filter, Domain domain,
                                  RelationType relationType, int limitSize, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String filter, Domain domain,
                                  RelationType relationType, int limitSize, long posIndex,
                                  List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String filter, Sense filterObject, List<Long> lexicons);

    Map<Long, String> dbGetSynsetsDescriptionIdx(List<Long> idx, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);

    String dbRebuildUnitsStr(Synset synset, List<Long> lexicons);

    PartOfSpeech dbGetPos(Synset synset, List<Long> lexicons);

    Domain dbGetDomain(Synset synset, List<Long> lexicons);

    List<Sense> dbGetUnits(Long synsetId, List<Long> lexicons);

    Synset fetchSynsetForSense(Sense sense, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(Sense unit, List<Long> lexicons);

    Synset dbGetUnit(Synset synset, List<Long> lexicons);

    void dbClone(Synset synset, List<Long> lexicons);

    HashMap<Long, SynsetDataEntry> prepareCacheForRootNode(Synset synset,
                                                           List<Long> lexicons);

    List<Sense> dbFastGetSenseBySynset(String filter, Domain domain,
                                       RelationType relationType, String definition, String comment,
                                       String artificial, int limitSize, long posIndex, List<Long> lexicons);

    List<Sense> dbFastGetSenseBySynsetUbyPose(String filter, Domain domain,
                                              RelationType relationType, String definition, String comment,
                                              String artificial, int limitSize,
                                              pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos,
                                              List<Long> lexicons);

    Boolean areSynsetsInSameLexicon(long synset1, long synset2);
}
