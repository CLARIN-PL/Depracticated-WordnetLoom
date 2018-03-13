package pl.edu.pwr.wordnetloom.user.service;

import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.model.UserSettings;


public interface UserServiceRemote {

    void save(User user);

    void update(UserSettings userSettings);

    User findUserByEmail(String email);

    User changePasswordByEmail(String email, String password);
}
