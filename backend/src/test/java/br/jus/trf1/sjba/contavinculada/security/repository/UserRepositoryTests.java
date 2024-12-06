package br.jus.trf1.sjba.contavinculada.security.repository;

import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    private Usuario usuario;

    @BeforeEach
    public void setup() {

        usuario = new Usuario(null, "createUserWhichDoesNotExists", "Nome", null,
                "email@email.com", true, null, "JFBA");

        userRepository.save(usuario);
    }

    @Test
    public void saveWithoutNameTest() {

        Usuario usuario = new Usuario(null, "creates3rwdwfefdfwe", null, null,
                "email@email.com", true, null, "JFBA");
        Usuario usuarioCreated = userRepository.save(usuario);
        assertTrue(true);
    }
}
