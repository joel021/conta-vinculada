package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.NivelEnsino;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.FuncionarioRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaFisicaService;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FuncionarioControllerTests extends  ControllerTests {


    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    private Funcionario funcionario;

    @BeforeEach
    public void setup() {

        Usuario usuario = userService.save(new Usuario(null, "userNonAdmin",
                null, null, null, true, List.of(Papel.ROLE_GUEST), "JFBA"));
        userNonAdmin = userService.allowAccess(usuario);

        Usuario usuarioAdmin = userService.save(new Usuario(null, "userAdmin",
                null, null, null, true, List.of(Papel.ROLE_ADMIN), "JFBA"));
        userAdmin = userService.allowAccess(usuarioAdmin);

        PessoaFisica pessoaFisica = PessoaFisica.builder().cpf("12121212121").build();
        pessoaFisica.setNome("Nome Funcionário");
        pessoaFisica = pessoaFisicaService.saveIfNotExists(pessoaFisica);

        funcionario = Funcionario.builder()
                .pessoaFisica(pessoaFisica)
                .nivel(NivelEnsino.M)
                .racaCor("Raça")
                .matricula("ba"+pessoaFisica.getCpf())
                .criadoPor(usuario)
                .criadoEm(Calendar.getInstance())
                .build();
        funcionario = funcionarioRepository.save(funcionario);
    }

    @Test
    public void updateTest() throws Exception {

        Map<String, Object> funcionarioToUpdate = new HashMap<>();
        funcionarioToUpdate.put("raca", "Raça Updated");
        funcionarioToUpdate.put("matricula", "Matrícula");
        funcionarioToUpdate.put("nivel", funcionario.getNivel());
        funcionarioToUpdate.put("pessoaFisica", funcionario.getPessoaFisica());

        mockMvc.perform(MockMvcRequestBuilders.patch("/funcionario/"+funcionario.getIdFuncionario())
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(new ObjectMapper().writer().writeValueAsString(funcionarioToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void updateMissingCpfTest() throws Exception {

        Map<String, Object> pessoaFisica = new HashMap<>();
        pessoaFisica.put("nome", "Nome Updated");

        Map<String, Object> funcionarioToUpdate = new HashMap<>();
        funcionarioToUpdate.put("raca", "Raça Updated");
        funcionarioToUpdate.put("matricula", funcionario.getMatricula());
        funcionarioToUpdate.put("nivel", funcionario.getNivel());
        funcionarioToUpdate.put("pessoaFisica", pessoaFisica);

        mockMvc.perform(MockMvcRequestBuilders.patch("/funcionario/"+funcionario.getIdFuncionario())
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(new ObjectMapper().writer().writeValueAsString(funcionarioToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateMissingPessoaFisicaTest() throws Exception {

        Map<String, Object> funcionarioToUpdate = new HashMap<>();
        funcionarioToUpdate.put("raca", "Raça Updated");
        funcionarioToUpdate.put("matricula", funcionario.getMatricula());
        funcionarioToUpdate.put("nivel", funcionario.getNivel());

        mockMvc.perform(MockMvcRequestBuilders.patch("/funcionario/"+funcionario.getIdFuncionario())
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(new ObjectMapper().writer().writeValueAsString(funcionarioToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void findByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/funcionario/"+funcionario.getIdFuncionario())
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void findByIdEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/funcionario/0")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void findByIdNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/funcionario/100")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void findByCpfTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/funcionario/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("cpf","12121212")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
