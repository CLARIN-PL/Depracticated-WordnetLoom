package pl.edu.pwr.wordnetloom.client.systems.enums;

/**
 * @author M.Stanek <mikol@e-informatyka.pl>
 */
public enum WorkState {

    /**
     * Nie zrobiony
     */
    TODO("Nie przetworzone"),
    /**
     * W czasie pracy
     */
    WORKING("Częściowo przetworzone"),
    /**
     * Zrobiony
     */
    DONE("Przetworzone"),
    /**
     * Błędny
     */
    MISTAKE("Błędne"),
    /**
     * Sprawdzony
     */
    VALIDATED("Sprawdzone"),
    /**
     * Wątpliwy
     */
    DOUBT("Wątpliwe");

//  ----------------------------------------------------------------------------
    private final String name;

    WorkState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     *
     * @param name
     * @return Workstae
     */
    public static WorkState decode(String name) {
        WorkState[] d = WorkState.values();
        for (WorkState work : d) {
            if (work.name.equals(name)) {
                return work;
            }
        }

        return WorkState.TODO;
    }
}
