package pl.edu.pwr.wordnetloom.domain.exception;

import pl.edu.pwr.wordnetloom.partofspeech.exception.*;
import javax.ejb.ApplicationException;

@ApplicationException
public class DomainNotFoundException extends RuntimeException {

    public DomainNotFoundException() {
    }

}
