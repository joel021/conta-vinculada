package br.jus.trf1.sjba.contavinculada.security.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.exception.AuthenticationException;
import br.jus.trf1.sjba.contavinculada.security.dto.UpdateUserDto;
import br.jus.trf1.sjba.contavinculada.security.dto.UserAuthenticatedDTO;
import br.jus.trf1.sjba.contavinculada.security.dto.UserDataDTO;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.repository.UserRepository;
import br.jus.trf1.sjba.contavinculada.security.service.LdapService;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private LdapService ldapService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserAuthenticatedDTO userAdmin;

    private UserAuthenticatedDTO userNonAdmin;

    private List<String> usersRegistered;

    @BeforeEach
    public void setup() throws AuthenticationException {

        UserDataDTO admin = new UserDataDTO();
        admin.setUsuario("admin");
        admin.setDominio("JFBA");
        admin.setPapels(List.of(Papel.ROLE_ADMIN));

        userAdmin = userService.allowAccess(userService.registerFromDataDto(admin));
        userNonAdmin = userService.allowAccess(userRepository.save(new Usuario(null, "userNonAdmin980398d",
                null, null, "userNonAdmin@email.com", true, List.of(Papel.ROLE_GUEST), "JFBA")));

        usersRegistered = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            String randomUserName = "randomUser"+i;
            userRepository.save(new Usuario(null, randomUserName, null, null,
                    randomUserName+"@email.com",false, List.of(Papel.ROLE_GUEST),"JFBA"));
            usersRegistered.add(randomUserName);
        }

        when(ldapService.authenticate("userNonAdmin980398d", "password")).thenReturn(true);
        when(ldapService.authenticate("uid=admin,ou=system", "secret")).thenReturn(true);
        when(ldapService.authenticate("uid=jduke,ou=Users,dc=jboss,dc=org", "theduke")).thenReturn(true);
        when(ldapService.authenticate("jfba\\uid=jduke,ou=Users,dc=jboss,dc=org", "theduke")).thenReturn(true);
        when(ldapService.authenticate("jfba\\randomUser1", "password")).thenReturn(true);
    }

    @AfterEach
    public void clean() {

        try {
            userService.deleteByUsuario(userAdmin.getUsuario());
            userService.deleteByUsuario(userNonAdmin.getUsuario());
        }catch (Exception e){}

        for(String username: usersRegistered){
            try {
                userService.deleteByUsuario(username);
            }catch (Exception e){}
        }
    }


    @Test
    public void loginLdapTest() throws Exception {

        UserDataDTO userAuth = new UserDataDTO();
        userAuth.setUsuario("uid=admin,ou=system");
        userAuth.setDominio("   ");
        userAuth.setSenha("secret");
        String jsonBodyString = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userAuth);
        String resultGetJsonString = mockMvc.perform(MockMvcRequestBuilders.post("/users/signin")
                        .content(jsonBodyString).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Map<String, Object> resultObject = new ObjectMapper().reader().readValue(resultGetJsonString, Map.class);
        assert resultObject.get("userDetails") != null;
    }

    @Test
    public void testWrongPass() throws Exception {

        UserDataDTO userAuth = new UserDataDTO();
        userAuth.setUsuario("uid=admin,ou=system");
        userAuth.setSenha("secret1");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/signin")
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userAuth)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    public void testWrongUser() throws Exception  {

        UserDataDTO userAuth = new UserDataDTO();
        userAuth.setUsuario("uid=admin,ou=system2");
        userAuth.setSenha("secret");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/signin")
                .content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userAuth)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    public void testInvalidUser() throws Exception  {
        UserDataDTO userAuth = new UserDataDTO();
        userAuth.setUsuario("jfba\\00000000");
        userAuth.setSenha("secret");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/signin")
                .content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userAuth)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCorruptUsername() throws Exception {

        UserDataDTO userAuth = new UserDataDTO();
        userAuth.setUsuario("jfba\00000000");
        userAuth.setSenha("secret");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/signin")
                .content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userAuth)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    public void loginLdapWithWrongCredencialsTest() throws Exception {

        UserDataDTO userAuth = new UserDataDTO();
        userAuth.setDominio("    ");
        userAuth.setUsuario("uid=admin,ou=system");
        userAuth.setSenha("wrong_password");

        String jsonBodyString = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userAuth);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/signin")
                .content(jsonBodyString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateForbiden() throws Exception {

        UpdateUserDto userToUpdate = new UpdateUserDto();
        userToUpdate.setNome("Tester User Updated");
        userToUpdate.setEmail("testerUpdater@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/")
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void updateAuthenticatedTest() throws Exception {

        SecaoJudiciaria secaoJudiciaria = new SecaoJudiciaria();
        secaoJudiciaria.setNome("Seção Judiciária da Bahia");
        secaoJudiciaria.setCnpjSecao("12345678912345");
        secaoJudiciaria.setSigla("SJBA");

        Unidade unidade = new Unidade();
        unidade.setSiglaUnidade("SERFI");
        unidade.setNomeUnidade("Seção de Registros Financeiros");
        unidade.setSecaoJudiciaria(secaoJudiciaria);

        UpdateUserDto userToUpdate = new UpdateUserDto();
        userToUpdate.setNome("Tester User Updated");
        userToUpdate.setEmail("testerUpdater@gmail.com");
        userToUpdate.setDominio("JFBA");
        userToUpdate.setUnidade(unidade);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Usuario userFromRepositoryOptional = userRepository.findByUsuario(userNonAdmin.getUsuario()).get();
        assertEquals(secaoJudiciaria.getSigla(), userFromRepositoryOptional.getUnidade().getSecaoJudiciaria().getSigla());
    }

    @Test
    public void updateAuthenticatedUnidadeCorrectTest() throws Exception {

        SecaoJudiciaria secaoJudiciaria = new SecaoJudiciaria();
        secaoJudiciaria.setNome("Seção Judiciária da Bahia");
        secaoJudiciaria.setCnpjSecao("12345678912345");
        secaoJudiciaria.setSigla("SJBA");

        Unidade unidade = new Unidade();
        unidade.setSiglaUnidade("SERFI");
        unidade.setNomeUnidade("Seção de Registros Financeiros");
        unidade.setSecaoJudiciaria(secaoJudiciaria);

        UpdateUserDto userToUpdate = new UpdateUserDto();
        userToUpdate.setNome("Tester User Updated");
        userToUpdate.setEmail("testerUpdater@gmail.com");
        userToUpdate.setUnidade(unidade);
        userToUpdate.setDominio("JFBA");
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Optional<Usuario> userFromRepositoryOptional = userRepository.findByUsuario(userNonAdmin.getUsuario());
        assert userFromRepositoryOptional.get().getEmail().equals(userToUpdate.getEmail());
    }

    @Test
    public void updateAuthenticatedNotAcceptableTest() throws Exception {

        UpdateUserDto userToUpdate = new UpdateUserDto();
        userToUpdate.setNome("Tester User Updated");
        userToUpdate.setEmail("testerUpdater@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateUserPassingAuthentication() throws Exception {

        SecaoJudiciaria secaoJudiciaria = new SecaoJudiciaria();
        secaoJudiciaria.setNome("Seção Judiciária da Bahia");
        secaoJudiciaria.setCnpjSecao("12345678912345");
        secaoJudiciaria.setSigla("SJBA");

        Unidade unidade = new Unidade();
        unidade.setSiglaUnidade("SERFI");
        unidade.setNomeUnidade("Seção de Registros Financeiros");
        unidade.setSecaoJudiciaria(secaoJudiciaria);

        UserDataDTO userToUpdate = new UserDataDTO();
        userToUpdate.setUsuario("uid=jduke,ou=Users,dc=jboss,dc=org");
        userToUpdate.setNome("Guest User Updated");
        userToUpdate.setEmail("guestUserUpdated@gmail.com");
        userToUpdate.setSenha("theduke");
        userToUpdate.setPapels(List.of(Papel.ROLE_GUEST));
        userToUpdate.setUnidade(unidade);
        userToUpdate.setDominio("JFBA");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/update/"+userToUpdate.getUsuario())
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter()
                        .writeValueAsString(userToUpdate)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Usuario userFromRepository = userRepository.findByUsuario("uid=jduke,ou=Users,dc=jboss,dc=org").get();
        assertEquals(secaoJudiciaria.getSigla(), userFromRepository.getUnidade().getSecaoJudiciaria().getSigla());
    }

    @Test
    public void updateNonAuthenticatedWithUnidade() throws Exception {

        UserDataDTO userToUpdate = new UserDataDTO();
        userToUpdate.setUsuario("uid=jduke,ou=Users,dc=jboss,dc=org");
        userToUpdate.setNome("Guest User Updated");
        userToUpdate.setEmail("guestUserUpdated@gmail.com");
        userToUpdate.setSenha("theduke");
        userToUpdate.setPapels(List.of(Papel.ROLE_GUEST));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/update/"+userToUpdate.getUsuario())
                .content(new ObjectMapper().writer().withDefaultPrettyPrinter()
                        .writeValueAsString(userToUpdate)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void listUsersTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void listUsersForbiddenTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void updateAuthorizationTest() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setUsuario(userNonAdmin.getUsuario());
        usuario.setEnabled(true);
        usuario.setPapeis(List.of(Papel.ROLE_GUEST));

        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/authorization")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                        .content(new ObjectMapper().writer().writeValueAsString(usuario)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Usuario user = new ObjectMapper().reader().readValue(result, Usuario.class);
        assert user.getPapeis().contains(Papel.ROLE_GUEST);
    }

    @Test
    public void updateNonAuthorizedTest() throws Exception {

        UserDataDTO newUserDto = new UserDataDTO();
        newUserDto.setNome("New User Of The System");
        newUserDto.setDominio("JFBA");
        newUserDto.setEmail("randomUser1@jfba.br");
        newUserDto.setUsuario("randomUser1");
        newUserDto.setSenha("password");

        SecaoJudiciaria secaoJudiciaria = new SecaoJudiciaria();
        secaoJudiciaria.setCnpjSecao("083232343232");
        secaoJudiciaria.setSiglaByDominio("JFBA");
        secaoJudiciaria.setNome("Seção Judiciária da Bahia");

        Unidade unidade = new Unidade();
        unidade.setNomeUnidade("Secao Judiciaria da Bahia");
        unidade.setSiglaUnidade("SJBA");

        unidade.setSecaoJudiciaria(secaoJudiciaria);
        newUserDto.setUnidade(unidade);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/update/newUserRegisteredWihtougEmail")
                .content(new ObjectMapper().writer().writeValueAsString(newUserDto)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
