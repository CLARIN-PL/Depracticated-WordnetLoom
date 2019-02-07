package pl.edu.pwr.wordnetloom.sense.service;

import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import java.util.List;

public interface SenseServiceRemote {

    Sense clone(Sense sense);

    Sense save(Sense sense);

    SenseAttributes addSenseAttribute(final Long senseId, final SenseAttributes attributes);

    List<SenseAttributes> findByLemmaWithSense(String lemma, List<Long> lexicons);

    void delete(Sense sense);

    void deleteAll();

    Sense findById(Long id);

    List<Sense> findByCriteria(SenseCriteriaDTO dto);

    int getCountUnitsByCriteria(SenseCriteriaDTO dto);

    List<Sense> filterSenseByLexicon(final List<Sense> senses, final List<Long> lexicons);

    List<Sense> findByLikeLemma(String lemma, List<Long> lexicons);

    Integer findCountByLikeLemma(String filter);

    List<Sense> findBySynset(Synset synset, List<Long> lexicons);

    List<Sense> findBySynset(Long synsetId);

    int findCountBySynset(Synset synset, List<Long> lexicons);

    int findNextVariant(String lemma, PartOfSpeech pos);

    int delete(List<Sense> list);

    int findInSynsetsCount();

    Integer countAll();

    int findHighestVariant(String word, Lexicon lexicon);

    List<Sense> findNotInAnySynset(String filter, PartOfSpeech pos);

    boolean checkIsInSynset(Sense unit);

    List<Long> getAllLemmasForLexicon(List<Long> lexicon);

    List<Sense> findSensesByWordId(Long id, Long lexicon);

    Sense findHeadSenseOfSynset(Long synsetId);

    Sense fetchSense(Long senseId);

    SenseAttributes fetchSenseAttribute(Long senseId);

    List<String> findUniqueExampleTypes();

    Sense saveSense(SenseAttributes attributes, String oldLemma);
}
