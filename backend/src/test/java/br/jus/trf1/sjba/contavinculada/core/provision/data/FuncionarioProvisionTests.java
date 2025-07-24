package br.jus.trf1.sjba.contavinculada.core.provision.data;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuncionarioProvisionTests {

    @Test
    public void setDataLiberacaoTest() {

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.34")
        );
        provision.setRemuneracao(new BigDecimal("1000"));

        FuncionarioProvision funcionarioProvision = new FuncionarioProvision();
        funcionarioProvision.addProvision(provision);
        funcionarioProvision.setDataLiberacao(LocalDate.now());

        assertEquals(provision.getTotalProvisaoMensal(), funcionarioProvision.getTotalLiberation());
    }

    @Test
    public void setDataLiberacaoNoLiberationTest() {

        Provision provision =new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.34")
        );
        provision.setRemuneracao(new BigDecimal("1000"));

        FuncionarioProvision funcionarioProvision = new FuncionarioProvision();
        funcionarioProvision.addProvision(provision);
        funcionarioProvision.setDataLiberacao(null);

        assertEquals(new BigDecimal("0"), funcionarioProvision.getTotalLiberation());
    }
}
