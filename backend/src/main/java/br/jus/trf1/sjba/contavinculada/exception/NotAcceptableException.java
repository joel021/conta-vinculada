package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;


public class NotAcceptableException extends WebServerException {

    private List<FieldError> fieldErrors;

    public NotAcceptableException(String msg) {
        super(msg, HttpStatus.NOT_ACCEPTABLE);
    }

    public NotAcceptableException(String msg, List<FieldError> fieldErrors) {
        super(msg, HttpStatus.NOT_ACCEPTABLE);
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

}
