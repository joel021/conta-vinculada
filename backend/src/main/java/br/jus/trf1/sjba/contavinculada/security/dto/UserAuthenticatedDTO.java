package br.jus.trf1.sjba.contavinculada.security.dto;

import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticatedDTO extends UserResponseDTO {

    private String token;

    public static UserAuthenticatedDTO getInstanceFrom(Usuario usuario) {

        UserAuthenticatedDTO userAuthenticatedDTO = new UserAuthenticatedDTO();
        userAuthenticatedDTO.setPapels(usuario.getPapeis());
        userAuthenticatedDTO.setNome(usuario.getNome());
        userAuthenticatedDTO.setUsuario(usuario.getUsuario());
        userAuthenticatedDTO.setEmail(usuario.getEmail());
        userAuthenticatedDTO.setUnidade(usuario.getUnidade());
        userAuthenticatedDTO.setDominio(usuario.getDominio());

        return userAuthenticatedDTO;
    }
}
