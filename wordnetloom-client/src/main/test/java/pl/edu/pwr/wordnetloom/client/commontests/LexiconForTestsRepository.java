package pl.edu.pwr.wordnetloom.client.commontests;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.util.Arrays;
import java.util.List;

@Ignore
public class LexiconForTestsRepository {

    public static Lexicon princenton() {
        return new Lexicon("Princenton", "PWN", "English");
    }

    public static Lexicon slowosiec() {
        return new Lexicon("Słowosieć", "PLWN", "Polish");
    }

    public static Lexicon germanet() {
        return new Lexicon("Germanet", "GWN", "German");
    }

    public static Lexicon yiddish() {
        return new Lexicon("Yiddish", "YWN", "Yiddish");
    }

    public static Lexicon lexiconWithId(final Lexicon lexicon, final Long id) {
        lexicon.setId(id);
        return lexicon;
    }

    public static List<Lexicon> allLexicons() {
        return Arrays.asList(lexiconWithId(princenton(), 2l), lexiconWithId(slowosiec(), 1l), lexiconWithId(germanet(), 3l), lexiconWithId(yiddish(), 4l));
    }
}
