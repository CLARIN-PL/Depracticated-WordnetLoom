package pl.edu.pwr.wordnetloom.client.security;

import pl.edu.pwr.wordnetloom.user.model.Role;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.model.UserSettings;

public class UserSessionContext {

    private static UserSessionContext instance;
    private User user;
    private String language;

    private UserSessionContext() {
    }

    private UserSessionContext(User user, String language) {
        this.user = user;
        this.language = language;
    }

    public static UserSessionContext getInstance() {
        if (instance == null) {
            synchronized (UserSessionContext.class) {
                if (instance == null) {
                    instance = new UserSessionContext();
                }
            }
        }
        return instance;
    }

    public static UserSessionContext initialiseAndGetInstance(User user, String language) {
        synchronized (UserSessionContext.class) {
            instance = new UserSessionContext(user, language);
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public String getLanguage() {
        return language;
    }

    public boolean hasRole(Role role) {
        return user.getRole().equals(role);
    }

    public String getFullName() {
        return user.getFullname();
    }

    public UserSettings getUserSettings() {
        return user.getSettings();
    }
}


