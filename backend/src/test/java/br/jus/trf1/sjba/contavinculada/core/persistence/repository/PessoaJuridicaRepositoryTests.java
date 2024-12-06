package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class PessoaJuridicaRepositoryTests {


    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @BeforeEach
    public void setup() {

        for(int i = 0; i < 5; i++) {
            PessoaJuridica pessoaJuridica = new PessoaJuridica("0000000003434"+i);
            pessoaJuridica.setNome("Name Created "+i);
            pessoaJuridicaRepository.save(pessoaJuridica);
        }
    }

    @Test
    public void findByNomeContainsTest() {

        List<PessoaJuridica> pessoaList = pessoaJuridicaRepository.findByNomeContains(Sort.by("nome").ascending(), "Created");
        assertFalse(pessoaList.isEmpty());
    }
}
