package br.jus.trf1.sjba.contavinculada.security.dto;


import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserResponseDTO {

    private String usuario;
    private String email;
    private String nome;
    private Unidade unidade;
    private Set<Papel> papels = new HashSet<>();
    private String dominio;

}
