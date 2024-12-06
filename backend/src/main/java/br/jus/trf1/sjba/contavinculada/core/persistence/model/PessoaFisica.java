package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Entity
public class PessoaFisica extends Pessoa {

    @Size(min = 11, max = 11, message = "Digite o CPF, ele deve ter 11 dígitos numéricos.")
    @Column(nullable = false)
    public String cpf;

    public PessoaFisica(String cpf) {
        this.cpf = cpf;
    }

    public PessoaFisica(){}

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
