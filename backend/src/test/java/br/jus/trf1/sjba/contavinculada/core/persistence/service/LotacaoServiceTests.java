package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Lotacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.LotacaoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LotacaoServiceTests {

    @Autowired
    private LotacaoService lotacaoService;

    @MockBean
    private LotacaoRepository lotacaoRepository;

    private Lotacao lotacao;

    private Lotacao newLotacao;

    @BeforeEach
    public void setup() {

        lotacao = Lotacao.builder()
                .descricao("SALVADOR")
                .idLotacao(1)
                .build();

        newLotacao = Lotacao.builder().descricao("SAO PAULO").build();

        when(lotacaoRepository.findById(1)).thenReturn(Optional.of(lotacao));
        when(lotacaoRepository.findByDescricao(lotacao.getDescricao())).thenReturn(List.of(lotacao));
        when(lotacaoRepository.save(newLotacao)).thenReturn(
                Lotacao.builder()
                        .idLotacao(2)
                        .descricao(newLotacao.getDescricao())
                        .build()
        );
    }

    @Test
    public void findByIdNullTest() {

        assertThrows(NotFoundException.class, () -> {
            lotacaoService.findById(null);
        });
    }

    @Test
    public void findByIdNotExistentTest() {

        assertThrows(NotFoundException.class, () -> {
            lotacaoService.findById(8987989);
        });
    }

    @Test
    public void findByIdTest() throws NotFoundException {

        Lotacao lotacaoFound = lotacaoService.findById(1);
        assertEquals(lotacao.getDescricao(), lotacaoFound.getDescricao());
    }

    @Test
    public void saveIfNotExistsExistentDescricaoTest() {

        Lotacao lotacaoExistent = Lotacao.builder().descricao(lotacao.getDescricao()).build();
        Lotacao lotacaoFound = lotacaoService.saveIfNotExists(lotacaoExistent);
        assertEquals(lotacao.getIdLotacao(), lotacaoFound.getIdLotacao());
    }

    @Test
    public void saveIfNotExistsExistentIdTest() {

        Lotacao lotacaoExistent = Lotacao.builder().idLotacao(lotacao.getIdLotacao()).build();
        Lotacao lotacaoFound = lotacaoService.saveIfNotExists(lotacaoExistent);
        assertEquals(lotacao.getDescricao(), lotacaoFound.getDescricao());
    }

    @Test
    public void saveIfNotExistsNotExistentTest() {

        Lotacao lotacaoToSave = Lotacao.builder()
                .descricao(newLotacao.getDescricao())
                .build();
        Lotacao saved = lotacaoService.saveIfNotExists(lotacaoToSave);
        assertEquals(2, saved.getIdLotacao());
    }
}
