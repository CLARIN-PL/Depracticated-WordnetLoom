package pl.edu.pwr.wordnetloom.commontests.partofspeech;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.Arrays;
import java.util.List;

@Ignore
public class PartOfSpeechForTestsRepository {

    public static PartOfSpeech verb() {
        PartOfSpeech p = new PartOfSpeech("EN", "verb");
        p.setName("PL", "czasownik");
        p.setColor("verb");
        return p;
    }

    public static PartOfSpeech adverb() {
        PartOfSpeech p = new PartOfSpeech("EN", "adverb");
        p.setName("PL", "przysłówek");
        p.setColor("adverb");
        return p;
    }

    public static PartOfSpeech adjective() {
        PartOfSpeech p = new PartOfSpeech("EN", "adjective");
        p.setName("PL", "przymiotnik");
        p.setColor("adjective");
        return p;
    }

    public static PartOfSpeech noun() {
        PartOfSpeech p = new PartOfSpeech("EN", "noun");
        p.setName("PL", "rzeczownik");
        p.setColor("noun");
        return p;
    }

    public static PartOfSpeech partOfSpeechWithId(PartOfSpeech pos, Long id) {
        pos.setId(id);
        return pos;
    }

    public static List<PartOfSpeech> allPartOfSpeechs() {
        return Arrays.asList(verb(), adjective(), adverb(), noun());
    }
}
