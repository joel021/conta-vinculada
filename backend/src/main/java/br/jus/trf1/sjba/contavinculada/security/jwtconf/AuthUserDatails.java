package br.jus.trf1.sjba.contavinculada.security.jwtconf;


import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthUserDatails extends Usuario implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPapeis();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return super.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public static AuthUserDatails instanceFrom(Usuario user) {

        AuthUserDatails userDatails = new AuthUserDatails();
        userDatails.setUsuario(user.getUsuario());
        userDatails.setPapeis(user.getPapeis());
        userDatails.setEnabled(user.isEnabled());
        userDatails.setEmail(user.getEmail());
        userDatails.setIdUsuario(user.getIdUsuario());
        userDatails.setUnidade(user.getUnidade());
        userDatails.setDominio(user.getDominio());

        return userDatails;
    }
}
