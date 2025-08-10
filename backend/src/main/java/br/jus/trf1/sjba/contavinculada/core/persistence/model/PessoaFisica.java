package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import br.jus.trf1.sjba.contavinculada.utils.StringUtils;
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

        StringUtils stringUtils = new StringUtils();
        String formatedCpf = stringUtils.onlyNumbers(cpf);

        if (formatedCpf.isEmpty()) {
            formatedCpf = "00000000000";
        } else if (formatedCpf.length() < 11) {
            formatedCpf = String.format("%011d", Long.parseLong(formatedCpf));
        }
        this.cpf = formatedCpf;
    }
}
