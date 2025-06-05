package br.jus.trf1.sjba.contavinculada.security.jwtconf;


import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class JwtTokenProviderTests {

    @Autowired
    private JwtService jwtTokenProvider;

    @Test
    public void createTokenTest(){
        Usuario usuario = new Usuario(null, "username", "email", null,
                "email@email.com", true, List.of(Papel.ROLE_ADMIN),"JFBA");
        String token = jwtTokenProvider.generateToken(AuthUserDatails.instanceFrom(usuario));
        assert token != null;
    }
}
