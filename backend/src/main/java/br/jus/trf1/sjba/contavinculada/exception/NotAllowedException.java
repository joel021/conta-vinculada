package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;

public class NotAllowedException extends WebServerException {

    public NotAllowedException(String msg){
        super(msg, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
