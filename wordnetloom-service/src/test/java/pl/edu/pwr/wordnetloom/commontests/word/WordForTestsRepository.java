package pl.edu.pwr.wordnetloom.commontests.word;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Ignore
public class WordForTestsRepository {

    public static Word zamek() {
        return new Word("zamek");
    }

    public static Word krowa() {
        return new Word("krowa");
    }

    public static Word pisac() {
        return new Word("pisać");
    }

    public static Word czerwony() {
        return new Word("czerwony");
    }

    public static Word wordWithId(final Word w, final Long id) {
        w.setId(id);
        return w;
    }

    public static List<Word> allWords() {
        return Arrays.asList(zamek(), krowa(), pisac(), czerwony());
    }
}
