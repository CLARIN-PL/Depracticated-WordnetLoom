package pl.edu.pwr.wordnetloom.partofspeech.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class PartOfSpeechNotFoundException extends RuntimeException {

    public PartOfSpeechNotFoundException() {
    }

}
