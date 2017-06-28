package pl.edu.pwr.wordnetloom.commontests.partofspeech;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Ignore
public class PartOfSpeechForTestsRepository {

    public static PartOfSpeech verb() {
        PartOfSpeech p = new PartOfSpeech("EN", "verb");
        p.setName("PL", "czasownik");
        return p;
    }

    public static PartOfSpeech adverb() {
        PartOfSpeech p = new PartOfSpeech("EN", "adverb");
        p.setName("PL", "przysłówek");
        return p;
    }

    public static PartOfSpeech adjective() {
        PartOfSpeech p = new PartOfSpeech("EN", "adjective");
        p.setName("PL", "przymiotnik");
        return p;
    }

    public static PartOfSpeech noun() {
        PartOfSpeech p = new PartOfSpeech("EN", "noun");
        p.setName("PL", "rzeczownik");
        return p;
    }

    public static PartOfSpeech partOfSpeechWithId(final PartOfSpeech pos, final Long id) {
        pos.setId(id);
        return pos;
    }

    public static List<PartOfSpeech> allPartOfSpeechs() {
        return Arrays.asList(verb(), adjective(), adverb(), noun());
    }
}
