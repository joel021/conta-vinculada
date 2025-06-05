package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaJuridicaRepository;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContratoControllerTests extends ControllerTests {

    @Autowired
    public ContratoRepository contratoRepository;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    private Contrato contratoExitent;

    private PessoaJuridica pessoaJuridicaExistent;

    @BeforeEach
    public void setup() {

        userNonAdmin = userService.allowAccess(userService.save(new Usuario(null, "userNonAdmin",
                null, null, null, true, List.of(Papel.ROLE_GUEST), "JFBA")));

        pessoaJuridicaExistent = new PessoaJuridica();
        pessoaJuridicaExistent.setNome("Pessoa");
        pessoaJuridicaExistent.setCnpj("00000000000000");
        pessoaJuridicaExistent.setIdPessoa(1);

        pessoaJuridicaRepository.save(pessoaJuridicaExistent);

        contratoExitent = new Contrato(1, "SJBA", null, null,
                null, null,null, null, null);
        contratoRepository.save(contratoExitent);
    }

    @Test
    public void searchByNameOfPeopleAndUnidadeTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("nomePessoaJuridica", "Nome Pessoa")
                        .param("unidade", "SJBA")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    public void searchByNameOfPeopleAndUnidadeUnauthorizedTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato/")
                .param("nomePessoaJuridica", "Nome Pessoa")
                .param("unidade", "SJBA")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

    }

    @Test
    public void findByIdTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato/"+contratoExitent.getIdContrato())
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
