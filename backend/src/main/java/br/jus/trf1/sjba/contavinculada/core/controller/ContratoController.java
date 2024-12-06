package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.ContratoService;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contrato")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @GetMapping("/")
    public ResponseEntity<List<Contrato>> searchByNomePessoaJuridica(@RequestParam String nomePessoaJuridica)  {

        AuthUserDatails userDetails = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok()
                .body(contratoService.searchByNomePessoaJuridica(nomePessoaJuridica, userDetails.siglaSecaoJudiciaria()));
    }

    @GetMapping("/{contratoId}")
    public ResponseEntity<Contrato> findById(@PathVariable Integer contratoId) throws NotFoundException {

        AuthUserDatails userDetails = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(contratoService.findById(contratoId, userDetails.siglaSecaoJudiciaria()));
    }

}
