package pl.edu.pwr.wordnetloom.client.security;

public class AuthenticationFailedEvent extends ErrorEvent{

    public AuthenticationFailedEvent(String message) {
        super(message);
    }
}
