package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.provision.data.FGTSProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FGTSContratoLiberacaoTests {

    @Test
    @DisplayName("Check total provisions. Should be 0.")
    public void funcionarioProvisionsTest() {

        FGTSContratoLiberacao fgtsContratoLiberacao = new FGTSContratoLiberacao()
                .funcionarioProvisions(new ArrayList<>());
        fgtsContratoLiberacao.setFuncionarioProvisions(new ArrayList<>());
        assertEquals(0, fgtsContratoLiberacao.getTotalProvision());
    }

    @Test
    @DisplayName("Check total provisions. Should be the total of provisions.")
    public void funcionarioProvisionsNonEmptyTest() {

        Provision provision = new Provision(0.2);
        provision.setRemuneracao(12000);

        FGTSProvision fgtsProvision = new FGTSProvision();
        fgtsProvision.addProvision(provision);

        FGTSContratoLiberacao fgtsContratoLiberacao = new FGTSContratoLiberacao()
                .funcionarioProvisions(List.of(fgtsProvision));

        final double total = provision.getTotalProvisaoMensal();

        assertEquals(total, fgtsContratoLiberacao.getTotalProvision());
    }

    @Test
    @DisplayName("Check total of liberartions. Should be 0, since it is not liberated.")
    public void funcionarioProvisionsNotLiberatedTest() {

        Provision provision = new Provision(0.2);
        provision.setRemuneracao(12000);

        FGTSProvision fgtsProvision = new FGTSProvision();
        fgtsProvision.addProvision(provision);

        FGTSContratoLiberacao fgtsContratoLiberacao = new FGTSContratoLiberacao()
                .funcionarioProvisions(List.of(fgtsProvision));

        assertEquals(0, fgtsContratoLiberacao.getTotalLiberation());
    }

    @Test
    @DisplayName("Check total provisions. Should be the total of provisions.")
    public void funcionarioProvisionsLiberatedTest() {

        Provision provision = new Provision(0.2);
        provision.setRemuneracao(12000);

        FGTSProvision fgtsProvision = new FGTSProvision();
        fgtsProvision.addProvision(provision);
        fgtsProvision.setDataLiberacao(LocalDate.now());

        FGTSContratoLiberacao fgtsContratoLiberacao = new FGTSContratoLiberacao()
                .funcionarioProvisions(List.of(fgtsProvision));

        final double total = provision.getTotalProvisaoMensal();
        assert total > 0;
        assertEquals(total, fgtsContratoLiberacao.getTotalLiberation());
    }

}
