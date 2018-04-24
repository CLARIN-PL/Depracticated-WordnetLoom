package pl.edu.pwr.wordnetloom.commontests.sense;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import java.util.Collections;
import java.util.List;

@Ignore
public class SenseForTestsRepository {

    public static Sense zamek() {
        Sense s = new Sense();
        return new Sense();
    }

    public static Sense senseWithId(Sense s, Long id) {
        s.setId(id);
        return s;
    }

    public static List<Sense> allSenses() {
        return Collections.singletonList(zamek());
    }
}
