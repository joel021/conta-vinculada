package br.jus.trf1.sjba.contavinculada.security.service;


import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public Usuario findByUsuario(String username) throws UsernameNotFoundException {

        Optional<Usuario> appUser = userRepository.findByUsuario(username);
        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("Este usuário não foi encontrado.");
        }
        return appUser.get();
    }

    @Override
    public AuthUserDatails loadUserByUsername(String username) throws UsernameNotFoundException {
        return AuthUserDatails.instanceFrom(findByUsuario(username));
    }
}
