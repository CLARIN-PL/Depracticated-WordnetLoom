package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.Lexicon;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;

@Remote
public interface LexicalUnitServiceRemote extends DAORemote {

    Sense dbClone(Sense unit);

    Sense dbGet(Long id);

    int dbGetSynsetsCount(Sense unit);

    void dbDeleteAll();

    int dbGetUnitsCount(String filter);

    int dbGetNextVariant(String lemma, PartOfSpeech pos);

    int dbDelete(List<Sense> list, String owner);

    int dbUsedUnitsCount();

    int dbGetUnitsCount();

    List<Sense> dbGetUnitsWithoutForms();

    List<Sense> dbGetUnitsNotInAnySynset(String filter, PartOfSpeech pos);

    List<Sense> dbGetUnitsAppearingInMoreThanOneSynset();

    boolean dbInAnySynset(Sense unit);

    boolean dbDelete(Sense unit, String owner);

    String getSensAtrribute(Sense sense, String nazwaPola);

    void setSenseAtrribute(Sense sense, String key, String value);

    Sense updateSense(Sense sense);

    Set<Long> dbUsedUnitsIDs();

    Word seekOrSaveWord(Word word);

    Word saveWord(Word word);

    List<Lexicon> getAllLexicons();

    List<Lexicon> getLexiconsFromList(List<Long> lexicons);

    List<Sense> dbFastGetUnits(String filter, List<Long> lexicons);

    List<Sense> dbFastGetUnits(String filter, PartOfSpeech pos, Domain domain, List<Long> lexicons);

    int dbGetHighestVariant(String word, List<Long> lexicons);

    List<Sense> dbFullGetUnits(String filter, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String lemma, List<Long> lexicon);

    List<Sense> dbFullGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(Sense unit, List<Long> lexicons);

    int dbGetUnitsCount(Synset synset, List<Long> lexicons);

    List<Sense> dbFastGetUnits(String filter, PartOfSpeech pos, Domain domain,
            RelationType relationType, String register, String comment,
            String example, int limitSize, List<Long> lexicons, StatusDictionary status);

    List<Sense> dbFastGetUnits(String filter, pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech pos, Domain domain,
            RelationType relationType, String register, String comment,
            String example, int limitSize, List<Long> lexicons, StatusDictionary status);

    List<Long> getAllLemasForLexicon(List<Long> lexicon);

    Lexicon dbSaveLexicon(Lexicon lexicon);

    List<Sense> getSensesByLemmaID(long id, long lexicon);
}
