package br.jus.trf1.sjba.contavinculada.core.auditing;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Auditable;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;

import java.util.Calendar;


public class AuditSetter {

    private final Usuario criator;
    private final Usuario deletor;

    public AuditSetter(Usuario criator, Usuario deletor) {
        this.criator = criator;
        this.deletor = deletor;
    }

    public <T extends Auditable> void setCriator(T auditableEntity)  throws NotAcceptableException {

        if (invalidUsuario(criator)) {
            throw new NotAcceptableException("Usuário criador não pode ser nulo.");
        }
        auditableEntity.setCriadoEm(Calendar.getInstance());
        auditableEntity.setCriadoPor(criator);
    }

    public <T extends Auditable> void setDeletor(T auditableEntity) throws NotAcceptableException {

        if (invalidUsuario(deletor)) {
            throw new NotAcceptableException("O usuário deletador não pode ser nulo.");
        }
        auditableEntity.setDeletadoEm(Calendar.getInstance());
        auditableEntity.setDeletadoPor(deletor);
    }

    public boolean invalidUsuario(Usuario usuario) {

        return usuario == null || usuario.getIdUsuario() == null;
    }

}
