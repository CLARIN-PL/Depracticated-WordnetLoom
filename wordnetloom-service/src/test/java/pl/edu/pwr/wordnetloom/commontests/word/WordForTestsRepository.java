package pl.edu.pwr.wordnetloom.commontests.word;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Ignore
public class WordForTestsRepository {

    public static Word zamek() {
        Word w =  new Word("zamek");
        w.setId(1L);
        return w;
    }

    public static Word krowa() {
        Word w =  new Word("krowa");
        w.setId(2L);
        return w;
    }

    public static Word pisac() {
        Word w =  new Word("pisać");
        w.setId(3L);
        return w;
    }

    public static Word czerwony() {
        Word w =  new Word("czerwony");
        w.setId(4L);
        return w;
    }

    public static Word wordWithId(final Word w, final Long id) {
        w.setId(id);
        return w;
    }

    public static List<Word> allWords() {
        return Arrays.asList(zamek(), krowa(), pisac(), czerwony());
    }
}
