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
        r.setName(5l);
        r.setShortDisplayText(6l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setName(7l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setName(8l);
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setName(9l);
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setName(10l);
        r.setDisplayText(11l);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setName(12l);
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
        r.setName(14l);
        r.setDisplayText(15l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia() {
        RelationType r = new RelationType();
        r.setName(16l);
        r.setDisplayText(17l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_miejsce() {
        RelationType r = new RelationType();
        r.setName(18l);
        r.setDisplayText(19l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_porcja() {
        RelationType r = new RelationType();
        r.setName(20l);
        r.setDisplayText(21l);
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType hiponimia() {
        RelationType r = new RelationType();
        r.setName(22l);
        r.setDisplayText(23l);
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
