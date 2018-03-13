package pl.edu.pwr.wordnetloom.client.commontests;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.Arrays;
import java.util.List;

@Ignore
public class RelationTypeForTestsRepository {

    public static RelationType antonimia() {
        RelationType r = new RelationType();
        r.setId(1L);
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
        r.setId(2L);
        r.setName(5L);
        r.setShortDisplayText(6L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setId(3L);
        r.setName(7L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setId(4L);
        r.setName(8L);
        r.setParent(aspektowosc());
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setId(4L);
        r.setName(9L);
        r.setParent(aspektowosc());
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setId(6L);
        r.setName(10L);
        r.setParent(rola());
        r.setDisplayText(11L);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setId(7L);
        r.setName(12L);
        r.setParent(rola());
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
        r.setId(8L);
        r.setName(14L);
        r.setDisplayText(15L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia() {
        RelationType r = new RelationType();
        r.setId(9L);
        r.setName(16L);
        r.setDisplayText(17L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_miejsce() {
        RelationType r = new RelationType();
        r.setId(10L);
        r.setName(18L);
        r.setParent(holonimia());
        r.setDisplayText(19L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_porcja() {
        RelationType r = new RelationType();
        r.setId(11L);
        r.setName(20L);
        r.setParent(holonimia());
        r.setDisplayText(21L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType hiponimia() {
        RelationType r = new RelationType();
        r.setId(12L);
        r.setName(22L);
        r.setDisplayText(23L);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static List<RelationType> allRelations() {
        return Arrays.asList(antonimia(), aspektowosc(), aspektowosc_czysta_DK_NDK(),
                aspektowosc_czysta_NDK_DK(), rola(), rola_agens(), rola_pacjens(),
                holonimia(), holonimia_miejsce(), holonimia_porcja(), hiperonimia(), hiponimia());
    }
}
