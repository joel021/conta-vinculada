package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.auditing.AuditCascadeSetter;
import br.jus.trf1.sjba.contavinculada.core.dto.FuncionarioContratosDto;
import br.jus.trf1.sjba.contavinculada.core.dto.UnidadeContratosFuncionariosDto;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.ContratoTerceirizadoService;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraint;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraintEnum;
import br.jus.trf1.sjba.contavinculada.core.validation.Validator;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contrato_terceirizado")
public class ContratoTerceirizadoController {

    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    public ResponseEntity<ContratoTerceirizado> register(@RequestBody @Valid ContratoTerceirizado contratoTerceirizado)
            throws NotFoundException, NotAcceptableException, IllegalAccessException {

        Validator validator = Validator.builder()
                .addConstraint("contrato", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "O contrato deve ser fornecido."))
                .addConstraint("funcionario", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "O funcionário deve ser informado."))
                .addConstraint("cargo", new FieldConstraint(FieldConstraintEnum.MIN_SIZE, 2,
                        "Defina um nome de cargo válido."))
                .addConstraint("remuneracao", new FieldConstraint(FieldConstraintEnum.MIN_SIZE, 1,
                        "A remuneração deve ser maior que zero."))
                .addConstraint("cargaHoraria", new FieldConstraint(FieldConstraintEnum.MIN_SIZE,1,
                        "A carga horária deve ser maior que zero."))
                .addConstraint("dataInicio", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Data de início não pode ser nula."))
                .addConstraint("idContrato", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Lotação deve ser forncida."))
                .addConstraint("nome", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "Lotação deve ser forncida."));

        validator.validateOrThrows(contratoTerceirizado);

        AuthUserDatails userDetails = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuditCascadeSetter auditCascadeSetter = new AuditCascadeSetter(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratoTerceirizadoService.register(
                        auditCascadeSetter.setCriadoPorAndReturn(contratoTerceirizado),
                        userDetails.siglaSecaoJudiciaria()));
    }

    @PatchMapping("/{funcionarioId}/{contratoId}")
    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    public ResponseEntity<Map<String, String>> setDataDesligamento(@PathVariable Integer funcionarioId,
                                                                   @PathVariable Integer contratoId,
                                                                   @RequestParam LocalDate dataDesligamento) {

        contratoTerceirizadoService.setDataDesligamento(funcionarioId, contratoId, dataDesligamento);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Funcionário desligado com sucesso.");
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) throws NotFoundException {

        AuthUserDatails userDetails = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contratoTerceirizadoService.delete(id, userDetails);
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Essa associação foi deletada com sucesso.");
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/contrato/{idContrato}")
    public ResponseEntity<List<FuncionarioContratosDto>> getFuncionarioByIdContrato(@PathVariable Integer idContrato) {

        return ResponseEntity.ok().body(contratoTerceirizadoService.funcionarioContratosDtoListByIdContrato(idContrato));
    }

    @GetMapping("/unidade/{unidade}")
    public ResponseEntity<UnidadeContratosFuncionariosDto> findByUnidade(@PathVariable String unidade) {
        return ResponseEntity.ok(contratoTerceirizadoService.funcionarionsByUnidade(unidade));
    }

}
