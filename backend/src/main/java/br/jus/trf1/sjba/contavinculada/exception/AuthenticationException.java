package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends WebServerException {

    public AuthenticationException(String msg){
        super(msg, HttpStatus.UNAUTHORIZED);
    }


}
