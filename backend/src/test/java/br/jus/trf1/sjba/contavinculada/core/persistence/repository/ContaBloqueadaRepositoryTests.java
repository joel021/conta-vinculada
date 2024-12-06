package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContaBloqueada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ContaBloqueadaRepositoryTests {

    @Autowired
    private ContaBloqueadaRepository contaBloqueadaRepository;

    @BeforeEach
    public void setup() {

        ContaBloqueada contaBloqueada = new ContaBloqueada(1, "3434", "323", "34342",
                new Date(), "24323", null);
        contaBloqueadaRepository.save(contaBloqueada);
    }

    @Test
    public void findTest() {
        assertTrue(contaBloqueadaRepository.findById(1).isPresent());
    }
}
