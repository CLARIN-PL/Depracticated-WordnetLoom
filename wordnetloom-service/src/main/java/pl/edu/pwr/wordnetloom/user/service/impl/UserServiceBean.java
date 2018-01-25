package pl.edu.pwr.wordnetloom.user.service.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.user.exception.UserNotFoundException;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.repository.UserRepository;
import pl.edu.pwr.wordnetloom.user.service.UserServiceLocal;
import pl.edu.pwr.wordnetloom.user.service.UserServiceRemote;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Stateless
@Local(UserServiceLocal.class)
@Remote(UserServiceRemote.class)
public class UserServiceBean implements UserServiceLocal {

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;

    @Override
    public void saveOrUpdate(User user) {
        ValidationUtils.validateEntityFields(validator, user);
        userRepository.saveOrUpdate(user);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public User changePasswordByEmail(String email, String password) {
        return userRepository.changeUserPassword(email, password);
    }


}
