package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaFisicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PessoaFisicaServiceTests {

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @MockBean
    private PessoaFisicaRepository pessoaFisicaRepository;

    private PessoaFisica pessoaFisica;

    @BeforeEach
    public void setup() {
        pessoaFisica = new PessoaFisica();
        pessoaFisica.setIdPessoa(1);
        pessoaFisica.setNome("Fulano de Tal");
        pessoaFisica.setCpf("99899878909");
        when(pessoaFisicaRepository.findByNomeContainsLimited(anyString())).thenReturn(List.of(pessoaFisica));
    }

    @Test
    public void findByNomeContainingTest() {

        assertFalse(pessoaFisicaService.findByNomeContaining("de Tal").isEmpty());
    }

    @Test
    public void findByNomeContainingEmptyTest() {

        assertTrue(pessoaFisicaService.findByNomeContaining("").isEmpty());
    }

    @Test
    public void findByNomeContainingSpaceTest() {

        assertTrue(pessoaFisicaService.findByNomeContaining(" ").isEmpty());
    }

    @Test
    public void findByNomeContainingNullTest() {

        assertTrue(pessoaFisicaService.findByNomeContaining(null).isEmpty());
    }
}
