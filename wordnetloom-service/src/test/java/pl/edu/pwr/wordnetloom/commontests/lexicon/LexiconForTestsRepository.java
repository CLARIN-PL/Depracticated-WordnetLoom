package pl.edu.pwr.wordnetloom.commontests.lexicon;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

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
        return Arrays.asList(princenton(), slowosiec(), germanet(), yiddish());
    }
}
