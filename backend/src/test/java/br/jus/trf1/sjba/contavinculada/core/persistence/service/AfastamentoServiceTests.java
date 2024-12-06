package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Afastamento;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.AfastamentoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AfastamentoServiceTests {

    @Autowired
    private AfastamentoService afastamentoService;

    @MockBean
    private AfastamentoRepository afastamentoRepository;

    @MockBean
    private ContratoTerceirizadoService contratoTerceirizadoService;

    private LocalDate dataInicio;

    private Afastamento afastamento;

    String unidade = "SJBA";

    @BeforeEach
    public void setup() throws NotFoundException {

        Contrato contrato = Contrato.builder().idContrato(1).numero("09303930").build();

        Funcionario funcionario = Funcionario.builder().idFuncionario(1).matricula("ba0000000000").build();

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder().id(1).funcionario(funcionario)
                .contrato(contrato).build();

        afastamento = Afastamento.builder().idAfastamento(1).substituido(contratoTerceirizado)
                .substituto(contratoTerceirizado)
                .build();

        dataInicio = LocalDate.now();
        when(afastamentoRepository.findBySubstituidoAndDataInicio(ContratoTerceirizado.builder().id(1).build(),
                dataInicio)).thenReturn(List.of(afastamento));
        when(contratoTerceirizadoService.findContratoTerceirizado(funcionario.getMatricula(), contrato.getNumero(), unidade))
                .thenReturn(contratoTerceirizado);
        when(afastamentoRepository.save(afastamento)).thenReturn(afastamento);
    }

    @Test
    public void findBySubstituidoAndDataInicioTest() {

        final var afastamentos = afastamentoService.findBySubstituidoAndDataInicio(1, dataInicio);
        assertFalse(afastamentos.isEmpty());
    }

    @Test
    public void saveIfNotExistsNotExistsTest() throws NotFoundException, NotAcceptableException {

        final var afastamentoExistent = afastamentoService.saveIfNotExists(afastamento, unidade);
        assertEquals(afastamento.getIdAfastamento(), afastamentoExistent.getIdAfastamento());
    }

    @Test
    public void saveIfNotExistsExistsTest() throws NotFoundException, NotAcceptableException {

        final var afastamentoNotExistent = afastamentoService.saveIfNotExists(afastamento, unidade);
        assertEquals(afastamento.getIdAfastamento(), afastamentoNotExistent.getIdAfastamento());
    }
}
