package br.jus.trf1.sjba.contavinculada.core.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FieldConstraint {

    FieldConstraintEnum constraintEnum;
    int limit;
    String message;

    public FieldConstraint(FieldConstraintEnum constraintEnum, int limit, String message) {
        this.constraintEnum = constraintEnum;
        this.limit = limit;
        this.message = message;
    }
    public FieldConstraint(FieldConstraintEnum constraintEnum, String message) {
        this.constraintEnum = constraintEnum;
        this.message = message;
    }
}
