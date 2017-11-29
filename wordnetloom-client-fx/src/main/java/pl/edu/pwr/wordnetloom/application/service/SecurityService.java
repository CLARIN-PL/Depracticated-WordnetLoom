package pl.edu.pwr.wordnetloom.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.wordnetloom.application.utils.Language;
import pl.edu.pwr.wordnetloom.user.model.Role;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.service.UserServiceRemote;

@Service
public class SecurityService {

    private User user;
    private Language language;

    private final EJBConnectionProviderService service;

    @Autowired
    public SecurityService(final EJBConnectionProviderService service) {
        this.service = service;
    }

    public boolean authenticate(String login, String password, Language lang) {

        try {
            if (user == null) {

                service.setLoginAndPassword(login, password);
                user = service.lookupForService(UserServiceRemote.class).findUserByEmail(login);

                if (user != null) {
                    language = lang;
                    return true;
                }
            }
        } catch (Exception ex) {
            service.destroyInstance();
            user = null;
            language = null;
            return false;
        }
        return false;
    }

    public Language getLanguage() {
        return language;
    }

    public boolean hasRole(Role role) {
        return user.getRole().equals(role);
    }

    public User getUser() {
        return user;
    }
}
