package pl.edu.pwr.wordnetloom.commontests.relationtype;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
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
        anto.setRelationArgument(RelationArgument.SENSE_RELATION);
        return anto;
    }

    public static RelationType rola() {
        RelationType rola = new RelationType();
        rola.setName("pl", "Rola");
        rola.setRelationArgument(RelationArgument.SENSE_RELATION);
        return rola;
    }

    public static RelationType aspektowosc() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType aspektowosc_czysta_DK_NDK() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość czysta DK-NDK");
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType aspektowosc_czysta_NDK_DK() {
        RelationType r = new RelationType();
        r.setName("pl", "aspektowość czysta NDK-DK");
        r.setAutoReverse(Boolean.TRUE);
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType rola_agens() {
        RelationType r = new RelationType();
        r.setName("pl", "agens|subiekt");
        r.setDisplayText("pl", "rol:ag");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType rola_pacjens() {
        RelationType r = new RelationType();
        r.setName("pl", "pacjens|obiekt");
        r.setDisplayText("pl", "rol:pacj");
        r.setRelationArgument(RelationArgument.SENSE_RELATION);
        return r;
    }

    public static RelationType relationWithId(RelationType type, Long id) {
        type.setId(id);
        return type;
    }

    public static RelationType hiperonimia() {
        RelationType r = new RelationType();
        r.setName("pl", "hiperonimia");
        r.setDisplayText("pl", "hiper");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        return r;
    }

    public static RelationType holonimia() {
        RelationType r = new RelationType();
        r.setName("pl", "holonimia");
        r.setDisplayText("pl", "holo");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        return r;
    }

    public static RelationType holonimia_miejsce() {
        RelationType r = new RelationType();
        r.setName("pl", "miejsce");
        r.setDisplayText("pl", "holo:msc");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        return r;
    }

    public static RelationType holonimia_porcja() {
        RelationType r = new RelationType();
        r.setName("pl", "porcja");
        r.setDisplayText("pl", "holo:porc");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        return r;
    }

    public static RelationType hiponimia() {
        RelationType r = new RelationType();
        r.setName("pl", "hiponimia");
        r.setDisplayText("pl", "hipo");
        r.setRelationArgument(RelationArgument.SYNSET_RELATION);
        return r;
    }

    public static List<RelationType> allRelations() {
        return Arrays.asList(antonimia(), aspektowosc(), aspektowosc_czysta_DK_NDK(),
                aspektowosc_czysta_NDK_DK(), rola(), rola_agens(), rola_pacjens(),
                holonimia(), holonimia_miejsce(), holonimia_porcja(), hiperonimia(), hiponimia());
    }
}
