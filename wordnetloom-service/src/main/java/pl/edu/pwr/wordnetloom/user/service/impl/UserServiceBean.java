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

}
