package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaFisicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoa_fisica")
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @GetMapping("/")
    public ResponseEntity<List<PessoaFisica>> findByNomeContaining(@RequestParam String nome) {
        return ResponseEntity.ok(pessoaFisicaService.findByNomeContaining(nome));
    }
}
