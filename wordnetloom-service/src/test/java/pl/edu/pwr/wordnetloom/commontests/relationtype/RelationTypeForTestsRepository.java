package pl.edu.pwr.wordnetloom.commontests.relationtype;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.Arrays;
import java.util.List;

@Ignore
public class RelationTypeForTestsRepository {

    public static RelationType antonimia() {
        RelationType anto = new RelationType();
        anto.setName("pl", "antonimia");
        anto.setDisplayText("pl", "anto");
        anto.setDescription("pl", "antonimia description");
        anto.setShortDisplayText("pl", "ant");
        anto.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return anto;
    }

    public static RelationType rola() {
        RelationType rola = new RelationType();
        rola.setName("pl", "Rola");
        rola.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return rola;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość");
        r.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość czysta DK-NDK");
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość czysta NDK-DK");
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setName("pl", "agens|subiekt");
        r.setDisplayText("pl", "rol:ag");
        r.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setName("pl", "pacjens|obiekt");
        r.setDisplayText("pl", "rol:pacj");
        r.setRelationArgument(RelationType.RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType relationWithId(RelationType type, Long id) {
        type.setId(id);
        return type;
    }

    public static List<RelationType> allRelations() {
        return Arrays.asList(antonimia(), aspektowosc(), aspektowosc_czysta_DK_NDK(), aspektowosc_czysta_NDK_DK(), rola(), rola_agens(), rola_pacjens());
    }
}
