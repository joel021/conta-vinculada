package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;

public class NotAuthorizedYetException extends WebServerException {

    public NotAuthorizedYetException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
