package pl.edu.pwr.wordnetloom.client.security;

public class PasswordChangedEvent {

    private String password;

    public PasswordChangedEvent(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
