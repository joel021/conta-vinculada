package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UnidadeRepositoryTests {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private SecaoJudiciariaRepository secaoJudiciariaRepository;
    private SecaoJudiciaria secaoJudiciaria;

    @BeforeEach
    public void setup() {

        secaoJudiciaria = new SecaoJudiciaria("9900909090909", "Seção Judiciária da Bahia", "SJBA");
        secaoJudiciariaRepository.save(secaoJudiciaria);

        Unidade unidade = new Unidade(1,"Setor de SERFI","SERFI",null, secaoJudiciaria);
        unidadeRepository.save(unidade);
    }

    @Test
    public void findTest() {

        assertEquals(secaoJudiciaria.getCnpjSecao(), unidadeRepository.findById(1).get().getSecaoJudiciaria().getCnpjSecao());
    }

}
