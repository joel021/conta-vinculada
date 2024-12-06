package br.jus.trf1.sjba.contavinculada.core.controller;


import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.FuncionarioService;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraint;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraintEnum;
import br.jus.trf1.sjba.contavinculada.core.validation.Validator;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<Funcionario> findById(@PathVariable Integer idFuncionario) throws NotFoundException {

        return ResponseEntity.ok(funcionarioService.findById(idFuncionario));
    }

    @PatchMapping("/{idFuncionario}")
    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    public ResponseEntity<Funcionario> update(@PathVariable Integer idFuncionario, @RequestBody Funcionario funcionario)
            throws NotAcceptableException, IllegalAccessException, NotFoundException {

        Validator funcionarioValidator = Validator.builder()
                .addConstraint("cpf", new FieldConstraint(FieldConstraintEnum.MIN_SIZE,11,
                        "Informe um CPF válido."))
                .addConstraint("cpf", new FieldConstraint(FieldConstraintEnum.MAX_SIZE,11,
                        "Informe um CPF válido."))
                .addConstraint("nome", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Informe o nome do funcionário."))
                .addConstraint("nome", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Informe o nome do funcionário."))
                .addConstraint("matricula", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Informe a matrícula do funcionário."))
                .addConstraint("nivel", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Informe o nível do funcionário."))
                .addConstraint("pessoaFisica", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Informe os dados pessoais do profissional."));;

        funcionarioValidator.validateOrThrows(funcionario);
        funcionarioValidator.validateOrThrows(funcionario.getPessoaFisica());

        funcionario.setIdFuncionario(idFuncionario);
        return ResponseEntity.ok().body(funcionarioService.update(funcionario));
    }

    @GetMapping("/")
    public ResponseEntity<List<Funcionario>> findByCpf(@RequestParam String cpf) {
        return ResponseEntity.ok().body(funcionarioService.findByCpf(cpf));
    }
}
