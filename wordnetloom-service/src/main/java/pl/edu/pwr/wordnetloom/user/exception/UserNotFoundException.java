package pl.edu.pwr.wordnetloom.user.exception;

import pl.edu.pwr.wordnetloom.domain.exception.*;
import pl.edu.pwr.wordnetloom.partofspeech.exception.*;
import javax.ejb.ApplicationException;

@ApplicationException
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

}
