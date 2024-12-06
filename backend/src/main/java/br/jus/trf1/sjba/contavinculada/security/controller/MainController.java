package br.jus.trf1.sjba.contavinculada.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public")
public class MainController {


    @GetMapping
    public ResponseEntity<String> home(){
        return ResponseEntity.status(HttpStatus.OK).body("Sistema de interface REST para contas vinculadas");
    }


}
