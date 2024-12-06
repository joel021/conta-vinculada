package br.jus.trf1.sjba.contavinculada.core.validation;

import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.*;

import static br.jus.trf1.sjba.contavinculada.utils.Reflection.eligibleClass;

@Getter
public class Validator {

    private Map<String, List<FieldConstraint>> constraints;
    private final List<FieldError> errors = new ArrayList<>();

    public Validator(Map<String, List<FieldConstraint>> constraints) {

        this.constraints = constraints;
        if (this.constraints == null) {
            this.constraints = new HashMap<>();
        }
    }

    public static Validator builder() {
        return new Validator(null);
    }

    public Validator addConstraint(String fieldName, FieldConstraint constraint) {

        List<FieldConstraint> fieldConstraints = constraints.get(fieldName);
        if (fieldConstraints == null) {
            fieldConstraints = new ArrayList<>();
        }
        fieldConstraints.add(constraint);

        constraints.put(fieldName, fieldConstraints);
        return this;
    }

    public <T> void validateOrThrows(T object) throws NotAcceptableException {

        if (object == null) {
            return;
        }

        try {
            validate(object);
        }catch (IllegalAccessException e){}

        if (!errors.isEmpty()) {
            throw new NotAcceptableException("Seus dados não foram aceitos.", errors);
        }
    }

    public <T> void validate(T object) throws IllegalAccessException {

        final Class<?> classObject = object.getClass();
        if (eligibleClass(classObject)) {

            for (Field field: classObject.getDeclaredFields()) {
                check(field.getName(), field.get(object));
            }
        }
    }

    public void check(String fieldName, Object field) {

        final List<FieldConstraint> fieldConstraintList = constraints.get(fieldName);

        if (fieldConstraintList != null) {

            for (FieldConstraint constraint: fieldConstraintList) {

                if (isInvalid(constraint, field)) {
                    errors.add(new FieldError(fieldName, fieldName, constraint.message));
                }
            }
        }

    }

    public boolean isInvalid(FieldConstraint constraint, Object field) {

        if (isNull(field)) {
            return true;
        }

        if (constraint.getConstraintEnum().equals(FieldConstraintEnum.NOT_EMPTY) && isEmpty(field)) {
            return true;
        }

        if (constraint.constraintEnum.equals(FieldConstraintEnum.MAX_SIZE) && outOfMax(field, constraint.limit)) {
            return true;
        }

        return constraint.constraintEnum.equals(FieldConstraintEnum.MIN_SIZE) && outOfMin(field, constraint.limit);
    }

    public boolean isEmpty(Object field) {

        if (field instanceof Collection) {
            return ((Collection<?>) field).isEmpty();
        } else if (field.getClass().isArray()) {
            return ((Object[]) field).length == 0;
        } else if (field instanceof String) {
            return ((String) field).trim().isEmpty();
        }
        return false;
    }

    public boolean isNull(Object field) {

        return field == null;
    }

    public boolean outOfMax(Object field, int limit) {

        if (field instanceof Integer) {
            return ((int) field) > limit;

        } else if (field instanceof Float) {
            return ((float) field) > limit;

        } else if (field instanceof  Double) {
            return ((double) field) > limit;

        } else if (field instanceof Collection) {
            return ((Collection<?>) field).size() > limit;

        } else if (field.getClass().isArray()) {
            return ((Object[]) field).length > limit;

        } else if (field instanceof String) {
            return ((String) field).length() > limit;
        }

        return false;
    }

    public boolean outOfMin(Object field, int limit) {

        if (field instanceof Integer) {
            return ((int) field) < limit;

        } else if (field instanceof Float) {
            return ((float) field) < limit;

        } else if (field instanceof  Double) {
            return ((double) field) < limit;

        } else if (field instanceof Collection) {
            return ((Collection<?>) field).size() < limit;

        } else if (field.getClass().isArray()) {
            return ((Object[]) field).length < limit;

        } else if (field instanceof String) {
            return ((String) field).length() < limit;
        }

        return false;
    }

}
