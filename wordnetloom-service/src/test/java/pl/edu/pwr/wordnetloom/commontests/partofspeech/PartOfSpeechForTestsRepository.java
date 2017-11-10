package pl.edu.pwr.wordnetloom.commontests.partofspeech;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.Arrays;
import java.util.List;

@Ignore
public class PartOfSpeechForTestsRepository {

    public static PartOfSpeech verb() {
        PartOfSpeech p = new PartOfSpeech();
        p.setName(1l);
        p.setColor("#001100");
        return p;
    }

    public static PartOfSpeech adverb() {
        PartOfSpeech p = new PartOfSpeech();
        p.setName(2l);
        p.setColor("#002200");
        return p;
    }

    public static PartOfSpeech adjective() {
        PartOfSpeech p = new PartOfSpeech();
        p.setName(3l);
        p.setColor("#003300");
        return p;
    }

    public static PartOfSpeech noun() {
        PartOfSpeech p = new PartOfSpeech();
        p.setName(4l);
        p.setColor("#004400");
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
