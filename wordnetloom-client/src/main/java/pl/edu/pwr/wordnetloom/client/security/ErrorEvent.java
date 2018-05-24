package pl.edu.pwr.wordnetloom.client.security;

public abstract class ErrorEvent {

    private final String message;

    public ErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
