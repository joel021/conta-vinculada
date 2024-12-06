package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.*;

@Entity
public class PessoaJuridica extends Pessoa {

    public String cnpj;

    public PessoaJuridica(String cnpj) {
        this.cnpj = cnpj;
    }

    public PessoaJuridica() {}

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
