package pl.edu.pwr.wordnetloom.localisation.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class UnsupportedLanguageException extends RuntimeException {

    private final String language;

    public UnsupportedLanguageException(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "UnsuportedLanguageException [Unsupported language = " + language + "]";
    }

}