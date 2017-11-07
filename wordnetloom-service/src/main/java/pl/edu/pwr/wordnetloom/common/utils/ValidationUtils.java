package pl.edu.pwr.wordnetloom.common.utils;

import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;
import pl.edu.pwr.wordnetloom.common.model.Localised;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Arrays;
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
        validateLocalization(entity);
    }

    private static <T> void validateLocalization(T enity) {
        Field[] fields = enity.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            if (field.getType().isAssignableFrom(Localised.class)) {
                field.setAccessible(true);
                try {
                    Localised l = (Localised) field.get(enity);
                    if (!l.isValid()) {
                        throw new FieldNotValidException(field.getName(), "May not be null");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
