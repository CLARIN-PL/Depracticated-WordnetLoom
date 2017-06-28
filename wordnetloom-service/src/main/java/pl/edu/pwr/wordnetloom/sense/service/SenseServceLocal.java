package pl.edu.pwr.wordnetloom.sense.service;

import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Local
public interface SenseServceLocal extends SenseServiceRemote {

    Sense dbClone(Sense unit);

    Sense dbGet(Long id);

    int dbGetSynsetsCount(Sense unit);

    void dbDeleteAll();

    int dbGetUnitsCount(String filter);

    int dbGetNextVariant(String lemma, PartOfSpeech pos);

    int dbUsedUnitsCount();

    Set<Long> dbUsedUnitsIDs();

    int dbGetUnitsCount();

    List<Sense> dbGetUnitsWithoutForms();

    List<Sense> dbGetUnitsAppearingInMoreThanOneSynset();

    Sense dbSave(Sense sense);

    boolean dbDelete(Sense sense, String owner);

    Domain[] dbGetUsedDomains();

    int dbDelete(List<Sense> list, String owner);

    List<Sense> dbGetUnitsNotInAnySynset(String filter, PartOfSpeech pos);

    boolean dbInAnySynset(Sense unit);

    String getSenseAtrribute(Sense sense, String nazwaPola);

    void setSenseAtrribute(Sense sense, String key, String value);

    Sense updateSense(Sense sense);

    Word seekOrSaveWord(Word word);

    Word saveWord(Word word);

    List<Lexicon> getAllLexicons();

    Lexicon getLexiconById(Long id);

    List<Lexicon> getLexiconsByIds(List<Long> lexiconsIds);

    List<Sense> dbFastGetUnits(String filter, PartOfSpeech pos, Domain domain, List<Long> lexicons);

    List<Sense> dbFastGetUnits(String filter, List<Long> lexicons);

    int dbGetHighestVariant(String word, List<Long> lexicons);

    List<Sense> dbFullGetUnits(String filter, List<Long> lexicons);

    List<Sense> filterSenseByLexicon(List<Sense> senses, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String lemma, List<Long> lexicons);

    List<Sense> dbFullGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);

    int dbGetUnitsCount(Synset synset, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(Sense sense, List<Long> lexicons);

    List<Sense> dbFastGetUnits(String filter, PartOfSpeech pos, Domain domain,
            RelationType relationType, String register, String comment,
            String example, int limitSize, List<Long> lexicons);

    List<Long> getAllLemmasForLexicon(List<Long> lexicon);

    Lexicon dbSaveLexicon(Lexicon lexicon);

    List<Sense> dbFastGetUnitsUby(String filter,
            Domain domain, RelationType relationType, String register,
            String comment, String example, int limitSize, List<Long> lexicons);

    List<Long> getAllLexiconIds();

    List<Sense> getSensesForLemmaID(long id, long lexicon);

}
