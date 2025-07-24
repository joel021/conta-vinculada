package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FeriasProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeriasPeriodProvisionTests {

    private FeriasPeriodProvision feriasProvisionWithTwoFullYear;
    private FeriasPeriodProvision feriasProvisionWithOneFullYear;
    private LocalDate twelveEndDate;

    private Liberacao liberation;

    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @BeforeEach
    public void setup() {

        liberation = Liberacao
                .builder()
                .dataLiberacao(LocalDate.now())
                .build();

        LocalDate inicioVigencia = LocalDate.of(2021,1, 1);
        feriasProvisionWithTwoFullYear = new FeriasPeriodProvision(List.of(liberation, liberation));
        feriasProvisionWithOneFullYear = new FeriasPeriodProvision(List.of(liberation));
        twelveEndDate = LocalDate.of(2022, 12, 16);

        for(int i = 1; i <= 12; i++) {
            Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                    new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34")
            );

            LocalDate date = inicioVigencia.plusMonths(i);

            provision.setDate(date);
            provision.setInicioVigencia(date);
            provision.setCriadoEm(date);

            if (i == 11) {
                provision.setDate(twelveEndDate);
                provision.setInicioVigencia(inicioVigencia);
                provision.setCriadoEm(inicioVigencia);
            }

            provision.setRemuneracao(new BigDecimal("1000"));
            feriasProvisionWithTwoFullYear.addProvision(date, provision);
            feriasProvisionWithOneFullYear.addProvision(date, provision);
        }
    }

    @Test
    public void shouldHaveFilledListTest() {

        assertEquals(1, feriasProvisionWithTwoFullYear.getFeriasProvisionList().size());
    }

    @Test
    @DisplayName("The employee has 12 months (full year) of provisions claimed.")
    public void shouldHave12ProvisionsOnTheFirstSetTest() {

        assertEquals(12, feriasProvisionWithTwoFullYear.getFeriasProvisionList().get(0).getProvisoes().size());
    }

    @Test
    @DisplayName("There is no provisions, should create a new batch.")
    public void addProvisionOneElementTest() {

        FeriasPeriodProvision feriasProvision = new FeriasPeriodProvision(List.of(liberation));

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34")
        );
        provision.setRemuneracao(new BigDecimal("1000"));
        provision.setDate(LocalDate.now());
        provision.setInicioVigencia(LocalDate.now());
        provision.setCriadoEm(LocalDate.now());

        feriasProvision.addProvision(LocalDate.now(), provision);

        assertFalse(feriasProvision.getFeriasProvisionList().isEmpty());
    }

    @Test
    @DisplayName("The employee has 2 full year, 1 already claimed. Add new one provision, should start new year of vocation.")
    public void shouldCreateNewOneAfter12Test() {

        Calendar endDate = Calendar.getInstance();
        Provision lastProvision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.35")
        );
        lastProvision.setRemuneracao(new BigDecimal("4000"));
        lastProvision.setDate(LocalDate.now());
        lastProvision.setInicioVigencia(LocalDate.now());
        lastProvision.setCriadoEm(LocalDate.now());

        feriasProvisionWithTwoFullYear.addProvision(lastProvision.getDate(), lastProvision);

        assertEquals(2, feriasProvisionWithTwoFullYear.getFeriasProvisionList().size());
    }

    @Test
    @DisplayName("The employee has only one full year. Try to add new provision, it should not start new year of vocation.")
    public void shouldNotCreateNewOneAfter12Test() {

        LocalDate date = LocalDate.of(2030, 2,1);
        Provision lastProvision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.35")
        );
        lastProvision.setRemuneracao(new BigDecimal("4000"));
        lastProvision.setDate(date);
        lastProvision.setInicioVigencia(date);
        lastProvision.setCriadoEm(date);

        feriasProvisionWithOneFullYear.addProvision(
                LocalDate.of(2033, 2,1),
                lastProvision
        );
        feriasProvisionWithOneFullYear.addProvision(
                LocalDate.of(2034, 2,1),
                lastProvision
        );

        List<FeriasProvision> feriasProvisionListLiberated = feriasProvisionWithOneFullYear.getFeriasProvisionList()
                        .stream().filter(feriasProvision -> feriasProvision.getDataLiberacao() != null).toList();

        assertEquals(1, feriasProvisionListLiberated.size());
    }

    @Test
    @DisplayName("The employee has only one full year. Try to add new provision, it should not add on the current batch")
    public void shouldNotAddNewProvisionTest() {

        LocalDate date = LocalDate.of(2030,2,1);
        Provision lastProvision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.35")
        );
        lastProvision.setRemuneracao(new BigDecimal("4000"));
        lastProvision.setDate(date);
        lastProvision.setInicioVigencia(date);
        lastProvision.setCriadoEm(date);

        feriasProvisionWithOneFullYear.addProvision(
                LocalDate.of(2031, 2,1),
                lastProvision
        );
        feriasProvisionWithOneFullYear.addProvision(
                LocalDate.of(2032, 2,1),
                lastProvision);

        assertEquals(12, feriasProvisionWithOneFullYear.getFeriasProvisionList().get(0).getProvisoes().size());
    }

    @Test
    public void alreadyInsertedTest() {

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.35")
        );
        LocalDate endDate = LocalDate.of(2023, 2, 1);
        provision.setDate(endDate);
        provision.setInicioVigencia(endDate);
        provision.setCriadoEm(endDate);

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberation));
        feriasPeriodProvision.addProvision(
                LocalDate.of(2022, 4, 1),
                provision
        );

        assertTrue(feriasPeriodProvision.alreadyInserted(LocalDate.of(2022, 4, 1)));
    }

    @Test
    public void alreadyInsertedAlreadyTest() {

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34")
        );
        LocalDate endDate = LocalDate.of(2023,2, 1);
        provision.setDate(endDate);
        provision.setInicioVigencia(endDate);
        provision.setCriadoEm(endDate);

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberation));
        feriasPeriodProvision.addProvision(
                LocalDate.of(2022,1, 1),
                provision
        );
        assertTrue(feriasPeriodProvision.alreadyInserted(LocalDate.of(2022, 1, 30)));
    }

    @Test
    public void alreadyInsertedNotTest() {

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.34")
        );
        LocalDate endDate = LocalDate.of(2023, 2, 1);
        provision.setDate(endDate);
        provision.setInicioVigencia(endDate);
        provision.setCriadoEm(endDate);

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberation));
        feriasPeriodProvision.addProvision(
                LocalDate.of(2022,1, 1),
                provision
        );

        assertFalse(feriasPeriodProvision.alreadyInserted(LocalDate.of(2022, 2, 1)));
    }

    @Test
    public void filterFeriasTest() {

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberation));

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("6600"));

        final BigDecimal subTotal = provision.getFerias().add(provision.getAbFerias()).setScale(SCALE, ROUNDING_MODE);
        final BigDecimal expected = subTotal.add(subTotal.multiply(provision.getRATE_INC_GRUPO_A())).setScale(SCALE, ROUNDING_MODE);

        Provision provisionFiltered = feriasPeriodProvision.filterFerias(provision);
        final BigDecimal actual = provisionFiltered.getTotalProvisaoMensal().setScale(SCALE, ROUNDING_MODE);

        assertEquals(expected, actual, "Expected: " + expected.toPlainString() + ", actual: " + actual.toPlainString());
    }


    @Test
    @DisplayName("Only two liberation. The last package of employee vacation is not liberated.")
    public void createNewFeriasProvisionTest() {

        Liberacao liberacao1 = Liberacao
                .builder()
                .dataLiberacao(LocalDate.of(2022,1,1))
                .build();

        Liberacao liberacao2 = Liberacao
                .builder()
                .dataLiberacao(LocalDate.of(2022,2,1))
                .build();

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberacao1, liberacao2));

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.358")
        );
        provision.setDate(LocalDate.of(2021, 1, 1));
        provision.setInicioVigencia(LocalDate.of(2021,1,1));
        provision.setRemuneracao(new BigDecimal("6600"));

        feriasPeriodProvision.createNewFeriasProvision(provision);
        feriasPeriodProvision.createNewFeriasProvision(provision);
        feriasPeriodProvision.createNewFeriasProvision(provision);

        assertNull(feriasPeriodProvision.getFeriasProvisionList().get(2).getDataLiberacao());
    }

    @Test
    @DisplayName("Only two liberation. The first and second are liberated.")
    public void createNewFeriasProvisionTwoTest() {

        Liberacao liberacao1 = Liberacao
                .builder()
                .dataLiberacao(LocalDate.of(2022,1,1))
                .build();

        Liberacao liberacao2 = Liberacao
                .builder()
                .dataLiberacao(LocalDate.of(2022,2,1))
                .build();

        FeriasPeriodProvision feriasPeriodProvision = new FeriasPeriodProvision(List.of(liberacao1, liberacao2));

        Provision provision = new Provision(new BigDecimal("0.0909"), new BigDecimal("0.0909"),
                new BigDecimal("0.0303"), new BigDecimal("0.0349"), new BigDecimal("0.358")
        );
        provision.setDate(LocalDate.of(2021, 2, 1));
        provision.setInicioVigencia(LocalDate.of(2021,1,1));
        provision.setRemuneracao(new BigDecimal("6600"));

        feriasPeriodProvision.createNewFeriasProvision(provision);
        feriasPeriodProvision.createNewFeriasProvision(provision);
        feriasPeriodProvision.createNewFeriasProvision(provision);

        assertNotNull(feriasPeriodProvision.getFeriasProvisionList().get(1).getDataLiberacao());
    }

}
