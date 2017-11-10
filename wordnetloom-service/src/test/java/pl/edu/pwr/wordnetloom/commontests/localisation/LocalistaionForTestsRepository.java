package pl.edu.pwr.wordnetloom.commontests.localisation;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;

import java.util.Arrays;
import java.util.List;

@Ignore
public class LocalistaionForTestsRepository {

    public static LocalisedString nameString() {
        LocalisedString s = new LocalisedString();
        s.addKey(null, "pl");
        s.setValue("nazwa");
        return s;
    }

    public static LocalisedString descriptionString() {
        LocalisedString s = new LocalisedString();
        s.addKey(null, "pl");
        s.setValue("opis");
        return s;
    }

    public static LocalisedString shortString() {
        LocalisedString s = new LocalisedString();
        s.addKey(null, "pl");
        s.setValue("short");
        return s;
    }

    public static LocalisedString shortENString() {
        LocalisedString s = new LocalisedString();
        s.addKey(null, "en");
        s.setValue("short");
        return s;
    }

    public static LocalisedString withId(LocalisedString str, Long id, String lang) {
        str.addKey(id, lang);
        return str;
    }

    public static LocalisedString descriptionENString() {
        LocalisedString s = new LocalisedString();
        s.addKey(null, "en");
        s.setValue("desc");
        return s;
    }

    public static List<LocalisedString> allStrings() {
        return Arrays.asList(nameString(), descriptionString(), shortString(), shortENString(), descriptionENString());
    }
}
