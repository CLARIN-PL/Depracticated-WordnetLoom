package pl.edu.pwr.wordnetloom.client;

import pl.edu.pwr.wordnetloom.client.security.ErrorEvent;

public class ApplicationErrorEvent extends ErrorEvent {
    public ApplicationErrorEvent(String message) {
        super(message);
    }
}
