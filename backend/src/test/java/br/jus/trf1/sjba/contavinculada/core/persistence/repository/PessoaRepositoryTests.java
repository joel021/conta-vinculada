package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Pessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PessoaRepositoryTests {

    @Autowired
    private PessoaRepository pessoaRepository;

    @BeforeEach
    public void setup() {

        for(int i = 0; i < 5; i++) {
            pessoaRepository.save(new Pessoa(null, "Pessoa Created "+i));
        }
    }

    @Test
    public void findByNomeContainsTest() {

        List<Pessoa> pessoaList = pessoaRepository.findByNomeContains(Sort.by("nome").ascending(), "Created");
        assertFalse(pessoaList.isEmpty());
    }
}
