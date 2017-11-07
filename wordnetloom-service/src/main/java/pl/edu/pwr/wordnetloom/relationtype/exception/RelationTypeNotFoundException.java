package pl.edu.pwr.wordnetloom.relationtype.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class RelationTypeNotFoundException extends RuntimeException {

    public RelationTypeNotFoundException() {
    }

}
