package pl.edu.pwr.wordnetloom.remote;

import pl.edu.pwr.wordnetloom.user.model.User;

public class UserSession {

    private final User user;
    private final String username;
    private final String password;
    private final String language;

    public UserSession(String username, String password, String language, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
        this.language = language;
    }

    public UserSession(String username, String password, String language) {
        this.username = username;
        this.password = password;
        this.language = language;
        user = null;
    }

    public UserSession(UserSession data, User user) {
        username = data.username;
        password = data.password;
        language = data.language;
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }

}
