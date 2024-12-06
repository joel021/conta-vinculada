package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Cidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CidadeReporitoryTests {

    @Autowired
    private CidadeRepository cidadeRepository;

    private Cidade cidade;

    @BeforeEach
    public void setup() {
        cidade = new Cidade(1, "Sapeaçu", "Bahia", "BA");
        cidadeRepository.save(cidade);
    }

    @Test
    public void findCidadeTest() {

        assertTrue(cidadeRepository.findById(1).isPresent());
    }
}
