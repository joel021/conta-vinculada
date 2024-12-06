package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.auditing.AuditCascadeSetter;
import br.jus.trf1.sjba.contavinculada.core.dto.LiberacaoDTO;
import br.jus.trf1.sjba.contavinculada.core.liberation.data.DecimoContratoLiberacao;
import br.jus.trf1.sjba.contavinculada.core.liberation.data.FGTSContratoLiberacao;
import br.jus.trf1.sjba.contavinculada.core.liberation.data.FeriasContratoLiberacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.LiberacaoService;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraint;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraintEnum;
import br.jus.trf1.sjba.contavinculada.core.validation.Validator;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/liberacao")
public class LiberacaoController {

    @Autowired
    private LiberacaoService liberacaoService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Liberacao> save(@RequestBody @Valid LiberacaoDTO liberacaoDTO,
                                          BindingResult bindingResult)
            throws NotFoundException, NotAcceptableException, IllegalAccessException {

        if (bindingResult.hasErrors()) {
            throw new NotAcceptableException("Os dados que você forneceu não foram aceitos.", bindingResult.getFieldErrors());
        }
        AuthUserDatails user = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuditCascadeSetter auditCascadeSetter = new AuditCascadeSetter(user);

        return ResponseEntity
                .ok()
                .body(liberacaoService.registerLiberacao(
                        auditCascadeSetter.setCriadoPorAndReturn(liberacaoDTO.liberacao()),
                        liberacaoDTO.getDataDesligamento()
                ));
    }

    @GetMapping("/DECIMO_TERCEIRO/{idContrato}")
    public ResponseEntity<DecimoContratoLiberacao> findDecimoTerceiroByIdContratoAndFechamento(
            @PathVariable int idContrato,
            LocalDate fechamento
    ) throws NotAcceptableException {

        if (fechamento == null) {
            throw new NotAcceptableException("Forneça a data de fechamento.");
        }
        return ResponseEntity.ok(liberacaoService.findDecimoTerceiro(idContrato, fechamento));
    }

    @GetMapping("/FERIAS/{idContrato}")
    public ResponseEntity<FeriasContratoLiberacao> findFeriasByIdContratoAndFechamento(
            @PathVariable int idContrato,
            LocalDate fechamento
    ) throws NotAcceptableException {

        if (fechamento == null) {
            throw new NotAcceptableException("Forneça a data de fechamento.");
        }
        return ResponseEntity.ok(liberacaoService.findFerias(idContrato, fechamento));
    }

    @GetMapping("/FGTS/{idContrato}")
    public ResponseEntity<FGTSContratoLiberacao> findFGTSByIdContratoAndFechamento(
            @PathVariable int idContrato,
            LocalDate fechamento
    ) throws NotAcceptableException {

        if (fechamento == null) {
            throw new NotAcceptableException("Forneça a data de fechamento.");
        }
        return ResponseEntity.ok(liberacaoService.findFGTS(idContrato, fechamento));
    }

}
