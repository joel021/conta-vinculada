package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.service.ProvisaoService;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/provisoes")
public class ProvisaoController {

    @Autowired
    private ProvisaoService provisaoService;

    @GetMapping("/{idContrato}")
    private ResponseEntity<?> getProvisoes(@PathVariable int idContrato, @RequestParam LocalDate date)
            throws NotAcceptableException, NotFoundException {

        return ResponseEntity.ok().body(provisaoService.getAllProvisoes(idContrato, date));
    }
}
