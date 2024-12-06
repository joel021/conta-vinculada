package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProvisionCalcTests {

    private static ProvisionCalc provisionCalc;
    private static ContratoTerceirizado contratoTerceirizado;
    private static LocalDate endDate;

    private static LocalDate startDate;

    @BeforeAll
    public static void setup() {
        
        startDate = LocalDate.of(2000, 1, 1);
        provisionCalc = new ProvisionCalc(36.64);
        contratoTerceirizado = ContratoTerceirizado.builder()
                .dataInicio(startDate)
                .remuneracao(10000.55f)
                .criadoEm(Calendar.getInstance())
                .build();
        
        endDate = LocalDate.of(2024, 12, 31);
    }

    @Test
    public void fromContratoTerceirizadoDecimoTest() {

        double decimo = provisionCalc.getRATE_DECIMO() * contratoTerceirizado.getRemuneracao();

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado,endDate);
        assertEquals(decimo, provision.getDecimo());
    }

    @Test
    public void fromContratoTerceirizadoFeriasTest() {

        double ferias = provisionCalc.getRATE_FERIAS() * contratoTerceirizado.getRemuneracao();
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado,endDate);
        assertEquals(ferias, provision.getFerias());
    }

    @Test
    public void fromContratoTerceirizadoAbFeriasTest() {

        double abFerias = provisionCalc.getRATE_AB_FERIAS() * contratoTerceirizado.getRemuneracao();
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(abFerias, provision.getAbFerias());
    }

    @Test
    public void fromContratoTerceirizadoSubTotalTest() {

        double decimo = provisionCalc.getRATE_DECIMO() * contratoTerceirizado.getRemuneracao();
        double ferias = provisionCalc.getRATE_FERIAS() * contratoTerceirizado.getRemuneracao();
        double abFerias = provisionCalc.getRATE_AB_FERIAS() * contratoTerceirizado.getRemuneracao();
        double subTotal = decimo + ferias + abFerias;

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void fromContratoTerceirizadoIncGrupoATest() {


        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        double incGrupoA = provisionCalc.getPER_INC_GRUPO_A() / 100 * provision.getSubTotal();

        assertEquals(incGrupoA, provision.getIncGrupoA());
    }

    @Test
    public void fromContratoTerceirizadoMultaFGTSTest() {

        double multaFGTSExpected = provisionCalc.getRATE_MULTA_FGTS() * contratoTerceirizado.getRemuneracao();

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        double multaFGTS = Math.floor(provision.getMultaFGTS() * 100) / 100;

        assertEquals(Math.floor(multaFGTSExpected * 100) / 100, multaFGTS);
    }

    @Test
    public void fromContratoTerceirizadoTotalProvisaoMensalTest() {

        double decimo = provisionCalc.getRATE_DECIMO() * contratoTerceirizado.getRemuneracao();
        double ferias = provisionCalc.getRATE_FERIAS() * contratoTerceirizado.getRemuneracao();
        double abFerias = provisionCalc.getRATE_AB_FERIAS() * contratoTerceirizado.getRemuneracao();
        double subTotal = decimo + ferias + abFerias;

        double multaFGTS = provisionCalc.getRATE_MULTA_FGTS() * contratoTerceirizado.getRemuneracao();
        double incGrupoA = provisionCalc.getPER_INC_GRUPO_A() / 100 * subTotal;

        double totalProvisaoMensal = subTotal + multaFGTS + incGrupoA;

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(Math.floor(totalProvisaoMensal*100)/100, Math.floor(provision.getTotalProvisaoMensal()*100)/100);
    }

    @Test
    public void onStartDateNotCalcTest() {

        contratoTerceirizado.setDataInicio(LocalDate.now().withDayOfMonth(20));

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, LocalDate.now());
        assertEquals(0d, provision.getFerias());
    }

    @Test
    public void dateBeforeStartDateTest() {

        LocalDate startDate = LocalDate.of(2022, 1, 1);
        contratoTerceirizado.setDataInicio(startDate);

        LocalDate date = LocalDate.of(2021, 12, 31);
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, date);
        assertEquals(0d, provision.getFerias());
    }

    @Test
    public void onEndDateNotCalcTest() {

        LocalDate endDate = LocalDate.now().withDayOfMonth(13);

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(0d, provision.getFerias());
    }
}
