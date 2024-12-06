package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProvisionTests {

    @Test
    public void setRemuneracaoFeriasTest() {

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(800);

        assertEquals(800 * provision.getRATE_FERIAS(), provision.getFerias());
    }

    @Test
    public void setRemuneracaoAbFeriasTest() {

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(800);

        assertEquals(800 * provision.getRATE_AB_FERIAS(), provision.getAbFerias());
    }

    @Test
    public void setRemuneracaoDecimoTest() {

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(800);

        assertEquals(800 * provision.getRATE_DECIMO(), provision.getDecimo());
    }

    @Test
    public void setRemuneracaoMultaFGTSTest() {

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(800);

        assertEquals(800 * provision.getRATE_MULTA_FGTS(), provision.getMultaFGTS());
    }

    @Test
    public void setRemuneracaoSubTotalTest() {

        final double remunaracao = 800;
        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remunaracao);

        final double abFerias = remunaracao * provision.getRATE_AB_FERIAS();
        final double ferias = remunaracao * provision.getRATE_FERIAS();
        final double decimo = remunaracao * provision.getRATE_DECIMO();

        final double subTotal = decimo + ferias + abFerias;

        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setRemuneracaoIncGrupoATest() {

        final double remunaracao = 800;
        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remunaracao);

        final double abFerias = remunaracao * provision.getRATE_AB_FERIAS();
        final double ferias = remunaracao * provision.getRATE_FERIAS();
        final double decimo = remunaracao * provision.getRATE_DECIMO();

        final double subTotal = decimo + ferias + abFerias;
        final double incGrupoA = provision.getRATE_INC_GRUPO_A() * subTotal;

        assertEquals(incGrupoA, provision.getIncGrupoA());
    }

    @Test
    public void setRemuneracaoTotalProvisaoMensalTest() {

        final double remuneracao = 800;
        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);

        final double abFerias = remuneracao * provision.getRATE_AB_FERIAS();
        final double ferias = remuneracao * provision.getRATE_FERIAS();
        final double decimo = remuneracao * provision.getRATE_DECIMO();
        final double multaFGTS = remuneracao * provision.getRATE_MULTA_FGTS();

        final double subTotal = decimo + ferias + abFerias;
        final double incGrupoA = provision.getRATE_INC_GRUPO_A() * subTotal;

        final double totalProvisaoMensal = subTotal + incGrupoA + multaFGTS;

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setDecimoSubtotalTest() {

        final double remuneracao = 800;
        final double decimo = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setDecimo(decimo);

        final double subTotal = decimo + provision.getFerias() + provision.getAbFerias();
        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setDecimoTotalMensalProvisaoTest() {

        final double remuneracao = 800;
        final double decimo = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setDecimo(decimo);

        final double subTotal = decimo + provision.getFerias() + provision.getAbFerias();
        final double totalProvisaoMensal = subTotal + provision.getIncGrupoA() + provision.getMultaFGTS();

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setFeriasSubtotalTest() {

        final double remuneracao = 800;
        final double ferias = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setFerias(ferias);

        final double subTotal = provision.getDecimo() + ferias + provision.getAbFerias();

        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setFeriasTotalMensalProvisaoTest() {

        final double remuneracao = 800;
        final double ferias = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setFerias(ferias);

        final double subTotal = provision.getDecimo() + ferias + provision.getAbFerias();
        final double totalProvisaoMensal = subTotal + provision.getIncGrupoA() + provision.getMultaFGTS();

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setSetAbFeriasAbSubtotalTest() {

        final double remuneracao = 800;
        final double abFerias = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setAbFerias(abFerias);

        final double subTotal = provision.getDecimo() + provision.getFerias() + abFerias;

        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void setAbFeriasTotalMensalProvisaoTest() {

        final double remuneracao = 800;
        final double abFerias = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setAbFerias(abFerias);

        final double subTotal = provision.getDecimo() + provision.getFerias() + abFerias;
        final double totalProvisaoMensal = subTotal + provision.getIncGrupoA() + provision.getMultaFGTS();

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setSetIncGrupoATotalProvisaoMensalTest() {

        final double remuneracao = 800;
        final double incGrupoA = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setIncGrupoA(incGrupoA);

        final double totalProvisaoMensal = provision.getSubTotal() + incGrupoA + provision.getMultaFGTS();

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

    @Test
    public void setMultaFGTSTest() {

        final double remuneracao = 800;
        final double multaFGTS = 100;

        Provision provision = new Provision(0.358);
        provision.setRemuneracao(remuneracao);
        provision.setMultaFGTS(multaFGTS);

        final double totalProvisaoMensal = provision.getSubTotal() + provision.getIncGrupoA() + multaFGTS;

        assertEquals(totalProvisaoMensal, provision.getTotalProvisaoMensal());
    }

}
