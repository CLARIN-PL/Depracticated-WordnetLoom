package pl.edu.pwr.wordnetloom.client.systems.enums;

import java.util.HashMap;
import java.util.Map;

public enum Language {
    English("en");

    private final String abbreviation;

    private static final Map<String, Language> lookup = new HashMap<>();

    static {
        for (Language abbr : Language.values()) {
            lookup.put(abbr.getAbbreviation(), abbr);
        }
    }

    Language(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Language get(String abbreviation) {
        if (abbreviation == null) {
            return English;
        }
        return lookup.get(abbreviation);
    }
}
