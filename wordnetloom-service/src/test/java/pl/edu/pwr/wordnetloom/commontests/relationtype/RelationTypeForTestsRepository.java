package pl.edu.pwr.wordnetloom.commontests.relationtype;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository;
import pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static pl.edu.pwr.wordnetloom.commontests.utils.TestRepositoryUtils.findByPropertyNameAndValue;

@Ignore
public class RelationTypeForTestsRepository {

    public static RelationType antonimia() {
        RelationType r = new RelationType();
        r.setName(1L);
        r.setDisplayText(2L);
        r.setDescription(3L);
        r.setShortDisplayText(4L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola() {
        RelationType r = new RelationType();
        r.setName(5L);
        r.setShortDisplayText(6L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setName(7L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setName(8L);
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setName(9L);
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setName(10L);
        r.setDisplayText(11L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setName(12L);
        r.setDisplayText(13L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType relationWithId(RelationType type, Long id) {
        type.setId(id);
        return type;
    }

    public static RelationType hiperonimia() {
        RelationType r = new RelationType();
        r.setName(14L);
        r.setDisplayText(15L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia() {
        RelationType r = new RelationType();
        r.setName(16L);
        r.setDisplayText(17L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_miejsce() {
        RelationType r = new RelationType();
        r.setName(18L);
        r.setDisplayText(19L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_porcja() {
        RelationType r = new RelationType();
        r.setName(20L);
        r.setDisplayText(21L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType hiponimia() {
        RelationType r = new RelationType();
        r.setName(22L);
        r.setDisplayText(23L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType normalizeDependencies(RelationType rt, EntityManager em) {

        for (Lexicon lex : rt.getLexicons()) {
            Lexicon managedLexicon = findByPropertyNameAndValue(em, Lexicon.class, "name", lex.getName());
            lex.setId(managedLexicon.getId());
        }

        for (PartOfSpeech pos : rt.getPartsOfSpeech()) {
            PartOfSpeech managedPartOfSpeech = findByPropertyNameAndValue(em, PartOfSpeech.class, "color", pos.getColor());
            pos.setId(managedPartOfSpeech.getId());
        }

        return rt;
    }

    public static List<RelationType> allRelations() {
        return Arrays.asList(antonimia(), aspektowosc(), aspektowosc_czysta_DK_NDK(),
                aspektowosc_czysta_NDK_DK(), rola(), rola_agens(), rola_pacjens(),
                holonimia(), holonimia_miejsce(), holonimia_porcja(), hiperonimia(), hiponimia());
    }
}
