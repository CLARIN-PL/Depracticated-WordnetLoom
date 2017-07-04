package pl.edu.pwr.wordnetloom.sense.service;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Remote
public interface SenseServiceRemote {

    Sense clone(Sense unit);

    Sense findById(Long id);

    int dbGetSynsetsCount(Sense unit);

    void deleteAll();

    int dbGetUnitsCount(String filter);

    int dbGetNextVariant(String lemma, PartOfSpeech pos);

    int delete(List<Sense> list);

    boolean delete(Sense unit);

    int dbUsedUnitsCount();

    int dbGetUnitsCount();

    List<Sense> dbGetUnitsWithoutForms();

    List<Sense> dbGetUnitsNotInAnySynset(String filter, PartOfSpeech pos);

    List<Sense> dbGetUnitsAppearingInMoreThanOneSynset();

    boolean dbInAnySynset(Sense unit);

    Sense update(Sense sense);

    Set<Long> dbUsedUnitsIDs();

    Word seekOrSaveWord(Word word);

    Word saveWord(Word word);

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
            String example, int limitSize, List<Long> lexicons);

    List<Sense> dbFastGetUnits(String filter, Domain domain,
            RelationType relationType, String register, String comment,
            String example, int limitSize, List<Long> lexicons);

    List<Sense> getSensesByLemmaID(long id, long lexicon);
}
