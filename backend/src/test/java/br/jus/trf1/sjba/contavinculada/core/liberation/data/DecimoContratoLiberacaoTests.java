package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.DecimoTerceiroPeriodProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecimoContratoLiberacaoTests {

    @Test
    public void totalProvisionTest() {

        Provision provision = new Provision(0.34);

        Liberacao liberacao = new Liberacao();
        liberacao.setDataLiberacao(LocalDate.of(2022,1,1));

        DecimoTerceiroPeriodProvision decimoTerceiroPeriodProvision = new DecimoTerceiroPeriodProvision(List.of(liberacao));
        decimoTerceiroPeriodProvision.setWorkPeriod(new WorkPeriod(LocalDate.of(2021,1,1), LocalDate.now()));
        decimoTerceiroPeriodProvision.addProvision(LocalDate.of(2022,2,1), provision);

        DecimoContratoLiberacao decimoContratoLiberacao = new DecimoContratoLiberacao();
        decimoContratoLiberacao.funcionarioProvisions(List.of(decimoTerceiroPeriodProvision));

        assertEquals(provision.getTotalProvisaoMensal(), decimoContratoLiberacao.getTotalProvision());
    }

    @Test
    public void totalLiberationTest() {

        Provision provision = new Provision(0.34);

        Liberacao liberacao = new Liberacao();
        liberacao.setDataLiberacao(LocalDate.of(2022,1,1));

        DecimoTerceiroPeriodProvision decimoTerceiroPeriodProvision = new DecimoTerceiroPeriodProvision(List.of(liberacao));
        decimoTerceiroPeriodProvision.setWorkPeriod(new WorkPeriod(LocalDate.of(2021,1,1),  LocalDate.now()));
        decimoTerceiroPeriodProvision.addProvision(LocalDate.of(2022,2,1), provision);

        DecimoContratoLiberacao decimoContratoLiberacao = new DecimoContratoLiberacao();
        decimoContratoLiberacao.funcionarioProvisions(List.of(decimoTerceiroPeriodProvision));

        assertEquals(provision.getTotalProvisaoMensal(), decimoContratoLiberacao.getTotalLiberation());
    }

    @Test
    public void totalNotLiberationTest() {

        Provision provision = new Provision(0.34);

        Liberacao liberacao = new Liberacao();
        liberacao.setDataLiberacao(LocalDate.of(2022,1,1));

        DecimoTerceiroPeriodProvision decimoTerceiroPeriodProvision = new DecimoTerceiroPeriodProvision(List.of(liberacao));
        decimoTerceiroPeriodProvision.setWorkPeriod(new WorkPeriod(LocalDate.of(2021,1,1), LocalDate.now()));
        decimoTerceiroPeriodProvision.addProvision(LocalDate.of(2022,2,1), provision);

        DecimoContratoLiberacao decimoContratoLiberacao = new DecimoContratoLiberacao();
        decimoContratoLiberacao.funcionarioProvisions(List.of(decimoTerceiroPeriodProvision));

        assertEquals(0, decimoContratoLiberacao.getTotalLiberation());
    }
}
