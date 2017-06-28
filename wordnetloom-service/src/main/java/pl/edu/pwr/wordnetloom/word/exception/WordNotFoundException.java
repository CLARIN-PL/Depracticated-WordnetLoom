package pl.edu.pwr.wordnetloom.word.exception;

import pl.edu.pwr.wordnetloom.lexicon.exception.*;
import javax.ejb.ApplicationException;

@ApplicationException
public class WordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5009782478373385127L;

}
