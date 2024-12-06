package br.jus.trf1.sjba.contavinculada.security.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UpdateUserDto {

    public String nome;
    public String email;
    public String dominio;
    public Unidade unidade;
}
