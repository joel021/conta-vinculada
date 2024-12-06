package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.LiberacaoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LiberacaoServiceTests {

    @MockBean
    private LiberacaoRepository liberacaoRepository;

    @MockBean
    private ContratoTerceirizadoService contratoTerceirizadoService;

    @Autowired
    private LiberacaoService liberacaoService;

    private Liberacao liberacao;

    private Liberacao fgtsLiberacao;

    ContratoTerceirizado contratoTerceirizadoUpdated;

    @BeforeEach
    public void setup() throws NotFoundException {

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(1);
        funcionario.setMatricula("matriculaFuncionario");

        Contrato contrato = Contrato.builder()
                .idContrato(1)
                .build();
        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .id(2)
                .funcionario(funcionario)
                .contrato(contrato)
                .build();
        contratoTerceirizadoUpdated = ContratoTerceirizado.builder()
                .id(2)
                .contrato(contrato)
                .dataDesligamento(LocalDate.of(2022, 1,1))
                .build();

        liberacao = Liberacao.builder()
                .idLiberacao(3)
                .contratoTerceirizado(contratoTerceirizado)
                .tipo(TipoLiberacao.DECIMO_TERCEIRO)
                .build();
        fgtsLiberacao = Liberacao.builder()
                .idLiberacao(4)
                .contratoTerceirizado(contratoTerceirizado)
                .tipo(TipoLiberacao.FGTS)
                .build();

        when(liberacaoRepository.findNonDeletedByContratoAndTipoLiberacao(
                contrato,
                TipoLiberacao.DECIMO_TERCEIRO)
        ).thenReturn(List.of(liberacao));

        when(liberacaoRepository.save(fgtsLiberacao)).thenReturn(fgtsLiberacao);
        when(contratoTerceirizadoService.findById(fgtsLiberacao.getContratoTerceirizado().getId()))
                .thenReturn(contratoTerceirizado);
    }

    @Test
    public void findByContratoIdAndTipoLiberacaoTest() {

        List<Liberacao> liberacaoList = liberacaoService.findByContratoIdAndTipoLiberacao(
                1,
                TipoLiberacao.DECIMO_TERCEIRO
        );

        assertFalse(liberacaoList.isEmpty());
    }

}
