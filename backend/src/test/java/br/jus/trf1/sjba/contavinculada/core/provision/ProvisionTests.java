package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProvisionTests {

    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @Test
    public void setRemuneracaoFeriasTest() {

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("800"));

        assertEquals(new BigDecimal("800").multiply(provision.getRATE_FERIAS()).setScale(SCALE, ROUNDING_MODE), provision.getFerias());
    }

    @Test
    public void setRemuneracaoAbFeriasTest() {

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("800"));

        assertEquals(new BigDecimal("800").multiply(provision.getRATE_AB_FERIAS()).setScale(SCALE, ROUNDING_MODE), provision.getAbFerias());
    }

    @Test
    public void setRemuneracaoDecimoTest() {

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("800"));

        assertEquals(new BigDecimal("800").multiply(provision.getRATE_DECIMO()).setScale(SCALE, ROUNDING_MODE), provision.getDecimo());
    }

    @Test
    public void setRemuneracaoMultaFGTSTest() {

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("800"));

        assertEquals(new BigDecimal("800").multiply(provision.getRATE_MULTA_FGTS()).setScale(SCALE, ROUNDING_MODE), provision.getMultaFGTS());
    }

    @Test
    public void setRemuneracaoSubTotalTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);

        final BigDecimal abFerias = remuneracao.multiply(provision.getRATE_AB_FERIAS()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal ferias = remuneracao.multiply(provision.getRATE_FERIAS()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal decimo = remuneracao.multiply(provision.getRATE_DECIMO()).setScale(SCALE, ROUNDING_MODE);

        final BigDecimal subTotal = decimo.add(ferias).add(abFerias).setScale(SCALE, ROUNDING_MODE);

        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setRemuneracaoIncGrupoATest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);

        final BigDecimal abFerias = remuneracao.multiply(provision.getRATE_AB_FERIAS()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal ferias = remuneracao.multiply(provision.getRATE_FERIAS()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal decimo = remuneracao.multiply(provision.getRATE_DECIMO()).setScale(SCALE, ROUNDING_MODE);

        final BigDecimal subTotal = decimo.add(ferias).add(abFerias).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal incGrupoA = provision.getRATE_INC_GRUPO_A().multiply(subTotal).setScale(SCALE, ROUNDING_MODE);

        assertEquals(incGrupoA, provision.getIncGrupoA());
    }

    @Test
    public void setRemuneracaoTotalProvisaoMensalTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);

        final BigDecimal abFerias = remuneracao.multiply(provision.getRATE_AB_FERIAS()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal ferias = remuneracao.multiply(provision.getRATE_FERIAS()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal decimo = remuneracao.multiply(provision.getRATE_DECIMO()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal multaFGTS = remuneracao.multiply(provision.getRATE_MULTA_FGTS()).setScale(SCALE, ROUNDING_MODE);

        final BigDecimal subTotal = decimo.add(ferias).add(abFerias).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal incGrupoA = provision.getRATE_INC_GRUPO_A().multiply(subTotal).setScale(SCALE, ROUNDING_MODE);

        final BigDecimal totalProvisaoMensal = subTotal.add(incGrupoA).add(multaFGTS).setScale(SCALE, ROUNDING_MODE);

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setDecimoSubtotalTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal decimo = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setDecimo(decimo);

        final BigDecimal subTotal = decimo.add(provision.getFerias()).add(provision.getAbFerias()).setScale(SCALE, ROUNDING_MODE);
        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setDecimoTotalMensalProvisaoTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal decimo = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setDecimo(decimo);

        final BigDecimal subTotal = decimo.add(provision.getFerias()).add(provision.getAbFerias()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal totalProvisaoMensal = subTotal.add(provision.getIncGrupoA()).add(provision.getMultaFGTS()).setScale(SCALE, ROUNDING_MODE);

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setFeriasSubtotalTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal ferias = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setFerias(ferias);

        final BigDecimal subTotal = provision.getDecimo().add(ferias).add(provision.getAbFerias()).setScale(SCALE, ROUNDING_MODE);

        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setFeriasTotalMensalProvisaoTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal ferias = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setFerias(ferias);

        final BigDecimal subTotal = provision.getDecimo().add(ferias).add(provision.getAbFerias()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal totalProvisaoMensal = subTotal.add(provision.getIncGrupoA()).add(provision.getMultaFGTS()).setScale(SCALE, ROUNDING_MODE);

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setSetAbFeriasAbSubtotalTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal abFerias = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setAbFerias(abFerias);

        final BigDecimal subTotal = provision.getDecimo().add(provision.getFerias()).add(abFerias).setScale(SCALE, ROUNDING_MODE);

        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setAbFeriasTotalMensalProvisaoTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal abFerias = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setAbFerias(abFerias);

        final BigDecimal subTotal = provision.getDecimo().add(provision.getFerias()).add(abFerias).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal totalProvisaoMensal = subTotal.add(provision.getIncGrupoA()).add(provision.getMultaFGTS()).setScale(SCALE, ROUNDING_MODE);

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setSetIncGrupoATotalProvisaoMensalTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal incGrupoA = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setIncGrupoA(incGrupoA);

        final BigDecimal totalProvisaoMensal = provision.getSubTotal().add(incGrupoA).add(provision.getMultaFGTS()).setScale(SCALE, ROUNDING_MODE);

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setMultaFGTSTest() {

        final BigDecimal remuneracao = new BigDecimal("800");
        final BigDecimal multaFGTS = new BigDecimal("100");

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(remuneracao);
        provision.setMultaFGTS(multaFGTS);

        final BigDecimal totalProvisaoMensal = provision.getSubTotal().add(provision.getIncGrupoA()).add(multaFGTS).setScale(SCALE, ROUNDING_MODE);

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

}