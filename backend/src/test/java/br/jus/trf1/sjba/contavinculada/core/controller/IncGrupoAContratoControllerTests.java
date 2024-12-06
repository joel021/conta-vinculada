package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaJuridicaRepository;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IncGrupoAContratoControllerTests extends ControllerTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    private Contrato contrato;
    private PessoaJuridica pessoaJuridica;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup() {

        userNonAdmin = userService.allowAccess(userService.save(new Usuario(null, "userNonAdmin",
                null, null, null, true, Set.of(Papel.ROLE_GUEST), "JFBA")));

        Usuario usuarioAdmin = userService.save(new Usuario(null, "userAdmin",
                null, null, null, true, Set.of(Papel.ROLE_ADMIN), "JFBA"));
        userAdmin = userService.allowAccess(usuarioAdmin);

        pessoaJuridica = new PessoaJuridica("994003003303");
        pessoaJuridica.setNome("Pessoa Juridica");
        LocalDate today = LocalDate.now();
        contrato = new Contrato(1, "SJBA", null, null, today,
                today, today, "893", null);

        pessoaJuridicaRepository.save(pessoaJuridica);
        contratoRepository.save(contrato);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void saveTest() throws Exception {

        Map<String, Object> incGrupoAContratoMap = new HashMap<>();
        incGrupoAContratoMap.put("incGrupoA", 0.12d);
        incGrupoAContratoMap.put("contrato", contrato);
        incGrupoAContratoMap.put("data", "2022-02-01");

        String contentString = mockMvc.perform(MockMvcRequestBuilders.post("/inc_grupo_a_contrato/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(incGrupoAContratoMap))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> contentMap = objectMapper.readValue(contentString, Map.class);
        assertEquals("2022-02-01", contentMap.get("data").toString().substring(0,10));
    }

    @Test
    public void saveForbiddenTest() throws Exception {

        Map<String, Object> incGrupoAContratoMap = new HashMap<>();
        incGrupoAContratoMap.put("incGrupoA", 0.12d);
        incGrupoAContratoMap.put("contrato", contrato);
        incGrupoAContratoMap.put("data", "2022-02-01");

        mockMvc.perform(MockMvcRequestBuilders.post("/inc_grupo_a_contrato/")
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .content(objectMapper.writeValueAsString(incGrupoAContratoMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Simulating passing json without all attribute definitions")
    public void saveNotProvidingSomeAttributesTest() throws Exception {

        HashMap<String, Object> incGrupoAContratoMap = new HashMap<>();
        incGrupoAContratoMap.put("incGrupoA", 0.12d);
        incGrupoAContratoMap.put("contrato", contrato);
        incGrupoAContratoMap.put("data", "2022-02-01");

        String resultString = mockMvc.perform(MockMvcRequestBuilders.post("/inc_grupo_a_contrato/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(incGrupoAContratoMap))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Map<String, Object> resultMap = objectMapper.reader().readValue(resultString, Map.class);

        assertNotNull(resultMap.get("criadoEm"));
    }

    @Test
    public void saveWithNull() throws Exception {

        IncGrupoAContrato incGrupoAContrato = new IncGrupoAContrato();
        incGrupoAContrato.setIncGrupoA(null);
        incGrupoAContrato.setContrato(contrato);
        incGrupoAContrato.setData(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/inc_grupo_a_contrato/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(incGrupoAContrato))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void saveWithoutContrato() throws Exception {

        IncGrupoAContrato incGrupoAContrato = new IncGrupoAContrato();
        incGrupoAContrato.setIncGrupoA(0.3);
        incGrupoAContrato.setContrato(null);
        incGrupoAContrato.setData(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/inc_grupo_a_contrato/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(incGrupoAContrato))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void saveWithoutContratoId() throws Exception {

        IncGrupoAContrato incGrupoAContrato = new IncGrupoAContrato();
        incGrupoAContrato.setIncGrupoA(0.3);
        incGrupoAContrato.setContrato(new Contrato());
        incGrupoAContrato.setData(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/inc_grupo_a_contrato/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(incGrupoAContrato))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
    }

    @Test
    public void findAllByIdContratoTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inc_grupo_a_contrato/contrato/"+contrato.getIdContrato())
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void findAllByIdContratoNotAllowedTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inc_grupo_a_contrato/contrato/"+1000)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }


}
