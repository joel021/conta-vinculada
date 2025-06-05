package br.jus.trf1.sjba.contavinculada.security.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataDTO extends UserAuthDTO {

  private String email;
  private String nome;
  private List<Papel> papels;
  private Unidade unidade;
}
