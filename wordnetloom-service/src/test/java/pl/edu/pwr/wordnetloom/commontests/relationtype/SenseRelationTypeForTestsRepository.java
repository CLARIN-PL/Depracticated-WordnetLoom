package pl.edu.pwr.wordnetloom.commontests.relationtype;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Ignore
public class SenseRelationTypeForTestsRepository {

    public static SenseRelationType rel_child() {
        return new SenseRelationType(rel_parent_top(), null, Boolean.FALSE);
    }

    public static SenseRelationType rel_parent_top() {
        return new SenseRelationType(null, null, Boolean.FALSE);
    }

    public static SenseRelationType rel_with_reverse() {
        return new SenseRelationType();
    }

    public static Lexicon yiddish() {
        return new Lexicon("Yiddish", "YWN", "Yiddish");
    }

    public static SenseRelationType relationWithId(final SenseRelationType type, final Long id) {
        type.setId(id);
        return type;
    }

    public static List<SenseRelationType> allRelations() {
        return Arrays.asList();
    }
}
