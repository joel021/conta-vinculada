package br.jus.trf1.sjba.contavinculada.core.auditing;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Auditable;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuditSetterTests {

    private static AuditSetter auditSetter;

    @BeforeAll
    public static void setup() {
        Usuario usuario = Usuario.builder().idUsuario(1).build();
        auditSetter = new AuditSetter(usuario, usuario);
    }

    @Test
    public void getCriadoPorTest() throws NotAcceptableException {

        final Auditable auditable = new ContratoTerceirizado();
        auditSetter.setCriator(auditable);
        assertNotNull(auditable.getCriadoPor());
    }

    @Test
    public void getCriadoEmTest() throws NotAcceptableException {

        final Auditable auditable = new ContratoTerceirizado();
        auditSetter.setCriator(auditable);
        assertNotNull(auditable.getCriadoEm());
    }

    @Test
    public void getDeletadoPorTest() throws NotAcceptableException {

        final Auditable auditable = new ContratoTerceirizado();
        auditSetter.setDeletor(auditable);
        assertNotNull(auditable.getDeletadoPor());
    }

    @Test
    public void getDeletadoEmTest() throws NotAcceptableException {

        final Auditable auditable = new ContratoTerceirizado();
        auditSetter.setDeletor(auditable);
        assertNotNull(auditable.getDeletadoEm());
    }

    @Test
    public void setCriadoPorNullTest() {

        final AuditSetter auditSetter = new AuditSetter(null, null);
        final Auditable auditable = new ContratoTerceirizado();

        assertThrows(NotAcceptableException.class, () -> {
            auditSetter.setCriator(auditable);
        });
    }

    @Test
    public void setCriadoPorInvalidTest() {

        AuditSetter auditSetter = new AuditSetter(new Usuario(), new Usuario());
        final Auditable auditable = new ContratoTerceirizado();

        assertThrows(NotAcceptableException.class, () -> {
            auditSetter.setCriator(auditable);
        });
    }

    @Test
    public void setDeletadoPorNullTest() {

        final AuditSetter auditSetter = new AuditSetter(null, null);
        final Auditable auditable = new ContratoTerceirizado();

        assertThrows(NotAcceptableException.class, () -> {
            auditSetter.setDeletor(auditable);
        });
    }

    @Test
    public void setDeletadoPorInvalidTest() {

        final AuditSetter auditSetter = new AuditSetter(new Usuario(), new Usuario());
        final Auditable auditable = new ContratoTerceirizado();

        assertThrows(NotAcceptableException.class, () -> {
            auditSetter.setDeletor(auditable);
        });
    }

}
