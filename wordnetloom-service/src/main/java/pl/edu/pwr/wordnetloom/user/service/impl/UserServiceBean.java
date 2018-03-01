package pl.edu.pwr.wordnetloom.user.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.user.exception.UserNotFoundException;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.model.UserSettings;
import pl.edu.pwr.wordnetloom.user.repository.UserRepository;
import pl.edu.pwr.wordnetloom.user.repository.UserSettingsRepository;
import pl.edu.pwr.wordnetloom.user.service.UserServiceLocal;
import pl.edu.pwr.wordnetloom.user.service.UserServiceRemote;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Local(UserServiceLocal.class)
@Remote(UserServiceRemote.class)
public class UserServiceBean implements UserServiceLocal {

    @Inject
    UserRepository userRepository;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Inject
    Validator validator;

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public void save(User user) {
        ValidationUtils.validateEntityFields(validator, user);
        userRepository.save(user);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public void update(UserSettings userSettings) {
        ValidationUtils.validateEntityFields(validator, userSettings);
        userSettingsRepository.update(userSettings);
    }

    @PermitAll
    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @RolesAllowed({"ADMIN", "USER"})
    @Override
    public User changePasswordByEmail(String email, String password) {
        return userRepository.changeUserPassword(email, password);
    }


}
