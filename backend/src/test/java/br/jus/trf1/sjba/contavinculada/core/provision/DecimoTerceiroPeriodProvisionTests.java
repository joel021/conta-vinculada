package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecimoTerceiroPeriodProvisionTests {


    @Test
    public void filterToDecimoTest() {

        List<Liberacao> liberacaoList = List.of(Liberacao.builder().dataLiberacao(LocalDate.now()).build(),
                Liberacao.builder().dataLiberacao(LocalDate.now()).build());

        DecimoTerceiroPeriodProvision decimoTerceiroPeriodProvision = new DecimoTerceiroPeriodProvision(liberacaoList);
        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("1999"));
        final BigDecimal expected = provision.getDecimo().add(provision.getDecimo().multiply(new BigDecimal("0.358")));

        Provision provisionFiltered = decimoTerceiroPeriodProvision.filterToDecimo(provision);
        assertTrue(expected.compareTo(provisionFiltered.getTotalProvisaoMensal())==0);
    }
}
