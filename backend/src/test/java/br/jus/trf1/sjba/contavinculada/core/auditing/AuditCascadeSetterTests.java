package br.jus.trf1.sjba.contavinculada.core.auditing;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuditCascadeSetterTests {

    private AuditCascadeSetter auditCascadeSetter;

    private Auditable auditableObject;

    @BeforeEach
    public void setup() {

        Usuario usuario = Usuario.builder().idUsuario(1).dominio("JFBA").build();
        auditCascadeSetter = new AuditCascadeSetter(usuario);
    }

    @BeforeEach
    public void createInstances() {

        LocalDate today = LocalDate.now();
        Contrato contrato = new Contrato(1, "SJBA", null, null, today,
                today, today, "893", null);

        Funcionario funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(new PessoaFisica("00000000000"))
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I).criadoEm(Calendar.getInstance())
                .build();

        auditableObject = new ContratoTerceirizado(1, funcionario, contrato, "Cargo", new BigDecimal("123.45333"),
                40, today, null, null, null, null, null, null);
    }

    @Test
    public void setCriadoPorTest() throws NotAcceptableException, IllegalAccessException {

        auditCascadeSetter.setCriadoPor(auditableObject);

        ContratoTerceirizado contratoTerceirizado = (ContratoTerceirizado) auditableObject;
        assertNotNull(contratoTerceirizado.getCriadoPor());
    }

    @Test
    public void setCriadoPorFirstLevelAttributeTest() throws NotAcceptableException, IllegalAccessException {

        auditCascadeSetter.setCriadoPor(auditableObject);

        ContratoTerceirizado contratoTerceirizado = (ContratoTerceirizado) auditableObject;
        assertNotNull(contratoTerceirizado.getFuncionario().getCriadoPor());
    }

    @Test
    public void setCriadoPorAndReturnTest() throws NotAcceptableException, IllegalAccessException {

        final var auditableObjectSet = auditCascadeSetter.setCriadoPorAndReturn(auditableObject);
        assertNotNull(auditableObjectSet.getCriadoEm());
    }

    @Test
    public void setCriadoPorAndReturnCriadoEmNotNullTest() throws NotAcceptableException, IllegalAccessException {

        final var auditableObjectSet = auditCascadeSetter.setCriadoPorAndReturn(auditableObject);
        assertNotNull(auditableObjectSet.getCriadoEm());
    }

}
