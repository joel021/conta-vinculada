package br.jus.trf1.sjba.contavinculada.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class WebServerException extends Exception {

    private HttpStatus status;

    public WebServerException(String msg) {
        super(msg);
    }

    public WebServerException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}
