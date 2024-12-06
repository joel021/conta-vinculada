package br.jus.trf1.sjba.contavinculada.security.model;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.security.dto.UpdateUserDto;
import br.jus.trf1.sjba.contavinculada.security.dto.UserAuthDTO;
import br.jus.trf1.sjba.contavinculada.security.dto.UserDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(unique = true, nullable = false)
    private String usuario;

    private String nome;

    @ManyToOne
    @JoinColumn(name="id_unidade", referencedColumnName = "idUnidade")
    private Unidade unidade;

    private String email;

    private boolean isEnabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Papel> papeis = new HashSet<>();

    @NotNull(message = "Dominínio do usuário não pode ser nulo.")
    private String dominio;

    public Set<Papel> getPapeis() {

        Set<Papel> papeisCopySet = new HashSet<>();
        final List<Papel> allPapeis = Arrays.stream(Papel.values()).toList();

        for (Papel papel:papeis.stream().toList()) {
            for(int i = papel.ordinal(); i < allPapeis.size(); i++) {
                papeisCopySet.add(allPapeis.get(i));
            }
        }
        this.papeis = papeisCopySet;
        return papeis;
    }

    public static Usuario getInstanceByDataDto(UserDataDTO userDataDTO){

        Usuario usuario = getInstanceByAuthDto(userDataDTO);

        usuario.setNome(userDataDTO.getNome());
        usuario.setEmail(userDataDTO.getEmail());
        usuario.setPapeis(userDataDTO.getPapels());
        usuario.setUnidade(userDataDTO.getUnidade());
        usuario.setDominio(userDataDTO.getDominio());

        if (usuario.getUnidade() == null) {
            usuario.setUnidade(new Unidade());
        }

        return usuario;
    }

    public static Usuario getInstanceByAuthDto(UserAuthDTO userAuthDTO){

        Usuario usuario = new Usuario();
        usuario.setUsuario(userAuthDTO.getUsuario());
        return usuario;
    }

    public static Usuario getInstanceByUpdateDto(UpdateUserDto updateUserDto) {

        Usuario usuario = new Usuario();
        usuario.setNome(updateUserDto.getNome());
        usuario.setEmail(updateUserDto.getEmail());
        usuario.setUnidade(updateUserDto.getUnidade());
        usuario.setDominio(updateUserDto.getDominio());

        return usuario;
    }

    public static Usuario getInstanceByMap(Map<String, Object> usuarioMap) {
        return new ObjectMapper().convertValue(usuarioMap, Usuario.class);
    }

    public String siglaSecaoJudiciaria() {

        return (dominio != null && dominio.trim().length() >= 4)
                ? dominio.replace("JF","SJ") : "UnidadeNotDefined";
    }
}
