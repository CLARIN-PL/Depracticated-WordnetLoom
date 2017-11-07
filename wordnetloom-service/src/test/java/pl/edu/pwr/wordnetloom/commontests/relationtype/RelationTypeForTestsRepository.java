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
        r.setName("pl", "antonimia");
        r.setDisplayText("pl", "anto");
        r.setDescription("pl", "antonimia description");
        r.setShortDisplayText("pl", "ant");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola() {
        RelationType r = new RelationType();
        r.setName("pl", "Rola");
        r.setShortDisplayText("pl", "rol");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość czysta DK-NDK");
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość czysta NDK-DK");
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setName("pl", "agens|subiekt");
        r.setDisplayText("pl", "rol:ag");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setName("pl", "pacjens|obiekt");
        r.setDisplayText("pl", "rol:pacj");
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
        r.setName("pl", "hiperonimia");
        r.setDisplayText("pl", "hiper");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia() {
        RelationType r = new RelationType();
        r.setName("pl", "holonimia");
        r.setDisplayText("pl", "holo");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_miejsce() {
        RelationType r = new RelationType();
        r.setName("pl", "miejsce");
        r.setDisplayText("pl", "holo:msc");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType holonimia_porcja() {
        RelationType r = new RelationType();
        r.setName("pl", "porcja");
        r.setDisplayText("pl", "holo:porc");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        r.addLexicon(LexiconForTestsRepository.slowosiec());
        r.addPartOfSpeech(PartOfSpeechForTestsRepository.verb());
        return r;
    }

    public static RelationType hiponimia() {
        RelationType r = new RelationType();
        r.setName("pl", "hiponimia");
        r.setDisplayText("pl", "hipo");
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
