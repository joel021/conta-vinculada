package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PessoaFisicaControllerTests extends ControllerTests {


    @BeforeEach
    public void setup() {
        userNonAdmin = userService.allowAccess(userService.save(new Usuario(null, "userNonAdmin",
                null, null, null, true, List.of(Papel.ROLE_GUEST), "JFBA")));
    }

    @Test
    public void findByNomeContainingTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/pessoa_fisica/?nome=Nome")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
