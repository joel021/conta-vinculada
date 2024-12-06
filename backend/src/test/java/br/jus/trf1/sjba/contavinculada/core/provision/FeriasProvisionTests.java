package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.provision.data.FeriasProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeriasProvisionTests {

    @Test
    @DisplayName("Professional started at 2024-01-13, the current date is 2024-01-31. Should choose 2024-01-13 as start date.")
    public void addProvisionStartDateTest() {

        FeriasProvision feriasProvision = new FeriasProvision();
        Provision provision = new Provision(0.34);
        provision.setDate(LocalDate.of(2024, 1, 31));
        provision.setInicioVigencia(LocalDate.of(2024, 1, 13));

        feriasProvision.addProvision(provision);

        assertEquals(feriasProvision.getWorkPeriod().getStartDate(), LocalDate.of(2024, 1, 13));
    }

    @Test
    @DisplayName("Professional started at 2024-01-13, the current date is 2024-01-31. Should choose 2024-01-31 as end date.")
    public void addProvisionEndDateTest() {

        FeriasProvision feriasProvision = new FeriasProvision();
        Provision provision = new Provision(0.34);
        provision.setDate(LocalDate.of(2024, 1, 31));
        provision.setInicioVigencia(LocalDate.of(2024, 1, 13));

        feriasProvision.addProvision(provision);

        assertEquals(LocalDate.of(2024, 1, 31), feriasProvision.getWorkPeriod().getEndDate());
    }

    @Test
    @DisplayName("Professional started at 2023-01-13, the current date is 2024-01-31. Should choose 2024-01-01 start date.")
    public void addProvisionStartDateBeforeCurrentDateTest() {

        FeriasProvision feriasProvision = new FeriasProvision();
        Provision provision = new Provision(0.34);
        provision.setDate(LocalDate.of(2024, 1, 31));
        provision.setInicioVigencia(LocalDate.of(2023, 1, 13));

        feriasProvision.addProvision(provision);

        assertEquals(LocalDate.of(2024, 1, 1), feriasProvision.getWorkPeriod().getStartDate());
    }

    @Test
    @DisplayName("The end date should be equals the date of the last provision.")
    public void addProvisionEndDateEqualsLastDateTest() {

        FeriasProvision feriasProvision = new FeriasProvision();
        Provision provision = new Provision(0.34);
        provision.setDate(LocalDate.of(2024, 1, 31));
        provision.setInicioVigencia(LocalDate.of(2024, 1, 13));

        Provision provision2 = new Provision(0.34);
        provision2.setDate(LocalDate.of(2024, 3, 31));
        provision2.setInicioVigencia(LocalDate.of(2024, 1, 13));

        Provision provision3 = new Provision(0.34);
        provision3.setDate(LocalDate.of(2024, 2, 28));
        provision3.setInicioVigencia(LocalDate.of(2024, 1, 13));

        feriasProvision.addProvision(provision);
        feriasProvision.addProvision(provision2);
        feriasProvision.addProvision(provision3);

        assertEquals(LocalDate.of(2024, 3, 31), feriasProvision.getWorkPeriod().getEndDate());
    }
}
