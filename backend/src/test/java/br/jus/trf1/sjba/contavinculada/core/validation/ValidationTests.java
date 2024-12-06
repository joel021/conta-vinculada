package br.jus.trf1.sjba.contavinculada.core.validation;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {

    @Test
    public void buildValidatorTest() {

        final Validator validator = Validator
                .builder()
                .addConstraint("contrato", FieldConstraint.builder()
                        .constraintEnum(FieldConstraintEnum.NOT_NULL)
                        .message("O contrato não pode ser nulo.")
                        .build());

        assertFalse(validator.getConstraints().isEmpty());
    }

    @Test
    public void checkTest() {

        final ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(1)
                .dataInicio(LocalDate.now())
                .build();

        final Validator validator = Validator
                .builder()
                .addConstraint("contrato", FieldConstraint.builder()
                        .constraintEnum(FieldConstraintEnum.NOT_NULL)
                        .message("O contrato não pode ser nulo.")
                        .build());

        validator.check("contrato", contratoTerceirizado.getContrato());

        assertFalse(validator.getErrors().isEmpty());
    }

    @Test
    public void validateNullTest() {

        final ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(1)
                .dataInicio(LocalDate.now())
                .build();

        final Validator validator = Validator
                .builder()
                .addConstraint("cargo", FieldConstraint.builder()
                        .constraintEnum(FieldConstraintEnum.NOT_NULL)
                        .message("O cargo não pode ser nulo.")
                        .build());

        assertThrows(NotAcceptableException.class, () -> {
            validator.validateOrThrows(contratoTerceirizado);
        });

    }

    @Test
    public void validateMaxLimitTest() {

        final ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(1)
                .dataInicio(LocalDate.now())
                .cargo("Cefeeffefeefefefefefefefefefefefefefefefef")
                .build();

        final Validator validator = Validator
                .builder()
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MAX_SIZE,
                        10, "O cargo não pode ser maior que 10."))
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MIN_SIZE,
                        2, "O cargo não pode ser menor que 2."));

        assertThrows(NotAcceptableException.class, () -> {
            validator.validateOrThrows(contratoTerceirizado);
        });

    }

    @Test
    public void validateMinLimitTest() {

        final ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(1)
                .dataInicio(LocalDate.now())
                .cargo("C")
                .build();

        final Validator validator = Validator
                .builder()
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MIN_SIZE,
                        2, "O cargo não pode ser menor que 2."))
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MAX_SIZE,
                        10, "O cargo não pode ser maior que 10."));

        assertThrows(NotAcceptableException.class, () -> {
            validator.validateOrThrows(contratoTerceirizado);
        });

    }

    @Test
    public void outOfMinTest() {

        final Validator validator = new Validator(null);
        final ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(1)
                .dataInicio(LocalDate.now())
                .cargo("C")
                .build();
        final boolean outOfMin = validator.outOfMin(contratoTerceirizado.getCargo(), 2);
        assertTrue(outOfMin);
    }

    @Test
    public void validateTest() throws NotAcceptableException, IllegalAccessException {

        final ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(1)
                .dataInicio(LocalDate.now())
                .cargo("Cargo")
                .build();

        final Validator validator = Validator
                .builder()
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MIN_SIZE,
                        2, "O cargo não pode ser menor que 2."))
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MAX_SIZE,
                        10, "O cargo não pode ser maior que 10."));

        validator.validateOrThrows(contratoTerceirizado);

    }

}
