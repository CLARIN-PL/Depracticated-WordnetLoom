package pl.edu.pwr.wordnetloom.common.utils;

import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

public abstract class ValidationUtils {

    public static <T> void validateEntityFields(Validator validator, T entity) {
        Set<ConstraintViolation<T>> errors = validator.validate(entity);
        Iterator<ConstraintViolation<T>> itErrors = errors.iterator();
        if (itErrors.hasNext()) {
            ConstraintViolation<T> violation = itErrors.next();
            throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }
}
