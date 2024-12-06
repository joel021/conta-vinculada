package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class SecaoJudiciaria {

    @Id
    public String cnpjSecao;

    public String nome;

    @NotBlank(message = "Forneça a sigla da Seção Judiciária.")
    public String sigla;

    public void setSiglaByDominio(String dominio) {
        final String dominioFormatted = (dominio != null) ? dominio : "";
        sigla = "SJ"+dominioFormatted.substring(2);
    }
}

