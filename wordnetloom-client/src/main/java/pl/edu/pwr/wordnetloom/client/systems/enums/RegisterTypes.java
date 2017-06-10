package pl.edu.pwr.wordnetloom.client.systems.enums;

import java.util.HashMap;
import java.util.Map;

public enum RegisterTypes {
    BRAK_REJESTRU("brak rejestru"),
    OG("og."),
    DAW("daw."),
    KSIAZK("książk."),
    NIENORM("nienorm."),
    POSP("posp."),
    POT("pot."),
    REG("reg."),
    SPECJ("specj."),
    SROD("środ."),
    URZ("urz."),
    WULG("wulg.");

    private final String abbreviation;

    private static final Map<String, RegisterTypes> lookup = new HashMap<>();

    static {
        for (RegisterTypes abbr : RegisterTypes.values()) {
            lookup.put(abbr.getAbbreviation(), abbr);
        }
    }

    RegisterTypes(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static RegisterTypes get(String abbreviation) {
        if (abbreviation == null) {
            return BRAK_REJESTRU;
        }
        return lookup.get(abbreviation);
    }
}
