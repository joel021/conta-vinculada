package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaJuridicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PessoaJuridicaServiceTests {

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void createTest() {

        PessoaJuridica pessoaJuridicaCreated = new PessoaJuridica("00000000000000");
        PessoaJuridica pessoaJuridicaSaved = pessoaJuridicaRepository.save(pessoaJuridicaCreated);

        assertEquals(pessoaJuridicaSaved.getCnpj(), pessoaJuridicaCreated.getCnpj());
    }

    @Test
    public void findPessoaJuricaTest() {

        PessoaJuridica pessoaJuridicaCreated = new PessoaJuridica("00000000000001");
        pessoaJuridicaRepository.save(pessoaJuridicaCreated);

        assertTrue(pessoaJuridicaRepository.findByCnpj(pessoaJuridicaCreated.getCnpj()).isPresent());
    }
}
