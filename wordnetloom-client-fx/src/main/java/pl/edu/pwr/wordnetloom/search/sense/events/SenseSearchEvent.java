package pl.edu.pwr.wordnetloom.search.sense.events;

public class SenseSearchEvent {

    private String text;

    public SenseSearchEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
