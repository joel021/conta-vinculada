package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PessoaFisicaRepositoryTests {

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    private PessoaFisica pessoaFisica;

    @BeforeEach
    public void setup() {

        pessoaFisica = new PessoaFisica("00000000004");
        pessoaFisica.setNome("Pessoa Física");
        pessoaFisicaRepository.save(pessoaFisica);
    }
    @Test
    public void findTest() {

        assertTrue(pessoaFisicaRepository.findByCpf(pessoaFisica.getCpf()).isPresent());
    }

    @Test
    public void findByNomeContainsTest() {

        assertFalse(pessoaFisicaRepository.findByNomeContainsLimited("Física").isEmpty());
    }

    @Test
    public void findByNomeContainsNullTest() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited(null).isEmpty());
    }

    @Test
    public void findByNomeContainsEmptyTest() {
        assertFalse(pessoaFisicaRepository.findByNomeContainsLimited("").isEmpty());
    }

    @Test
    public void findByNomeContainsSpecialBTest() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited("\b").isEmpty());
    }

    @Test
    public void findByNomeContainsSpecial0Test() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited("\0").isEmpty());
    }

    @Test
    public void findByNomeContainsSpecialNTest() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited("\n").isEmpty());
    }

    @Test
    public void findByNomeContainsSpecialRTest() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited("\r").isEmpty());
    }

    @Test
    public void findByNomeContainsSpecialTTest() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited("\t").isEmpty());
    }

    @Test
    public void findByNomeContainsSpecialZTest() {
        assertTrue(pessoaFisicaRepository.findByNomeContainsLimited("\";delete * from funcionario").isEmpty());
    }
}
