package pl.edu.pwr.wordnetloom.commontests.relationtype;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

@Ignore
public class SenseRelationTypeForTestsRepository {

    public static SenseRelationType antonimia() {
        SenseRelationType anto = new SenseRelationType();
        anto.setName("pl", "antonimia");
        anto.setDispalyText("pl", "anto");
        return anto;
    }

    public static SenseRelationType rola() {
        SenseRelationType rola = new SenseRelationType();
        rola.setName("pl", "Rola");
        return rola;
    }

    public static SenseRelationType aspektowosc() {
        SenseRelationType r = new SenseRelationType();
        r.setName("pl", "aspektowość");
        return r;
    }

    public static SenseRelationType aspektowosc_czysta_DK_NDK() {
        SenseRelationType r = new SenseRelationType();
        r.setName("pl", "aspektowość czysta DK-NDK");
        r.setAutoReverse(Boolean.TRUE);
        return r;
    }

    public static SenseRelationType aspektowosc_czysta_NDK_DK() {
        SenseRelationType r = new SenseRelationType();
        r.setName("pl", "aspektowość czysta NDK-DK");
        r.setAutoReverse(Boolean.TRUE);
        return r;
    }

    public static SenseRelationType rola_agens() {
        SenseRelationType r = new SenseRelationType();
        r.setName("pl", "agens|subiekt");
        r.setDispalyText("pl", "rol:ag");
        return r;
    }

    public static SenseRelationType rola_pacjens() {
        SenseRelationType r = new SenseRelationType();
        r.setName("pl", "pacjens|obiekt");
        r.setDispalyText("pl", "rol:pacj");
        return r;
    }

    public static SenseRelationType relationWithId(final SenseRelationType type, final Long id) {
        type.setId(id);
        return type;
    }

    public static List<SenseRelationType> allRelations() {
        return Arrays.asList(antonimia(), aspektowosc(), aspektowosc_czysta_DK_NDK(), aspektowosc_czysta_NDK_DK(), rola(), rola_agens(), rola_pacjens());
    }
}
