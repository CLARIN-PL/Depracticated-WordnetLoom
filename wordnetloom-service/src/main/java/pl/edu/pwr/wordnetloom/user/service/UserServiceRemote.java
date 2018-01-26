package pl.edu.pwr.wordnetloom.user.service;

import pl.edu.pwr.wordnetloom.user.model.User;

public interface UserServiceRemote {

    void saveOrUpdate(User user);

    User findUserByEmail(String email);

    User changePasswordByEmail(String email, String password);
}
