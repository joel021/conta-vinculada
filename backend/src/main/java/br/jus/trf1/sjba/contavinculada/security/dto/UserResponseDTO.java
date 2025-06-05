package br.jus.trf1.sjba.contavinculada.security.dto;


import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponseDTO {

    private String usuario;
    private String email;
    private String nome;
    private Unidade unidade;
    private List<Papel> papels = new ArrayList<>();
    private String dominio;

}
