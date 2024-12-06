package br.jus.trf1.sjba.contavinculada.core.auditing;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Auditable;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.Reflection.eligibleClass;

public class AuditCascadeSetter {

    private final AuditSetter auditSetter;

    public AuditCascadeSetter(Usuario criadoPor) {
        auditSetter = new AuditSetter(criadoPor, null);
    }

    public <T extends Auditable> T setCriadoPorAndReturn(T object) throws NotAcceptableException, IllegalAccessException {

        setCriadoPor(object);
        return object;
    }

    public void setCriadoPor(Object auditableObject) throws NotAcceptableException, IllegalAccessException {

        List<Object> objectStack = new ArrayList<>();
        objectStack.add(auditableObject);

        while(objectStack.size() > 0) {

            Object object = objectStack.remove(0);
            set(object);

            updateStack(objectStack, object);
        }
    }

    public void updateStack(List<Object> objectStack, Object object) throws IllegalAccessException {

        if (object == null) {
            return;
        }

        if (eligibleClass(object.getClass())) {

            final Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                objectStack.add(field.get(object));
            }
        }
    }

    public void set(Object potentialAuditable) throws NotAcceptableException {

        if (potentialAuditable instanceof Auditable) {
            auditSetter.setCriator((Auditable) potentialAuditable);
        }
    }


}
