package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.auditing.AuditCascadeSetter;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.IncGrupoAContratoService;
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

import java.util.List;

@RestController
@RequestMapping("/inc_grupo_a_contrato")
public class IncGrupoAContratoController {

    @Autowired
    private IncGrupoAContratoService incGrupoAContratoService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<IncGrupoAContrato> save(@RequestBody @Valid IncGrupoAContrato incGrupoAContrato,
                                                  BindingResult bindingResult)
            throws NotFoundException, NotAcceptableException, IllegalAccessException {

        if (bindingResult.hasErrors()) {
            throw new NotAcceptableException("Os dados que você forneceu não foram aceitos.", bindingResult.getFieldErrors());
        }
        AuthUserDatails userDetails = (AuthUserDatails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuditCascadeSetter auditCascadeSetter = new AuditCascadeSetter(userDetails);

        return ResponseEntity
                .ok()
                .body(incGrupoAContratoService.save(auditCascadeSetter.setCriadoPorAndReturn(incGrupoAContrato), userDetails.siglaSecaoJudiciaria()));
    }

    @GetMapping("/contrato/{idContrato}")
    public ResponseEntity<List<IncGrupoAContrato>> findAllByIdContrato(@PathVariable Integer idContrato) {

        return ResponseEntity.ok().body(incGrupoAContratoService.findByIdContrato(idContrato));
    }

}
