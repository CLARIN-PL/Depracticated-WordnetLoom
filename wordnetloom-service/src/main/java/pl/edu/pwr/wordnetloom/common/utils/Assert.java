package pl.edu.pwr.wordnetloom.common.utils;

import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;

public abstract class Assert {

    public static void notNull(Object object, String field) {
        if (object == null) {
            throw new FieldNotValidException(field, "may not be null");
        }
    }
}

