package pl.edu.pwr.wordnetloom.synset.service;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SynsetServiceRemote {

    void clone(Synset synset);

    boolean delete(Synset synset);

    void deleteSensesFromSynset(Collection<Sense> senses, Synset synset);

    Synset findSynsetBySense(Sense sense, List<Long> lexicons);

    Synset fetchSynset(Synset synset);

    Synset findById(Long id);

    List<Synset> findSynsetsByCriteria(SynsetCriteriaDTO criteria);

    Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons, NodeDirection[] directions);

    DataEntry findSynsetDataEntry(UUID synsetUUID, List<Long> lexicons);

    Synset save(Synset synset);

    Synset addSenseToSynset(Sense unit, Synset synset);

    int getCountSynsetsByCriteria(SynsetCriteriaDTO criteria);

    SynsetAttributes save(SynsetAttributes attributes);

    SynsetAttributes addSynsetAttribute(Long synsetId, SynsetAttributes attributes);

    SynsetAttributes fetchSynsetAttributes(Synset synset);

    void merge(Synset target, Synset source);

    void remove(Synset synset);
}
