package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProvisionCalcTests {

    private ProvisionCalc provisionCalc;
    private ContratoTerceirizado contratoTerceirizado;
    private LocalDate endDate;
    private LocalDate startDate;

    private static final int SCALE = 10; // Standard scale for intermediate calculations
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @BeforeEach
    public void setup() {

        startDate = LocalDate.of(2000, 1, 1);
        provisionCalc = new ProvisionCalc(new BigDecimal("36.64"));
        contratoTerceirizado = ContratoTerceirizado.builder()
                .dataInicio(startDate)
                .remuneracao(new BigDecimal("10000.55")) // Changed float to BigDecimal
                .criadoEm(Calendar.getInstance())
                .build();

        endDate = LocalDate.of(2024, 12, 31);
    }

    @Test
    public void fromContratoTerceirizadoDecimoTest() {

        BigDecimal decimo = provisionCalc.getRATE_DECIMO().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(decimo, provision.getDecimo());
    }

    @Test
    public void fromContratoTerceirizadoFeriasTest() {

        BigDecimal ferias = provisionCalc.getRATE_FERIAS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(ferias, provision.getFerias());
    }

    @Test
    public void fromContratoTerceirizadoAbFeriasTest() {

        BigDecimal abFerias = provisionCalc.getRATE_AB_FERIAS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(abFerias, provision.getAbFerias());
    }

    @Test
    public void fromContratoTerceirizadoSubTotalTest() {

        BigDecimal decimo = provisionCalc.getRATE_DECIMO().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal ferias = provisionCalc.getRATE_FERIAS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal abFerias = provisionCalc.getRATE_AB_FERIAS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal subTotal = decimo.add(ferias).add(abFerias).setScale(SCALE, ROUNDING_MODE);

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(subTotal, provision.getSubTotal());
    }

    @Test
    public void fromContratoTerceirizadoIncGrupoATest() {

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        BigDecimal incGrupoA = provisionCalc.getPER_INC_GRUPO_A().divide(new BigDecimal("100"), SCALE, ROUNDING_MODE)
                .multiply(provision.getSubTotal())
                .setScale(SCALE, ROUNDING_MODE);

        assertEquals(incGrupoA, provision.getIncGrupoA());
    }

    @Test
    public void fromContratoTerceirizadoMultaFGTSTest() {

        BigDecimal multaFGTSExpected = provisionCalc.getRATE_MULTA_FGTS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        BigDecimal multaFGTS = provision.getMultaFGTS().setScale(2, RoundingMode.FLOOR); // Applied RoundingMode.FLOOR

        assertEquals(multaFGTSExpected.setScale(2, RoundingMode.FLOOR), multaFGTS); // Applied RoundingMode.FLOOR
    }

    @Test
    public void fromContratoTerceirizadoTotalProvisaoMensalTest() {

        BigDecimal decimo = provisionCalc.getRATE_DECIMO().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal ferias = provisionCalc.getRATE_FERIAS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal abFerias = provisionCalc.getRATE_AB_FERIAS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal subTotal = decimo.add(ferias).add(abFerias).setScale(SCALE, ROUNDING_MODE);

        BigDecimal multaFGTS = provisionCalc.getRATE_MULTA_FGTS().multiply(contratoTerceirizado.getRemuneracao()).setScale(SCALE, ROUNDING_MODE);
        BigDecimal incGrupoA = provisionCalc.getPER_INC_GRUPO_A().divide(new BigDecimal("100"), SCALE, ROUNDING_MODE)
                .multiply(subTotal).setScale(SCALE, ROUNDING_MODE);

        BigDecimal totalProvisaoMensal = subTotal.add(multaFGTS).add(incGrupoA).setScale(SCALE, ROUNDING_MODE);

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);
        assertEquals(totalProvisaoMensal.setScale(2, RoundingMode.FLOOR), provision.getTotalProvisaoMensal().setScale(2, RoundingMode.FLOOR));
    }

    @Test
    public void onStartDateNotCalcTest() {

        contratoTerceirizado.setDataInicio(LocalDate.now().withDayOfMonth(20));

        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, LocalDate.now());
        assertTrue(BigDecimal.ZERO.compareTo(provision.getFerias()) == 0);
    }

    @Test
    public void dateBeforeStartDateTest() {

        LocalDate startDate = LocalDate.of(2022, 1, 1);
        contratoTerceirizado.setDataInicio(startDate);

        LocalDate date = LocalDate.of(2021, 12, 31);
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, date);

        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = provision.getFerias();
        BigDecimal tolerance = new BigDecimal("1e-6");

        assertTrue(expected.subtract(actual).abs().compareTo(tolerance) <= 0,
                "Expected " + expected + " but got " + actual + " with tolerance " + tolerance);
    }

    @Test
    public void onEndDateNotCalcTest() {

        LocalDate endDate = LocalDate.now().withDayOfMonth(13);
        Provision provision = provisionCalc.fromContratoTerceirizado(contratoTerceirizado, endDate);

        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = provision.getFerias();
        BigDecimal tolerance = new BigDecimal("1e-6");
        assertTrue(expected.subtract(actual).abs().compareTo(tolerance) <= 0,
                "Expected " + expected + " but got " + actual + " with tolerance " + tolerance);
    }
}