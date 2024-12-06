package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecimoTerceiroPeriodProvisionTests {


    @Test
    public void filterToDecimoTest() {

        List<Liberacao> liberacaoList = List.of(Liberacao.builder().dataLiberacao(LocalDate.now()).build(),
                Liberacao.builder().dataLiberacao(LocalDate.now()).build());

        DecimoTerceiroPeriodProvision decimoTerceiroPeriodProvision = new DecimoTerceiroPeriodProvision(liberacaoList);
        Provision provision = new Provision(35.8/100.0d);
        provision.setRemuneracao(1999);
        final double expected = provision.getDecimo() + provision.getDecimo() * 35.8/100.0d;

        Provision provisionFiltered = decimoTerceiroPeriodProvision.filterToDecimo(provision);
        assertEquals(expected, provisionFiltered.getTotalProvisaoMensal());
    }
}
