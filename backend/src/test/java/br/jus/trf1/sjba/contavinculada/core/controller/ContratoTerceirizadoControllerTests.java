package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoTerceirizadoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.FuncionarioRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaFisicaService;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContratoTerceirizadoControllerTests extends ControllerTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoTerceirizadoRepository contratoTerceirizadoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    private Contrato contratoExitent;

    private ContratoTerceirizado contratoTerceirizado;

    private Funcionario funcionario;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup() {

        Usuario adminUsuario = userService.save(new Usuario(2, "userAdmin", null, null,
                null, true, List.of(Papel.ROLE_ADMIN), "JFBA"));
        userAdmin = userService.allowAccess(adminUsuario);

        Usuario usuario = userService.save(new Usuario(1, "userNonAdmin",
                null, null, null, true, List.of(Papel.ROLE_GUEST), "JFBA"));
        userNonAdmin = userService.allowAccess(usuario);

        PessoaFisica pessoaFisica = pessoaFisicaService.saveIfNotExists(new PessoaFisica("00930300000"));
        Calendar today = Calendar.getInstance();
        funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(pessoaFisica)
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I).criadoEm(today)
                .criadoPor(usuario).racaCor("Pardo")
                .build();

        contratoExitent = new Contrato(6238262, "SJBA", null,
                null, LocalDate.now(), null,null, "089ws3829", null);
        Optional<Contrato> contratoOptional = contratoRepository.findByNumero(contratoExitent.getNumero());
        contratoExitent = contratoOptional.orElseGet(() -> contratoRepository.save(contratoExitent));

        contratoTerceirizado = new ContratoTerceirizado(8731, funcionario, contratoExitent, "Cargo",
                123.45333f, 40, LocalDate.now(), null, Calendar.getInstance(),
                null, null, null, null);

        funcionarioRepository.save(funcionario);
        contratoRepository.save(contratoExitent);
        contratoTerceirizadoRepository.save(contratoTerceirizado);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getFuncionarioByIdContratoTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato_terceirizado/contrato/"+contratoExitent.getIdContrato())
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("page", "0")
                        .param("quantity", "10")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void getFuncionarioByIdContratoNotExistentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato_terceirizado/contrato/9909")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("page", "0")
                        .param("quantity", "10")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void accessForbiddenTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato_terceirizado/contrato/9909")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void registerNewContratoNewFuncionarioTest() throws Exception {

        PessoaFisica pessoaFisica = PessoaFisica.builder()
                .cpf("08989898989")
                .build();

        Funcionario funcionario = Funcionario.builder()
                .matricula("ba0897979879898989")
                .pessoaFisica(pessoaFisica)
                .nivel(NivelEnsino.M)
                .isOficialJustica(false)
                .build();

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .contrato(contratoExitent)
                .funcionario(funcionario)
                .cargo("Cargo")
                .remuneracao(4500)
                .cargaHoraria(40)
                .dataInicio(LocalDate.now())
                .lotacao(Lotacao.builder().descricao("SALVADOR").build())
                .build();

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                        .content(objectMapper.writeValueAsString(contratoTerceirizado))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        Map<String, Object> responseObject = new ObjectMapper().reader().readValue(response, Map.class);
        assertNotNull(responseObject.get("id"));
    }

    @Test
    public void registerNewContratoExistentTest() throws Exception {

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .contrato(contratoExitent)
                .funcionario(funcionario)
                .cargo("Cargo")
                .remuneracao(6000)
                .cargaHoraria(40)
                .dataInicio(LocalDate.of(2022,3,3))
                .lotacao(Lotacao.builder().descricao("SALVADOR").build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizado))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Two roles with the same start date. Should refuse the register.")
    public void violateUniqueConstraintTest() throws Exception {

        ContratoTerceirizado contratoTerceirizadoCreated = ContratoTerceirizado.builder()
                .contrato(contratoExitent)
                .funcionario(funcionario)
                .cargo("Cargo")
                .remuneracao(6000)
                .cargaHoraria(40)
                .dataInicio(contratoTerceirizado.getDataInicio())
                .lotacao(Lotacao.builder().descricao("SALVADOR").build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizadoCreated))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void registerNewContratoExistentForbiddenTest() throws Exception {

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .contrato(contratoExitent)
                .funcionario(funcionario)
                .cargo("Cargo")
                .remuneracao(6000)
                .cargaHoraria(40)
                .dataInicio(LocalDate.now())
                .lotacao(Lotacao.builder().descricao("SALVADOR").build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizado))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }


    @Test
    public void registerNewContratoTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .contrato(contratoExitent)
                .funcionario(funcionario)
                .cargo("Cargo")
                .remuneracao(6000)
                .cargaHoraria(40)
                .dataInicio(LocalDate.of(2021,4,4))
                .lotacao(Lotacao.builder().descricao("SALVADOR").build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizado))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void addAdictiveTermTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);

        Map<String, Object> contratoTerceirizadoToUpdate = new HashMap<>();
        contratoTerceirizadoToUpdate.put("id", contratoTerceirizado.getId());
        contratoTerceirizadoToUpdate.put("contrato", contratoExitent);
        contratoTerceirizadoToUpdate.put("funcionario", funcionario);
        contratoTerceirizadoToUpdate.put("cargo", "New Cargo");
        contratoTerceirizadoToUpdate.put("remuneracao", 10000);
        contratoTerceirizadoToUpdate.put("cargaHoraria", 35);
        contratoTerceirizadoToUpdate.put("dataInicio", "2021-01-23");
        contratoTerceirizadoToUpdate.put("lotacao", Lotacao.builder().descricao("CAMAÇARI").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizadoToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void updateWithoutRemunarationTest() throws Exception {

        Map<String, Object> contratoTerceirizadoToUpdate = new HashMap<>();
        contratoTerceirizadoToUpdate.put("id", contratoTerceirizado.getId());
        contratoTerceirizadoToUpdate.put("contrato", contratoExitent);
        contratoTerceirizadoToUpdate.put("funcionario", funcionario);
        contratoTerceirizadoToUpdate.put("cargo", "New Cargo");
        contratoTerceirizadoToUpdate.put("cargaHoraria", 35);
        contratoTerceirizadoToUpdate.put("dataInicio", "2021-01-01");
        contratoTerceirizadoToUpdate.put("lotacao", Lotacao.builder().descricao("CAMAÇARI").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizadoToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateRemunarationZeroTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);

        Map<String, Object> contratoTerceirizadoToUpdate = new HashMap<>();
        contratoTerceirizadoToUpdate.put("id", contratoTerceirizado.getId());
        contratoTerceirizadoToUpdate.put("contrato", contratoExitent);
        contratoTerceirizadoToUpdate.put("funcionario", funcionario);
        contratoTerceirizadoToUpdate.put("cargo", "New Cargo");
        contratoTerceirizadoToUpdate.put("remuneracao", 0);
        contratoTerceirizadoToUpdate.put("cargaHoraria", 35);
        contratoTerceirizadoToUpdate.put("dataInicio", "2021-01-01");
        contratoTerceirizadoToUpdate.put("lotacao", Lotacao.builder().descricao("CAMAÇARI").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/contrato_terceirizado/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(contratoTerceirizadoToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void deleteTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/contrato_terceirizado/"+contratoTerceirizado.getId())
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        ContratoTerceirizado contratoTerceirizadoDeleted = contratoTerceirizadoRepository.findById(contratoTerceirizado.getId()).get();
        assertEquals(userAdmin.getUsuario(), contratoTerceirizadoDeleted.getDeletadoPor().getUsuario());
    }

    @Test
    public void findByUnidadeTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato_terceirizado/unidade/"+contratoTerceirizado.getContrato().getUnidade())
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void setDataDesligamentoTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/contrato_terceirizado/"+
                        contratoTerceirizado.getFuncionario().getIdFuncionario()+"/"
                        +contratoExitent.getIdContrato())
                .header("Authorization", "Bearer " + userAdmin.getToken())
                        .param("dataDesligamento", "2021-01-01")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        ContratoTerceirizado contratoTerceirizadoUnactive = contratoTerceirizadoRepository.findById(contratoTerceirizado.getId()).get();
        assertNotNull(contratoTerceirizadoUnactive.getDataDesligamento());
    }

}
