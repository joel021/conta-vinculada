package br.jus.trf1.sjba.contavinculada.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {


  @ExceptionHandler(value = {AccessDeniedException.class})
  protected ResponseEntity<Object> handleAccessDenied(Exception ex, WebRequest request) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }

  @ExceptionHandler(value = {WebServerException.class})
  public ResponseEntity<Object> handleApiException(WebServerException ex, WebRequest request) {

    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(value = {NotAcceptableException.class})
  public ResponseEntity<Object> handleNotAcceptableException(NotAcceptableException ex, WebRequest request) {

    Map<String, Object> error = new HashMap<>();
    error.put("error", ex.getMessage());
    error.put("errors", ex.getFieldErrors());
    return ResponseEntity.status(ex.getStatus()).body(error);
  }
}
