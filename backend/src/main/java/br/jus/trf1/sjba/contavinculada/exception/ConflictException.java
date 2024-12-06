package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends WebServerException {

    public ConflictException(String msg) {
        super(msg, HttpStatus.CONFLICT);
    }

}
