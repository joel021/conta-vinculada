package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.SecaoJudiciariaRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.UnidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UnidadeServiceTests {

    @MockBean
    private UnidadeRepository unidadeRepository;

    @MockBean
    private SecaoJudiciariaRepository secaoJudiciariaRepository;

    @Autowired
    private UnidadeService unidadeService;

    @BeforeEach
    public void setup() {

        Unidade unidade = new Unidade(1, null, "SJBA",
                null, new SecaoJudiciaria("0000000000000","Seção Judiciária da Bahia", "SJBA"));

        when(unidadeRepository.save(any(Unidade.class))).thenReturn(unidade);
        when(unidadeRepository.findBySiglaUnidade("SJBA")).thenReturn(Collections.singletonList(unidade));
        when(unidadeRepository.findBySiglaUnidade(any())).thenReturn(new ArrayList<>());

        SecaoJudiciaria secaoJudiciaria =  new SecaoJudiciaria("0000000002000","Seção Judiciária Qualquer", "SJQQ");
        when(secaoJudiciariaRepository.findBySigla("SJQQ")).thenReturn(Collections.singletonList(secaoJudiciaria));
        when(secaoJudiciariaRepository.save(secaoJudiciaria)).thenReturn(secaoJudiciaria);
    }

    @Test
    public void findBySiglaUnidadeOrCreateTest() {

        Unidade unidade = unidadeService.findBySiglaUnidadeOrCreate("SJBA");
        assertNotNull(unidade.getSecaoJudiciaria());
    }

    @Test
    public void findBySiglaUnidadeOrCreateNotExistTest() {

        Unidade unidade = unidadeService.findBySiglaUnidadeOrCreate("SJBASS");
        assertNotNull(unidade);
    }

    @Test
    public void findBySiglaUnidadeOrCreateNotExistsButExistsSecaoTest() {

        Unidade unidade = unidadeService.findBySiglaUnidadeOrCreate("SJQQ");
        assertNotNull(unidade.getSecaoJudiciaria().getCnpjSecao());
    }

}
