package pl.edu.pwr.wordnetloom.client.commontests;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.Arrays;
import java.util.List;

@Ignore
public class PartOfSpeechForTestsRepository {

    public static PartOfSpeech verb() {
        PartOfSpeech p = new PartOfSpeech();
        p.setId(1l);
        p.setName(1l);
        p.setColor("#001100");
        return p;
    }

    public static PartOfSpeech noun() {
        PartOfSpeech p = new PartOfSpeech();
        p.setId(2l);
        p.setName(4l);
        p.setColor("#004400");
        return p;
    }

    public static PartOfSpeech adverb() {
        PartOfSpeech p = new PartOfSpeech();
        p.setId(3l);
        p.setName(2l);
        p.setColor("#002200");
        return p;
    }

    public static PartOfSpeech adjective() {
        PartOfSpeech p = new PartOfSpeech();
        p.setId(4l);
        p.setName(3l);
        p.setColor("#003300");
        return p;
    }


    public static List<PartOfSpeech> allPartOfSpeechs() {
        return Arrays.asList(verb(), adjective(), adverb(), noun());
    }
}
