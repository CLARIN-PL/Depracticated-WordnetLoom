package pl.edu.pwr.wordnetloom.rest.sense.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class SenseNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5009782478373385127L;
}