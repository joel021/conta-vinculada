package br.jus.trf1.sjba.contavinculada.security.service;

import br.jus.trf1.sjba.contavinculada.exception.AuthenticationException;
import br.jus.trf1.sjba.contavinculada.exception.NotAllowedException;
import br.jus.trf1.sjba.contavinculada.security.dto.UserDataDTO;
import br.jus.trf1.sjba.contavinculada.security.dto.UserResponseDTO;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private LdapService ldapService;

    private Usuario userToTest;

    private List<Usuario> adminsModified;

    private Usuario usuarioToUpdate;

    @BeforeEach
    public void setup() throws AuthenticationException {

        userToTest = new Usuario();
        userToTest.setNome("User to test");
        userToTest.setUsuario("ba07577081531TestCreation");
        userToTest.setEmail("userToTest@gmail.com");
        userToTest.setDominio("JFBA");

        adminsModified = new ArrayList<>();

        usuarioToUpdate = new Usuario(null, "thisUserDoesNotExistsInThisContext", "null",
                null, "email@email.com", true, null, "JFBA");

        userRepository.save(usuarioToUpdate);
        when(ldapService.authenticate(usuarioToUpdate.getUsuario(), "password")).thenReturn(true);
    }

    @AfterEach
    public void clean() {

        try {
            userService.deleteByUsuario(userToTest.getUsuario());
        } catch (Exception ignored){}

        userService.deleteByUsuario(usuarioToUpdate.getUsuario());

        for (Usuario usuario : adminsModified) {
            usuario.setPapeis(List.of(Papel.ROLE_ADMIN));
            userRepository.save(usuario);
        }
    }

    @Test
    public void updateWithLdapVerificationTest() throws AuthenticationException {

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUsuario(usuarioToUpdate.getUsuario());
        userDataDTO.setSenha("password");

        UserResponseDTO resultUsuario = userService.updateWithLdapVerification(userDataDTO);

        assertEquals(userDataDTO.getUsuario(), resultUsuario.getUsuario(), "Assert the user was updated.");
    }

    @Test
    public void searchTest(){

        userRepository.save(userToTest);
        Usuario result = userService.findByUsuario(userToTest.getUsuario());
        assert result != null;
    }

    @Test
    public void findOrRegisterUserTest(){

        Usuario registered = userService.findOrRegisterUser(userToTest.getUsuario(), "JFBA");
        assert registered != null;
    }

    @Test
    public void updateAuthorizationTest() throws NotAllowedException {

        //1. Set all admins from the system to guest users
        List<Usuario> adminsUsers = userRepository.findByPapeisContains(Papel.ROLE_ADMIN);

        for(Usuario adminUser: adminsUsers) {
            adminUser.setPapeis(List.of(Papel.ROLE_GUEST));
            adminsModified.add(userRepository.saveAndFlush(adminUser));
        }

        //2. Take only one admin to the system
        adminsUsers.get(0).setPapeis(List.of(Papel.ROLE_ADMIN));
        userRepository.saveAndFlush(adminsUsers.get(0));

        //Test: try to update the unique admin user to guest user, expect a exception
        Usuario userToUpdate = adminsUsers.get(0);
        userToUpdate.setPapeis(List.of(Papel.ROLE_GUEST));

        assertThrows(NotAllowedException.class, () -> {
            userService.updateAuthorization(userToUpdate);
        });
    }

}
