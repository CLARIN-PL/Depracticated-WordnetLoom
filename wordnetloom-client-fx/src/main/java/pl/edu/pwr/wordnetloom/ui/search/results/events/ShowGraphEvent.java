package pl.edu.pwr.wordnetloom.ui.search.results.events;

import pl.edu.pwr.wordnetloom.sense.model.Sense;

public class ShowGraphEvent {

    private final Sense sense;

    public ShowGraphEvent(Sense s) {
        sense = s;
    }

    public Sense getSense() {
        return sense;
    }
}
