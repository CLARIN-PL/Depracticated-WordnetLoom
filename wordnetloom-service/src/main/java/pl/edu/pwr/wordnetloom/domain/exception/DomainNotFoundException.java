package pl.edu.pwr.wordnetloom.domain.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class DomainNotFoundException extends RuntimeException {

    public DomainNotFoundException() {
    }

}
