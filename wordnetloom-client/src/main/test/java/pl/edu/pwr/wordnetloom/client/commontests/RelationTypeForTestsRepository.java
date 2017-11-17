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
        r.setId(1l);
        r.setName(1l);
        r.setDisplayText(2l);
        r.setDescription(3l);
        r.setShortDisplayText(4l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola() {
        RelationType r = new RelationType();
        r.setId(2l);
        r.setName(5l);
        r.setShortDisplayText(6l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setId(3l);
        r.setName(7l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setId(4l);
        r.setName(8l);
        r.setParent(aspektowosc());
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setId(4l);
        r.setName(9l);
        r.setParent(aspektowosc());
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setId(6l);
        r.setName(10l);
        r.setParent(rola());
        r.setDisplayText(11l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setId(7l);
        r.setName(12l);
        r.setParent(rola());
        r.setDisplayText(13l);
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
        r.setId(8l);
        r.setName(14l);
        r.setDisplayText(15l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia() {
        RelationType r = new RelationType();
        r.setId(9l);
        r.setName(16l);
        r.setDisplayText(17l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_miejsce() {
        RelationType r = new RelationType();
        r.setId(10l);
        r.setName(18l);
        r.setParent(holonimia());
        r.setDisplayText(19l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_porcja() {
        RelationType r = new RelationType();
        r.setId(11l);
        r.setName(20l);
        r.setParent(holonimia());
        r.setDisplayText(21l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType hiponimia() {
        RelationType r = new RelationType();
        r.setId(12l);
        r.setName(22l);
        r.setDisplayText(23l);
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
