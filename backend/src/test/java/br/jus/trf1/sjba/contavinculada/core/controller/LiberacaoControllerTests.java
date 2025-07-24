package br.jus.trf1.sjba.contavinculada.core.controller;


import br.jus.trf1.sjba.contavinculada.core.dto.LiberacaoDTO;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.ContratoTerceirizadoService;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
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

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LiberacaoControllerTests extends ControllerTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private IncGrupoAContratoRepository incGrupoAContratoRepository;

    @Autowired
    private LiberacaoRepository liberacaoRepository;

    @Autowired
    private OficioMovimentacaoRepository oficioMovimentacaoRepository;

    private ContratoTerceirizado contratoTerceirizado;

    private Contrato contrato;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws NotFoundException, NotAcceptableException {


        Usuario usuario = userService.save(new Usuario(null, "userNonAdmin",
                null, null, null, true, List.of(Papel.ROLE_GUEST),"JFBA"));
        userNonAdmin = userService.allowAccess(usuario);

        Usuario usuarioAdmin = userService.save(new Usuario(null, "userAdmin",
                null, null, null, true, List.of(Papel.ROLE_ADMIN),"JFBA"));
        userAdmin = userService.allowAccess(usuarioAdmin);

        Calendar today = Calendar.getInstance();
        Funcionario funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(new PessoaFisica("00000000000"))
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I).criadoEm(today)
                .criadoPor(usuario).racaCor("Pardo")
                .build();

        LocalDate inicioContrato = LocalDate.of(2021, 11, 8);

        contrato = new Contrato(0, "SJBA", null,null, inicioContrato,
                inicioContrato, inicioContrato,"80976993", null);
        Optional<Contrato> contratoOptional = contratoRepository.findByNumero(contrato.getNumero());
        contrato = contratoOptional.orElseGet(() -> contratoRepository.save(contrato));

        incGrupoAContratoRepository.save(IncGrupoAContrato.builder()
                .data(inicioContrato)
                .contrato(contrato)
                .incGrupoA(new BigDecimal("35.8"))
                .build());

        Lotacao lotacao = Lotacao.builder()
                .descricao("SALVADOR")
                .build();
        contratoTerceirizado = new ContratoTerceirizado(18765, funcionario, contrato, "34f", new BigDecimal("6600"),
                40, inicioContrato, null, Calendar.getInstance(), null, null, lotacao, null);

        contratoTerceirizado = contratoTerceirizadoService.saveIfNotExists(contratoTerceirizado, "SJBA");

        OficioMovimentacao oficioMovimentacao = new OficioMovimentacao(1,1,2022);
        oficioMovimentacaoRepository.save(oficioMovimentacao);

        liberacaoRepository.save(LiberacaoDTO.builder()
                        .tipo(TipoLiberacao.DECIMO_TERCEIRO)
                        .oficioMovimentacao(oficioMovimentacao)
                        .criadoEm(Calendar.getInstance())
                        .contratoTerceirizado(contratoTerceirizado)
                        .dataLiberacao(LocalDate.of(2021, 12, 31))
                .build());

        liberacaoRepository.save(LiberacaoDTO.builder()
                .tipo(TipoLiberacao.FERIAS)
                .oficioMovimentacao(oficioMovimentacao)
                .criadoEm(Calendar.getInstance())
                .contratoTerceirizado(contratoTerceirizado)
                .dataLiberacao(LocalDate.of(2021, 12, 31))
                .build());

        liberacaoRepository.save(LiberacaoDTO.builder()
                .tipo(TipoLiberacao.FGTS)
                .oficioMovimentacao(oficioMovimentacao)
                .criadoEm(Calendar.getInstance())
                .contratoTerceirizado(contratoTerceirizado)
                .dataLiberacao(LocalDate.of(2021, 12, 31))
                .build());

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void saveTest() throws Exception {

        LiberacaoDTO liberacaoDTO = new LiberacaoDTO();
        liberacaoDTO.setContratoTerceirizado(contratoTerceirizado);
        liberacaoDTO.setTipo(TipoLiberacao.FGTS);
        liberacaoDTO.setOficioMovimentacao(new OficioMovimentacao(1,1,2023));

        mockMvc.perform(MockMvcRequestBuilders.post("/liberacao/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(liberacaoDTO)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void saveSetDataDesligamentoTest() throws Exception {

        LiberacaoDTO liberacaoDTO = new LiberacaoDTO();
        liberacaoDTO.setContratoTerceirizado(contratoTerceirizado);
        liberacaoDTO.setTipo(TipoLiberacao.FGTS);
        liberacaoDTO.setDataDesligamento(LocalDate.of(2022,1,1));
        liberacaoDTO.setOficioMovimentacao(new OficioMovimentacao(1,1,2023));

        mockMvc.perform(MockMvcRequestBuilders.post("/liberacao/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(liberacaoDTO))
                .contentType(MediaType.APPLICATION_JSON));

        ContratoTerceirizado contratoTerceirizadoUpdated = contratoTerceirizadoService.findById(contratoTerceirizado.getId());
        assertEquals(LocalDate.of(2022,1,1), contratoTerceirizadoUpdated.getDataDesligamento());
    }

    @Test
    public void saveForbiddenTest() throws Exception {

        LiberacaoDTO liberacaoDTO = new LiberacaoDTO();
        liberacaoDTO.setContratoTerceirizado(contratoTerceirizado);
        liberacaoDTO.setTipo(TipoLiberacao.FGTS);

        mockMvc.perform(MockMvcRequestBuilders.post("/liberacao/")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .content(objectMapper.writeValueAsString(liberacaoDTO)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void saveByJsonTest() throws Exception {

        HashMap<String, Integer> oficioMovimentacao = new HashMap<>();
        oficioMovimentacao.put("docSei", 1);
        oficioMovimentacao.put("numeroOficio", 1);
        oficioMovimentacao.put("anoOficio", 2023);

        HashMap<String, Object> contratoTerceirizadoMap = new HashMap<>();
        contratoTerceirizadoMap.put("id", contratoTerceirizado.getId());

        HashMap<String, Object> liberacaoDTOMap = new HashMap<>();
        liberacaoDTOMap.put("contratoTerceirizado", contratoTerceirizadoMap);
        liberacaoDTOMap.put("tipo", "FGTS");
        liberacaoDTOMap.put("oficioMovimentacao", oficioMovimentacao);

        String resultString = mockMvc.perform(MockMvcRequestBuilders.post("/liberacao/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .param("dataDesligamento", "2022-01-21")
                .content(objectMapper.writeValueAsString(liberacaoDTOMap))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> resultMap = new ObjectMapper().reader().readValue(resultString, Map.class);
        assertNotNull(resultMap.get("dataLiberacao"));
    }

    @Test
    public void saveWhenContratoDoesNotExistsTest() throws Exception {

        LiberacaoDTO liberacaoDTO = new LiberacaoDTO();
        ContratoTerceirizado contratoTerceirizado2 = new ContratoTerceirizado();
        contratoTerceirizado2.setId(293322);
        liberacaoDTO.setContratoTerceirizado(contratoTerceirizado2);
        liberacaoDTO.setTipo(TipoLiberacao.FGTS);
        liberacaoDTO.setOficioMovimentacao(new OficioMovimentacao(1,1,2023));

        mockMvc.perform(MockMvcRequestBuilders.post("/liberacao/")
                .header("Authorization", "Bearer " + userAdmin.getToken())
                .content(objectMapper.writeValueAsString(liberacaoDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void saveWhenContratoForbiddenTest() throws Exception {

        LiberacaoDTO liberacaoDTO = new LiberacaoDTO();
        ContratoTerceirizado contratoTerceirizado2 = new ContratoTerceirizado();
        contratoTerceirizado2.setId(293322);
        liberacaoDTO.setContratoTerceirizado(contratoTerceirizado2);
        liberacaoDTO.setTipo(TipoLiberacao.FGTS);

        mockMvc.perform(MockMvcRequestBuilders.post("/liberacao/")
                .content(objectMapper.writeValueAsString(liberacaoDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void findDecimoTerceiroByIdContratoAndFechamentoTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.DECIMO_TERCEIRO+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void findDecimoTerceiroByIdContratoAndNotFechamentoTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.DECIMO_TERCEIRO+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotAcceptable());
    }

    @Test
    public void findFeriasByIdContratoAndNotFechamentoTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FERIAS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotAcceptable());
    }

    @Test
    public void findFeriasByIdContratoAndFechamentoTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FERIAS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void findFGTSByIdContratoAndFechamentoTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FGTS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }


    @Test
    @DisplayName("Check total provision for the employee")
    public void findFGTSByIdContratoAndFechamentoTotalProvisionTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        String resultJsonString = mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FGTS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> fgtsContratoLiberacao = objectMapper.reader().readValue(resultJsonString, Map.class);
        assertTrue((double) fgtsContratoLiberacao.get("totalProvision") > 0);
    }

    @Test
    @DisplayName("Check total liberation of FGTS for the employee")
    public void findFGTSByIdContratoAndFechamentoTotalLiberationTest() throws Exception {

        String resultJsonString = mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FGTS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", LocalDate.now().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> fgtsContratoLiberacao = objectMapper.reader().readValue(resultJsonString, Map.class);
        assertTrue((double) fgtsContratoLiberacao.get("totalLiberation") > 0);
    }

    @Test
    @DisplayName("Check total provision of Ferias for the employee")
    public void findFeriasByIdContratoAndFechamentoProvisionTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        String resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FERIAS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> fgtsContratoLiberacao = objectMapper.reader().readValue(resultJson, Map.class);
        assertTrue((double) fgtsContratoLiberacao.get("totalProvision") > 0);
    }

    @Test
    @DisplayName("Check total liberation of Ferias for the employee")
    public void findFeriasByIdContratoAndFechamentoLiberationTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        String resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.FERIAS+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> fgtsContratoLiberacao = objectMapper.reader().readValue(resultJson, Map.class);
        assertEquals((double) fgtsContratoLiberacao.get("totalProvision"), (double) fgtsContratoLiberacao.get("totalLiberation"));
    }

    @Test
    @DisplayName("Check total liberation of Decimo for the employee")
    public void findDecimoByIdContratoAndFechamentoLiberationTest() throws Exception {

        String resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.DECIMO_TERCEIRO+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", LocalDate.of(2021,12,31).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> fgtsContratoLiberacao = objectMapper.reader().readValue(resultJson, Map.class);
        assertEquals((double) fgtsContratoLiberacao.get("totalProvision"), (double) fgtsContratoLiberacao.get("totalLiberation"));
    }

    @Test
    @DisplayName("Check total provision of Decimo for the employee")
    public void findDecimoByIdContratoAndFechamentoProvisionTest() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 31);

        String resultJson = mockMvc.perform(MockMvcRequestBuilders.get("/liberacao/"+TipoLiberacao.DECIMO_TERCEIRO+"/"+contrato.getIdContrato())
                        .header("Authorization", "Bearer " + userNonAdmin.getToken())
                        .param("fechamento", new Date(calendar.getTime().getTime()).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Map<String, Object> fgtsContratoLiberacao = objectMapper.reader().readValue(resultJson, Map.class);
        assertTrue((double) fgtsContratoLiberacao.get("totalProvision") > 0);
    }
}
