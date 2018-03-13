package pl.edu.pwr.wordnetloom.commontests.sense;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Ignore
public class SenseForTestsRepository {

    public static Sense zamek() {
        return new Sense();
    }

    public static Sense senseWithId(final Sense s, final Long id) {
        s.setId(id);
        return s;
    }

    public static List<Sense> allSenses() {
        return Collections.singletonList(zamek());
    }
}
