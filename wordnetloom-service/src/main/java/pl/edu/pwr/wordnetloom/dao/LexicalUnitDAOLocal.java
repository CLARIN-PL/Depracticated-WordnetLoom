package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.dto.SenseFilter;
import pl.edu.pwr.wordnetloom.dto.SenseGraphDTO;
import pl.edu.pwr.wordnetloom.model.*;
import pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.Particle;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface LexicalUnitDAOLocal extends DAOLocal {

    Sense dbClone(Sense unit);

    Sense dbGet(Long id);

    Sense dbGetWithYiddish(Long id);

    int dbGetSynsetsCount(Sense unit);

    void dbDeleteAll();

    int dbGetUnitsCount(String filter);

    int dbGetNextVariant(String lemma, pl.edu.pwr.wordnetloom.model.PartOfSpeech pos);

    int dbUsedUnitsCount();

    Set<Long> dbUsedUnitsIDs();

    int dbGetUnitsCount();

    List<Sense> dbGetUnitsWithoutForms();

    List<Sense> dbGetUnitsAppearingInMoreThanOneSynset();

    Sense dbSave(Sense sense);

    boolean dbDelete(Sense sense, String owner);

    Domain[] dbGetUsedDomains();

    int dbDelete(List<Sense> list, String owner);

    List<Sense> dbGetUnitsNotInAnySynset(String filter, pl.edu.pwr.wordnetloom.model.PartOfSpeech pos);

    boolean dbInAnySynset(Sense unit);

    String getSenseAtrribute(Sense sense, String nazwaPola);

    void setSenseAtrribute(Sense sense, String key, String value);

    Sense updateSense(Sense sense);

    Word seekOrSaveWord(Word word);

    Word saveWord(Word word);

    List<Lexicon> getAllLexicons();

    Lexicon getLexiconById(Long id);

    List<Lexicon> getLexiconsByIds(List<Long> lexiconsIds);

    int dbGetHighestVariant(String word, List<Long> lexicons);

    List<Sense> dbFullGetUnits(String filter, List<Long> lexicons);

    List<Sense> filterSenseByLexicon(List<Sense> senses, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String lemma, List<Long> lexicons);

    List<Sense> dbFullGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);

    int dbGetUnitsCount(Synset synset, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(Sense sense, List<Long> lexicons);

    List<Long> getAllLemmasForLexicon(List<Long> lexicon);

    Lexicon dbSaveLexicon(Lexicon lexicon);

    List<Sense> dbFastGetUnits(CriteriaDTO dto, PartOfSpeech pos, int limitSize, List<Long> lexicons);

    List<Long> getAllLexiconIds();

    List<Sense> getSensesForLemmaID(long id, long lexicon);

    YiddishSenseExtension save(YiddishSenseExtension ext);

    Particle saveParticle(Particle p);

    void removeParticle(Particle p);

    void dbRemoveYiddishSenseExtension(YiddishSenseExtension sense);

    int dbGetUnitCountByDomain(final String domain);

    List<CountModel> dbGetEtymologicalRootsCount();

    PaginatedData<Sense> findByFilter(SenseFilter filter);

    SenseGraphDTO getGraphForSense(Long id);

    List<Sense> findBySynsetWithYiddish(Synset synset);
}
