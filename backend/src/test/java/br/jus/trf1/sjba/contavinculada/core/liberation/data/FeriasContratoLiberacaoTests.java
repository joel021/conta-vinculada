package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.FeriasPeriodProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeriasContratoLiberacaoTests {

    @Test
    public void totalProvisionTest() {

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34"));
        provision.setDate(LocalDate.now());
        provision.setInicioVigencia(LocalDate.of(2021,1,1));

        Liberacao liberacao = new Liberacao();
        liberacao.setDataLiberacao(LocalDate.of(2022,1,1));

        FeriasPeriodProvision decimoTerceiroPeriodProvision = new FeriasPeriodProvision(List.of(liberacao));
        decimoTerceiroPeriodProvision.addProvision(LocalDate.of(2022,2,1), provision);

        FeriasContratoLiberacao contratoLiberacao = new FeriasContratoLiberacao();
        contratoLiberacao.feriasPeriodProvisionList(List.of(decimoTerceiroPeriodProvision));

        assertEquals(provision.getTotalProvisaoMensal(), contratoLiberacao.getTotalProvision());
    }

    @Test
    public void totalLiberationTest() {

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34"));
        provision.setDate(LocalDate.now());
        provision.setInicioVigencia(LocalDate.of(2021,1,1));
        Liberacao liberacao = new Liberacao();
        liberacao.setDataLiberacao(LocalDate.of(2022,1,1));

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberacao));
        feriasPeriodProvision.addProvision(LocalDate.of(2022,2,1), provision);

        FeriasContratoLiberacao contratoLiberacao = new FeriasContratoLiberacao();
        contratoLiberacao.feriasPeriodProvisionList(List.of(feriasPeriodProvision));

        assertEquals(provision.getTotalProvisaoMensal(), contratoLiberacao.getTotalLiberation());
    }

    @Test
    public void totalNotLiberationTest() {

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34"));
        provision.setDate(LocalDate.now());
        provision.setInicioVigencia(LocalDate.of(2021,1,1));

        Liberacao liberacao = new Liberacao();
        liberacao.setDataLiberacao(LocalDate.of(2022,1,1));

        FeriasPeriodProvision periodProvision = new FeriasPeriodProvision(List.of(liberacao));
        periodProvision.addProvision(LocalDate.of(2022,2,1), provision);

        FeriasContratoLiberacao contratoLiberacao = new FeriasContratoLiberacao();
        contratoLiberacao.feriasPeriodProvisionList(List.of(periodProvision));

        BigDecimal expected = BigDecimal.ZERO.setScale(8, RoundingMode.HALF_UP);
        BigDecimal actual = contratoLiberacao.getTotalLiberation().setScale(8, RoundingMode.HALF_UP);

        BigDecimal delta = new BigDecimal("0.0001");

        assertTrue(
                expected.subtract(actual).abs().compareTo(delta) < 1,
                "Values differ by more than 4 decimal places"
        );
    }
}
