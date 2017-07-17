package pl.edu.pwr.wordnetloom.client.plugins.login.data;

import pl.edu.pwr.wordnetloom.user.model.User;

public class UserSessionData {

    private final String username;
    private final String password;
    private final User user;
    private final String language;

    public UserSessionData(String username, String password, String language, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
        this.language = language;
    }

    public UserSessionData(String username, String password, String language) {
        this.username = username;
        this.password = password;
        this.language = language;
        this.user = null;
    }

    public UserSessionData(UserSessionData data, User user) {
        this.username = data.username;
        this.password = data.password;
        this.language = data.language;
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
