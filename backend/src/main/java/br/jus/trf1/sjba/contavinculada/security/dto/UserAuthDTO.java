package br.jus.trf1.sjba.contavinculada.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO {

    private String dominio;

    private String usuario;

    private String senha;
}
