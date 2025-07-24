package br.jus.trf1.sjba.contavinculada.core.controller;


import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoTerceirizadoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.FuncionarioRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.IncGrupoAContratoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaFisicaService;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProvisaoControllerTests extends ControllerTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoTerceirizadoRepository contratoTerceirizadoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @Autowired
    private IncGrupoAContratoRepository incGrupoAContratoRepository;

    private Contrato contrato;

    private Contrato contratoWithoutIncGrupoA;

    private LocalDate date;

    @BeforeEach
    public void setup() {

        date = LocalDate.of(2024, 2, 1);

        Usuario usuario = userService.save(new Usuario(1, "userNonAdmin",
                null, null, null, true, List.of(Papel.ROLE_GUEST), "JFBA"));
        userNonAdmin = userService.allowAccess(usuario);

        PessoaFisica pessoaFisica = pessoaFisicaService.saveIfNotExists(new PessoaFisica("00930300000"));
        Calendar today = Calendar.getInstance();
        Funcionario funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(pessoaFisica)
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I).criadoEm(today)
                .criadoPor(usuario).racaCor("Pardo")
                .build();

        contrato = new Contrato(1, "JFBA", null,
                null, LocalDate.now(), null, date, "0893829",
                null);
        contratoWithoutIncGrupoA = new Contrato(2, "JFBA", null,
                null, LocalDate.now(), null, date, "0893823",
                null);
        IncGrupoAContrato incGrupoAContrato = new IncGrupoAContrato();
        incGrupoAContrato.setContrato(contrato);
        incGrupoAContrato.setData(LocalDate.now());
        incGrupoAContrato.setIncGrupoA(new BigDecimal("34.5"));

        ContratoTerceirizado contratoTerceirizado = new ContratoTerceirizado(1, funcionario, contrato, "Cargo",
                new BigDecimal("6.000"), 40, date, null, Calendar.getInstance(),
                null, null, null, null);

        funcionarioRepository.save(funcionario);
        contratoRepository.save(contrato);
        contratoRepository.save(contratoWithoutIncGrupoA);
        contratoTerceirizadoRepository.save(contratoTerceirizado);
        incGrupoAContratoRepository.save(incGrupoAContrato);

        for(int i = 0; i < 10; i++) {
            ContratoTerceirizado contratoTerceirizadoI = ContratoTerceirizado
                    .builder()
                    .contrato(contratoWithoutIncGrupoA)
                    .funcionario(funcionario)
                    .remuneracao(new BigDecimal("100.0").add(new BigDecimal(String.valueOf(i))))
                    .dataInicio(LocalDate.now())
                    .build();
            contratoTerceirizadoRepository.save(contratoTerceirizadoI);
        }
    }

    @Test
    public void getProvisoesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/provisoes/"+contrato.getIdContrato())
                        .param("date", date.toString())
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void getProvisoesWithoutIncGrupoATest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/provisoes/"+contratoWithoutIncGrupoA.getIdContrato())
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .param("date", date.toString())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void getProvisoesContratoNotExistentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/provisoes/300")
                .header("Authorization", "Bearer " + userNonAdmin.getToken())
                .param("date", date.toString())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
