package pl.edu.pwr.wordnetloom.label.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class UnsupportedLanguageException extends RuntimeException {

    private String language;

    public UnsupportedLanguageException(final String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "UnsuportedLanguageException [Unsupported language = " +language+ "]";
    }

}