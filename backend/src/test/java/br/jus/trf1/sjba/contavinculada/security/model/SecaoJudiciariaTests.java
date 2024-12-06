package br.jus.trf1.sjba.contavinculada.security.model;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecaoJudiciariaTests {

    @Test
    public void setSiglaByDominioTest() {

        SecaoJudiciaria secaoJudiciaria = new SecaoJudiciaria();
        secaoJudiciaria.setSiglaByDominio("JFBA");
        assertEquals("SJBA", secaoJudiciaria.getSigla());
    }
}
