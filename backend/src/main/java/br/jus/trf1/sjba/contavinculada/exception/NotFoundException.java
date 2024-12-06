package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends WebServerException {
    public NotFoundException(String msg){
        super(msg, HttpStatus.NOT_FOUND);
    }
}
