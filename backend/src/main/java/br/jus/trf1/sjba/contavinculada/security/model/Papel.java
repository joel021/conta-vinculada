package br.jus.trf1.sjba.contavinculada.security.model;

import org.springframework.security.core.GrantedAuthority;

public enum Papel implements GrantedAuthority {

  ROLE_ADMIN, ROLE_MANAGER, ROLE_EXECUTOR, ROLE_GUEST;

  public String getAuthority() {
    return name();
  }

}
