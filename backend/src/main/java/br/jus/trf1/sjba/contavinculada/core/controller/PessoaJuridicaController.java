package br.jus.trf1.sjba.contavinculada.core.controller;


import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaJuridicaService;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pessoa_juridica")
public class PessoaJuridicaController {

    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    @GetMapping("/")
    public ResponseEntity<List<PessoaJuridica>> findAll(@RequestParam int pageNumber,
                                                                           @RequestParam int pageSize)  {

        AuthUserDatails userDetails = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (pageNumber < 0 || pageSize < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok()
                .body(pessoaJuridicaService.findAll(pageNumber, pageSize));
    }
}
