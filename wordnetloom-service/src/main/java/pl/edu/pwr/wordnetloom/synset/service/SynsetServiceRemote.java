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

public interface SynsetServiceRemote {

    void clone(Synset synset);

    boolean delete(Synset synset);

    Synset findSynsetBySense(Sense sense, List<Long> lexicons);

    Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons, NodeDirection[] directions);

    DataEntry findSynsetDataEntry(Long synsetId, List<Long> lexicons);

    Synset save(Synset synset);

    SynsetAttributes addSynsetAttribute(Long synsetId, SynsetAttributes attributes);

    Synset addSenseToSynset(Sense unit, Synset synset);

    void deleteSensesFromSynset(Collection<Sense> senses, Synset synset);

    List<Synset> findSynsetsByCriteria(SynsetCriteriaDTO criteria);

    int getCountSynsetsByCriteria(SynsetCriteriaDTO criteria);

    Synset fetchSynset(Long synsetId);

    Synset findById(Long id);

    SynsetAttributes fetchSynsetAttributes(Long synsetId);
}
