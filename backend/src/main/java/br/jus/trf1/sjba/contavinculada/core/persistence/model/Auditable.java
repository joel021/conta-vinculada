package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import br.jus.trf1.sjba.contavinculada.security.model.Usuario;

import java.util.Calendar;

public interface Auditable {

    public void setCriadoPor(Usuario usuario);

    public void setDeletadoEm(Calendar date);

    public void setDeletadoPor(Usuario usuario);

    public void setCriadoEm(Calendar date);

    public Usuario getCriadoPor();

    public Usuario getDeletadoPor();

    public Calendar getCriadoEm();

    public Calendar getDeletadoEm();

}
